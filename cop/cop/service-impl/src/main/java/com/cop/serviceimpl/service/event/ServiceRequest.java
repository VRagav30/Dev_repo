package com.cop.serviceimpl.service.event;

import java.math.BigDecimal;
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
import com.cop.repository.transaction.NumberRangeRepository;
import com.cop.repository.transaction.OrderHeaderRepository;
import com.cop.repository.transaction.OrganisationRepository;
import com.cop.repository.transaction.PlantRepository;
import com.cop.repository.transaction.ServiceRepository;
import com.cop.serviceapi.service.CostTotalService;
import com.cop.utils.TransactionUtils;

@Component
public class ServiceRequest {
	@Autowired
	TransactionUtils transUtil;
	@Autowired
	OrganisationRepository or;
	@Autowired
	CavalRepository cvr;
	@Autowired
	CostTotalRepository ctr;
	@Autowired
	MMTotalRespository mtr;
	@Autowired
	PlantRepository pr;
	@Autowired
	OrderHeaderRepository Ohr;
	@Autowired
	FydPeriodRepository fpr;

	@Autowired
	AltuomRepository mar;
	@Autowired
	CurrencyExchangeRateRepository cerr;
	@Autowired
	NumberRangeRepository nrr;
	@Autowired
	CavalRepository cr;
	@Autowired
	ServiceRepository sr;
	@Autowired
	MaterialRepository mr;
	private Organisation org;
	private Orderheader oh, loh;
	private Altuom matalt;
	private Serviceitem srvc;
	private Caval caval;
	private Material mat;
	@Autowired
	CostTotalService cs;
	private Currencyexchangerate cer;

	public Costtotal generateCostTotal(Costtotal ct)

	{
		ct = cs.initializeCostTotal(ct);
		List<Organisation> orgList = or.findAllByOrgnCode(ct.getOrgncode());
		org = orgList.get(0);

		List<Orderheader> ohList = Ohr.findAllByOrdernum(ct.getObjcode(), ct.getObjitnum());
		oh = ohList.get(0);

		List<Serviceitem> srvcList = sr.findAllbyItemcodeandPlant(ct.getItemcode(), ct.getPlant());
		srvc = srvcList.get(0);

		/*--------------------------------ALTOUM-----------------------------------------*/
		System.out.println(oh.getPlant() + " " + ct.getItemcode() + " " + ct.getDocuom() + " " + srvc.getBuom());
		List<Altuom> altList = mar.findAltuombyMatCodePlantAltUOM(oh.getPlant(), ct.getItemcode(), ct.getDocuom(),
				srvc.getBuom());
		matalt = altList.get(0);

		ct.setValsub(oh.getValsub());
		System.out.println("BUOM from MATALT" + matalt.getBuom().toUpperCase());
		System.out.println("ct docqty" + ct.getDocqty());

		List<BigDecimal> result = transUtil.generateTransactionQtyAndAmount(ct.getTrnsevent(), ct.getDate(),
				oh.getOrgncode(), ct.getPlant(), ct.getItemcode(), ct.getDocqty(), ct.getDocuom(), ct.getOrdamount(),
				ct.getOrdcurrency(), oh.getBuom(), oh.getOrdgruom(), org.getCurro(), oh.getGroumrate(),
				ct.getExchangerate());
		ct.setBuomqty(result.get(0));
		ct.setOrdamount(result.get(1));
		ct.setAmountincurro(result.get(2));
		ct.setExchangerate(result.get(5));
		ct.setStocktype(null);

		ct.setPrtnrcode(oh.getCostcenter());
		// CostACC
		System.out.println("trnsevent is " + ct.getTrnsevent() + "orgncode is " + oh.getOrgncode() + " valgroup is "
				+ srvc.getValgrp());
		List<Caval> cvList = cvr.findAllbyOrgnCodeMatValGrp(ct.getTrnsevent(), oh.getOrgncode(), srvc.getValgrp());
		caval = cvList.get(0);
		ct.setCostacc(caval.getCostacc());
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

	public Costtotal generateCostTotalContra(Costtotal ct) {
		List<Orderheader> ohList = Ohr.findAllByOrdernum(ct.getObjcode(), ct.getObjitnum());
		oh = ohList.get(0);

		// ct.setCostid(null);
		ct.setTrnsevent("SR-CONTRA");
		ct.setCobjtype("CTR");
		ct.setObjcode(oh.getCostcenter());

		ct.setObjitnum(null);

		if (oh.getLinkedobjitnum() != null)
			ct.setItemcode(oh.getItemcode());
		// ct.setDocitnum(ct.getDocitnum().add(new BigDecimal(10)));
		ct.setPrtnrcode(oh.getObjcode() + oh.getObjitnum());

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

		List<Orderheader> LinkedohList = Ohr.findAllByOrdernum(oh.getLinkedobjcode(), oh.getLinkedobjitnum());
		loh = LinkedohList.get(0);
		mt.setItemcode(loh.getItemcode());
		mt.setValsub(loh.getValsub());
		mt.setBatchnumber(loh.getBatchnumber());
		mt.setBuom(loh.getBuom());
		List<Material> matList = mr.findAllByMatlcodeandPlantValsub(loh.getItemcode(), loh.getPlant(), loh.getValsub());
		mat = matList.get(0);

		mt.setTrnsevent("SR-Contra");

		// mt.setDocitnum(mt.getDocitnum().add(new BigDecimal(10)));
		mt.setAdindicator("+");

		mt.setStocktype("O");
		mt.setObjcode(loh.getObjcode());
		mt.setObjitnum(loh.getObjitnum());
		mt.setLinkedobjcode(oh.getObjcode());
		mt.setLinkedobjitnum(oh.getObjitnum());
		mt.setPrdamount(new BigDecimal(0));
		mt.setPrdqty(new BigDecimal(0));
		List<Caval> cvList = cvr.findAllbyOrgnCodeMatValGrp("INV", oh.getOrgncode(), mat.getValgrp());
		caval = cvList.get(0);
		mt.setCostacc(caval.getCostacc());
		List<Orderheader> linkedOhList = Ohr.findAllByOrdernum(mt.getLinkedobjcode(), mt.getLinkedobjitnum());
		oh = linkedOhList.get(0);

		BigDecimal[] stock;
		List<BigDecimal[]> mtrList;
		if (mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("NO"))
			mtrList = mtr.findCstockStockwithoutBatch(mt.getItemcode(), mt.getPlant(), mt.getDate(), mt.getStocktype(),
					mt.getValsub(), mt.getSaleorder());
		else
			mtrList = mtr.findCstockStockwithBatch(mt.getItemcode(), mt.getPlant(), mt.getDate(), mt.getStocktype(),
					mt.getValsub(), mt.getBatchnumber(), mt.getSaleorder());
		BigDecimal stockValueCorrection = mt.getAmountincurro();

		if (mtrList.isEmpty()) {
			mt.setCstock(mt.getBuomqty());
			mt.setStockvalue(mt.getAmountincurro());
			if (mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("NO"))
				mt.setBstock(mt.getBuomqty());
			System.out.println("CSTOCK = " + mt.getCstock() + " | StockValue = " + mt.getStockvalue());

		} else {
			stock = mtrList.get(0);
			BigDecimal cstock = stock[0];
			BigDecimal stockvalue = stock[1];
			BigDecimal bstock = new BigDecimal(0);
			if (stock[2] != null)
				bstock = stock[2];
			System.out.println("SR contra " + cstock + "  " + stockvalue + " " + mt.getItemcode());
			mt.setCstock(cstock);
			mt.setStockvalue(stockvalue.add(stockValueCorrection));
			if (mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("NO"))
				mt.setBstock(mt.getBuomqty().add(bstock));
			System.out.println("CSTOCK = " + mt.getCstock() + " | StockValue = " + mt.getStockvalue());

		}

		List<Orderheader> oList = Ohr.findAllByOrdernum(oh.getObjcode(), oh.getObjitnum());
		oh = oList.get(0);
		List<Currencyexchangerate> cerList = this.cerr.findAllbyOrgncodeNdCurrency(oh.getOrgncode(),
				mt.getOrdcurrency(), this.org.getCurro(), new java.sql.Date((mt.getDate()).getTime()));
		cer = cerList.get(0);

		if (mt.getExchangerate() == null) {
			mt.setExchangerate(cer.getTcurrv().divide(cer.getScnos()));
		}
		if (oh.getExchangerate() == null)
			oh.setExchangerate(new BigDecimal(0));

		if (mt.getExchangerate().equals(new BigDecimal(1))) {
			mt.setExchangerate(null);
			mt.setExchangeratediff(null);
		} else
			mt.setExchangeratediff((mt.getExchangerate().subtract(oh.getExchangerate())).multiply(mt.getOrdamount()));

		// PRDAMOUNT
		mt.setPrdamount(mt.getAmountincurro());
		List<Mmtotal> mtrLst;
		if (mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("NO"))
			mtrLst = mtr.findallbyBatchcosting(mt.getItemcode(), mt.getPlant(), mt.getDate(), mt.getStocktype(),
					mt.getValsub(), mt.getSaleorder());
		else
			mtrLst = mtr.findCstockStockvaluebyGRDateandstocktype(mt.getItemcode(), mt.getPlant(), mt.getDate(),
					mt.getStocktype(), mt.getValsub(), mt.getBatchnumber(), mt.getSaleorder());
		for (Mmtotal mmtotal : mtrLst) {
			System.out.println("Updating " + mmtotal.getMmtotalid());
			mmtotal.setCstock(mmtotal.getCstock());
			mmtotal.setStockvalue(mmtotal.getStockvalue().add(stockValueCorrection));
			if (mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("NO")) {
				mt.setBstock(mmtotal.getBstock().add(mt.getBuomqty()));}
			mtr.save(mmtotal);
		}
		return mt;
	}

}
