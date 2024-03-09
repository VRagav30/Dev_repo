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
public class SaleDeliveryReturn {
	
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
		System.out.println("BUOM QTY"+mt.getBuomqty()+" Groumrate"+oh.getGroumrate());
		
		mt.setDocqty(oh.getOrdmqty());
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

		List<Material> matList = mr.findAllByMatlcodeandPlantValsub(mt.getItemcode(), mt.getPlant(), mt.getValsub());
		mat = matList.get(0);
		
		System.out.println("---------------"+oh.getGroumrate());
		if(oh.getGroumrate()!=null)
		{
		mt.setOrdamount(mt.getBuomqty().multiply(oh.getGroumrate()));
		mt.setAmountincurro(mt.getOrdamount());
		}
		else {
			System.out.println("Please enter groumrate in order creation");
				mt.setAmountincurro(mt.getOrdamount());
		}	
				List<Caval> cvList = cvr.findAllbyOrgnCodeMatValGrp("INV", oh.getOrgncode(), mat.getValgrp());
				caval = cvList.get(0);
				mt.setCostacc(caval.getCostacc());
				mt.setOrdcurrency(oh.getOrdcurrency());
				
				// CSTOCK STOCKVALUE
				mt.setAdindicator("+");
				BigDecimal stock[];
				BigDecimal stockValueCorrection = new BigDecimal(0), cstockCorrection = new BigDecimal(0);
				List<BigDecimal[]> mtrList; if (mat.getBatch().equalsIgnoreCase("YES") &&
						  mat.getBatchcosting().equalsIgnoreCase("NO")) mtrList =
						  mtr.findCstockStockwithoutBatch(mt.getItemcode(), mt.getPlant(),
						  mt.getDate(), mt.getStocktype(), mt.getValsub(), mt.getSaleorder()); else
						  mtrList = mtr.findCstockStockwithBatch(mt.getItemcode(), mt.getPlant(),
						  mt.getDate(), mt.getStocktype(), mt.getValsub(), mt.getBatchnumber(),
						  mt.getSaleorder());
				
				
				cstockCorrection = mt.getBuomqty();
				stockValueCorrection = mt.getAmountincurro();
				if (mtrList.isEmpty()) {
					mt.setCstock(cstockCorrection);
					mt.setStockvalue(stockValueCorrection);
					System.out.println("IF CSTOCK = " + mt.getCstock() + " | StockValue = " + mt.getStockvalue());

				} else {
					stock = mtrList.get(0);
					System.out.println(mtrList.size());
					BigDecimal cstock = stock[0];
					BigDecimal stockvalue = stock[1];
					System.out.println(cstock+"----"+stockvalue+"----"+stock[2]+"----"+cstockCorrection+"----"+stockValueCorrection);
					mt.setCstock(cstock.add(cstockCorrection));
					mt.setStockvalue(stockvalue.add(stockValueCorrection));
					System.out.println("else CSTOCK = " + mt.getCstock() + " | StockValue = " + mt.getStockvalue());

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
					mmtotal.setCstock(mmtotal.getCstock().add(cstockCorrection));
					mmtotal.setStockvalue(mmtotal.getStockvalue().add(stockValueCorrection));
					System.out.println("NEW : CSTOCK = " + mmtotal.getCstock() + " | StockValue = " + mmtotal.getStockvalue());
					if (mt.getBatchnumber()!=null && mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("NO"))
						mt.setBstock(mmtotal.getBstock().add(mt.getBuomqty()));
					mtr.save(mmtotal);

				}
				return mt;
	}
}
