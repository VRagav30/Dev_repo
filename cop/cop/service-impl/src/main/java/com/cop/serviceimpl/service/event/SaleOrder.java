package com.cop.serviceimpl.service.event;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cop.model.database.Caval;
import com.cop.model.database.Material;
import com.cop.model.database.Mmtotal;
import com.cop.model.database.Mrc;
import com.cop.model.database.Numberrange;
import com.cop.model.database.Orderheader;
import com.cop.repository.transaction.CavalRepository;
import com.cop.repository.transaction.MMTotalRespository;
import com.cop.repository.transaction.MaterialRepository;
import com.cop.repository.transaction.MrcRepository;
import com.cop.repository.transaction.NumberRangeRepository;
import com.cop.repository.transaction.OrderHeaderRepository;
import com.cop.serviceapi.service.MMTotalService;

@Component
public class SaleOrder {
	
	@Autowired
	MMTotalService mmts;
	@Autowired
	MaterialRepository mr;
	@Autowired
	MrcRepository mrcr;
	@Autowired
	CavalRepository cvr;
	@Autowired
	NumberRangeRepository nrr;
	@Autowired
	MMTotalRespository mtr;
	@Autowired
	OrderHeaderRepository Ohr;
	private Numberrange nr;
	private Material mat;
	private Orderheader oh;
	private Caval caval;
	public Mmtotal generateMMTotal(Mmtotal mt) {
		
		System.out.println("Entering Sale order");
		mt = mmts.intializeMMTotal(mt);
		List<Orderheader> ohList = Ohr.findAllByOrdernum(mt.getObjcode(), mt.getObjitnum());
		oh = ohList.get(0);
		//STOCKTYPE
		if (mt.getSaleorder() != null)
			mt.setStocktype("S");
		else
			mt.setStocktype("O");

		//DOCNUM and DOCITNUM
		if (mt.getDocnumber() == null) {

			// DOCNUM
			List<Numberrange> nrList = nrr.findAllByNumobject("MMTOTALDOC");
			nr = nrList.get(0);
			BigDecimal currentNum = (nr.getCurrentnumber() == null ? nr.getRangefrom()
					: new BigDecimal(nr.getCurrentnumber().intValue() + 1));
			nr.setCurrentnumber(currentNum);
			mt.setDocnumber(currentNum);
			mt.setDocitnum(new BigDecimal(10));
			nrr.save(nr);
		}

		// ORDAMOUNT
				List<Material> matList = mr.findAllByMatlcodeandPlantValsub(mt.getItemcode(), mt.getPlant(),mt.getValsub());
				mat = matList.get(0);
				
				
				List<Mrc> MrcListStandard = mrcr.findAllbyMatlcodePlantDateMpricetyp(mt.getItemcode(), mt.getPlant(),mt.getDate(), "STD");

				List<Mrc> MrcListMoving = mrcr.findAllbyMatlcodePlantDateMpricetyp(mt.getItemcode(), mt.getPlant(),	mt.getDate(), "MOVING");

				List<Mrc> MrcListPlan = mrcr.findAllbyMatlcodePlantDateMpricetyp(mt.getItemcode(), mt.getPlant(), mt.getDate(),	"PLAN");

				if ((mat.getMattyp().equals("SFG") || mat.getMattyp().equals("FG")) )
					if ((MrcListStandard.size() > 0) && (MrcListStandard.get(0) != null)) {
						System.out.println(MrcListStandard.get(0).getPrice());
						mt.setOrdamount(mt.getBuomqty().multiply(MrcListStandard.get(0).getPrice()));
					} else if ((MrcListMoving.size() > 0) && (MrcListMoving.get(0) != null))
						mt.setOrdamount(mt.getBuomqty().multiply(MrcListMoving.get(0).getPrice()));
					else if ((MrcListPlan.size() > 0) && (MrcListPlan.get(0) != null))
						mt.setOrdamount(mt.getBuomqty().multiply(MrcListPlan.get(0).getPrice()));
				if ((mat.getMattyp() != ("SFG")) && (mat.getMattyp() != ("FG")))
						
					if ((MrcListMoving.size() > 0) && (MrcListMoving.get(0) != null))
						mt.setOrdamount(mt.getBuomqty().multiply(MrcListMoving.get(0).getPrice()));
					else if ((MrcListStandard.size() > 0) && (MrcListStandard.get(0) != null))
						mt.setOrdamount(mt.getBuomqty().multiply(MrcListStandard.get(0).getPrice()));
					else if ((MrcListPlan.size() > 0) && (MrcListPlan.get(0) != null))
						mt.setOrdamount(mt.getBuomqty().multiply(MrcListPlan.get(0).getPrice()));
				
				mt.setAmountincurro(mt.getOrdamount());
				
				List<Caval> cvList = cvr.findAllbyOrgnCodeMatValGrp("SD", oh.getOrgncode(), mat.getValgrp());
				caval = cvList.get(0);
				mt.setCostacc(caval.getCostacc());
				mt.setOrdcurrency(oh.getOrdcurrency());
				
				// CTOCK ADINDICATOR STOCKVALUE
				mt.setAdindicator("-");
				BigDecimal[] stock;
				List<BigDecimal[]> mtrList;
				if (mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("NO"))
					mtrList = mtr.findCstockStockwithoutBatch(mt.getItemcode(), mt.getPlant(), mt.getDate(), mt.getStocktype(),
							mt.getValsub(), mt.getSaleorder());
				else
					mtrList = mtr.findCstockStockwithBatch(mt.getItemcode(), mt.getPlant(), mt.getDate(), mt.getStocktype(),
							mt.getValsub(), mt.getBatchnumber(), mt.getSaleorder());
				if (mtrList.isEmpty()) {
					mt.setCstock(mt.getBuomqty().negate());
					mt.setStockvalue(mt.getAmountincurro().negate());
				} else {
					stock = mtrList.get(0);
					BigDecimal cstock = stock[0];
					BigDecimal stockvalue = stock[1];
					mt.setCstock(cstock.subtract(mt.getBuomqty()));
					mt.setStockvalue(stockvalue.subtract(mt.getAmountincurro()));
					System.out.println("CSTOCK = " + mt.getCstock() + " | StockValue = " + mt.getStockvalue());

				}
		
		return mt;
	}
}
