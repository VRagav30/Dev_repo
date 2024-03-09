package com.cop.serviceimpl.service.event;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cop.model.database.Altuom;
import com.cop.model.database.Caval;
import com.cop.model.database.Costtotal;
import com.cop.model.database.Currencyexchangerate;
import com.cop.model.database.Fydperiod;
import com.cop.model.database.Material;
import com.cop.model.database.Mmtotal;
import com.cop.model.database.Mrc;
import com.cop.model.database.Orderheader;
import com.cop.model.database.Organisation;
import com.cop.model.database.Serviceitem;
import com.cop.repository.transaction.AltuomRepository;
import com.cop.repository.transaction.CavalRepository;
import com.cop.repository.transaction.CurrencyExchangeRateRepository;
import com.cop.repository.transaction.FydPeriodRepository;
import com.cop.repository.transaction.MMTotalRespository;
import com.cop.repository.transaction.MaterialRepository;
import com.cop.repository.transaction.MrcRepository;
import com.cop.repository.transaction.OrderHeaderRepository;
import com.cop.repository.transaction.OrganisationRepository;
import com.cop.repository.transaction.PlantRepository;
import com.cop.repository.transaction.ServiceRepository;
import com.cop.serviceapi.service.CostTotalService;
import com.cop.serviceapi.service.MMTotalService;
import com.cop.utils.TransactionUtils;

@Component
public class PurchaseDNVR {

	Costtotal ct;
	@Autowired
	PlantRepository pr;
	@Autowired
	FydPeriodRepository fpr;
	@Autowired
	MMTotalRespository mtr;
	@Autowired
	AltuomRepository mar;
	@Autowired
	OrderHeaderRepository Ohr;
	@Autowired
	OrganisationRepository or;
	@Autowired
	CurrencyExchangeRateRepository cerr;
	@Autowired
	CavalRepository cvr;
	@Autowired
	MaterialRepository mr;
	@Autowired
	CostTotalService cs;
	@Autowired
	MrcRepository mrcr;
	@Autowired
	ServiceRepository sr;
	private Serviceitem srvc;
	@Autowired
	MMTotalService mmts;
	@Autowired
	TransactionUtils transUtil;

	private Caval caval;
	private Material mat;
	private Currencyexchangerate cer;
	private Organisation org;
	private Orderheader oh, loh;
	Altuom matalt;

	public Costtotal generateCostTotal(Costtotal ct) {
		List<Orderheader> ohList = Ohr.findAllByOrdernum(ct.getObjcode(), ct.getObjitnum());
		oh = ohList.get(0);
		if (ct.getItemtype().equals("M")) {
			if (oh.getSoassignment() != null)
				ct.setStocktype("S");
			else
				ct.setStocktype("O");
		}
		ct.setBuom(oh.getBuom());
		// derive from orderheader using ordernum/objcode and objitnum
		ct.setOrgncode(oh.getOrgncode());

		// derive from orderheader using ordernum/objcode and objitnum
		ct.setPlant(oh.getPlant());
		ct.setDocuom(null);
		ct.setDocqty(null);
		if (oh.getValsub() != null)
			ct.setValsub(oh.getValsub());
		if (oh.getLinkedobjcode() != null)
			ct.setLinkedobjcode(oh.getLinkedobjcode());
		if (oh.getLinkedobjitnum() != null)
			ct.setLinkedobjitnum(oh.getLinkedobjitnum());
		// Equal to system year or system year minus one year
		if ((ct.getPlant() != null) && (ct.getDate() != null)) {

			String plant = ct.getPlant();
			String orgCode = pr.findOrgCodeByPlantId(plant);
			System.out.println(orgCode);
			String fydcode = or.findFydCodeByOrgnCode(orgCode);
			System.out.println(fydcode);
			java.sql.Date d = new java.sql.Date((ct.getDate()).getTime());
			System.out.println(d);
			List<Fydperiod> fp = fpr.findAllbyDate(fydcode, d);
			System.out.println(fp.get(0).getPeriod());
			ct.setYear((fp.get(0).getYear()));
			ct.setPeriod(fp.get(0).getPeriod());
		}

		// As per OBJCODE - OBJITNUM vendor
		ct.setVendor(oh.getVendor());

		// ORDCURRENCY
		ct.setOrdcurrency(oh.getOrdcurrency()); // derive from order header

		//
		if (ct.getItemcode() == null)
			ct.setItemcode(oh.getItemcode());

		if (oh.getCostcenter() != null)
			ct.setPrtnrcode(oh.getCostcenter());

		/*------------------------------------Derived------------------------------*/

		ct.setUsercode("SYSTEM"); // hardcode as SYSTEM - not needed from excel

		// time stamp
		ct.setCreatedtime(new Timestamp(new Date().getTime()));

		if (ct.getItemtype().equals("M")) {
			List<Material> matList = mr.findAllByMatlcodeandPlantValsub(ct.getItemcode(), ct.getPlant(),
					ct.getValsub());
			mat = matList.get(0);

			List<Caval> cvList = cvr.findAllbyOrgnCodeMatValGrp("INV", oh.getOrgncode(), mat.getValgrp());
			caval = cvList.get(0);
			ct.setCostacc(caval.getCostacc());

		} else if (ct.getItemtype().equals("S")) {
			List<Serviceitem> srvcList = sr.findAllbyItemcodeandPlant(ct.getItemcode(), ct.getPlant());
			srvc = srvcList.get(0);

			List<Caval> cvList = cvr.findAllbyOrgnCodeMatValGrp("SR", oh.getOrgncode(), srvc.getValgrp());
			ct.setCostacc(cvList.get(0).getCostacc());
		}
		List<Organisation> orgList = this.or.findAllByOrgnCode(ct.getOrgncode());
		this.org = orgList.get(0);

		// ExchangeRate 5th OCT
		List<Currencyexchangerate> cerList = this.cerr.findAllbyOrgncodeNdCurrency(ct.getOrgncode(),
				ct.getOrdcurrency(), this.org.getCurro(), new java.sql.Date((ct.getDate()).getTime()));
		this.cer = cerList.get(0);

		if (ct.getExchangerate() == null)
			ct.setExchangerate(this.cer.getTcurrv().divide(this.cer.getScnos()));

		// ORDAMOUNT and amount incurro
		if (ct.getOrdamount() != null)

			if (ct.getOrdcurrency().equals(this.org.getCurro())) {
				ct.setAmountincurro(ct.getOrdamount());
			} else {

				ct.setAmountincurro(ct.getOrdamount().multiply(ct.getExchangerate()));
			}

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
		// 5th Oct adding code for Er eRD unselleted PRD
		// EXCHANGE RATE DIFF
		if (oh.getExchangerate() == null)
			oh.setExchangerate(new BigDecimal(0));

		if (ct.getExchangerate().equals(new BigDecimal(1))) {
			ct.setExchangerate(null);
			ct.setExchangeratediff(null);
		} else
			ct.setExchangeratediff((ct.getExchangerate().subtract(oh.getExchangerate())).multiply(ct.getOrdamount()));

		return ct;
	}

	public Mmtotal generateMMTotal(Mmtotal mt) {

		if (mt.getObjcode() != null) {
			List<Orderheader> ohList = Ohr.findAllByOrdernum(mt.getObjcode(), mt.getObjitnum());
			oh = ohList.get(0);
		}

		if (mt.getObjcode() != null) {
			if (mt.getCobjtype().equalsIgnoreCase(oh.getObjtype()))
				System.out.println("Correct objtype");
			else
				mt.setCobjtype(oh.getObjtype());
		}
		List<Mrc> MrcList = mrcr.findAllbyItemcodePlantDateMpricetyp(mt.getItemcode(), mt.getPlant(), mt.getDate());

		// BigDecimal groumrate = transUtil.unitConvertor(mt.getPlant(),
		// mt.getItemcode(), mat.getBuom(), oh.getOrdgruom(),
		// mt.getDocuom(), oh.getGroumrate());

		if (mt.getPlant() == null)
			mt.setPlant(oh.getPlant());
		if (mt.getItemcode() == (null))
			mt.setItemcode(oh.getItemcode());

		// Equal to system year or system year minus one year
		if ((mt.getPlant() != null) && (mt.getDate() != null)) {

			String plant = mt.getPlant();
			String orgCode = pr.findOrgCodeByPlantId(plant);
			System.out.println(orgCode);
			String fydcode = or.findFydCodeByOrgnCode(orgCode);
			System.out.println(fydcode);
			java.sql.Date d = new java.sql.Date((mt.getDate()).getTime());
			System.out.println(d);
			List<Fydperiod> fp = fpr.findAllbyDate(fydcode, d);
			System.out.println(fp.get(0).getPeriod());
			mt.setYear((fp.get(0).getYear()));
			mt.setPeriod(fp.get(0).getPeriod());
		}
		mt.setValsub(oh.getValsub());
		// DOCQTY

		// BUOM
		mt.setBuom(oh.getBuom());
		List<Material> matList = mr.findAllByMatlcodeandPlantValsub(mt.getItemcode(), mt.getPlant(), oh.getValsub());
		mat = matList.get(0);

		mt.setUsercode("SYSTEM");
		mt.setCreatedtime(new Timestamp(new Date().getTime()));
		// 5th Oct adding code for Er eRD unselleted PRD
		List<Currencyexchangerate> cerList = this.cerr.findAllbyOrgncodeNdCurrency(oh.getOrgncode(),
				mt.getOrdcurrency(), this.org.getCurro(), new java.sql.Date((mt.getDate()).getTime()));
		this.cer = cerList.get(0);
		if (mt.getExchangerate() == null)
			mt.setExchangerate(cer.getTcurrv().divide(cer.getScnos()));

		if (mt.getOrdamount() != null) {
			if (mt.getOrdcurrency().equals(this.org.getCurro())) {
				mt.setAmountincurro(mt.getOrdamount());
			} else {

				mt.setAmountincurro(mt.getOrdamount().multiply(mt.getExchangerate()));
			}
		}
		// EXCHANGE RATE DIFF
		if (oh.getExchangerate() == null)
			oh.setExchangerate(new BigDecimal(0));

		if (mt.getExchangerate().equals(new BigDecimal(1))) {
			mt.setExchangerate(null);
			mt.setExchangeratediff(null);
		} else

			mt.setExchangeratediff((mt.getExchangerate().subtract(oh.getExchangerate())).multiply(mt.getOrdamount()));

		mt.setOrdcurrency(oh.getOrdcurrency());
		// CostACc
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
		// saleorder
		mt.setSaleorder(oh.getSoassignment());
		// stocktype
		if (mt.getSaleorder() != null)
			mt.setStocktype("S");
		else
			mt.setStocktype("O");

		// prdAmount
		mt.setPrdamount(mt.getAmountincurro());

		mt.setPrdqty(new BigDecimal(0));

		// CSTOCK and StockValue

		// BigDecimal[] stock;
		// List<BigDecimal[]> mtrList =
		// mtr.findCstockbyPlantandstocktype(mt.getItemcode(), mt.getPlant(),
		// mt.getDate(),
		// "O");

		BigDecimal[] stock;

		List<BigDecimal[]> mtrList;
		if (mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("NO"))
			mtrList = mtr.findCstockStockwithoutBatch(mt.getItemcode(), mt.getPlant(), mt.getDate(), mt.getStocktype(),
					mt.getValsub(), mt.getSaleorder());
		else
			mtrList = mtr.findCstockStockwithBatch(mt.getItemcode(), mt.getPlant(), mt.getDate(), mt.getStocktype(),
					mt.getValsub(), mt.getBatchnumber(), mt.getSaleorder());

		if (mtrList.isEmpty()) {
			mt.setCstock(new BigDecimal(0));
			mt.setStockvalue(mt.getAmountincurro().negate());
			System.out.println("CSTOCK = " + mt.getCstock() + " | StockValue = " + mt.getStockvalue());
		} else {
			stock = mtrList.get(0);
			BigDecimal cstock = stock[0];
			BigDecimal stockvalue = stock[1];
			mt.setCstock(cstock);
			mt.setStockvalue(stockvalue.subtract(mt.getAmountincurro()));
			System.out.println("CSTOCK = " + mt.getCstock() + " | StockValue = " + mt.getStockvalue());
			BigDecimal bstock = new BigDecimal(0);
			if (stock[2] != null)
				bstock = stock[2];
			 if (mat.getBatch().equalsIgnoreCase("YES") &&  mat.getBatchcosting().equalsIgnoreCase("NO"))
					 mt.setBstock(mt.getBuomqty().subtract(bstock));

		}
		mt.setAdindicator("-");

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
			mmtotal.setCstock(mmtotal.getCstock().subtract(mt.getBuomqty()));
			mmtotal.setStockvalue(mmtotal.getStockvalue().subtract(mt.getStockvalue()));
			System.out.println("NEW : CSTOCK = " + mmtotal.getCstock() + " | StockValue = " + mmtotal.getStockvalue());
			if (mt.getBatchnumber() != null && mat.getBatch().equalsIgnoreCase("YES")
					&& mat.getBatchcosting().equalsIgnoreCase("NO"))
				mt.setBstock(mmtotal.getBstock().subtract(mt.getBuomqty()));
			mtr.save(mmtotal);
		}
		return mt;
	}

	public Costtotal generateCostTotalContra(Costtotal ct) {

		List<Orderheader> ohList = this.Ohr.findAllByOrdernum(ct.getObjcode(), ct.getObjitnum());
		this.oh = ohList.get(0);
		// BigDecimal groumrate = transUtil.unitConvertor(ct.getPlant(),
		// ct.getItemcode(), oh.getBuom(), oh.getOrdgruom(),
		// ct.getDocuom(), oh.getGroumrate());
		ct.setObjcode(oh.getCostcenter());
		ct.setCobjtype("CTR");
		ct.setTrnsevent("DNVR-CONTRA");
		ct.setBuom(oh.getBuom());

		// 5th Oct adding ER and ER dIFF
		List<Currencyexchangerate> cerList = this.cerr.findAllbyOrgncodeNdCurrency(ct.getOrgncode(),
				ct.getOrdcurrency(), this.org.getCurro(), new java.sql.Date((ct.getDate()).getTime()));
		this.cer = cerList.get(0);
		if (ct.getExchangerate() == null)
			ct.setExchangerate(cer.getTcurrv().divide(cer.getScnos()));
		if (ct.getOrdamount() != null)

			if (ct.getOrdcurrency().equals(this.org.getCurro())) {
				ct.setAmountincurro(ct.getOrdamount());
			} else {
				System.out.println(
						"ct.getOrgncode(), ct.getOrdcurrency(), org.getCurro(), new java.sql.Date((ct.getDate()).getTime() "
								+ ct.getOrgncode() + ct.getOrdcurrency() + this.org.getCurro() + ct.getDate());

				ct.setAmountincurro(ct.getOrdamount().multiply(ct.getExchangerate()));
			}
		// EXCHANGE RATE DIFF
		if (oh.getExchangerate() == null)
			oh.setExchangerate(new BigDecimal(0));

		if (ct.getExchangerate().equals(new BigDecimal(1))) {
			ct.setExchangerate(null);
			ct.setExchangeratediff(null);
		} else
			ct.setExchangeratediff((ct.getExchangerate().subtract(oh.getExchangerate())).multiply(ct.getOrdamount()));

		ct.setValsub(oh.getValsub());
		String partnerCode = oh.getObjcode() + "" + oh.getObjitnum();
		ct.setPrtnrcode(partnerCode);
		ct.setDocnumber(ct.getDocnumber());
		// ct.setDocitnum(ct.getDocitnum().add(new BigDecimal(10)));
		List<Caval> cvList = cvr.findAllbyOrgnCodeMatValGrp("SR", oh.getOrgncode(), srvc.getValgrp());
		System.out.println(srvc.getValgrp());
		caval = cvList.get(0);
		ct.setCostacc(caval.getCostacc());
		ct.setObjitnum(null);

		if (ct.getLinkedobjcode() != null) {
			List<Orderheader> LinkedohList = Ohr.findAllByOrdernum(ct.getLinkedobjcode(), ct.getLinkedobjitnum());
			loh = LinkedohList.get(0);

			if (loh.getCostcenter() != null)
				ct.setObjcode(loh.getCostcenter());
		}
		return ct;

	}

	public Mmtotal generateMMTotalContra(Mmtotal mt) {
		List<Orderheader> ohList = Ohr.findAllByOrdernum(mt.getObjcode(), mt.getObjitnum());
		oh = ohList.get(0);

		List<Orderheader> LinkedohList = Ohr.findAllByOrdernum(mt.getLinkedobjcode(), mt.getLinkedobjitnum());
		loh = LinkedohList.get(0);

		mt.setBuom(loh.getBuom());

		System.out.println("orgncode" + oh.getOrgncode());

		mt.setTrnsevent("DNVR-CONTRA");

		List<Material> matList = mr.findAllByMatlcodeandPlantValsub(loh.getItemcode(), loh.getPlant(), loh.getValsub());
		mat = matList.get(0);
		/*
		 * List<Caval> cvList = cvr.findAllbyOrgnCodeMatValGrp("INV", loh.getOrgncode(),
		 * mat.getValgrp());
		 * 
		 * caval = cvList.get(0); mt.setCostacc(caval.getCostacc());
		 */

		// COSTACC SAME AS IN DNVR - 27TH DEC 2020
		List<Serviceitem> srvcList = sr.findAllbyItemcodeandPlant(oh.getItemcode(), oh.getPlant());
		srvc = srvcList.get(0);
		List<Caval> cvList = cvr.findAllbyOrgnCodeMatValGrp("SR", oh.getOrgncode(), srvc.getValgrp());
		caval = cvList.get(0);
		mt.setCostacc(caval.getCostacc());
		mt.setValsub(loh.getValsub());
		if (mt.getBatchnumber() == null)
			mt.setBatchnumber(loh.getBatchnumber());

		mt.setItemcode(loh.getItemcode());
		mt.setStocktype("O");
		mt.setObjcode(loh.getObjcode());
		mt.setObjitnum(loh.getObjitnum());

		mt.setLinkedobjcode(oh.getObjcode());
		mt.setLinkedobjitnum(oh.getObjitnum());

		List<Currencyexchangerate> cerList = this.cerr.findAllbyOrgncodeNdCurrency(loh.getOrgncode(),
				mt.getOrdcurrency(), this.org.getCurro(), new java.sql.Date((mt.getDate()).getTime()));
		this.cer = cerList.get(0);
		if (mt.getExchangerate() == null)
			mt.setExchangerate(cer.getTcurrv().divide(this.cer.getScnos()));

		if (mt.getOrdamount() != null)
			if (mt.getOrdcurrency().equals(this.org.getCurro())) {
				mt.setAmountincurro(mt.getOrdamount());
			} else {

				mt.setAmountincurro(mt.getOrdamount().multiply(mt.getExchangerate()));
			}

		// EXCHANGE RATE DIFF

		if (oh.getExchangerate() == null)
			oh.setExchangerate(new BigDecimal(0));

		if (mt.getExchangerate().equals(new BigDecimal(1))) {
			mt.setExchangerate(null);
			mt.setExchangeratediff(null);
		} else
			mt.setExchangeratediff((mt.getExchangerate().subtract(oh.getExchangerate())).multiply(mt.getOrdamount()));

		// mt.setPrdqty(mt.getBuomqty());
		mt.setPrdamount(mt.getAmountincurro());
		System.out.println("orgncode" + oh.getOrgncode());

		mt.setAdindicator("-");
		BigDecimal[] stock;

		mt.setCobjtype(loh.getObjtype());

		List<BigDecimal[]> mtrList;
		if (mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("NO"))
			mtrList = mtr.findCstockStockwithoutBatch(mt.getItemcode(), mt.getPlant(), mt.getDate(), mt.getStocktype(),
					mt.getValsub(), mt.getSaleorder());
		else
			mtrList = mtr.findCstockStockwithBatch(mt.getItemcode(), mt.getPlant(), mt.getDate(), mt.getStocktype(),
					mt.getValsub(), mt.getBatchnumber(), mt.getSaleorder());

		/*
		 * //remodified code to check fetch cost by batch irrespective costing status on
		 * 9/6/21 List<BigDecimal[]> mtrList =
		 * mtr.findCstockbyItemcodePlantValsubBatch(mt.getItemcode(), mt.getPlant(),
		 * mt.getDate(), mt.getValsub(), mt.getBatchnumber(),mt.getSaleorder());
		 */

		if (mtrList.isEmpty()) {
			mt.setCstock(mt.getBuomqty());
			mt.setStockvalue(mt.getPrdamount());
			System.out.println("CSTOCK = " + mt.getCstock() + " | StockValue = " + mt.getStockvalue());

		} else {
			stock = mtrList.get(0);
			BigDecimal cstock = stock[0];
			BigDecimal stockvalue = stock[1];
			mt.setCstock(cstock);
			mt.setStockvalue(stockvalue.subtract(mt.getPrdamount()));
			System.out.println("CSTOCK = " + mt.getCstock() + " | StockValue = " + mt.getStockvalue());
			BigDecimal bstock = new BigDecimal(0);
			if (stock[2] != null)
				bstock = stock[2];
			 if (mat.getBatch().equalsIgnoreCase("YES") &&  mat.getBatchcosting().equalsIgnoreCase("NO"))
					 mt.setBstock(mt.getBuomqty().subtract(bstock));

		}

		List<Mmtotal> mtrLst;
		if (mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("NO"))
			mtrLst = mtr.findallbyBatchcosting(mt.getItemcode(), mt.getPlant(), mt.getDate(), mt.getStocktype(),
					mt.getValsub(), mt.getSaleorder());
		else
			mtrLst = mtr.findCstockStockvaluebyGRDateandstocktype(mt.getItemcode(), mt.getPlant(), mt.getDate(),
					mt.getStocktype(), mt.getValsub(), mt.getBatchnumber(), mt.getSaleorder());
		for (Mmtotal mmtotal : mtrLst) {
			System.out.println("Updating " + mmtotal.getMmtotalid());
			// mmtotal.setCstock(mmtotal.getCstock());
			mmtotal.setStockvalue(mmtotal.getStockvalue().subtract(mt.getStockvalue()));
			if (mt.getBatchnumber() != null && mat.getBatch().equalsIgnoreCase("YES")
					&& mat.getBatchcosting().equalsIgnoreCase("NO"))
				mt.setBstock(mmtotal.getBstock().subtract(mt.getBuomqty()));
			mtr.save(mmtotal);
		}
		return mt;

	}
}
