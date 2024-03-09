package com.cop.serviceimpl.service.event;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cop.model.database.Altuom;
import com.cop.model.database.Caval;
import com.cop.model.database.Costtotal;
import com.cop.model.database.Material;
import com.cop.model.database.Mmtotal;
import com.cop.model.database.Mrc;
import com.cop.model.database.Numberrange;
import com.cop.model.database.Orderheader;
import com.cop.model.database.Transaction;
import com.cop.repository.transaction.AltuomRepository;
import com.cop.repository.transaction.CavalRepository;
import com.cop.repository.transaction.CostTotalRepository;
import com.cop.repository.transaction.MMTotalRespository;
import com.cop.repository.transaction.MaterialRepository;
import com.cop.repository.transaction.MrcRepository;
import com.cop.repository.transaction.NumberRangeRepository;
import com.cop.repository.transaction.OrderHeaderRepository;
import com.cop.serviceapi.service.CostTotalService;
import com.cop.serviceapi.service.MMTotalService;

@Component
public class StockTransportOrder {

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
	@Autowired
	NumberRangeRepository nrr;
	@Autowired
	CostTotalRepository ctr;
	private Orderheader oh;
	private Material mat;
	private Numberrange nr;
	Costtotal ct;
	@Autowired
	CostTotalService cs;
	@Autowired
	AltuomRepository mar;
	Altuom matalt;

	public void processSTOPRRecords(List<Transaction> prRecords) {
		List<Mmtotal> prTransactions = new ArrayList<>();
		Map<String, Integer> docnummap = new HashMap<>();
		Map<String, Integer> docitnummap = new HashMap<>();

		for (Transaction source : prRecords) {
			Mmtotal target = new Mmtotal();
			BeanUtils.copyProperties(source, target);
			Mmtotal mt = this.generateMMTotalPurchaseRecipet(target);
			prTransactions.add(mt);
		}

		Iterator<Mmtotal> it = prTransactions.iterator();
		while (it.hasNext()) {
			String key;
			Mmtotal mt = it.next();

			key = mt.getPlant().toString() + mt.getObjcode().toString() + mt.getCobjtype();

			if (docnummap.containsKey(key)) {

				mt.setDocnumber(new BigDecimal(docnummap.get(key)));

				int newitemnum = docitnummap.get(key) + 10;
				docitnummap.put(key, newitemnum);
				mt.setDocitnum(new BigDecimal(newitemnum));
				mtr.save(mt);
			} else {

				// DOCNUM
				List<Numberrange> nrList = nrr.findAllByNumobject("MMTOTALDOC");
				nr = nrList.get(0);
				BigDecimal currentNum = (nr.getCurrentnumber() == null ? nr.getRangefrom()
						: new BigDecimal(nr.getCurrentnumber().intValue() + 1));
				nr.setCurrentnumber(currentNum);
				mt.setDocnumber(currentNum);
				docnummap.put(key, mt.getDocnumber().intValue());
				docitnummap.put(key, 10);
				mt.setDocitnum(new BigDecimal(10));
				mtr.save(mt);
				nrr.save(nr);
			}

		}
	}

	public Mmtotal generateMMTotal(Mmtotal mt) {

		System.out.println("Entering Stock transaport order");
		mt = mmts.intializeMMTotal(mt);
		List<Orderheader> ohList = Ohr.findAllByOrdernum(mt.getObjcode(), mt.getObjitnum());
		oh = ohList.get(0);

		mt.setPlant(oh.getRplant());

		List<Costtotal> ctList = ctr.findAllBytrnsevent(oh.getPlant(), mt.getItemcode(), "DE", mt.getDate());

		System.out.println("plant=" + mt.getPlant() + "itemcode=" + mt.getItemcode());
		// ORDAMOUNT
		List<Material> matList = mr.findAllByMatlcodeandPlantValsub(mt.getItemcode(), oh.getRplant(), oh.getValsub());
		mat = matList.get(0);

		if (mat.getBatchcosting().equalsIgnoreCase("YES"))
			mt.setBatchnumber(oh.getBatchnumber());
		else
			mt.setBatchnumber(null);
		System.out.println("plant value in sto" + mt.getPlant());

		List<Mrc> MrcListStandard = mrcr.findAllbyMpricetypValsub(mt.getItemcode(), mt.getPlant(), mt.getDate(), "STD",
				mt.getValsub(), mt.getBatchnumber());

		List<Mrc> MrcListMoving = mrcr.findAllbyMpricetypValsub(mt.getItemcode(), mt.getPlant(), mt.getDate(), "MOVING",
				mt.getValsub(), mt.getBatchnumber());

		List<Mrc> MrcListPlan = mrcr.findAllbyMpricetypValsub(mt.getItemcode(), mt.getPlant(), mt.getDate(), "PLAN",
				mt.getValsub(), mt.getBatchnumber());

		if ((mat.getMattyp().equals("SFG") || mat.getMattyp().equals("FG")))
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

		mt.setOrdcurrency(oh.getOrdcurrency());
		mt.setSplant(oh.getPlant());
		mt.setTplant(oh.getRplant());

		mt.setAmountincurro(mt.getOrdamount());
		// COSTACC
		List<Caval> cvList = cvr.findAllbyOrgnCodeMatValGrp("INV", oh.getOrgncode(), mat.getValgrp());
		caval = cvList.get(0);
		mt.setCostacc(caval.getCostacc());
		// STOCKTYPE
		if (mt.getSaleorder() != null)
			mt.setStocktype("S");
		else
			mt.setStocktype("O");

		// CSTOCK ADINDICATOR STOCKVALUE
		mt.setAdindicator("+");

		mt.setIntransitqty(mt.getBuomqty());

		// Intransitvalue
		// BUQTY * receiving plant MRC price as per prioriey sequence logic (Same as in
		// Amtincurro ) + DE

		List<Material> RmatList = mr.findAllByMatlcodeandPlantValsub(mt.getItemcode(), mt.getPlant(), oh.getValsub());
		mat = RmatList.get(0);

		List<Mrc> RMrcListStandard = mrcr.findAllbyMpricetypValsub(mt.getItemcode(), mt.getPlant(), mt.getDate(), "STD",
				mt.getValsub(), mt.getBatchnumber());

		List<Mrc> RMrcListMoving = mrcr.findAllbyMpricetypValsub(mt.getItemcode(), mt.getPlant(), mt.getDate(),
				"MOVING", mt.getValsub(), mt.getBatchnumber());

		List<Mrc> RMrcListPlan = mrcr.findAllbyMpricetypValsub(mt.getItemcode(), mt.getPlant(), mt.getDate(), "PLAN",
				mt.getValsub(), mt.getBatchnumber());

		if ((mat.getMattyp().equals("SFG") || mat.getMattyp().equals("FG")))
			if ((RMrcListStandard.size() > 0) && (RMrcListStandard.get(0) != null)) {
				System.out.println(RMrcListStandard.get(0).getPrice());
				mt.setIntransitvalue(mt.getBuomqty().multiply(RMrcListStandard.get(0).getPrice()));
			} else if ((RMrcListMoving.size() > 0) && (RMrcListMoving.get(0) != null))
				mt.setIntransitvalue(mt.getBuomqty().multiply(RMrcListMoving.get(0).getPrice()));
			else if ((RMrcListPlan.size() > 0) && (RMrcListPlan.get(0) != null))
				mt.setIntransitvalue(mt.getBuomqty().multiply(RMrcListPlan.get(0).getPrice()));
		if ((mat.getMattyp() != ("SFG")) && (mat.getMattyp() != ("FG"))) {
			if ((RMrcListMoving.size() > 0) && (RMrcListMoving.get(0) != null))
				mt.setIntransitvalue(mt.getBuomqty().multiply(RMrcListMoving.get(0).getPrice()));
			else if ((RMrcListStandard.size() > 0) && (RMrcListStandard.get(0) != null))
				mt.setIntransitvalue(mt.getBuomqty().multiply(RMrcListStandard.get(0).getPrice()));
			else if ((RMrcListPlan.size() > 0) && (RMrcListPlan.get(0) != null))
				mt.setIntransitvalue(mt.getBuomqty().multiply(RMrcListPlan.get(0).getPrice()));
		}
		// adding DE to
		if (ctList.size() > 0 && (ctList.get(0).getItemcode().contentEquals(mt.getItemcode())))
			mt.setIntransitvalue(mt.getIntransitvalue().add(ctList.get(0).getAmountincurro()));

		/*
		 * if (mt.getDocnumber() == null) {
		 * 
		 * // DOCNUM List<Numberrange> nrList = nrr.findAllByNumobject("MMTOTALDOC"); nr
		 * = nrList.get(0); BigDecimal currentNum = (nr.getCurrentnumber() == null ?
		 * nr.getRangefrom() : new BigDecimal(nr.getCurrentnumber().intValue() + 1));
		 * nr.setCurrentnumber(currentNum); mt.setDocnumber(currentNum);
		 * mt.setDocitnum(new BigDecimal(10)); nrr.save(nr);
		 */

		String key;
		Map<String, Integer> docnummap = new HashMap<>();
		Map<String, Integer> docitnummap = new HashMap<>();

		if ((mt.getCobjtype() != null) && mt.getCobjtype().equals("STO"))
			key = mt.getPlant().toString() + mt.getObjcode().toString() + mt.getCobjtype().toString();
		else
			key = mt.getPlant().toString() + mt.getTrnsevent().toString();

		if (docnummap.containsKey(key)) {

			mt.setDocnumber(new BigDecimal(docnummap.get(key)));

			int newitemnum = docitnummap.get(key) + 10;
			docitnummap.put(key, newitemnum);
			mt.setDocitnum(new BigDecimal(newitemnum));
		} else {

			// DOCNUM
			List<Numberrange> nrList = nrr.findAllByNumobject("MMTOTALDOC");
			nr = nrList.get(0);
			BigDecimal currentNum = (nr.getCurrentnumber() == null ? nr.getRangefrom()
					: new BigDecimal(nr.getCurrentnumber().intValue() + 1));
			nr.setCurrentnumber(currentNum);
			mt.setDocnumber(currentNum);
			docnummap.put(key, mt.getDocnumber().intValue());
			docitnummap.put(key, 10);
			mt.setDocitnum(new BigDecimal(10));
		}
		// CSTOCK STOCKVALUE
		mt.setAdindicator("+");
		BigDecimal stock[];

		// 9/6/2021 maintaing previous Cstock in cstock value fro STO transaction
		List<BigDecimal[]> mtrList;
		if (mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("NO"))
			mtrList = mtr.findCstockStockwithoutBatch(mt.getItemcode(), mt.getPlant(), mt.getDate(), mt.getStocktype(),
					mt.getValsub(), mt.getSaleorder());
		else
			mtrList = mtr.findCstockStockwithBatch(mt.getItemcode(), mt.getPlant(), mt.getDate(), mt.getStocktype(),

					mt.getValsub(), mt.getBatchnumber(), mt.getSaleorder());

		if (mtrList.isEmpty()) {
			mt.setCstock(mt.getBuomqty());
			mt.setStockvalue(mt.getAmountincurro());
		}
		// if (mat.getBatch().equalsIgnoreCase("YES") &&
		// mat.getBatchcosting().equalsIgnoreCase("NO"))
		// mt.setBstock(mt.getBuomqty());}
		else {
			stock = mtrList.get(0);
			BigDecimal cstock = stock[0];
			BigDecimal stockvalue = stock[1];
			mt.setCstock(cstock);
			mt.setStockvalue(stockvalue);
			/*
			 * BigDecimal bstock = new BigDecimal(0); if (stock[2] != null) bstock =
			 * stock[2]; if (mat.getBatch().equalsIgnoreCase("YES") &&
			 * mat.getBatchcosting().equalsIgnoreCase("NO"))
			 * mt.setBstock(mt.getBuomqty().add(bstock));
			 */

			System.out.println("else CSTOCK = " + mt.getCstock() + " | StockValue = " + mt.getStockvalue());
		}
		mt.setBatchnumber(oh.getBatchnumber());
		BigDecimal[] btstock;
		BigDecimal bs = new BigDecimal(0);
		if (mt.getBatchnumber() != null) {
			List<BigDecimal[]> btList;
			btList = mtr.findCstockStockwithBatch(mt.getItemcode(), mt.getPlant(), mt.getDate(), mt.getStocktype(),
					mt.getValsub(), mt.getBatchnumber(), mt.getSaleorder());

			if (btList.isEmpty())
				bs = mt.getBuomqty();
			else {
				btstock = btList.get(0);
				if (btstock[2] != null)
					bs = btstock[2];
				else
					bs = new BigDecimal(0);
				if (mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("NO"))
					mt.setBstock(mt.getBuomqty().add(bs));
			}
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
			mmtotal.setCstock(mmtotal.getCstock());
			mmtotal.setStockvalue(mmtotal.getStockvalue());
			System.out.println("NEW : CSTOCK = " + mmtotal.getCstock() + " | StockValue = " + mmtotal.getStockvalue());
			if (mt.getBatchnumber() != null && mat.getBatch().equalsIgnoreCase("YES")
					&& mat.getBatchcosting().equalsIgnoreCase("NO"))
				mt.setBstock(mmtotal.getBstock().add(mt.getBuomqty()));
			mtr.save(mmtotal);
		}

		return mt;
	}

	public Mmtotal generateMMTotalContra(Mmtotal mt) {

		System.out.println("ENter STO Contra");
		List<Orderheader> ohList = Ohr.findAllByOrdernum(mt.getObjcode(), mt.getObjitnum());
		oh = ohList.get(0);
		mt.setPlant(oh.getPlant());
		mt.setTrnsevent("STO-CONTRA");
		mt.setObjitnum(mt.getObjitnum().add(new BigDecimal(10)));
		// mt.setDocitnum(mt.getDocitnum().add(new BigDecimal(10)));
		System.out.println("plant=" + mt.getPlant() + "itemcode=" + mt.getItemcode());
		// ORDAMOUNT
		List<Material> matList = mr.findAllByMatlcodeandPlantValsub(mt.getItemcode(), oh.getPlant(), oh.getValsub());
		mat = matList.get(0);

		List<Mrc> SMrcListStandard = mrcr.findAllbyMatlcodePlantDateMpricetyp(mt.getItemcode(), oh.getPlant(),
				mt.getDate(), "STD");

		List<Mrc> SMrcListMoving = mrcr.findAllbyMatlcodePlantDateMpricetyp(mt.getItemcode(), oh.getPlant(),
				mt.getDate(), "MOVING");

		List<Mrc> SMrcListPlan = mrcr.findAllbyMatlcodePlantDateMpricetyp(mt.getItemcode(), oh.getPlant(), mt.getDate(),
				"PLAN");
		System.out.println("Plant value in MRC" + mt.getPlant());

		if ((mat.getMattyp().equals("SFG") || mat.getMattyp().equals("FG")))
			if ((SMrcListStandard.size() > 0) && (SMrcListStandard.get(0) != null)) {
				System.out.println(SMrcListStandard.get(0).getPrice());
				mt.setOrdamount(mt.getBuomqty().multiply(SMrcListStandard.get(0).getPrice()));
			} else if ((SMrcListMoving.size() > 0) && (SMrcListMoving.get(0) != null))
				mt.setOrdamount(mt.getBuomqty().multiply(SMrcListMoving.get(0).getPrice()));
			else if ((SMrcListPlan.size() > 0) && (SMrcListPlan.get(0) != null))
				mt.setOrdamount(mt.getBuomqty().multiply(SMrcListPlan.get(0).getPrice()));
		if ((mat.getMattyp() != ("SFG")) && (mat.getMattyp() != ("FG")))

			if ((SMrcListMoving.size() > 0) && (SMrcListMoving.get(0) != null))
				mt.setOrdamount(mt.getBuomqty().multiply(SMrcListMoving.get(0).getPrice()));
			else if ((SMrcListStandard.size() > 0) && (SMrcListStandard.get(0) != null))
				mt.setOrdamount(mt.getBuomqty().multiply(SMrcListStandard.get(0).getPrice()));
			else if ((SMrcListPlan.size() > 0) && (SMrcListPlan.get(0) != null))
				mt.setOrdamount(mt.getBuomqty().multiply(SMrcListPlan.get(0).getPrice()));

		// Amount in curro
		mt.setAmountincurro(mt.getOrdamount());
		// CTOCK ADINDICATOR STOCKVALUE
		mt.setAdindicator("-");
		BigDecimal[] stock;

		/*
		 * List<BigDecimal[]> mtrList; if (mat.getBatch().equalsIgnoreCase("YES") &&
		 * mat.getBatchcosting().equalsIgnoreCase("NO")) mtrList =
		 * mtr.findCstockStockwithoutBatch(mt.getItemcode(), mt.getPlant(),
		 * mt.getDate(), mt.getStocktype(), mt.getValsub(), mt.getSaleorder()); else
		 * mtrList = mtr.findCstockStockwithBatch(mt.getItemcode(), mt.getPlant(),
		 * mt.getDate(), mt.getStocktype(), mt.getValsub(), mt.getBatchnumber(),
		 * mt.getSaleorder());
		 * 
		 * if (mtrList.isEmpty()) { mt.setCstock(mt.getBuomqty().negate());
		 * mt.setStockvalue(mt.getAmountincurro().negate()); } else { stock =
		 * mtrList.get(0); BigDecimal cstock = stock[0]; BigDecimal stockvalue =
		 * stock[1]; mt.setCstock(cstock.subtract(mt.getBuomqty()));
		 * mt.setStockvalue(stockvalue.subtract(mt.getAmountincurro()));
		 * System.out.println("CSTOCK = " + mt.getCstock() + " | StockValue = " +
		 * mt.getStockvalue());
		 * 
		 * } mt.setIntransitqty(new BigDecimal(0)); mt.setIntransitvalue(new
		 * BigDecimal(0)); mt.setDocitnum(mt.getDocitnum().add(new BigDecimal(10)));
		 * 
		 * List<Mmtotal> mtrLst; if (mat.getBatch().equalsIgnoreCase("YES") &&
		 * mat.getBatchcosting().equalsIgnoreCase("NO")) mtrLst =
		 * mtr.findallbyBatchcosting(mt.getItemcode(), mt.getPlant(), mt.getDate(),
		 * mt.getStocktype(), mt.getValsub(), mt.getSaleorder()); else mtrLst =
		 * mtr.findCstockStockvaluebyGRDateandstocktype(mt.getItemcode(), mt.getPlant(),
		 * mt.getDate(), mt.getStocktype(), mt.getValsub(), mt.getBatchnumber(),
		 * mt.getSaleorder()); for (Mmtotal mmtotal : mtrLst) {
		 * System.out.println("Updating " + mmtotal.getMmtotalid());
		 * mmtotal.setCstock(mmtotal.getCstock().add(mt.getCstock()));
		 * mmtotal.setStockvalue(mmtotal.getStockvalue().add(mt.getStockvalue())); if
		 * (mat.getBatch().equalsIgnoreCase("YES") &&
		 * mat.getBatchcosting().equalsIgnoreCase("NO"))
		 * mt.setBstock(mmtotal.getBstock().add(mt.getBuomqty())); mtr.save(mmtotal); }
		 */

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
			/*  */
		} else {
			stock = mtrList.get(0);
			BigDecimal cstock = stock[0];
			BigDecimal stockvalue = stock[1];
			mt.setCstock(cstock.subtract(mt.getBuomqty()));
			mt.setStockvalue(stockvalue.subtract(mt.getAmountincurro()));
			/*
			 * BigDecimal bstock = new BigDecimal(0); if (stock[2] != null) bstock =
			 * stock[2]; if (mat.getBatch().equalsIgnoreCase("YES") &&
			 * mat.getBatchcosting().equalsIgnoreCase("NO"))
			 * mt.setBstock(mt.getBuomqty().subtract(bstock));
			 */
			System.out.println("CSTOCK = " + mt.getCstock() + " | StockValue = " + mt.getStockvalue());

		}
		BigDecimal[] btstock;
		BigDecimal bs = new BigDecimal(0);
		if (mt.getBatchnumber() != null) {
			List<BigDecimal[]> btList;
			btList = mtr.findCstockStockwithBatch(mt.getItemcode(), mt.getPlant(), mt.getDate(), mt.getStocktype(),
					mt.getValsub(), mt.getBatchnumber(), mt.getSaleorder());
			
			if (btList.isEmpty())
				bs = mt.getBuomqty();
			else {
				btstock = btList.get(0);
				if (btstock[2] != null)
					bs = btstock[2];
				else
					bs = new BigDecimal(0);
				if (mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("NO"))
					mt.setBstock(mt.getBuomqty().add(bs));
			}
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
		return mt;
	}

	public Costtotal generateCostTotal(Costtotal ct) {

		ct = cs.initializeCostTotal(ct);
		List<Orderheader> ohList = Ohr.findAllByOrdernum(ct.getObjcode(), ct.getObjitnum());
		oh = ohList.get(0);
		ct.setPlant(oh.getPlant());
		List<Material> matList = mr.findAllByMatlcodeandPlantValsub(ct.getItemcode(), ct.getPlant(), oh.getValsub());
		mat = matList.get(0);

		List<Altuom> altList = this.mar.findAltuombyMatCodePlantAltUOM(this.oh.getPlant(), ct.getItemcode(),
				ct.getDocuom(), this.mat.getBuom());
		this.matalt = altList.get(0);

		ct.setRplant(oh.getRplant());
		// DOCNUM //DOCITNUM
		ct.setAmountincurro(ct.getOrdamount());

		if (ct.getDocnumber() == null) {
			// BUOMQTY
			if (ct.getDocuom().toString().toUpperCase().equals(this.matalt.getBuom().toUpperCase())) {
				ct.setBuomqty(ct.getDocqty());
			} // need to derive
			else {
				BigDecimal qty = this.matalt.getBuqty().divide(this.matalt.getAltuomqty());
				System.out.println("buqty " + this.matalt.getBuqty() + "altuomgty " + this.matalt.getAltuomqty()
						+ "Quantity ratio " + qty);
				ct.setBuomqty(ct.getDocqty().multiply(qty));
			}
			// DOCNUM
			List<Numberrange> nrList = nrr.findAllByNumobject("COSTTOTALDOC");
			nr = nrList.get(0);
			BigDecimal currentNum = (nr.getCurrentnumber() == null ? nr.getRangefrom()
					: new BigDecimal(nr.getCurrentnumber().intValue() + 1));
			nr.setCurrentnumber(currentNum);
			ct.setDocnumber(currentNum);
			ct.setDocitnum(new BigDecimal(10));
			nrr.save(nr);
		}

		return ct;
	}

	public Mmtotal generateMMTotalPurchaseRecipet(Mmtotal mt) {

		System.out.println("Entering STO-PR");
		mt = mmts.intializeMMTotal(mt);
		List<Orderheader> ohList = Ohr.findAllByOrdernum(mt.getObjcode(), mt.getObjitnum());
		oh = ohList.get(0);

		mt.setTrnsevent("PR");

		System.out.println("plant=" + mt.getPlant() + "itemcode=" + mt.getItemcode());
		if (oh.getSoassignment() != null)
			mt.setStocktype("S");
		else
			mt.setStocktype("O");

		List<Mmtotal> mt1 = mtr.findAllbyObjcodeandObjitnum(mt.getObjcode(), mt.getObjitnum(), mt.getDate());

		if (mt.getBuomqty().compareTo(mt1.get(0).getIntransitqty()) == 0)
			mt.setOrdamount(mt1.get(0).getIntransitvalue());
		else
			mt.setOrdamount((mt1.get(0).getIntransitvalue()
					.divide(mt1.get(0).getIntransitqty(), 2, RoundingMode.HALF_DOWN).multiply(mt.getDocqty())));

		mt.setAmountincurro(mt.getOrdamount());
		mt.setSplant(oh.getPlant());
		mt.setTplant(oh.getRplant());

		List<Material> matList = mr.findAllByMatlcodeandPlantValsub(mt.getItemcode(), oh.getPlant(), oh.getValsub());
		mat = matList.get(0);
		List<Caval> cvList = cvr.findAllbyOrgnCodeMatValGrp("INV", oh.getOrgncode(), mat.getValgrp());
		caval = cvList.get(0);
		mt.setCostacc(caval.getCostacc());
		mt.setOrdcurrency(mt1.get(0).getOrdcurrency());

		System.out.println("buomqty" + mt.getBuomqty());

		if (mt.getBuomqty().compareTo(mt1.get(0).getIntransitqty()) == 0) {
			mt.setIntransitqty(new BigDecimal(0));
			mt.setIntransitvalue(new BigDecimal(0));
		} else {
			mt.setIntransitqty(mt1.get(0).getIntransitqty().subtract(mt.getBuomqty()));
			mt.setIntransitvalue(mt1.get(0).getIntransitvalue().subtract(mt.getAmountincurro()));
		}
		// CTOCK ADINDICATOR STOCKVALUE
		mt.setAdindicator("+");
		BigDecimal[] stock;

		List<BigDecimal[]> mtrList;
		if (mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("NO"))
			mtrList = mtr.findCstockStockwithoutBatch(mt.getItemcode(), mt.getPlant(), mt.getDate(), mt.getStocktype(),
					mt.getValsub(), mt.getSaleorder());
		else
			mtrList = mtr.findCstockStockwithBatch(mt.getItemcode(), mt.getPlant(), mt.getDate(), mt.getStocktype(),
					mt.getValsub(), mt.getBatchnumber(), mt.getSaleorder());

		if (mtrList.isEmpty() || mtrList.get(0).equals(null)) {
			mt.setCstock(mt.getBuomqty());
			mt.setStockvalue(mt.getAmountincurro());
		} else {
			stock = mtrList.get(0);
			BigDecimal cstock = stock[0];
			BigDecimal stockvalue = stock[1];
			System.out.println(mt.getBuomqty());
			if (mt.getBuomqty().compareTo(mt1.get(0).getIntransitqty()) == 0) {
				mt.setCstock(cstock.add(mt.getBuomqty()));
				mt.setStockvalue(stockvalue.add(mt1.get(0).getIntransitvalue()));
			} else {
				mt.setCstock(cstock.add(mt.getBuomqty()));
				mt.setStockvalue(stockvalue.add(mt.getAmountincurro()));
			}

		}

		System.out.println("CSTOCK = " + mt.getCstock() + " | StockValue = " + mt.getStockvalue());

		mt.setSplant(oh.getPlant());
		mt.setTplant(oh.getRplant());

		// INTRANSIT VALUE , INTRANSQTY

		// mt.setDocnumber(mt1.get(0).getDocnumber());
		// mt.setDocitnum(mt1.get(0).getDocitnum().add(new BigDecimal(10)));

		System.out.println("REFERENCE COLUMS VALUES ");
		System.out.println("Plant  " + mt1.get(0).getPlant());
		System.out.println("mmid " + mt1.get(0).getMmtotalid());
		System.out.println("IntransitValue " + mt1.get(0).getIntransitvalue());
		System.out.println("Intransitqty " + mt1.get(0).getIntransitqty());

		BigDecimal[] btstock;
		BigDecimal bs = new BigDecimal(0);
		if (mt.getBatchnumber() != null) {
			List<BigDecimal[]> btList;
			btList = mtr.findCstockStockwithBatch(mt.getItemcode(), mt.getPlant(), mt.getDate(), mt.getStocktype(),
					mt.getValsub(), mt.getBatchnumber(), mt.getSaleorder());
					if (btList.isEmpty())
				bs = mt.getBuomqty();
			else {
				btstock = btList.get(0);
				if (btstock[2] != null)
					bs = btstock[2];
				else
					bs = new BigDecimal(0);
				if (mat.getBatch().equalsIgnoreCase("YES") && mat.getBatchcosting().equalsIgnoreCase("NO"))
					mt.setBstock(mt.getBuomqty().add(bs));
			}
		}

		return mt;
	}
}
