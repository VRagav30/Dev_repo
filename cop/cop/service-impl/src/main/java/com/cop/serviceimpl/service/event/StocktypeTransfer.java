package com.cop.serviceimpl.service.event;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cop.model.database.Caval;
import com.cop.model.database.Material;
import com.cop.model.database.Mmtotal;
import com.cop.model.database.Mrc;
import com.cop.model.database.Organisation;
import com.cop.repository.transaction.CavalRepository;
import com.cop.repository.transaction.MMTotalRespository;
import com.cop.repository.transaction.MaterialRepository;
import com.cop.repository.transaction.MrcRepository;
import com.cop.repository.transaction.OrganisationRepository;
import com.cop.repository.transaction.PlantRepository;
import com.cop.serviceapi.service.MMTotalService;

@Component
public class StocktypeTransfer {
	@Autowired
	MMTotalService mts;
	@Autowired
	MrcRepository mrcr;
	@Autowired
	MaterialRepository mr;
	@Autowired
	CavalRepository cvr;
	@Autowired
	MMTotalRespository mmtr;
	@Autowired
	PlantRepository pr;
	@Autowired
	OrganisationRepository or;

	
	private Material mat;
	private Caval caval;

	public Mmtotal generateMMTotal(Mmtotal mt)
	{
		mt = mts.intializeMMTotal(mt);
		String Orgncode = pr.findOrgCodeByPlantId(mt.getPlant());
		List<Organisation> olist = or.findAllByOrgnCode(Orgncode);
		mt.setOrdcurrency(olist.get(0).getCurro());
		mt.setSaleorder(mt.getTso());
		//mt.setTstype(mt.getStocktype());
		//mt.setTmaterial(mt.getItemcode());
		
		
		List<Material> matList = mr.findAllByMatlcodeandPlantValsub(mt.getItemcode(), mt.getPlant(), mt.getValsub());
		mat = matList.get(0);

		// ORDAMOUNT
		List<Mrc> MrcListStandard = mrcr.findAllbyMpricetypValsub(mt.getItemcode(), mt.getPlant(), mt.getDate(), "STD",
				mt.getValsub(),mt.getBatchnumber());

		List<Mrc> MrcListMoving = mrcr.findAllbyMpricetypValsub(mt.getItemcode(), mt.getPlant(), mt.getDate(), "MOVING",
				mt.getValsub(),mt.getBatchnumber());

		List<Mrc> MrcListPlan = mrcr.findAllbyMpricetypValsub(mt.getItemcode(), mt.getPlant(), mt.getDate(), "PLAN",
				mt.getValsub(),mt.getBatchnumber());
		if ((mat.getMattyp() != ("SFG")) && (mat.getMattyp() != ("FG"))) {
			if ((MrcListMoving.size() > 0) && (MrcListMoving.get(0) != null))
				mt.setOrdamount(mt.getBuomqty().multiply(MrcListMoving.get(0).getPrice()));
			else if ((MrcListStandard.size() > 0) && (MrcListStandard.get(0) != null))
				mt.setOrdamount(mt.getBuomqty().multiply(MrcListStandard.get(0).getPrice()));
			else if ((MrcListPlan.size() > 0) && (MrcListPlan.get(0) != null))
				mt.setOrdamount(mt.getBuomqty().multiply(MrcListPlan.get(0).getPrice()));
		}
		if ((mat.getMattyp().equals("SFG") || mat.getMattyp().equals("FG")) ) {
			if ((MrcListStandard.size() > 0) && (MrcListStandard.get(0) != null)) {
				System.out.println(MrcListStandard.get(0).getPrice());
				mt.setOrdamount(mt.getBuomqty().multiply(MrcListStandard.get(0).getPrice()));
			} else if ((MrcListMoving.size() > 0) && (MrcListMoving.get(0) != null))
				mt.setOrdamount(mt.getBuomqty().multiply(MrcListMoving.get(0).getPrice()));
			else if ((MrcListPlan.size() > 0) && (MrcListPlan.get(0) != null))
				mt.setOrdamount(mt.getBuomqty().multiply(MrcListPlan.get(0).getPrice()));
		}
		mt.setAmountincurro(mt.getOrdamount());
		
		List<Caval> cvList = cvr.findAllbyOrgnCodeMatValGrp("INV", Orgncode, mat.getValgrp());
		caval = cvList.get(0);
		mt.setCostacc(caval.getCostacc());

		// CSTOCK STOCKVALUE
		mt.setAdindicator("+");
		BigDecimal stock[];
		BigDecimal stockValueCorrection = new BigDecimal(0), cstockCorrection = new BigDecimal(0);
		List<BigDecimal[]> mmtrList; if (mat.getBatch().equalsIgnoreCase("YES") &&
				  mat.getBatchcosting().equalsIgnoreCase("NO")) mmtrList =
				  mmtr.findCstockStockwithoutBatch(mt.getItemcode(), mt.getPlant(),
				  mt.getDate(), mt.getStocktype(), mt.getValsub(), mt.getSaleorder()); else
				  mmtrList = mmtr.findCstockStockwithBatch(mt.getItemcode(), mt.getPlant(),
				  mt.getDate(), mt.getStocktype(), mt.getValsub(), mt.getBatchnumber(),
				  mt.getSaleorder());
		cstockCorrection = mt.getBuomqty();
		stockValueCorrection = mt.getAmountincurro();
		if (mmtrList.isEmpty()) {
			mt.setCstock(cstockCorrection);
			mt.setStockvalue(stockValueCorrection);
			System.out.println("IF CSTOCK = " + mt.getCstock() + " | StockValue = " + mt.getStockvalue());

		} else {
			stock = mmtrList.get(0);
			BigDecimal cstock = stock[0];
			BigDecimal stockvalue = stock[1];
			mt.setCstock(cstock.add(cstockCorrection));
			mt.setStockvalue(stockvalue.add(stockValueCorrection));
			System.out.println("else CSTOCK = " + mt.getCstock() + " | StockValue = " + mt.getStockvalue());

		}
		
		 List<Mmtotal> mtrLst;
			if (mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("NO"))
				mtrLst = mmtr.findallbyBatchcosting(mt.getItemcode(), mt.getPlant(), mt.getDate(), mt.getStocktype(),
						mt.getValsub(), mt.getSaleorder());
			else
				mtrLst = mmtr.findCstockStockvaluebyGRDateandstocktype(mt.getItemcode(), mt.getPlant(), mt.getDate(),
						mt.getStocktype(), mt.getValsub(), mt.getBatchnumber(), mt.getSaleorder());
		for (Mmtotal mmtotal : mtrLst) {
			System.out.println("Updating Stock " + mmtotal.getMmtotalid());
			System.out.println("OLD : CSTOCK = " + mmtotal.getCstock() + " | StockValue = " + mmtotal.getStockvalue());
			mmtotal.setCstock(mmtotal.getCstock().add(cstockCorrection));
			mmtotal.setStockvalue(mmtotal.getStockvalue().add(stockValueCorrection));
			System.out.println("NEW : CSTOCK = " + mmtotal.getCstock() + " | StockValue = " + mmtotal.getStockvalue());
			if (mt.getBatchnumber()!=null && mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("NO"))
				mt.setBstock(mmtotal.getBstock().add(mt.getBuomqty()));
			mmtr.save(mmtotal);

		}
		return mt;
	}
	public Mmtotal generateMMTotalContra(Mmtotal mt)
	{
		mt.setTrnsevent("ST-CONTRA");
		mt.setStocktype(mt.getSstype());
		System.out.println("StockTYPE " + mt.getStocktype());
		if(mt.getStocktype().equalsIgnoreCase("O"))
			mt.setSaleorder(null);
		else if (mt.getStocktype().equalsIgnoreCase("S"))
			mt.setSaleorder(mt.getSso());

		
		// ORDAMOUNT
				List<Mrc> MrcListStandard = mrcr.findAllbyMpricetypValsub(mt.getItemcode(), mt.getPlant(), mt.getDate(), "STD",
						mt.getValsub(),mt.getBatchnumber());

				List<Mrc> MrcListMoving = mrcr.findAllbyMpricetypValsub(mt.getItemcode(), mt.getPlant(), mt.getDate(), "MOVING",
						mt.getValsub(),mt.getBatchnumber());

				List<Mrc> MrcListPlan = mrcr.findAllbyMpricetypValsub(mt.getItemcode(), mt.getPlant(), mt.getDate(), "PLAN",
						mt.getValsub(),mt.getBatchnumber());
				if (mat.getOboind() == "YES") {
					if ((MrcListMoving.size() > 0) && (MrcListMoving.get(0) != null))
						mt.setOrdamount(mt.getBuomqty().multiply(MrcListMoving.get(0).getPrice()));
					else if ((MrcListStandard.size() > 0) && (MrcListStandard.get(0) != null))
						mt.setOrdamount(mt.getBuomqty().multiply(MrcListStandard.get(0).getPrice()));
					else if ((MrcListPlan.size() > 0) && (MrcListPlan.get(0) != null))
						mt.setOrdamount(mt.getBuomqty().multiply(MrcListPlan.get(0).getPrice()));
				}
				if (mat.getPrdind() == "YES") {
					if ((MrcListStandard.size() > 0) && (MrcListStandard.get(0) != null)) {
						System.out.println(MrcListStandard.get(0).getPrice());
						mt.setOrdamount(mt.getBuomqty().multiply(MrcListStandard.get(0).getPrice()));
					} else if ((MrcListMoving.size() > 0) && (MrcListMoving.get(0) != null))
						mt.setOrdamount(mt.getBuomqty().multiply(MrcListMoving.get(0).getPrice()));
					else if ((MrcListPlan.size() > 0) && (MrcListPlan.get(0) != null))
						mt.setOrdamount(mt.getBuomqty().multiply(MrcListPlan.get(0).getPrice()));
				}
				mt.setAmountincurro(mt.getOrdamount());
				// CSTOCK STOCKVALUE
				mt.setAdindicator("-");
				BigDecimal stock[];
				BigDecimal stockValueCorrection = new BigDecimal(0), cstockCorrection = new BigDecimal(0);
				List<BigDecimal[]> mmtrList; if (mat.getBatch().equalsIgnoreCase("YES") &&
						  mat.getBatchcosting().equalsIgnoreCase("NO")) mmtrList =
						  mmtr.findCstockStockwithoutBatch(mt.getItemcode(), mt.getPlant(),
						  mt.getDate(), mt.getStocktype(), mt.getValsub(), mt.getSaleorder()); else
						  mmtrList = mmtr.findCstockStockwithBatch(mt.getItemcode(), mt.getPlant(),
						  mt.getDate(), mt.getStocktype(), mt.getValsub(), mt.getBatchnumber(),
						  mt.getSaleorder());
				cstockCorrection = mt.getBuomqty();
				stockValueCorrection = mt.getAmountincurro();
				if (mmtrList.isEmpty()) {
					mt.setCstock(cstockCorrection);
					mt.setStockvalue(stockValueCorrection);
					System.out.println("IF CSTOCK = " + mt.getCstock() + " | StockValue = " + mt.getStockvalue());

				} else {
					stock = mmtrList.get(0);
					BigDecimal cstock = stock[0];
					BigDecimal stockvalue = stock[1];
					mt.setCstock(cstock.subtract(cstockCorrection));
					mt.setStockvalue(stockvalue.subtract(stockValueCorrection));
					System.out.println("else CSTOCK = " + mt.getCstock() + " | StockValue = " + mt.getStockvalue());

				}
				
				 List<Mmtotal> mtrLst;
					if (mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("NO"))
						mtrLst = mmtr.findallbyBatchcosting(mt.getItemcode(), mt.getPlant(), mt.getDate(), mt.getStocktype(),
								mt.getValsub(), mt.getSaleorder());
					else
						mtrLst = mmtr.findCstockStockvaluebyGRDateandstocktype(mt.getItemcode(), mt.getPlant(), mt.getDate(),
								mt.getStocktype(), mt.getValsub(), mt.getBatchnumber(), mt.getSaleorder());
				for (Mmtotal mmtotal : mtrLst) {
					System.out.println("Updating Stock " + mmtotal.getMmtotalid());
					System.out.println("OLD : CSTOCK = " + mmtotal.getCstock() + " | StockValue = " + mmtotal.getStockvalue());
					mmtotal.setCstock(mmtotal.getCstock().subtract(cstockCorrection));
					mmtotal.setStockvalue(mmtotal.getStockvalue().subtract(stockValueCorrection));
					System.out.println("NEW : CSTOCK = " + mmtotal.getCstock() + " | StockValue = " + mmtotal.getStockvalue());
					if (mt.getBatchnumber()!=null && mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("NO"))
						mt.setBstock(mmtotal.getBstock().subtract(mt.getBuomqty()));
					mmtr.save(mmtotal);
				}
		return mt;
	}

}
