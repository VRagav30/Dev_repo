package com.cop.serviceimpl.service.event;

import java.math.BigDecimal;
import com.cop.utils.TransactionUtils;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cop.model.database.Altuom;
import com.cop.model.database.Caval;
import com.cop.model.database.Costtotal;
import com.cop.model.database.Currencyexchangerate;
import com.cop.model.database.Material;
import com.cop.model.database.Mmtotal;
import com.cop.model.database.Mrc;
import com.cop.model.database.Orderheader;
import com.cop.model.database.Organisation;
import com.cop.model.database.Serviceitem;
import com.cop.repository.transaction.AltuomRepository;
import com.cop.repository.transaction.CavalRepository;
import com.cop.repository.transaction.CurrencyExchangeRateRepository;
import com.cop.repository.transaction.MMTotalRespository;
import com.cop.repository.transaction.MaterialRepository;
import com.cop.repository.transaction.MrcRepository;
import com.cop.repository.transaction.OrderHeaderRepository;
import com.cop.repository.transaction.OrganisationRepository;
import com.cop.repository.transaction.ServiceRepository;
import com.cop.serviceapi.service.CostTotalService;
import com.cop.serviceapi.service.MMTotalService;
import com.cop.serviceimpl.service.MMTotalServiceImpl;
import com.cop.utils.TransactionUtils;

@Component
public class PurchaseCNQR {
	Costtotal ct;

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

	private Mrc mrc;
	private Caval caval;
	private Material mat;
	private Currencyexchangerate cer;
	private Organisation org;
	private Orderheader oh, loh;
	Altuom matalt;

	public Costtotal generateCostTotal(Costtotal ct) {
		ct = cs.initializeCostTotal(ct);

		List<Orderheader> ohList = this.Ohr.findAllByOrdernum(ct.getObjcode(), ct.getObjitnum());
		this.oh = ohList.get(0);

		if (ct.getItemtype().equals("M")) {
			List<Material> matList = mr.findAllByMatlcodeandPlantValsub(ct.getItemcode(), ct.getPlant(),
					ct.getValsub());
			mat = matList.get(0);
		}
		if (ct.getItemtype().equals("S")) {
			List<Serviceitem> srvcList = sr.findAllbyItemcodeandPlant(ct.getItemcode(), ct.getPlant());
			srvc = srvcList.get(0);
			/*--------------------------------ALTOUM-----------------------------------------*/
			// System.out.println(oh.getPlant() + " " + ct.getItemcode() + " " +
			// ct.getDocuom() + " " + srvc.getBuom());
			List<Altuom> altList = mar.findAltuombyMatCodePlantAltUOM(ct.getPlant(), ct.getItemcode(), ct.getDocuom(),
					srvc.getBuom());
			matalt = altList.get(0);

		}

		List<Organisation> orgList = this.or.findAllByOrgnCode(ct.getOrgncode());
		this.org = orgList.get(0);

		List<Mrc> mrcList = mrcr.findAllbyValsubbatchocsting(ct.getItemcode(), ct.getPlant(), ct.getDate(),
				ct.getValsub(), ct.getBatchnumber());
		if (mrcList.size() > 0) {
			mrc = mrcList.get(0);
		}

		// getting price based on batch and batchcosting
		BigDecimal price = new BigDecimal(0);

		if (ct.getItemtype().equalsIgnoreCase("M") && mrcList.size() > 0) {
			if (mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("YES")
					&& mrc.getBatchnumber().equalsIgnoreCase(ct.getBatchnumber())) {
				price = mrc.getPrice();
			}
		} else {
			System.out.println("Batch not available hence taking groumrate");
			price = oh.getGroumrate();
		}

		List<BigDecimal> result = transUtil.generateTransactionQtyAndAmount(ct.getTrnsevent(), ct.getDate(),
				oh.getOrgncode(), ct.getPlant(), ct.getItemcode(), ct.getDocqty(), ct.getDocuom(), ct.getOrdamount(),
				ct.getOrdcurrency(), oh.getBuom(), oh.getOrdgruom(), org.getCurro(), price, ct.getExchangerate());
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

		System.out.println("Entering PURCHASE CNQR");
		mt = mmts.intializeMMTotal(mt);
		List<Orderheader> ohList = Ohr.findAllByOrdernum(mt.getObjcode(), mt.getObjitnum());
		oh = ohList.get(0);
		List<Mrc> MrcList = mrcr.findAllbyItemcodePlantDateMpricetyp(mt.getItemcode(), mt.getPlant(), mt.getDate());

		List<Material> matList = mr.findAllByMatlcodeandPlantValsub(mt.getItemcode(), mt.getPlant(), mt.getValsub());
		mat = matList.get(0);

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

		BigDecimal price = new BigDecimal(0);

		if (MrcList.size() > 0) {
			if (mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("YES")
					&& mrc.getBatchnumber().equalsIgnoreCase(mt.getBatchnumber())) {
				price = mrc.getPrice();
			}
		} else
			System.out.println("Batch not available hence taking groumrate");
		price = oh.getGroumrate();

		List<BigDecimal> result = transUtil.generateTransactionQtyAndAmount(mt.getTrnsevent(), mt.getDate(),
				oh.getOrgncode(), mt.getPlant(), mt.getItemcode(), mt.getDocqty(), mt.getDocuom(), mt.getOrdamount(),
				mt.getOrdcurrency(), oh.getBuom(), oh.getOrdgruom(), org.getCurro(), price, mt.getExchangerate());
		mt.setBuomqty(result.get(0));
		mt.setOrdamount(result.get(1));
		mt.setAmountincurro(result.get(2));
		mt.setPrdqty(result.get(3));
		mt.setPrdamount(result.get(4));
		mt.setExchangerate(result.get(5));

		// CSTOCK AND STOCKVALUE
		mt.setAdindicator("+");
		BigDecimal[] stock;
		BigDecimal stockValueCorrection = new BigDecimal(0), cstockCorrection = new BigDecimal(0);

		List<BigDecimal[]> mtrList;
		if (mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("NO"))
			mtrList = mtr.findCstockStockwithoutBatch(mt.getItemcode(), mt.getPlant(), mt.getDate(), mt.getStocktype(),
					mt.getValsub(), mt.getSaleorder());
		else
			mtrList = mtr.findCstockStockwithBatch(mt.getItemcode(), mt.getPlant(), mt.getDate(), mt.getStocktype(),
					mt.getValsub(), mt.getBatchnumber(), mt.getSaleorder());

		cstockCorrection = mt.getBuomqty();
		if (mtrList.isEmpty()) {
			mt.setCstock(new BigDecimal(0));
			mt.setStockvalue(mt.getPrdamount());
			System.out.println("CSTOCK = " + mt.getCstock() + " | StockValue = " + mt.getStockvalue());

		} else {
			stock = mtrList.get(0);
			BigDecimal cstock = stock[0];
			BigDecimal stockvalue = stock[1];
			mt.setCstock(cstock);
			if (mt.getBuomqty().compareTo(cstock) == 1) {
				BigDecimal prdRatio = mt.getPrdamount().divide(mt.getPrdqty());
				stockValueCorrection = prdRatio.multiply(mt.getCstock());
				mt.setStockvalue(stockvalue.subtract(stockValueCorrection));
			} else {
				stockValueCorrection = mt.getPrdamount();
				mt.setStockvalue(stockvalue.subtract(stockValueCorrection));
				BigDecimal bstock = new BigDecimal(0);
				if (stock[2] != null)
					bstock = stock[2];
				 if (mat.getBatch().equalsIgnoreCase("YES") &&  mat.getBatchcosting().equalsIgnoreCase("NO"))
						 mt.setBstock(mt.getBuomqty().subtract(bstock));
			}

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
			System.out.println("Updating Stock " + mmtotal.getMmtotalid());
			System.out.println("OLD : CSTOCK = " + mmtotal.getCstock() + " | StockValue = " + mmtotal.getStockvalue());
			// mmtotal.setCstock(mmtotal.getCstock().subtract(cstockCorrection));
			mmtotal.setStockvalue(mmtotal.getStockvalue().subtract(stockValueCorrection));
			System.out.println("NEW : CSTOCK = " + mmtotal.getCstock() + " | StockValue = " + mmtotal.getStockvalue());
			if (mt.getBatchnumber() != null && mat.getBatch().equalsIgnoreCase("YES")
					&& mat.getBatchcosting().equalsIgnoreCase("NO"))
				mt.setBstock(mmtotal.getBstock().add(mt.getBuomqty()));
			mtr.save(mmtotal);
		}

		return mt;
	}

	// CNQR CONTRA
	public Costtotal generateCostTotalContra(Costtotal ct) {

		List<Orderheader> ohList = this.Ohr.findAllByOrdernum(ct.getObjcode(), ct.getObjitnum());
		this.oh = ohList.get(0);

		if (ct.getLinkedobjcode() != null) {
			List<Orderheader> lohList = Ohr.findAllByOrdernum(ct.getLinkedobjcode(), ct.getLinkedobjitnum());
			loh = lohList.get(0);
			if (loh.getCostcenter() != null)
				ct.setObjcode(loh.getCostcenter());
			else
				ct.setObjcode(oh.getCostcenter());
		}
		ct.setCobjtype("CTR");
		ct.setTrnsevent("CNQR-CONTRA");
		List<BigDecimal> result = transUtil.generateTransactionQtyAndAmount(ct.getTrnsevent(), ct.getDate(),
				oh.getOrgncode(), ct.getPlant(), ct.getItemcode(), ct.getDocqty(), ct.getDocuom(), ct.getOrdamount(),
				ct.getOrdcurrency(), oh.getBuom(), oh.getOrdgruom(), org.getCurro(), oh.getGroumrate(),
				ct.getExchangerate());
		ct.setBuomqty(result.get(0));
		ct.setOrdamount(result.get(1));
		ct.setAmountincurro(result.get(2));
		ct.setExchangerate(result.get(5));

		ct.setValsub(oh.getValsub());
		String partnerCode = oh.getObjcode() + "" + oh.getObjitnum();
		ct.setPrtnrcode(partnerCode);
		ct.setDocnumber(ct.getDocnumber());

		List<Caval> cvList = cvr.findAllbyOrgnCodeMatValGrp("SR", oh.getOrgncode(), srvc.getValgrp());
		System.out.println(srvc.getValgrp());
		caval = cvList.get(0);
		ct.setCostacc(caval.getCostacc());
		ct.setObjitnum(null);

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

	public Mmtotal generateMMTotalContra(Mmtotal mt) {
		System.out.println("Generating mmtotal cnqr-contra 27/08/2020");
		String OrginalObjcode = mt.getObjcode();
		BigDecimal OriginalObjitnum = mt.getObjitnum();
		mt.setTrnsevent("CNQR-CONTRA");

		List<Orderheader> ohList = Ohr.findAllByOrdernum(mt.getObjcode(), mt.getObjitnum());
		oh = ohList.get(0);
System.out.println("oh value"+oh.getItemcode());
		List<Orderheader> LinkedohList = Ohr.findAllByOrdernum(mt.getLinkedobjcode(), mt.getLinkedobjitnum());
		loh = LinkedohList.get(0);
		mt.setBuom(loh.getBuom());
		System.out.println("loh value"+loh.getItemcode());
		List<BigDecimal> result = transUtil.generateTransactionQtyAndAmount(mt.getTrnsevent(), mt.getDate(),
				oh.getOrgncode(), mt.getPlant(), oh.getItemcode(), mt.getDocqty(), mt.getDocuom(), mt.getOrdamount(),
				mt.getOrdcurrency(), oh.getBuom(), oh.getOrdgruom(), org.getCurro(), oh.getGroumrate(),
				mt.getExchangerate());
		// mt.setBuomqty(result.get(0));
		mt.setValsub(loh.getValsub());
		mt.setOrdamount(result.get(1));
		mt.setAmountincurro(result.get(2));
		// mt.setPrdqty(result.get(3));
		mt.setPrdamount(result.get(4));
		mt.setExchangerate(result.get(5));

		BigDecimal buomqty = transUtil.findBuqty(oh.getObjcode(), oh.getObjitnum(), loh.getObjcode(), loh.getObjitnum(),
				mt.getDocuom(), mt.getDocqty());
		mt.setBuomqty(buomqty);
		mt.setPrdqty(mt.getBuomqty());
		System.out.println("orgncode" + loh.getOrgncode());
		List<Material> matList = mr.findAllByMatlcodeandPlantValsub(loh.getItemcode(), loh.getPlant(), loh.getValsub());
		mat = matList.get(0);

		/*
		 * List<Caval> cvList = cvr.findAllbyOrgnCodeMatValGrp("INV", oh.getOrgncode(),
		 * mat.getValgrp());
		 * 
		 * caval = cvList.get(0); mt.setCostacc(caval.getCostacc());
		 */
		// COSTACC SAME AS IN CNQR - 27TH DEC 2020
		List<Serviceitem> srvcList = sr.findAllbyItemcodeandPlant(oh.getItemcode(), oh.getPlant());
		srvc = srvcList.get(0);
		List<Caval> cvList = cvr.findAllbyOrgnCodeMatValGrp("SR", oh.getOrgncode(), srvc.getValgrp());
		caval = cvList.get(0);
		mt.setCostacc(caval.getCostacc());

		mt.setValsub(loh.getValsub());
		mt.setBatchnumber(loh.getBatchnumber());

		mt.setAdindicator("+");

		mt.setItemcode(loh.getItemcode());
		mt.setStocktype("O");
		mt.setObjcode(mt.getLinkedobjcode());

		// mt.setValsub(oh.getValsub());

		mt.setLinkedobjcode(OrginalObjcode);
		mt.setLinkedobjitnum(OriginalObjitnum);

		BigDecimal stockValueCorrection = mt.getPrdamount();

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
			mt.setStockvalue(mt.getPrdamount());
			System.out.println("CSTOCK = " + mt.getCstock() + " | StockValue = " + mt.getStockvalue());

		} else {
			stock = mtrList.get(0);
			BigDecimal cstock = stock[0];
			BigDecimal stockvalue = stock[1];
			mt.setCstock(cstock);

			mt.setStockvalue(stockvalue.add(stockValueCorrection));
			System.out.println("CSTOCK = " + mt.getCstock() + " | StockValue = " + mt.getStockvalue());
			BigDecimal bstock = new BigDecimal(0);
			if (stock[2] != null)
				bstock = stock[2];
			 if (mat.getBatch().equalsIgnoreCase("YES") &&  mat.getBatchcosting().equalsIgnoreCase("NO"))
					 mt.setBstock(mt.getBuomqty().subtract(bstock));

		}
		// EXCHANGE RATE DIFF
		List<Orderheader> ohList2 = Ohr.findAllByOrdernum(OrginalObjcode, OriginalObjitnum);
		oh = ohList2.get(0);
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
			// mmtotal.setCstock(mmtotal.getCstock().add(mt.getBuomqty()));
			mmtotal.setStockvalue(mmtotal.getStockvalue().add(stockValueCorrection));

			if (mt.getBatchnumber() != null && mat.getBatch().equalsIgnoreCase("YES")
					&& mat.getBatchcosting().equalsIgnoreCase("NO"))
				mt.setBstock(mmtotal.getBstock().add(mt.getBuomqty()));
			mtr.save(mmtotal);
		}
		return mt;

	}
}
