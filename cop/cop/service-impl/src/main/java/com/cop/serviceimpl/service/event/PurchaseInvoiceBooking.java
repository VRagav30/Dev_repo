package com.cop.serviceimpl.service.event;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cop.model.database.Altuom;
import com.cop.model.database.Caval;
import com.cop.model.database.Costtotal;
import com.cop.model.database.Currencyexchangerate;
import com.cop.model.database.Material;
import com.cop.model.database.Mmtotal;
import com.cop.model.database.Orderheader;
import com.cop.model.database.Organisation;
import com.cop.model.database.Serviceitem;
import com.cop.repository.transaction.AltuomRepository;
import com.cop.repository.transaction.CavalRepository;
import com.cop.repository.transaction.CostTotalRepository;
import com.cop.repository.transaction.CurrencyExchangeRateRepository;
import com.cop.repository.transaction.FydPeriodRepository;
import com.cop.repository.transaction.MMTotalRespository;
import com.cop.repository.transaction.MaterialRepository;
import com.cop.repository.transaction.MrcRepository;
import com.cop.repository.transaction.NumberRangeRepository;
import com.cop.repository.transaction.OrderHeaderRepository;
import com.cop.repository.transaction.OrganisationRepository;
import com.cop.repository.transaction.PlantRepository;
import com.cop.repository.transaction.ServiceRepository;
import com.cop.serviceapi.service.CostTotalService;
import com.cop.serviceapi.service.MMTotalService;
import com.cop.utils.TransactionUtils;

@Component
public class PurchaseInvoiceBooking {

	@Autowired
	TransactionUtils transUtil;
	Costtotal ct;
	@Autowired
	OrganisationRepository or;
	@Autowired
	CavalRepository cvr;
	@Autowired
	CostTotalRepository ctr;
	@Autowired
	PlantRepository pr;
	@Autowired
	OrderHeaderRepository Ohr;
	@Autowired
	FydPeriodRepository fpr;
	@Autowired
	MaterialRepository mr;
	@Autowired
	MrcRepository mrcr;
	@Autowired
	AltuomRepository mar;
	@Autowired
	MMTotalRespository mtr;
	@Autowired
	CurrencyExchangeRateRepository cerr;
	@Autowired
	CavalRepository cr;
	@Autowired
	ServiceRepository sr;
	private Serviceitem srvc;
	private Material mat;
	private Caval caval;
	NumberRangeRepository nrr;
	private Organisation org;
	private Orderheader oh, loh;
	private Altuom matalt;
	private Currencyexchangerate cer;
	@Autowired
	CostTotalService cs;
	@Autowired
	MMTotalService mts;
	private Costtotal linkedct;

	public Costtotal generateCostTotal(Costtotal ct) {
		ct = this.cs.initializeCostTotal(ct);
		List<Orderheader> ohList = Ohr.findAllByOrdernum(ct.getObjcode(), ct.getObjitnum());
		oh = ohList.get(0);

		System.out.println("or = " + or);
		// AmountInCurro
		List<Organisation> orgList = or.findAllByOrgnCode(ct.getOrgncode());
		org = orgList.get(0);

		if (ct.getItemtype().equals("M")) {
			List<Material> matList = mr.findAllByMatlcodeandPlantValsub(ct.getItemcode(), ct.getPlant(),
					ct.getValsub());
			mat = matList.get(0);
		}
		if (ct.getItemtype().equals("S")) {
			List<Serviceitem> srvcList = sr.findAllbyItemcodeandPlant(ct.getItemcode(), ct.getPlant());
			srvc = srvcList.get(0);
			/*--------------------------------ALTOUM-----------------------------------------*/
			System.out.println(oh.getPlant() + " " + ct.getItemcode() + " " + ct.getDocuom() + " " + srvc.getBuom());
			List<Altuom> altList = mar.findAltuombyMatCodePlantAltUOM(oh.getPlant(), ct.getItemcode(), ct.getDocuom(),
					srvc.getBuom());
			matalt = altList.get(0);
		}
		// comment for now

		if (ct.getRefdocnum() != null) {
			List<Costtotal> costtotalList = ctr.findAllbyRefdocnum(ct.getRefdocnum());
			linkedct = costtotalList.get(0);

			if (linkedct.getDocnumber().equals(ct.getRefdocnum())) {

				System.out.println("Found a matching record ready to process");

				ct.setDocqty(linkedct.getDocqty());

			}

		}

		/*--------------------------------MATALTOUM-----------------------------------------*/

		List<Altuom> altList = mar.findAltuombyMatCodePlantAltUOM(oh.getPlant(), ct.getItemcode(), ct.getDocuom(),
				ct.getBuom());
		matalt = altList.get(0);

		System.out.println("BUOM from MATALT" + matalt.getBuom().toUpperCase());
		System.out.println("ct buomqty" + ct.getDocqty());

		List<BigDecimal> result = transUtil.generateTransactionQtyAndAmount(ct.getTrnsevent(), ct.getDate(),
				oh.getOrgncode(), ct.getPlant(), ct.getItemcode(), ct.getDocqty(), ct.getDocuom(), ct.getOrdamount(),
				ct.getOrdcurrency(), oh.getBuom(), oh.getOrdgruom(), org.getCurro(), oh.getGroumrate(),
				ct.getExchangerate());
		ct.setBuomqty(result.get(0));
		ct.setOrdamount(result.get(1));
		ct.setAmountincurro(result.get(2));
		ct.setExchangerate(result.get(5));

		// costacc
		if (ct.getItemtype().equals("M")) {
			List<Caval> cvList = cvr.findAllbyOrgnCodeMatValGrp("INV", oh.getOrgncode(), mat.getValgrp());
			caval = cvList.get(0);
			ct.setCostacc(caval.getCostacc());
		} else if (ct.getItemtype().equals("S")) {
			List<Serviceitem> srvcList = sr.findAllbyItemcodeandPlant(ct.getItemcode(), ct.getPlant());
			srvc = srvcList.get(0);
			List<Caval> cvList = cvr.findAllbyOrgnCodeMatValGrp("SR", oh.getOrgncode(), srvc.getValgrp());
			caval = cvList.get(0);
			ct.setCostacc(caval.getCostacc());
		}
		/*-------------------------------------------------------------------------*/

		/*-------------------------------------------------------------------------*/
		// Partnercode
		if (ct.getItemtype().equals("S")) {
			ct.setPrtnrcode(oh.getCostcenter());
		}

		// EXCHANGE RATE DIFF
		if (oh.getExchangerate() == null)
			oh.setExchangerate(new BigDecimal(0));
		System.out.println("EXCHANGE RATE =" + ct.getExchangerate());
		if (ct.getExchangerate().equals(new BigDecimal(1))) {
			ct.setExchangerate(null);
			ct.setExchangeratediff(null);
		} else
			ct.setExchangeratediff((ct.getExchangerate().subtract(oh.getExchangerate())).multiply(ct.getOrdamount()));
		return ct;
	}

	public Mmtotal generateMMTotal(Mmtotal mt) {
		mt = mts.intializeMMTotal(mt);

		List<Orderheader> ohList = Ohr.findAllByOrdernum(mt.getObjcode(), mt.getObjitnum());
		oh = ohList.get(0);

		List<Material> matList = mr.findAllByMatlcodeandPlantValsub(mt.getItemcode(), mt.getPlant(), mt.getValsub());
		mat = matList.get(0);

		List<Organisation> orgList = this.or.findAllByOrgnCode(oh.getOrgncode());
		this.org = orgList.get(0);

		if (oh.getObjtype().equalsIgnoreCase("PUR")) {
			List<Caval> cvList = cvr.findAllbyOrgnCodeMatValGrp("INV", oh.getOrgncode(), mat.getValgrp());
			caval = cvList.get(0);
			mt.setCostacc(caval.getCostacc());
			System.out.println("for Purchase");
		} else if (oh.getObjtype().equalsIgnoreCase("JOB")) {
			List<Caval> cvList = cvr.findAllbyOrgnCodeMatValGrp("PC", oh.getOrgncode(), mat.getValgrp());
			caval = cvList.get(0);
			mt.setCostacc(caval.getCostacc());
			System.out.println("for Job");
		}

		List<BigDecimal> result = transUtil.generateTransactionQtyAndAmount(mt.getTrnsevent(), mt.getDate(),
				oh.getOrgncode(), mt.getPlant(), mt.getItemcode(), mt.getDocqty(), mt.getDocuom(), mt.getOrdamount(),
				mt.getOrdcurrency(), oh.getBuom(), oh.getOrdgruom(), org.getCurro(), oh.getGroumrate(),
				mt.getExchangerate());
		mt.setBuomqty(result.get(0));
		mt.setOrdamount(result.get(1));
		mt.setAmountincurro(result.get(2));
		mt.setPrdqty(result.get(3));
		mt.setPrdamount(result.get(4));
		mt.setExchangerate(result.get(5));

		BigDecimal[] stock;
		BigDecimal stockValueCorrection = new BigDecimal(0), cstockCorrection = new BigDecimal(0);
		List<BigDecimal[]> mtrList;
		if (mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("NO"))
			mtrList = mtr.findCstockStockwithoutBatch(mt.getItemcode(), mt.getPlant(), mt.getDate(), mt.getStocktype(),
					mt.getValsub(), mt.getSaleorder());
		else
			mtrList = mtr.findCstockStockwithBatch(mt.getItemcode(), mt.getPlant(), mt.getDate(), mt.getStocktype(),
					mt.getValsub(), mt.getBatchnumber(), mt.getSaleorder());

		if (mtrList.isEmpty()) {
			mt.setCstock(new BigDecimal(0));
			mt.setStockvalue(new BigDecimal(0));
			System.out.println("CSTOCK = " + mt.getCstock() + " | StockValue = " + mt.getStockvalue());
			// if (mat.getBatch().equalsIgnoreCase("YES") && 	 mat.getBatchcosting().equalsIgnoreCase("NO"))
			//mt.setBstock(mt.getBuomqty());
		} else {
			stock = mtrList.get(0);
			BigDecimal cstock = stock[0];
			BigDecimal stockvalue = stock[1];
			BigDecimal bstock = new BigDecimal(0);
			// if(stock[2]!=null)
			// bstock=stock[2];
			mt.setCstock(cstock);
			if (mt.getBuomqty().compareTo(cstock) == 1) {
				BigDecimal prdRatio = mt.getPrdamount().divide(mt.getPrdqty());
				stockValueCorrection = prdRatio.multiply(mt.getCstock());
				mt.setStockvalue(stockvalue.add(stockValueCorrection));

				System.out.println(
						"CSTOCK from comparision =  " + mt.getCstock() + " | StockValue = " + mt.getStockvalue());
			} else {
				stockValueCorrection = mt.getPrdamount();
				mt.setStockvalue(stockvalue.add(stockValueCorrection));
				
				if (stock[2] != null)
					bstock = stock[2];
				 if (mat.getBatch().equalsIgnoreCase("YES") &&  mat.getBatchcosting().equalsIgnoreCase("NO"))
						 mt.setBstock(mt.getBuomqty().add(bstock));
			}

			System.out.println("CSTOCK = " + mt.getCstock() + " | StockValue = " + mt.getStockvalue());

		}
		// 5th Oct EXCHANGE RATE DIFF
		if (oh.getExchangerate() == null)
			oh.setExchangerate(new BigDecimal(0));

		if (mt.getExchangerate().equals(new BigDecimal(1))) {
			mt.setExchangerate(null);
			mt.setExchangeratediff(null);
		} else
			mt.setExchangeratediff((mt.getExchangerate().subtract(oh.getExchangerate())).multiply(mt.getOrdamount()));
		// 6th Oct unselleted PRD
		mt.setUnsettledprd(mt.getPrdamount().subtract(stockValueCorrection));
		mt.setAdindicator("+");
		// setting prdqty as 0 where prdamount is 0 as per actual costing line item
		// requirement
		if (mt.getPrdamount().doubleValue() == 0)
			mt.setPrdqty(new BigDecimal(0));

		List<Mmtotal> mtrLst;
		if (mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("NO"))
			mtrLst = mtr.findallbyBatchcosting(mt.getItemcode(), mt.getPlant(), mt.getDate(), mt.getStocktype(),
					mt.getValsub(), mt.getSaleorder());
		else
			mtrLst = mtr.findCstockStockvaluebyGRDateandstocktype(mt.getItemcode(), mt.getPlant(), mt.getDate(),
					mt.getStocktype(), mt.getValsub(), mt.getBatchnumber(), mt.getSaleorder());
		for (Mmtotal mmtotal : mtrLst) {
			System.out.println("Updating " + mmtotal.getMmtotalid());
			mmtotal.setCstock(mmtotal.getCstock().add(cstockCorrection));
			mmtotal.setStockvalue(mmtotal.getStockvalue().add(stockValueCorrection));
			if (mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("NO"))
				mt.setBstock(mmtotal.getBstock().add(mt.getBuomqty()));
			mtr.save(mmtotal);
		}
		return mt;
	}

	public Costtotal generateCostTotalContra(Costtotal ct) {
		List<Orderheader> ohList = Ohr.findAllByOrdernum(ct.getObjcode(), ct.getObjitnum());
		oh = ohList.get(0);
		if (ct.getLinkedobjcode() != null) {
			List<Orderheader> LinkedohList = Ohr.findAllByOrdernum(ct.getLinkedobjcode(), ct.getLinkedobjitnum());
			loh = LinkedohList.get(0);

			if (loh.getCostcenter() != null)
				ct.setObjcode(loh.getCostcenter());
		}
		ct.setTrnsevent("IB-CONTRA");
		ct.setCobjtype("CTR");

		List<BigDecimal> result = transUtil.generateTransactionQtyAndAmount(ct.getTrnsevent(), ct.getDate(),
				oh.getOrgncode(), ct.getPlant(), ct.getItemcode(), ct.getDocqty(), ct.getDocuom(), ct.getOrdamount(),
				ct.getOrdcurrency(), oh.getBuom(), oh.getOrdgruom(), org.getCurro(), oh.getGroumrate(),
				ct.getExchangerate());
		ct.setBuomqty(result.get(0));
		ct.setOrdamount(result.get(1));
		ct.setAmountincurro(result.get(2));
		ct.setExchangerate(result.get(5));

		ct.setObjcode(oh.getCostcenter());
		ct.setObjitnum(null);
		ct.setValsub(oh.getValsub());

		if (oh.getLinkedobjitnum() != null)
			ct.setItemcode(oh.getItemcode());
		// ct.setDocitnum(ct.getDocitnum().add(new BigDecimal(10)));
		ct.setPrtnrcode(oh.getObjcode() + oh.getObjitnum());
		if (ct.getItemtype().equals("M")) {
			List<Caval> cvList = cvr.findAllbyOrgnCodeMatValGrp("INV", oh.getOrgncode(), mat.getValgrp());
			caval = cvList.get(0);
			ct.setCostacc(caval.getCostacc());
		} else if (ct.getItemtype().equals("S")) {
			List<Serviceitem> srvcList = sr.findAllbyItemcodeandPlant(ct.getItemcode(), ct.getPlant());
			srvc = srvcList.get(0);
			List<Caval> cvList = cvr.findAllbyOrgnCodeMatValGrp("SR", oh.getOrgncode(), srvc.getValgrp());
			caval = cvList.get(0);
			ct.setCostacc(caval.getCostacc());
		}
		ct.setCostacc(this.caval.getCostacc());
		if (oh.getExchangerate() == null)
			oh.setExchangerate(new BigDecimal(0));
		System.out.println("EXCHANGE RATE =" + ct.getExchangerate());
		if (ct.getExchangerate().equals(new BigDecimal(1))) {
			ct.setExchangerate(null);
			ct.setExchangeratediff(null);
		} else
			ct.setExchangeratediff((ct.getExchangerate().subtract(oh.getExchangerate())).multiply(ct.getOrdamount()));

		return ct;
	}

	public Mmtotal generateMMTotalContra(Mmtotal mt) {

		List<Orderheader> ohList = Ohr.findAllByOrdernum(mt.getObjcode(), mt.getObjitnum());
		oh = ohList.get(0);

		List<Orderheader> LinkedohList = Ohr.findAllByOrdernum(mt.getLinkedobjcode(), mt.getLinkedobjitnum());
		loh = LinkedohList.get(0);
		mt.setItemcode(loh.getItemcode());

		System.out.println(oh.getObjcode() + oh.getValsub() + "oh values");
		System.out.println(loh.getObjcode() + loh.getValsub() + "loh values");
		mt.setBuom(loh.getBuom());

		mt.setTrnsevent("IB-CONTRA");
		List<BigDecimal> result = transUtil.generateTransactionQtyAndAmount(mt.getTrnsevent(), mt.getDate(),
				oh.getOrgncode(), mt.getPlant(), oh.getItemcode(), mt.getDocqty(), mt.getDocuom(), mt.getOrdamount(),
				mt.getOrdcurrency(), oh.getBuom(), oh.getOrdgruom(), org.getCurro(), oh.getGroumrate(),
				mt.getExchangerate());
		mt.setBuomqty(result.get(0));
		mt.setOrdamount(result.get(1));
		mt.setAmountincurro(result.get(2));
		mt.setPrdqty(result.get(3));
		mt.setPrdamount(result.get(4));
		mt.setExchangerate(result.get(5));

		BigDecimal buomqty = transUtil.findBuqty(oh.getObjcode(), oh.getObjitnum(), loh.getObjcode(), loh.getObjitnum(),
				mt.getDocuom(), mt.getDocqty());
		mt.setBuomqty(buomqty);
		mt.setPrdqty(mt.getBuomqty());
		// VALUES BEING DERIVED FOR LOADING OF COAL / FRIEGHT etc.
		// So LinkedObjcode will give us the order which is linked to Loaading /frieght
		// ie COAL

		System.out.println("VAlues of linked ........... " + mt.getLinkedobjcode());
		System.out.println(mt.getLinkedobjitnum());
		System.out.println("orgncode" + oh.getOrgncode());

		List<Material> matList = mr.findAllByMatlcodeandPlantValsub(loh.getItemcode(), loh.getPlant(), loh.getValsub());
		mat = matList.get(0);

		// List<Caval> cvList = cvr.findAllbyOrgnCodeMatValGrp("INV", oh.getOrgncode(),
		// mat.getValgrp());

		// caval = cvList.get(0);
		// mt.setCostacc(ct.getCostacc());

		// COSTACC SAME AS IN IB - 27TH DEC 2020
		List<Serviceitem> srvcList = sr.findAllbyItemcodeandPlant(oh.getItemcode(), oh.getPlant());
		srvc = srvcList.get(0);
		List<Caval> cvList = cvr.findAllbyOrgnCodeMatValGrp("SR", oh.getOrgncode(), srvc.getValgrp());
		caval = cvList.get(0);
		mt.setCostacc(caval.getCostacc());

		mt.setValsub(loh.getValsub());
		if (mt.getBatchnumber() == null)
			mt.setBatchnumber(loh.getBatchnumber());
		mt.setStocktype("O");
		mt.setObjcode(loh.getObjcode());
		mt.setObjitnum(loh.getObjitnum());
		mt.setLinkedobjcode(oh.getObjcode());
		mt.setLinkedobjitnum(oh.getObjitnum());

		mt.setAdindicator("+");
		BigDecimal[] stock;
		List<BigDecimal[]> mtrList;
		if (mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("NO"))
			mtrList = mtr.findCstockStockwithoutBatch(mt.getItemcode(), mt.getPlant(), mt.getDate(), mt.getStocktype(),
					mt.getValsub(), mt.getSaleorder());
		else
			mtrList = mtr.findCstockStockwithBatch(mt.getItemcode(), mt.getPlant(), mt.getDate(), mt.getStocktype(),
					mt.getValsub(), mt.getBatchnumber(), mt.getSaleorder());

		BigDecimal stockValueCorrection = new BigDecimal(0), cstockCorrection = new BigDecimal(0);
		BigDecimal cstock = new BigDecimal(0);
		BigDecimal stockvalue = new BigDecimal(0);
		BigDecimal bstock = new BigDecimal(0);
		if (!mtrList.isEmpty()) {

			stock = mtrList.get(0);
			cstock = stock[0];
			stockvalue = stock[1];
			if (stock[2] != null)
				bstock = stock[2];
		}
		System.out.println("cstock and stockvaLue from else block " + cstock + "  " + stockvalue);
		mt.setCstock(cstock);
		// mt.setBstock(bstock);
		if (mt.getBuomqty().compareTo(cstock) == 1) {
			BigDecimal temp = (mt.getPrdamount().divide(mt.getPrdqty(), 2, RoundingMode.HALF_DOWN));
			stockValueCorrection = temp.multiply(mt.getCstock());
			mt.setStockvalue(stockvalue.add(stockValueCorrection));
			if (mat.getBatch().equalsIgnoreCase("YES") &&
			 mat.getBatchcosting().equalsIgnoreCase("NO"))
			mt.setBstock(mt.getBstock().add(mt.getBuomqty()));
			System.out
					.println("CSTOCK from comparision =  " + mt.getCstock() + " | StockValue = " + mt.getStockvalue());
		} else {
			stockValueCorrection = mt.getPrdamount();
			mt.setStockvalue(stockvalue.add(stockValueCorrection));
			
			// mt.setBstock(mt.getBstock().add(bstock));
		}

		// 5th Oct EXCHANGE RATE DIFF
		// List<Orderheader> ohList1 = Ohr.findAllByOrdernum(OrginalObjcode,
		// OriginalObjitnum);
		// oh = ohList1.get(0);
		if (oh.getExchangerate() == null)
			oh.setExchangerate(new BigDecimal(0));

		if (mt.getExchangerate().equals(new BigDecimal(1))) {
			mt.setExchangerate(null);
			mt.setExchangeratediff(null);
		} else
			mt.setExchangeratediff((mt.getExchangerate().subtract(oh.getExchangerate())).multiply(mt.getOrdamount()));
		// 6th Oct unselleted PRD
		mt.setUnsettledprd(mt.getPrdamount().subtract(stockValueCorrection));

		// setting prdqty as 0 where prdamount is 0 as per actual costing line item
		// requirement
		if (mt.getPrdamount().doubleValue() == 0)
			mt.setPrdqty(new BigDecimal(0));

		mt.setCobjtype(loh.getObjtype());
		List<Mmtotal> mtrLst;
		if (mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("NO"))
			mtrLst = mtr.findallbyBatchcosting(mt.getItemcode(), mt.getPlant(), mt.getDate(), mt.getStocktype(),
					mt.getValsub(), mt.getSaleorder());
		else
			mtrLst = mtr.findCstockStockvaluebyGRDateandstocktype(mt.getItemcode(), mt.getPlant(), mt.getDate(),
					mt.getStocktype(), mt.getValsub(), mt.getBatchnumber(), mt.getSaleorder());
		for (Mmtotal mmtotal : mtrLst) {
			System.out.println("Updating " + mmtotal.getMmtotalid());
			mmtotal.setCstock(mmtotal.getCstock().add(cstockCorrection));
			mmtotal.setStockvalue(mmtotal.getStockvalue().add(stockValueCorrection));
			if (mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("NO"))
				mt.setBstock(mmtotal.getBstock().add(mt.getBuomqty()));
			mtr.save(mmtotal);
		}
		return mt;
	}

}
