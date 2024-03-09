package com.cop.serviceimpl.service.event;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cop.model.database.Bom;
import com.cop.model.database.Costtotal;
import com.cop.model.database.Mmtotal;
import com.cop.model.database.Numberrange;
import com.cop.model.database.Oprnprice;
import com.cop.model.database.Orderheader;
import com.cop.model.database.Prcsstep;
import com.cop.model.database.Prcsstepops;
import com.cop.model.database.Transaction;
import com.cop.repository.transaction.BomRepository;
import com.cop.repository.transaction.CostTotalRepository;
import com.cop.repository.transaction.MMTotalRespository;
import com.cop.repository.transaction.NumberRangeRepository;
import com.cop.repository.transaction.OprnpriceRepository;
import com.cop.repository.transaction.OrderHeaderRepository;
import com.cop.repository.transaction.PrcsstepRepository;
import com.cop.repository.transaction.PrcsstepopsRepository;

@Component
public class Rework {

	@Autowired
	PrcsstepRepository psr;
	@Autowired
	OprnpriceRepository opr;
	@Autowired
	PrcsstepopsRepository psor;
	@Autowired
	OrderHeaderRepository Ohr;
	@Autowired
	BomRepository bomr;
	private Orderheader oh;
	@Autowired
	private ReworkGoodReceived rgr;
	@Autowired
	private ReworkGoodsIssued rgi;
	@Autowired
	private ReworkOperationConfirmation roc;
	@Autowired
	NumberRangeRepository nrr;
	@Autowired
	CostTotalRepository ctr;
	@Autowired
	MMTotalRespository mtr;
	private Numberrange nr;
	Costtotal oct;
	Mmtotal gimt;

	public void processReworkRecords(List<Transaction> productionRecords) {
		Map<String, Map<String, List<Transaction>>> map = splitByCobjtypeAndTrnsevent(productionRecords);
		Iterator<String> itr = map.keySet().iterator();

		while (itr.hasNext()) {

			List<Costtotal> ocTransactions = new ArrayList<>();
			List<Mmtotal> giTransactions = new ArrayList<>();
			List<Mmtotal> grTransactions = new ArrayList<>();
			List<Mmtotal> grTransactionAutopost = new ArrayList<>();
			List<Mmtotal> bpTransactions=new ArrayList<>();

			Map<String, List<Transaction>> tempMap = map.get(itr.next());
			for (String str : tempMap.keySet())
				switch (str) {
				case "OC":

					for (Transaction source : tempMap.get("OC")) {
						Costtotal target = new Costtotal();
						BeanUtils.copyProperties(source, target);
						ocTransactions.add(target);
					}
					break;
				case "GI":

					for (Transaction source : tempMap.get("GI")) {
						Mmtotal target = new Mmtotal();
						BeanUtils.copyProperties(source, target);
						giTransactions.add(target);
					}
					break;
				case "GR":

					for (Transaction source : tempMap.get("GR")) {
						Mmtotal target = new Mmtotal();
						BeanUtils.copyProperties(source, target);
						List<Orderheader> ohList = Ohr.findAllByOrdernum(target.getObjcode(), target.getObjitnum());

						if (ohList.size() > 0 && ohList.get(0).getAutopost().equals("YES")) {
							grTransactionAutopost.add(target);
						} else {
							grTransactions.add(target);
						}
					}
					break;
				
				default:
					System.out.println("In correct event in Subcon!");
				}
			// AUTOPOST

			if (grTransactionAutopost.size() > 0) {
				for (Mmtotal grmmtotal : grTransactionAutopost) {
					System.out.println("Deriving values for : " + grmmtotal.getItemcode());
					
					List<Orderheader> ohList = Ohr.findAllByOrdernum(grmmtotal.getObjcode(), grmmtotal.getObjitnum());
					oh = ohList.get(0);
					List<Prcsstep> pList = psr.findAllbyprcsnum(oh.getPrscnum());
					String partnercode = pList.get(0).getPrcsctr();
					
					
					List<Oprnprice> oplist = opr.findListofOprnbyorgncodeandcctr(oh.getOrgncode(), partnercode,"P");
					
					for (Oprnprice op : oplist) {
						oct = new Costtotal();
						oct.setPrtnrcode(partnercode);
						oct.setDate(grmmtotal.getDate());
						oct.setDocdesc("OC AUTOPOST");
						oct.setCobjtype("PROD");
						oct.setObjcode(oh.getObjcode());
						oct.setItemcode(op.getOprn().toUpperCase());
						oct.setItemtype("O");
						oct.setObjitnum(oh.getObjitnum());
						oct.setTrnsevent("OC");
						oct.setDocuom(op.getOpuom());
						// DOCQTY
						List<Prcsstepops> psoList = psor.findAllbyoprn(op.getOprn(), oh.getPrscnum());
						oct.setDocqty((psoList.get(0).getOpqty().divide(pList.get(0).getPrcsuomqty()))
								.multiply(grmmtotal.getDocqty()));
						ocTransactions.add(oct);
					}
					System.out.println("Derived and saved values for OC : " + oct.getItemcode());
					List<Bom> bomList = bomr.findAllbyBomcodeandItemcode(grmmtotal.getItemcode(), oh.getBomcode(),
							oh.getPlant());
					for (Bom bom : bomList) {
						gimt = new Mmtotal();
						gimt.setDate(grmmtotal.getDate());
						gimt.setDocdesc("GI AUTOPOST");
						gimt.setCobjtype("PROD");
						gimt.setObjcode(grmmtotal.getObjcode());
						gimt.setItemcode(bom.getImatl().toUpperCase());
						gimt.setObjitnum(grmmtotal.getObjitnum());
						// DOCQTY
						if (oh.getAutopost().equals("YES") && oh.getOrdgruom().equalsIgnoreCase(bom.getPrduom())) {
							gimt.setDocqty(
									(bom.getIbuqty().divide(bom.getPrduomqty())).multiply(grmmtotal.getDocqty()));
						}
						gimt.setTrnsevent("GI");
						giTransactions.add(gimt);
						System.out.println(
								"Derived and saved values for GI : " + gimt.getItemcode() + " " + gimt.getDocqty());
					}
					grTransactions.add(grmmtotal);
					System.out.println("Derived and saved values for GR : " + grmmtotal.getItemcode());
				}
			}
				List<Numberrange> nrList = nrr.findAllByNumobject("COSTTOTALDOC");
				nr = nrList.get(0);
				BigDecimal currentNum = (nr.getCurrentnumber() == null ? nr.getRangefrom()
						: new BigDecimal(nr.getCurrentnumber().intValue() + 1));
				int objItNum = 10;
				new BigDecimal(0);
				for (Costtotal ct : ocTransactions) {
					ct.setDocnumber(currentNum);
					ct.setDocitnum(new BigDecimal(objItNum));
					objItNum += 10;
					ct = roc.generateCostTotal(ct);
					// orderAmount = ct.getOrdamount().add(orderAmount);

					Costtotal ctContra = new Costtotal();
					BeanUtils.copyProperties(ct, ctContra);
					ctContra.setDocitnum(new BigDecimal(objItNum));
					objItNum += 10;
					ctContra = roc.generateCostTotalContra(ctContra);
					ctr.save(ct);
					ctr.save(ctContra);
				}
				for (Mmtotal mt : giTransactions) {
					mt.setDocnumber(currentNum);
					mt.setDocitnum(new BigDecimal(objItNum));
					objItNum += 10;
					mt = rgi.generateMMTotal(mt);
					// orderAmount = mt.getOrdamount().add(orderAmount);
					mtr.save(mt);
				}
				for (Mmtotal mt : grTransactions) {
					mt.setDocnumber(currentNum);
					mt.setDocitnum(new BigDecimal(objItNum));
					objItNum += 10;
					// mt.setOrdamount(orderAmount);
					mt = rgr.generateMMTotal(mt);
					mtr.save(mt);
				}
				
				nr.setCurrentnumber(currentNum);
				nrr.save(nr);

			
		}
	}

	private Map<String, Map<String, List<Transaction>>> splitByCobjtypeAndTrnsevent(
			List<Transaction> subconJobRecords) {
		Map<String, Map<String, List<Transaction>>> map = new HashMap<>();
		Iterator<Transaction> itr = subconJobRecords.iterator();
		while (itr.hasNext()) {
			Transaction transaction = itr.next();
			if (map.containsKey(transaction.getObjcode())) {
				Map<String, List<Transaction>> tempMap = map.get(transaction.getObjcode());
				if (tempMap.containsKey(transaction.getTrnsevent())) {
					List<Transaction> tempList = tempMap.get(transaction.getTrnsevent());
					tempList.add(transaction);
					tempMap.put(transaction.getTrnsevent(), tempList);
				} else {
					List<Transaction> tempList = new ArrayList<>();
					tempList.add(transaction);
					tempMap.put(transaction.getTrnsevent(), tempList);
				}
				map.put(transaction.getObjcode(), tempMap);
			} else {
				Map<String, List<Transaction>> tempMap = new HashMap<>();

				List<Transaction> tempList = new ArrayList<>();
				tempList.add(transaction);
				tempMap.put(transaction.getTrnsevent(), tempList);

				map.put(transaction.getObjcode(), tempMap);
			}
		}
		return map;
	}

}
