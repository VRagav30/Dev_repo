package com.cop.serviceimpl.service.event;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cop.model.database.Bom;
import com.cop.model.database.Caval;
import com.cop.model.database.Material;
import com.cop.model.database.Mmtotal;
import com.cop.model.database.Mrc;
import com.cop.model.database.Orderheader;
import com.cop.repository.transaction.BomRepository;
import com.cop.repository.transaction.CavalRepository;
import com.cop.repository.transaction.MMTotalRespository;
import com.cop.repository.transaction.MaterialRepository;
import com.cop.repository.transaction.MrcRepository;
import com.cop.repository.transaction.OrderHeaderRepository;
import com.cop.serviceapi.service.MMTotalService;

@Component
public class SubconGoodsIssued {

	@Autowired
	BomRepository bomr;
	private Bom bom;
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
	private Mmtotal mt1;

	public Mmtotal generateMMTotal(Mmtotal mt) {

		System.out.println("Entering Subcon Goods issued");
		mt = mmts.intializeMMTotal(mt);

		List<Orderheader> ohList = Ohr.findAllByOrdernum(mt.getObjcode(), mt.getObjitnum());
		oh = ohList.get(0);
		List<Material> matList = mr.findAllByMatlcodeandPlantValsub(mt.getItemcode(), mt.getPlant(), oh.getValsub());
		mat = matList.get(0);

		if (mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("NO"))
			mt.setBstock(mt.getBuomqty());

		mt.setVendor(oh.getVendor());

		// STOCKTYPE
		if (oh.getSoassignment() != null)
			mt.setStocktype("SV");
		else
			mt.setStocktype("OV");
		List<Mrc> MrcListStandard = mrcr.findAllbyMpricetypValsub(mt.getItemcode(), mt.getPlant(), mt.getDate(), "STD",
				mt.getValsub(), mt.getBatchnumber());

		List<Mrc> MrcListMoving = mrcr.findAllbyMpricetypValsub(mt.getItemcode(), mt.getPlant(), mt.getDate(), "MOVING",
				mt.getValsub(), mt.getBatchnumber());

		List<Mrc> MrcListPlan = mrcr.findAllbyMpricetypValsub(mt.getItemcode(), mt.getPlant(), mt.getDate(), "PLAN",
				mt.getValsub(), mt.getBatchnumber());

		if ((mat.getMattyp() != ("SFG")) && (mat.getMattyp() != ("FG"))) {
			if ((MrcListMoving.size() > 0) && (MrcListMoving.get(0) != null))
				mt.setOrdamount(mt.getBuomqty().multiply(MrcListMoving.get(0).getPrice()));
			else if ((MrcListStandard.size() > 0) && (MrcListStandard.get(0) != null))
				mt.setOrdamount(mt.getBuomqty().multiply(MrcListStandard.get(0).getPrice()));
			else if ((MrcListPlan.size() > 0) && (MrcListPlan.get(0) != null))
				mt.setOrdamount(mt.getBuomqty().multiply(MrcListPlan.get(0).getPrice()));
		}
		if ((mat.getMattyp().equals("SFG") || mat.getMattyp().equals("FG"))) {
			if ((MrcListStandard.size() > 0) && (MrcListStandard.get(0) != null)) {
				System.out.println(MrcListStandard.get(0).getPrice());
				mt.setOrdamount(mt.getBuomqty().multiply(MrcListStandard.get(0).getPrice()));
			} else if ((MrcListMoving.size() > 0) && (MrcListMoving.get(0) != null))
				mt.setOrdamount(mt.getBuomqty().multiply(MrcListMoving.get(0).getPrice()));
			else if ((MrcListPlan.size() > 0) && (MrcListPlan.get(0) != null))
				mt.setOrdamount(mt.getBuomqty().multiply(MrcListPlan.get(0).getPrice()));
		}

		// Amount in curro
		mt.setAmountincurro(mt.getOrdamount());
		// COSTACC
		BigDecimal[] stock;
		List<Caval> cvList = cvr.findAllbyOrgnCodeMatValGrp("GI", oh.getOrgncode(), mat.getValgrp());
		caval = cvList.get(0);
		mt.setCostacc(caval.getCostacc());

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
			System.out.println("CSTOCK = " + mt.getCstock() + " | StockValue = " + mt.getStockvalue());
		} else {
			stock = mtrList.get(0);
			BigDecimal cstock = stock[0];
			BigDecimal stockvalue = stock[1];
			mt.setCstock(cstock.subtract(mt.getBuomqty()));
			mt.setStockvalue(stockvalue.subtract(mt.getAmountincurro()));
			System.out.println("CSTOCK = " + mt.getCstock() + " | StockValue = " + mt.getStockvalue());

		}
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
			mmtotal.setStockvalue(mmtotal.getStockvalue().subtract(mt.getAmountincurro()));
			System.out.println("NEW : CSTOCK = " + mmtotal.getCstock() + " | StockValue = " + mmtotal.getStockvalue());
			if (mt.getBatchnumber() != null && mat.getBatch().equalsIgnoreCase("YES")
					&& mat.getBatchcosting().equalsIgnoreCase("NO"))
				mt.setBstock(mmtotal.getBstock().subtract(mt.getBuomqty()));
			mtr.save(mmtotal);
		}
		mt.setAdindicator("-");

		mt.setOrdcurrency(oh.getOrdcurrency());
		return mt;
	}
}
