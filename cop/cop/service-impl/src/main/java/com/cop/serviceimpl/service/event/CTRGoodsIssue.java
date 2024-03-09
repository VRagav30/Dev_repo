package com.cop.serviceimpl.service.event;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cop.model.database.Altuom;
import com.cop.model.database.Caval;
import com.cop.model.database.Fydperiod;
import com.cop.model.database.Material;
import com.cop.model.database.Mmtotal;
import com.cop.model.database.Mrc;
import com.cop.model.database.Numberrange;
import com.cop.repository.transaction.AltuomRepository;
import com.cop.repository.transaction.CTRRepository;
import com.cop.repository.transaction.CavalRepository;
import com.cop.repository.transaction.FydPeriodRepository;
import com.cop.repository.transaction.MMTotalRespository;
import com.cop.repository.transaction.MaterialRepository;
import com.cop.repository.transaction.MrcRepository;
import com.cop.repository.transaction.NumberRangeRepository;
import com.cop.repository.transaction.OrganisationRepository;
import com.cop.repository.transaction.PlantRepository;
import com.cop.serviceapi.service.MMTotalService;
@Component
public class CTRGoodsIssue {
	@Autowired
	AltuomRepository mar;
	@Autowired
	FydPeriodRepository fpr;
	@Autowired
	PlantRepository pr;
	@Autowired
	OrganisationRepository or;
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
	CTRRepository ctrr;
	private Numberrange nr;
	private Material mat;
	private Altuom matalt;
	
	private Caval caval;
	public Mmtotal generateMMTotal(Mmtotal mt) {
	System.out.println("Entering CTR ");
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
	// BUOM
				List<Material> matList = mr.findAllByMatlcodeandPlantValsub(mt.getItemcode(),mt.getPlant(),mt.getValsub());
				mat = matList.get(0);
				mt.setBuom(mat.getBuom().toUpperCase());
				// BUOMQTY
				List<Altuom> altList = mar.findAltuombyMatCodePlantAltUOM(mt.getPlant(), mt.getItemcode(), mt.getDocuom(),
						mat.getBuom());
				matalt = altList.get(0);
				if (mt.getDocuom().toString().toUpperCase().equals(matalt.getBuom().toUpperCase()))
					mt.setBuomqty(mt.getDocqty());
				else {
					BigDecimal qty = matalt.getBuqty().divide(matalt.getAltuomqty());
					System.out.println(
							"buqty " + matalt.getBuqty() + "altuomgty " + matalt.getAltuomqty() + "Quantity ratio " + qty);
					mt.setBuomqty(mt.getDocqty().multiply(qty));
				}
				mt.setUsercode("SYSTEM");
				mt.setCreatedtime(new Timestamp(new Date().getTime()));
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
			List<Material> matList1 = mr.findAllByMatlcodeandPlantValsub(mt.getItemcode(), mt.getPlant(),mt.getValsub());
			mat = matList1.get(0);
			
			String orgncode=ctrr.findOrgncode(mt.getObjcode());
			
			
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
			
			List<Caval> cvList = cvr.findAllbyOrgnCodeMatValGrp("GI", orgncode, mat.getValgrp());
			caval = cvList.get(0);
			mt.setCostacc(caval.getCostacc());
			
			
			// CTOCK ADINDICATOR STOCKVALUE
			mt.setAdindicator("-");
			BigDecimal[] stock;
			List<BigDecimal[]> mtrList = mtr.findCstockbyItemcodePlantValsubBatch(mt.getItemcode(), mt.getPlant(),
					mt.getDate(), mt.getValsub(), mt.getBatchnumber(),mt.getSaleorder());
			
			if (mtrList.isEmpty()) {
				mt.setCstock(mt.getBuomqty().negate());
				mt.setStockvalue(mt.getAmountincurro().negate());
			} else {
				stock = mtrList.get(0);
				BigDecimal cstock = stock[0];
				BigDecimal stockvalue = stock[1];
				mt.setCstock(cstock.subtract(mt.getBuomqty()));
				mt.setStockvalue(stockvalue.subtract(mt.getAmountincurro()));
				BigDecimal bstock=stock[2];
				 if (mat.getBatch().equalsIgnoreCase("YES") &&  mat.getBatchcosting().equalsIgnoreCase("NO"))
						 mt.setBstock(mt.getBuomqty().subtract(bstock));
				System.out.println("CSTOCK = " + mt.getCstock() + " | StockValue = " + mt.getStockvalue());

			}
	
	return mt;
}

}
