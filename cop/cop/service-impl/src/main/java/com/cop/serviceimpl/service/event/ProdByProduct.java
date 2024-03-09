package com.cop.serviceimpl.service.event;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cop.model.database.Caval;
import com.cop.model.database.Material;
import com.cop.model.database.Mmtotal;
import com.cop.model.database.Mrc;
import com.cop.model.database.Orderheader;
import com.cop.repository.transaction.CavalRepository;
import com.cop.repository.transaction.MMTotalRespository;
import com.cop.repository.transaction.MaterialRepository;
import com.cop.repository.transaction.MrcRepository;
import com.cop.repository.transaction.OrderHeaderRepository;
import com.cop.serviceapi.service.MMTotalService;

@Component

public class ProdByProduct {
	@Autowired
	OrderHeaderRepository Ohr;
	@Autowired
	MaterialRepository mr;
	@Autowired
	CavalRepository cvr;
	private Caval caval;
	@Autowired
	MMTotalRespository mtr;
	@Autowired
	MrcRepository mrcr;
	@Autowired
	MMTotalService mmts;
	private Orderheader oh;
	private Material mat;

	public Mmtotal generateMMTotal(Mmtotal mt) {
		System.out.println("Entering Prod By Product.");
		mt = mmts.intializeMMTotal(mt);
		List<Orderheader> ohList = Ohr.findAllByOrdernum(mt.getObjcode(), mt.getObjitnum());
		oh = ohList.get(0);
		List<Material> matList = mr.findAllByMatlcodeandPlantValsub(mt.getItemcode(),mt.getPlant(),oh.getValsub());
		mat = matList.get(0);
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


		

		// AMOUNTINCURRO
		mt.setAmountincurro(mt.getOrdamount());
		
		mt.setOrdcurrency(oh.getOrdcurrency());

		// COSTACC
		List<Caval> cvList = cvr.findAllbyOrgnCodeMatValGrp("GR", oh.getOrgncode(), mat.getValgrp());
		caval = cvList.get(0);
		mt.setCostacc(caval.getCostacc());
		
		// SALE ORDER
				mt.setSaleorder(oh.getSoassignment());
				// stocktype
				if (mt.getSaleorder() != null)
					mt.setStocktype("S");
				else
					mt.setStocktype("O");

				// CSTOCK, STOCKVALUE, ADINDICATOR
				mt.setAdindicator("+");
				BigDecimal[] stock;
				/*
				 * List<BigDecimal[]> mtrList; if (mat.getBatch().equalsIgnoreCase("YES") &&
				 * mat.getBatchcosting().equalsIgnoreCase("NO")) mtrList =
				 * mtr.findCstockStockwithoutBatch(mt.getItemcode(), mt.getPlant(),
				 * mt.getDate(), mt.getStocktype(), mt.getValsub(), mt.getSaleorder()); else
				 * mtrList = mtr.findCstockStockwithBatch(mt.getItemcode(), mt.getPlant(),
				 * mt.getDate(), mt.getStocktype(), mt.getValsub(), mt.getBatchnumber(),
				 * mt.getSaleorder());
				 */
				
				//remodified code to check fetch cost by batch irrespective costing status on 9/6/21
				List<BigDecimal[]> mtrList = mtr.findCstockbyItemcodePlantValsubBatch(mt.getItemcode(), mt.getPlant(),
						mt.getDate(), mt.getValsub(), mt.getBatchnumber(),mt.getSaleorder());
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
					BigDecimal bstock = new BigDecimal(0);
					if (stock[2] != null)
						bstock = stock[2];
					 if (mat.getBatch().equalsIgnoreCase("YES") &&  mat.getBatchcosting().equalsIgnoreCase("NO"))
							 mt.setBstock(mt.getBuomqty().add(bstock));
				}
return mt;
}
}