package com.cop.serviceimpl.service.event;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
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
public class PurchaseGoodsRecieve {
	@Autowired
	MMTotalService mmts;

	@Autowired
	CostTotalService cs;
	@Autowired
	TransactionUtils transUtil;
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
	CurrencyExchangeRateRepository cerr;
	@Autowired
	NumberRangeRepository nrr;
	@Autowired
	CavalRepository cr;
	@Autowired
	ServiceRepository sr;
	@Autowired
	MMTotalRespository mtr;

	private Serviceitem srvc;
	private Material mat;
	private Organisation org;
	private Orderheader oh;
	private Altuom matalt;
	private Currencyexchangerate cer;
	private Caval caval;
	private Mrc mrc;
	private Mmtotal mtGR, mtGI;

	public Costtotal generateCostTotal(Costtotal ct) {

		System.out.println("organisation code from costotal " + ct.getOrgncode() + this.or);
		// AmountInCurro
		ct = cs.initializeCostTotal(ct);
		List<Organisation> orgList = this.or.findAllByOrgnCode(ct.getOrgncode());
		this.org = orgList.get(0);

		List<Orderheader> ohList = this.Ohr.findAllByOrdernum(ct.getObjcode(), ct.getObjitnum());
		this.oh = ohList.get(0);
		if (ct.getValsub() == null)
			ct.setValsub(oh.getValsub());

		if (ct.getItemtype().equals("M")) {
			List<Material> matList = mr.findAllByMatlcodeandPlantValsub(ct.getItemcode(), ct.getPlant(),
					ct.getValsub());
			mat = matList.get(0);
		}

		System.out.println("org = " + this.org);

		/*--------------------------------MATALTOUM-----------------------------------------*/
		// System.out.println(this.oh.getPlant() + " " + ct.getItemcode() + " " +
		// ct.getDocuom() + " " + this.mat.getBuom());
		List<Altuom> altList = this.mar.findAltuombyMatCodePlantAltUOM(this.oh.getPlant(), ct.getItemcode(),
				ct.getDocuom(), this.mat.getBuom());
		this.matalt = altList.get(0);

		List<Mrc> mrcList = mrcr.findAllbyValsubbatchocsting(ct.getItemcode(), ct.getPlant(), ct.getDate(),
				ct.getValsub(), ct.getBatchnumber());
		if (mrcList.size() > 0) {
			mrc = mrcList.get(0);
		}
		// 11-11-2020 adding variable price, to decide between groumrate and mrc price
		// to be picked based on batchmanagement and batch costing

		// getting price based on batch and batchcosting
		BigDecimal price = new BigDecimal(0);

		if (ct.getItemtype().equalsIgnoreCase("M") && mrcList.size() > 0) {
			if (mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("YES")
					&& mrc.getBatchnumber().equalsIgnoreCase(ct.getBatchnumber())) {
				price = mrc.getPrice();
			} else {
				System.out.println("Batch not available hence taking groumrate");
				price = oh.getGroumrate();
			}
		} else
			price = oh.getGroumrate();
		List<BigDecimal> result = transUtil.generateTransactionQtyAndAmount(ct.getTrnsevent(), ct.getDate(),
				oh.getOrgncode(), ct.getPlant(), ct.getItemcode(), ct.getDocqty(), ct.getDocuom(), ct.getOrdamount(),
				ct.getOrdcurrency(), oh.getBuom(), oh.getOrdgruom(), org.getCurro(), price, ct.getExchangerate());
		ct.setBuomqty(result.get(0));
		ct.setOrdamount(result.get(1));
		ct.setAmountincurro(result.get(2));
		ct.setExchangerate(result.get(5));

		System.out.println("Organisation code" + ct.getOrgncode());

		// costacc
		List<Caval> cvList = this.cvr.findAllbyOrgnCodeMatValGrp("INV", this.oh.getOrgncode(), this.mat.getValgrp());
		this.caval = cvList.get(0);
		ct.setCostacc(this.caval.getCostacc());
		ct.setSaleorder(oh.getSoassignment());

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

	public List<Mmtotal> generateMMTotal(Mmtotal mt) {

		System.out.println("Entering Purchase GR");
		mtGR = mmts.intializeMMTotal(mt);
		List<Orderheader> ohList = Ohr.findAllByOrdernum(mtGR.getObjcode(), mtGR.getObjitnum());
		oh = ohList.get(0);
		List<Mrc> MrcList = mrcr.findAllbyValsubbatchocsting(mtGR.getItemcode(), mtGR.getPlant(), mtGR.getDate(),
				mtGR.getValsub(), mtGR.getBatchnumber());
		if (MrcList.size() > 0)
			mrc = MrcList.get(0);

		List<Material> matList = mr.findAllByMatlcodeandPlantValsub(mtGR.getItemcode(), mtGR.getPlant(),
				mtGR.getValsub());
		mat = matList.get(0);

		List<Organisation> orgList = this.or.findAllByOrgnCode(oh.getOrgncode());
		this.org = orgList.get(0);
//11-11-2020 adding varaible price, to decide between groumrate and mrc price to be picked based on batchmanagement and batch costing

		BigDecimal price = new BigDecimal(0);

		if (MrcList.size() > 0) {
			if (mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("YES")
					&& mrc.getBatchnumber().equalsIgnoreCase(mt.getBatchnumber())) {
				price = mrc.getPrice();
			} else {
				System.out.println("Batch not available hence taking groumrate");
				price = oh.getGroumrate();
			}
		} else
			price = oh.getGroumrate();
		System.out.println("currency" + mtGR.getOrdcurrency() + "" + oh.getOrdgruom());
		List<BigDecimal> result = transUtil.generateTransactionQtyAndAmount(mtGR.getTrnsevent(), mtGR.getDate(),
				oh.getOrgncode(), mtGR.getPlant(), mtGR.getItemcode(), mtGR.getDocqty(), mtGR.getDocuom(),
				mtGR.getOrdamount(), mtGR.getOrdcurrency(), oh.getBuom(), oh.getOrdgruom(), org.getCurro(), price,
				mtGR.getExchangerate());
		mtGR.setBuomqty(result.get(0));
		mtGR.setOrdamount(result.get(1));
		mtGR.setAmountincurro(result.get(2));
		mtGR.setPrdqty(result.get(3));
		mtGR.setPrdamount(result.get(4));
		mtGR.setExchangerate(result.get(5));

		// CSTOCK STOCKVALUE
		BigDecimal[] stock;
		BigDecimal stockValueCorrection = new BigDecimal(0), cstockCorrection = new BigDecimal(0);

		List<BigDecimal[]> mtrList;
		if (mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("NO"))
			mtrList = mtr.findCstockStockBatchmanagement(mtGR.getItemcode(), mtGR.getPlant(), mtGR.getDate(),
					mtGR.getStocktype(), mtGR.getValsub(), mtGR.getSaleorder());
		else
			mtrList = mtr.findCstockStockwithBatch(mtGR.getItemcode(), mtGR.getPlant(), mtGR.getDate(),
					mtGR.getStocktype(), mtGR.getValsub(), mtGR.getBatchnumber(), mtGR.getSaleorder());

		cstockCorrection = mtGR.getBuomqty();
		stockValueCorrection = mtGR.getAmountincurro();
		if (mtrList.isEmpty()) {
			System.out.println("MTRLIST is empty");
			mtGR.setCstock(cstockCorrection);
			mtGR.setStockvalue(stockValueCorrection);
			//if (mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("NO"))
				//mtGR.setBstock(mtGR.getBuomqty());
			System.out.println("IF CSTOCK = " + mtGR.getCstock() + " | StockValue = " + mtGR.getStockvalue());

		} else {
			stock = mtrList.get(0);
			BigDecimal cstock = stock[0];
			BigDecimal stockvalue = stock[1];
			BigDecimal bstock = new BigDecimal(0);

			mtGR.setCstock(cstock.add(cstockCorrection));

			mtGR.setStockvalue(stockvalue.add(stockValueCorrection));

			/*
			 * if (stock[2] != null) bstock = stock[2]; if
			 * (mat.getBatch().equalsIgnoreCase("YES") &&
			 * mat.getBatchcosting().equalsIgnoreCase("NO"))
			 * mtGR.setBstock(mtGR.getBuomqty().add(bstock));
			 */

			System.out.println("else CSTOCK = " + mtGR.getCstock() + " | StockValue = " + mtGR.getStockvalue());

		}

		// EXCHANGE RATE DIFF

		// EXCHANGE RATE DIFF
		if (oh.getExchangerate() == null)
			oh.setExchangerate(new BigDecimal(0));

		if (mtGR.getExchangerate().equals(new BigDecimal(1))) {
			mtGR.setExchangerate(null);
			mtGR.setExchangeratediff(null);
		} else
			mtGR.setExchangeratediff(
					(mtGR.getExchangerate().subtract(oh.getExchangerate())).multiply(mtGR.getOrdamount()));

		// costacc
		List<Caval> cvList = this.cvr.findAllbyOrgnCodeMatValGrp("INV", this.oh.getOrgncode(), this.mat.getValgrp());
		this.caval = cvList.get(0);
		mtGR.setCostacc(this.caval.getCostacc());

		System.out.println(mtGR.getTrnsevent());
		
		BigDecimal[] btstock ; BigDecimal bs=new BigDecimal(0);
		if(mt.getBatchnumber()!=null) {
		List<BigDecimal[]>  btList;
		btList=mtr.findCstockStockwithBatch(mtGR.getItemcode(), mtGR.getPlant(), mtGR.getDate(), mtGR.getStocktype(),
		mtGR.getValsub(), mtGR.getBatchnumber(), mtGR.getSaleorder());
		
		if(btList.isEmpty()) 
			 bs=mtGR.getBuomqty();
		else
			{
			btstock=btList.get(0);
			if (btstock[2] != null)
			bs = btstock[2];
		else bs=new BigDecimal(0);
		 if (mat.getBatch().equalsIgnoreCase("YES") &&  mat.getBatchcosting().equalsIgnoreCase("NO"))
				 mtGR.setBstock(mtGR.getBuomqty().add(bs));}}

		// 11-11-2020 adding Batch costing logic to generate and update cstock and
		// stockvalue
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
			mmtotal.setCstock(mmtotal.getCstock().add(cstockCorrection));
			mmtotal.setStockvalue(mmtotal.getStockvalue().add(stockValueCorrection));
			if (mt.getBatchnumber() != null && mat.getBatch().equalsIgnoreCase("YES")
					&& mat.getBatchcosting().equalsIgnoreCase("NO"))
				mt.setBstock(mmtotal.getBstock().add(mt.getBuomqty()));
			System.out.println("NEW : CSTOCK = " + mmtotal.getCstock() + " | StockValue = " + mmtotal.getStockvalue());
			mtr.save(mmtotal);

		}
		mt.setOrdcurrency(oh.getOrdcurrency());
		mt.setAdindicator("+");

		List<Mmtotal> mmlist = new ArrayList<>();
		mmlist.add(mtGR);
		mtGI = new Mmtotal();
		if (oh.getCostcenter() != null) {
			BeanUtils.copyProperties(mtGR, mtGI);
			// costacc
			List<Caval> cvList1 = this.cvr.findAllbyOrgnCodeMatValGrp("GI", this.oh.getOrgncode(),
					this.mat.getValgrp());
			this.caval = cvList1.get(0);
			mtGI.setCostacc(this.caval.getCostacc());
			mtGI.setCobjtype("CTR");
			mtGI.setAdindicator("-");
			mtGI.setTrnsevent("GI");
			mtGI.setObjcode(oh.getCostcenter());
			mtGI.setAmountincurro(mtGR.getAmountincurro());
			mtGI.setCstock(mtGR.getCstock().subtract(mtGI.getCstock()));
			mtGI.setStockvalue(mtGR.getStockvalue().subtract(mtGI.getAmountincurro()));
			mtGI.setDocdesc("immidiate GI ");
			mmlist.add(mtGI);
		}

		return mmlist;
	}

}
