package com.cop.serviceimpl.service.event;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cop.model.database.Altuom;
import com.cop.model.database.Material;
import com.cop.model.database.Mmtotal;
import com.cop.model.database.Mrc;
import com.cop.repository.transaction.AltuomRepository;
import com.cop.repository.transaction.MMTotalRespository;
import com.cop.repository.transaction.MaterialRepository;
import com.cop.repository.transaction.MrcRepository;

@Component
public class VendorStock {

	@Autowired
	MaterialRepository mr;
	@Autowired
	AltuomRepository mar;
	@Autowired
	MrcRepository mrcr;
	@Autowired
	MMTotalRespository mtr;

	private Mmtotal mtvsc;
	private Altuom matalt;
	private Mrc mrc;
	private Material mat;

	public List<Mmtotal> generateMMTotal(Mmtotal mt) throws ParseException {

		System.out.println(mt.getPlant());
		System.out.println(mt.getItemcode());
		System.out.println(mt.getDocuom());

		List<Altuom> altList = this.mar.findAltuombyMatCodePlantAltUOM(mt.getPlant(), mt.getItemcode(), mt.getDocuom(),
				mt.getBuom());
		this.matalt = altList.get(0);

		System.out.println(this.matalt.getBuom());

		/// BUOMQTY
		if (mt.getDocuom().toString().toUpperCase().equals(this.matalt.getBuom().toUpperCase())) {
			mt.setBuomqty(mt.getDocqty());
		} // need to derive
		else {

			BigDecimal qty = this.matalt.getBuqty().divide(this.matalt.getAltuomqty());
			System.out.println("buqty " + this.matalt.getBuqty() + "altuomgty " + this.matalt.getAltuomqty()
					+ "Quantity ratio " + qty);
			mt.setBuomqty(mt.getDocqty().multiply(qty));
		}

		List<Material> matList = mr.findAllByMatlcodeandPlantValsub(mt.getItemcode(), mt.getPlant(), mt.getValsub());
		mat = matList.get(0);
		if (mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("NO"))
			mt.setBstock(mt.getBuomqty());
		// AmountIncurro
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
		mt.setTvendor(mt.getVendor());

		BigDecimal[] stock;
		System.out.println(mt.getItemcode());
		System.out.println(mt.getPlant());
		System.out.println(mt.getDate());
		
		  List<BigDecimal[]> mtrList; 
		  if (mat.getBatch().equalsIgnoreCase("YES") &&
		  mat.getBatchcosting().equalsIgnoreCase("NO")) mtrList =
		  mtr.findCstockStockwithoutBatch(mt.getItemcode(), mt.getPlant(),
		  mt.getDate(), mt.getStocktype(), mt.getValsub(), mt.getSaleorder()); 
		  else
		  mtrList = mtr.findCstockStockwithBatch(mt.getItemcode(), mt.getPlant(),
		  mt.getDate(), mt.getStocktype(), mt.getValsub(), mt.getBatchnumber(),
		  mt.getSaleorder());
		 
		
		
				
		if (mtrList.isEmpty()) {
			mt.setCstock(mt.getBuomqty());
			mt.setStockvalue(mt.getAmountincurro());
		} else {
			stock = mtrList.get(0);
			BigDecimal cstock = stock[0];
			BigDecimal stockvalue = stock[1];

			mt.setCstock(mt.getBuomqty().add(cstock));
			mt.setStockvalue(mt.getAmountincurro().add(stockvalue));
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
			mmtotal.setCstock(mmtotal.getCstock().add(mt.getBuomqty()));
			mmtotal.setStockvalue(mmtotal.getStockvalue().add(mt.getAmountincurro()));
			if (mt.getBatchnumber()!=null && mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("NO"))
				mt.setBstock(mmtotal.getBstock().add(mt.getBuomqty()));
			mtr.save(mmtotal);
		}
		mt.setAdindicator("+");

		if (mt.getSstype() != null)
			if ((mt.getStocktype().equals("SV")) && (mt.getSstype().equals("S")))
				mt.setSstype(mt.getSstype());
			else if (mt.getStocktype().equals("OV") && mt.getSstype().equals("O"))
				mt.setSstype(mt.getSstype());
			else
				System.out.println("Entry Failure");
		mt.setSplant(mt.getPlant());
		mt.setTstype(mt.getStocktype());

		// For VS Contra
		this.mtvsc = new Mmtotal();
		this.mtvsc.setTrnsevent("VS-CONTRA");
		this.mtvsc.setPlant(mt.getPlant());
		this.mtvsc.setDate(mt.getDate());
		this.mtvsc.setYear(mt.getYear());
		this.mtvsc.setPeriod(mt.getPeriod());
		this.mtvsc.setItemcode(mt.getItemcode());
		this.mtvsc.setVendor(mt.getVendor());
		this.mtvsc.setDocnumber(mt.getDocnumber());
		this.mtvsc.setDocitnum(mt.getDocitnum().add(new BigDecimal(10)));
		this.mtvsc.setDocdesc(mt.getDocdesc());
		this.mtvsc.setRefdocnum(mt.getRefdocnum());
		this.mtvsc.setUsercode(mt.getUsercode());
		this.mtvsc.setCreatedtime(new Timestamp(new Date().getTime()));
		this.mtvsc.setBatchnumber(mt.getBatchnumber());
		this.mtvsc.setSaleorder(mt.getSaleorder());
		this.mtvsc.setDocqty(mt.getDocqty());
		this.mtvsc.setDocuom(mt.getDocuom());
		this.mtvsc.setBuom(mt.getBuom());
		this.mtvsc.setValsub(mt.getValsub());
		this.mtvsc.setBuomqty(mt.getBuomqty());
		this.mtvsc.setAmountincurro(mt.getAmountincurro());
		this.mtvsc.setAdindicator("-");
		this.mtvsc.setStocktype(mt.getSstype());
		List<BigDecimal[]> mtrListvsc;
		if (mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("NO"))
			mtrListvsc = mtr.findCstockStockwithoutBatch(mtvsc.getItemcode(), mtvsc.getPlant(), mtvsc.getDate(),
					mtvsc.getStocktype(), mtvsc.getValsub(), mtvsc.getSaleorder());
		else
			mtrListvsc = mtr.findCstockStockwithBatch(mtvsc.getItemcode(), mtvsc.getPlant(), mtvsc.getDate(),
					mtvsc.getStocktype(), mtvsc.getValsub(), mtvsc.getBatchnumber(), mtvsc.getSaleorder());
		BigDecimal[] stockvsc;
		if (mtrListvsc.isEmpty()) {
			this.mtvsc.setCstock(this.mtvsc.getBuomqty().negate());
			this.mtvsc.setStockvalue(this.mtvsc.getAmountincurro().negate());
		} else {
			System.out.println("Ampount in curro" + this.mtvsc.getAmountincurro());
			stockvsc = mtrListvsc.get(0);
			BigDecimal cstock = stockvsc[0];
			BigDecimal stockvalue = stockvsc[1];
			System.out.println("OSctock " + cstock + "OsStockvalue  " + stockvalue);
			this.mtvsc.setCstock(cstock.subtract(this.mtvsc.getBuomqty()));
			this.mtvsc.setStockvalue(stockvalue.subtract(this.mtvsc.getAmountincurro()));
		}
		List<Mmtotal> mtrLst1;
		/*
		 * if (mat.getBatch().equalsIgnoreCase("YES") &&
		 * mat.getBatchcosting().equalsIgnoreCase("NO")) mtrLst1 =
		 * mtr.findallbyBatchcosting(mtvsc.getItemcode(), mtvsc.getPlant(),
		 * mtvsc.getDate(), mtvsc.getStocktype(), mtvsc.getValsub(),
		 * mtvsc.getSaleorder()); else
		 */
			mtrLst1 = mtr.findCstockStockvaluebyGRDateandstocktype(mtvsc.getItemcode(), mtvsc.getPlant(),
					mtvsc.getDate(), mtvsc.getStocktype(), mtvsc.getValsub(), mtvsc.getBatchnumber(),
					mtvsc.getSaleorder());
		for (Mmtotal mmtotal : mtrLst1) {
			System.out.println("Updating " + mmtotal.getMmtotalid());
			mmtotal.setCstock(mmtotal.getCstock().subtract(mtvsc.getBuomqty()));
			mmtotal.setStockvalue(mmtotal.getStockvalue().subtract(mtvsc.getAmountincurro()));
			if (mtvsc.getBatchnumber()!=null && mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("NO"))
				mtvsc.setBstock(mmtotal.getBstock().add(mtvsc.getBuomqty()));
			mtr.save(mmtotal);
		}
		this.mtvsc.setSplant(mt.getSplant());
		this.mtvsc.setSstype(mt.getSstype());
		this.mtvsc.setTstype(mt.getTstype());
		this.mtvsc.setTvendor(this.mtvsc.getVendor());
		System.out.println(mt);
		List<Mmtotal> mmlist = new ArrayList<>();
		mmlist.add(mt);
		mmlist.add(this.mtvsc);
		return mmlist;

	}

}
