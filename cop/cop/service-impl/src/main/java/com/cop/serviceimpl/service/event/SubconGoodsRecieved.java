package com.cop.serviceimpl.service.event;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cop.model.database.Altuom;
import com.cop.model.database.Caval;
import com.cop.model.database.Currencyexchangerate;
import com.cop.model.database.Material;
import com.cop.model.database.Mmtotal;
import com.cop.model.database.Orderheader;
import com.cop.model.database.Organisation;
import com.cop.repository.transaction.AltuomRepository;
import com.cop.repository.transaction.CavalRepository;
import com.cop.repository.transaction.CostTotalRepository;
import com.cop.repository.transaction.CurrencyExchangeRateRepository;
import com.cop.repository.transaction.MMTotalRespository;
import com.cop.repository.transaction.MaterialRepository;
import com.cop.repository.transaction.OrderHeaderRepository;
import com.cop.repository.transaction.OrganisationRepository;
import com.cop.serviceapi.service.MMTotalService;

@Component
public class SubconGoodsRecieved {
	@Autowired
	SubconProcessingCharge spc;
	@Autowired
	OrderHeaderRepository Ohr;
	@Autowired
	MMTotalRespository mtr;
	@Autowired
	AltuomRepository mar;
	@Autowired
	OrganisationRepository or;
	@Autowired
	CurrencyExchangeRateRepository cerr;
	@Autowired
	CavalRepository cvr;
	@Autowired
	CostTotalRepository ctr;
	@Autowired
	MaterialRepository mr;
	@Autowired
	MMTotalService mmts;
	private Altuom matalt;
	private Orderheader oh;
	private Caval caval;
	private Material mat;
	private Currencyexchangerate cer;
	private Organisation org;

	public Mmtotal generateMMTotal(Mmtotal mt) {
		System.out.println("entering subcon gr");
		mt = mmts.intializeMMTotal(mt);
		List<Orderheader> ohList = Ohr.findAllByOrdernum(mt.getObjcode(), mt.getObjitnum());
		oh = ohList.get(0);
		List<Material> matList = mr.findAllByMatlcodeandPlantValsub(mt.getItemcode(), mt.getPlant(), oh.getValsub());
		mat = matList.get(0);

		//if (mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("NO"))
			//mt.setBstock(mt.getBuomqty());

		mt.setVendor(oh.getVendor());

		List<Altuom> altList = mar.findAltuombyMatCodePlantAltUOM(oh.getPlant(), mt.getItemcode(), mt.getDocuom(),
				mt.getBuom());
		matalt = altList.get(0);

		System.out.println("BUOM from MATALT" + matalt.getBuom().toUpperCase());
		System.out.println("mt buomqty" + mt.getDocqty());

		/// BUOMQTY
		if (mt.getDocuom().toString().toUpperCase().equals(matalt.getBuom().toUpperCase()))
			mt.setBuomqty(mt.getDocqty());
		else {
			BigDecimal qty = matalt.getBuqty().divide(matalt.getAltuomqty());
			System.out.println(
					"buqty " + matalt.getBuqty() + "altuomgty " + matalt.getAltuomqty() + "Quantity ratio " + qty);
			mt.setBuomqty(mt.getDocqty().multiply(qty));
		}

		// Ordamount
		// BigDecimal giordamount =
		// this.mtr.findsumofOrdamountbytrnsevent(mt.getCobjtype(), mt.getObjcode(),
		// "GI");

		// BigDecimal pcordamount =
		// this.ctr.findsumofOrdamountbytrnsevent(mt.getCobjtype(), mt.getObjcode(),
		// "PC");
		// if (giordamount == null || pcordamount == null)
		// mt.setOrdamount(new BigDecimal(0));
		// else
		// mt.setOrdamount(giordamount.add(pcordamount));

		// Amountincurro
		List<Organisation> orgList = or.findAllByOrgnCode(oh.getOrgncode());
		org = orgList.get(0);
		List<Currencyexchangerate> cerList = cerr.findAllbyOrgncodeNdCurrency(oh.getOrgncode(), oh.getOrdcurrency(),
				org.getCurro(), new java.sql.Date((mt.getDate()).getTime()));
		cer = cerList.get(0);
		mt.setAmountincurro(mt.getOrdamount().multiply(cer.getTcurrv().divide(cer.getScnos())));
		// costacc
		List<Caval> cvList = cvr.findAllbyOrgnCodeMatValGrp("GR", oh.getOrgncode(), mat.getValgrp());
		caval = cvList.get(0);
		mt.setCostacc(caval.getCostacc());
		// REVERSAL Indicator
		// REVERSAL DOCUMENT
		if (mt.getSaleorder() != null)
			mt.setStocktype("S");
		else
			mt.setStocktype("O");

		BigDecimal[] stock;

		List<BigDecimal[]> mtrList;
		if (mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("NO"))
			mtrList = mtr.findCstockStockwithoutBatch(mt.getItemcode(), mt.getPlant(), mt.getDate(), mt.getStocktype(),
					mt.getValsub(), mt.getSaleorder());
		else
			mtrList = mtr.findCstockStockwithBatch(mt.getItemcode(), mt.getPlant(), mt.getDate(), mt.getStocktype(),
					mt.getValsub(), mt.getBatchnumber(), mt.getSaleorder());

		if (mtrList.isEmpty()) {
			mt.setCstock(mt.getBuomqty());
			mt.setStockvalue(mt.getAmountincurro());
			System.out.println("CSTOCK = " + mt.getCstock() + " | StockValue = " + mt.getStockvalue());
		} else {
			stock = mtrList.get(0);
			BigDecimal cstock = stock[0];
			BigDecimal stockvalue = stock[1];
			mt.setCstock(cstock.add(mt.getBuomqty()));
			mt.setStockvalue(stockvalue.add(mt.getAmountincurro()));
			System.out.println("CSTOCK = " + mt.getCstock() + " | StockValue = " + mt.getStockvalue());

		}
		//if (mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("NO"))
			//mt.setBstock(mt.getBuomqty());

		List<Mmtotal> mtrLst;
		if (mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("NO"))
			mtrLst = mtr.findallbyBatchcosting(mt.getItemcode(), mt.getPlant(), mt.getDate(), mt.getStocktype(),
					mt.getValsub(), mt.getSaleorder());
		else
			mtrLst = mtr.findCstockStockvaluebyGRDateandstocktype(mt.getItemcode(), mt.getPlant(), mt.getDate(),
					mt.getStocktype(), mt.getValsub(), mt.getBatchnumber(), mt.getSaleorder());
		for (Mmtotal mmtotal : mtrLst) {
			System.out.println("Updating Stock " + mmtotal.getMmtotalid());
			System.out.println("OLD : CSTOCK = " + mmtotal.getCstock() + " | StockValue = " + mmtotal.getStockvalue());
			mmtotal.setCstock(mmtotal.getCstock().add(mt.getBuomqty()));
			mmtotal.setStockvalue(mmtotal.getStockvalue().add(mt.getAmountincurro()));
			System.out.println("NEW : CSTOCK = " + mmtotal.getCstock() + " | StockValue = " + mmtotal.getStockvalue());
			if (mt.getBatchnumber() != null && mat.getBatch().equalsIgnoreCase("YES")
					&& mat.getBatchcosting().equalsIgnoreCase("NO"))
				mt.setBstock(mmtotal.getBstock().add(mt.getBuomqty()));
			mtr.save(mmtotal);
		}
		mt.setOrdcurrency(oh.getOrdcurrency());
		mt.setAdindicator("+");

		return mt;
	}
}