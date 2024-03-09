package com.cop.serviceimpl.service.event;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cop.model.database.Altuom;
import com.cop.model.database.Caval;
import com.cop.model.database.Fydperiod;
import com.cop.model.database.Material;
import com.cop.model.database.Mmtotal;
import com.cop.repository.transaction.AltuomRepository;
import com.cop.repository.transaction.CavalRepository;
import com.cop.repository.transaction.FydPeriodRepository;
import com.cop.repository.transaction.MMTotalRespository;
import com.cop.repository.transaction.MaterialRepository;
import com.cop.repository.transaction.OrganisationRepository;
import com.cop.repository.transaction.PlantRepository;

@Component
public class OpeningStock {
	@Autowired
	CavalRepository cr;
	@Autowired
	MaterialRepository mr;
	@Autowired
	MMTotalRespository mtr;
	@Autowired
	AltuomRepository mar;
	@Autowired
	PlantRepository pr;
	@Autowired
	OrganisationRepository or;
	@Autowired
	FydPeriodRepository fpr;

	private Material mat;
	private Caval caval;
	private Altuom matalt;

	public Mmtotal generateMMTotal(Mmtotal mt) throws ParseException {

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

		List<Altuom> altList = mar.findAltuombyMatCodePlantAltUOM(mt.getPlant(), mt.getItemcode(), mt.getDocuom(),
				mt.getBuom());
		matalt = altList.get(0);

		System.out.println(matalt.getBuom());

		/// BUOMQTY
		if (mt.getDocuom().toString().toUpperCase().equals(matalt.getBuom().toUpperCase()))
			mt.setBuomqty(mt.getDocqty());
		else {

			BigDecimal qty = matalt.getBuqty().divide(matalt.getAltuomqty());
			System.out.println(
					"buqty " + matalt.getBuqty() + "altuomgty " + matalt.getAltuomqty() + "Quantity ratio " + qty);
			mt.setBuomqty(mt.getDocqty().multiply(qty));
		}

		// Docitnum has to be created if more than one record for the combination of
		// Plant +itemcode+vendor/customer +date

		// COSTACC
		List<Material> matList = mr.findAllByMatlcodeandPlantValsub(mt.getItemcode(), mt.getPlant(), mt.getValsub());
		mat = matList.get(0);
		//if (mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("NO"))
			//mt.setBstock(mt.getBuomqty());

		// Derive from VALGRP of material code +Vevent - Inv
		List<Caval> cvList = cr.findAllbyMatlcode("INV", mat.getValgrp());
		caval = cvList.get(0);

		mt.setCostacc(caval.getCostacc());

		if (mt.getSaleorder() != null && mt.getVendor() != null)
			mt.setStocktype("SV");
		else if (mt.getSaleorder() != null)
			mt.setStocktype("S");
		else if (mt.getSaleorder() == null)
			mt.setStocktype("O");

		// CSTOCK
		// AddBUQTYtoexisting BuQty(plant +material
		// code+stocktype+Vendor/customer/saleorder/batch ) to arrive at new CSTOCK
		// List<BigDecimal[]> mtrList=mtr.findCstockStockValueforOs(mt.getMatlcode(),
		// mt.getPlant(), mt.getDate(), mt.getStocktype(), mt.getVendor());

		System.out.println(mt.getItemcode());
		System.out.println(mt.getPlant());
		System.out.println(mt.getDate());
		System.out.println(mt.getStocktype());
		// System.out.println(mtrList.toString()+"list");

		BigDecimal[] stock;
		BigDecimal[] batchstock;
		List<BigDecimal[]> mtrList;
		if (mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("NO"))
			mtrList = mtr.findCstockStockwithoutBatch(mt.getItemcode(), mt.getPlant(), mt.getDate(), mt.getStocktype(),
					mt.getValsub(), mt.getSaleorder());
		else
			mtrList = mtr.findCstockStockwithBatch(mt.getItemcode(), mt.getPlant(), mt.getDate(), mt.getStocktype(),
					mt.getValsub(), mt.getBatchnumber(), mt.getSaleorder());
		

		System.out.println(mtrList.toString().getBytes().toString() + "list");
		if (mtrList.isEmpty()) {
			mt.setCstock(mt.getBuomqty());
			mt.setStockvalue(mt.getAmountincurro());
			
			System.out.println(mt.getStockvalue() + "Stockvalue");
		} else {
			stock = mtrList.get(0);
			BigDecimal cstock = stock[0];
			BigDecimal stockvalue = stock[1];
			

			System.out.println(cstock + "hi " + stockvalue);
			mt.setCstock(mt.getBuomqty().add(cstock));
			mt.setStockvalue(mt.getAmountincurro().add(stockvalue));
			
		}
		BigDecimal[] btstock ; BigDecimal bs=new BigDecimal(0);
		if(mt.getBatchnumber()!=null) {
		List<BigDecimal[]>  btList;
		btList=mtr.findCstockStockwithBatch(mt.getItemcode(), mt.getPlant(), mt.getDate(), mt.getStocktype(),
		mt.getValsub(), mt.getBatchnumber(), mt.getSaleorder());
		
		if(btList.isEmpty()) 
			 bs=mt.getBuomqty();
		else
			{
			btstock=btList.get(0);
			if (btstock[2] != null)
			bs = btstock[2];
		else bs=new BigDecimal(0);
		 if (mat.getBatch().equalsIgnoreCase("YES") &&  mat.getBatchcosting().equalsIgnoreCase("NO"))
				 mt.setBstock(mt.getBuomqty().add(bs));}}
		List<Mmtotal> mtrLst;

		if (mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("NO"))
			mtrLst = mtr.findallbyBatchcosting(mt.getItemcode(), mt.getPlant(), mt.getDate(), mt.getStocktype(),
					mt.getValsub(), mt.getSaleorder());
		else

			mtrLst = mtr.findCstockStockvaluebyGRDateandstocktype(mt.getItemcode(), mt.getPlant(), mt.getDate(),
					mt.getStocktype(), mt.getValsub(), mt.getBatchnumber(), mt.getSaleorder());
		for (Mmtotal mmtotal : mtrLst) {
			System.out.println("Updating " + mmtotal.getMmtotalid());
			mmtotal.setCstock(mmtotal.getCstock().add(mt.getBuomqty()));
			mmtotal.setStockvalue(mmtotal.getStockvalue().add(mt.getAmountincurro()));
			if (mt.getBatchnumber() != null && mat.getBatch().equalsIgnoreCase("YES")
					&& mat.getBatchcosting().equalsIgnoreCase("NO"))
				mt.setBstock(mmtotal.getBstock().add(mt.getBuomqty()));
			mtr.save(mmtotal);
		}

		mt.setAdindicator("+");
		// value - is it same as stockvalue
		// Add amount in CURRO from Stock value (plant +material code +stocktype+Vendor
		// /customer/sale order /batch )
		// mt.setStockvalue(stockvalue);

		return mt;

	}

}
