package com.cop.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cop.model.database.Altuom;
import com.cop.model.database.Currencyexchangerate;
import com.cop.model.database.Orderheader;
import com.cop.repository.transaction.AltuomRepository;
import com.cop.repository.transaction.CurrencyExchangeRateRepository;
import com.cop.repository.transaction.MaterialRepository;
import com.cop.repository.transaction.MrcRepository;
import com.cop.repository.transaction.OrderHeaderRepository;

@Component
public class TransactionUtils {

	@Autowired
	AltuomRepository mar;
	
	@Autowired
	MaterialRepository matr;
	
	@Autowired
	MrcRepository mrcr;

	@Autowired
	CurrencyExchangeRateRepository cerr;

	@Autowired
	OrderHeaderRepository Ohr;
	private Orderheader oh, loh;

	public BigDecimal unitConvertor(String plant, String itemCode, String baseUnit, String fromUnit, String toUnit,
			BigDecimal fromValue) {

		System.out.println("plant-" + plant + "itemCode-" + itemCode + " baseUnit-" + baseUnit + " fromUnit-" + fromUnit
				+ " toUnit-" + toUnit + " fromValue-" + fromValue);

		if (fromUnit.equals(toUnit)) {
			return fromValue;
		}
		BigDecimal toValue = null;
		List<Altuom> altList = this.mar.findAltuombyMatCodePlantAltUOM(plant, itemCode, fromUnit, baseUnit);
		Altuom fromAltuom = altList.get(0);
		altList = this.mar.findAltuombyMatCodePlantAltUOM(plant, itemCode, toUnit, baseUnit);
		Altuom toAltuom = altList.get(0);
		System.out.println("fromAltuom " + fromAltuom);
		System.out.println("toAltuom " + toAltuom);
		toValue = fromValue.multiply((fromAltuom.getBuqty().divide(fromAltuom.getAltuomqty()))
				.multiply((toAltuom.getAltuomqty().divide(toAltuom.getBuqty(), 4, RoundingMode.HALF_UP))));
		System.out.println("toValue" + toValue);
		return toValue;
	}

	// 5th Oct taking ER input from user

	public List<BigDecimal> generateTransactionQtyAndAmount(String trnsEvent, Date date, String orgnCode, String plant,
			String itemCode, BigDecimal docQty, String docUom, BigDecimal docAmt, String docCurr, String bUom,
			String ordUom, String orgCurr, BigDecimal groumRate, BigDecimal exchangeRate) {
		dumpToConsole("Input to generateTransactionQtyAndAmount : ", trnsEvent, date, orgnCode, plant, itemCode, docQty, docUom, docAmt, docCurr, bUom, ordUom,
				orgCurr, groumRate, docAmt);
		List<BigDecimal> result = new ArrayList();

		List<Altuom> altList = mar.findAltuombyMatCodePlantAltUOM(plant, itemCode, docUom, bUom);
		Altuom matalt = altList.get(0);
		List<Currencyexchangerate> cerList = cerr.findAllbyOrgncodeNdCurrency(orgnCode, docCurr, orgCurr,
				new java.sql.Date((date).getTime()));
		Currencyexchangerate cer = cerList.get(0);
		BigDecimal ordAmt = null, curroAmt = null, prdAmt = null, prdQty = null;

		BigDecimal bQty = docQty.multiply(matalt.getBuqty().divide(matalt.getAltuomqty()));
		BigDecimal groumrate = this.unitConvertor(plant, itemCode, bUom, ordUom, docUom, groumRate);
		if (exchangeRate == null)
			exchangeRate = cer.getTcurrv().divide(cer.getScnos());
		BigDecimal docqty = this.unitConvertor(plant, itemCode, bUom, docUom, ordUom, docQty);

		dumpToConsole(bQty, groumrate, exchangeRate, docqty);

		if (trnsEvent.equals("IB-CONTRA") || trnsEvent.equals("DNQR-CONTRA") || trnsEvent.equals("CNQR-CONTRA")
				|| trnsEvent.equals("DNVR-CONTRA")) {

			if (docAmt == null)
				docAmt = new BigDecimal(0);

			ordAmt = docAmt.subtract(docqty.multiply(groumRate));
			curroAmt = ordAmt.multiply(exchangeRate);
			prdQty = bQty;
			prdAmt = curroAmt;
			dumpToConsole(ordAmt, docqty, groumRate);
		} else {
			if (docAmt == null) {
				ordAmt = docqty.multiply(groumrate);
				curroAmt = ordAmt.multiply(exchangeRate);
				prdQty = bQty;
				prdAmt = ordAmt.subtract(curroAmt);
			} else {
				ordAmt = docAmt;
				curroAmt = ordAmt.multiply(exchangeRate);
				prdQty = bQty;
				prdAmt = (ordAmt.subtract(docqty.multiply(groumRate)).multiply(exchangeRate));
			}

		}

		if (trnsEvent.equals("GR")) {
			prdQty = new BigDecimal(0);
			prdAmt = new BigDecimal(0);
		}

		result.add(bQty);
		result.add(ordAmt);
		result.add(curroAmt);
		result.add(prdQty);
		result.add(prdAmt);
		result.add(exchangeRate);
		dumpToConsole("Output from generateTransactionQtyAndAmount : ", bQty, ordAmt, curroAmt,prdQty,exchangeRate);
		return result;
	}

	public BigDecimal findBuqty(String objcode, BigDecimal objitnum, String linkedobjcode,
			BigDecimal linkedobjitnum, String tDocuom , BigDecimal docQty) {

		if (linkedobjcode != null) {
			List<Orderheader> ohList1 = Ohr.findAllByOrdernum(linkedobjcode, linkedobjitnum);
			oh = ohList1.get(0);
		}
		else {
			List<Orderheader> ohList = Ohr.findAllByOrdernum(objcode, objitnum);
			oh = ohList.get(0);
		}
		List<Altuom> altList = mar.findAltuombyMatCodePlantAltUOM(oh.getPlant(), oh.getItemcode(), tDocuom, oh.getBuom());
		Altuom matalt = altList.get(0);
		
		BigDecimal bQty = docQty.multiply(matalt.getBuqty().divide(matalt.getAltuomqty()));
		return bQty;
	}

	

	public void dumpToConsole(Object... args) {
		System.out.println("Dumping to console :");
		for (Object str : args)
			if (str != null)
				System.out.print(" " + str.toString() + " ");
			else
				System.out.print(" <<null>> ");
		System.out.println();
	}
}
