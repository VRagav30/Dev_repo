package com.cop.serviceimpl.service.event;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.cop.model.database.Costtotal;
import com.cop.model.database.Mmtotal;
import com.cop.model.database.Numberrange;
import com.cop.model.database.Orderheader;
import com.cop.model.database.Transaction;
import com.cop.repository.transaction.BomRepository;
import com.cop.repository.transaction.CostTotalRepository;
import com.cop.repository.transaction.MMTotalRespository;
import com.cop.repository.transaction.NumberRangeRepository;
import com.cop.repository.transaction.OprnpriceRepository;
import com.cop.repository.transaction.OrderHeaderRepository;
import com.cop.repository.transaction.PrcsstepRepository;
import com.cop.repository.transaction.PrcsstepopsRepository;
import com.cop.serviceapi.service.MMTotalService;

@Component
public class Purchase {

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
	private Orderheader oh,loh;
	@Autowired
	private PurchaseDNQR dnqr;
	@Autowired
	private PurchaseDNVR dnvr;
	@Autowired
	private PurchaseGoodsRecieve pgr;
	@Autowired
	private PurchaseInvoiceBooking pib;
	@Autowired
	private PurchaseServiceRequest pursr;
	@Autowired
	private PurchaseCNVR cnvr;
	@Autowired
	NumberRangeRepository nrr;
	@Autowired
	CostTotalRepository ctr;
	@Autowired
	MMTotalRespository mtr;
	@Autowired
	MMTotalService mts;
	private Numberrange nr;
	Costtotal oct;
	Mmtotal gimt;
@Autowired
PurchaseCNQR cnqr;
	public void processPurchaseRecords(List<Transaction> PurchaseRecords) {
		Map<String, Map<String, List<Transaction>>> map = splitByCobjtypeAndTrnsevent(PurchaseRecords);
		Iterator<String> itr = map.keySet().iterator();

		while (itr.hasNext()) {
			List<Costtotal> grCTTransactions = new ArrayList<>();
			List<Mmtotal> grMTTransactions = new ArrayList<>();
			
			List<Costtotal> ibCTTransactions = new ArrayList<>();
			List<Mmtotal> ibMTTransactions = new ArrayList<>();
			
			List<Costtotal> srCTTransactions = new ArrayList<>();
			List<Mmtotal> srMTTransactions = new ArrayList<>();
			
			List<Costtotal> dnqrCTTransactions = new ArrayList<>();
			List<Mmtotal> dnqrMTTransactions = new ArrayList<>();
			
			List<Costtotal> dnvrCTTransactions = new ArrayList<>();
			List<Mmtotal> dnvrMTTransactions = new ArrayList<>();
			
			List<Costtotal> cnqrCTTransactions = new ArrayList<>();
			List<Mmtotal> cnqrMTTransactions = new ArrayList<>();
			
			List<Costtotal> cnvrCTTransactions = new ArrayList<>();
			List<Mmtotal> cnvrMTTransactions = new ArrayList<>();
			
			Map<String, List<Transaction>> tempMap = map.get(itr.next());
			for (String str : tempMap.keySet())
				switch (str) {
				case "GR":
					for (Transaction source : tempMap.get("GR")) {
						Costtotal ctTarget = new Costtotal();
						BeanUtils.copyProperties(source, ctTarget);
						Costtotal grCostTotal = pgr.generateCostTotal(ctTarget);
						grCTTransactions.add(grCostTotal);
						
						Mmtotal mmTarget = new Mmtotal();
						BeanUtils.copyProperties(source, mmTarget);
						System.out.println("currency " +mmTarget.getOrdcurrency());
						List<Mmtotal> grMMTotal = pgr.generateMMTotal(mmTarget);
						for(Mmtotal mt:grMMTotal)
						grMTTransactions.add(mt);
					
					}
					break;
				case "IB":
					for (Transaction source : tempMap.get("IB")) {
						Costtotal ctTarget = new Costtotal();
						BeanUtils.copyProperties(source, ctTarget);
						Costtotal ibCostTotal = pib.generateCostTotal(ctTarget);
						ibCTTransactions.add(ibCostTotal);
						
						List<Orderheader> ohlist=Ohr.findAllByOrdernum(ctTarget.getObjcode(),ctTarget.getObjitnum());
						oh=ohlist.get(0);
						if(oh.getLinkedobjcode()!=null)
						{
						List<Orderheader> Lohlist=Ohr.findAllByOrdernum(oh.getLinkedobjcode(),oh.getObjitnum());
						loh=Lohlist.get(0);}
						
							
							if (ctTarget.getItemtype().equals("S")) {
								if ((ctTarget.getLinkedobjcode() != null) && (ctTarget.getLinkedobjitnum() != null) && loh.getCostcenter()==null) {
									System.out.println("Now insert in MMTOTAL for ib Contra");
									Mmtotal mtContra = new Mmtotal();
									BeanUtils.copyProperties(ctTarget, mtContra);
									
									mtContra = pib.generateMMTotalContra(mtContra);
									ibMTTransactions.add(mtContra);
									//mtr.save(mtContra);
								} else if(ctTarget.getPrtnrcode()!=null || loh.getCostcenter()!=null) {

									System.out.println("Now insert in COSTTOTAL for ib Contra");
									Costtotal ctContra = new Costtotal();
									BeanUtils.copyProperties(ctTarget, ctContra);
									ctContra.setCosttotalid(null);
									ctContra = pib.generateCostTotalContra(ctContra);
									ibCTTransactions.add(ctContra);
									//ctr.save(ctContra);

								}
							} else if (ctTarget.getItemtype().equals("M")){
								Mmtotal mmTarget = new Mmtotal();
								BeanUtils.copyProperties(ibCostTotal, mmTarget);
								Mmtotal ibMMTotal = pib.generateMMTotal(mmTarget);
								ibMTTransactions.add(ibMMTotal);
							}
						}
					break;
				case "SR":
					for (Transaction source : tempMap.get("SR")) {
							Costtotal ctTarget = new Costtotal();
							BeanUtils.copyProperties(source, ctTarget);
							Costtotal srCostTotal = pursr.generateCostTotal(ctTarget);
							srCTTransactions.add(srCostTotal);
						
							List<Orderheader> ohlist=Ohr.findAllByOrdernum(ctTarget.getObjcode(),ctTarget.getObjitnum());
							oh=ohlist.get(0);
							if(oh.getLinkedobjcode()!=null)
							{
							List<Orderheader> Lohlist=Ohr.findAllByOrdernum(oh.getLinkedobjcode(),oh.getObjitnum());
							loh=Lohlist.get(0);}
							if ((ctTarget.getLinkedobjcode() != null) && (ctTarget.getLinkedobjitnum() != null) && loh.getCostcenter()==null) {
								System.out.println("Now insert in MMTOTAL for SR Contra");
								Mmtotal mtContra = new Mmtotal();
								BeanUtils.copyProperties(srCostTotal, mtContra);
								
								mtContra = pursr.generateMMTotalContra(mtContra);
								
								srMTTransactions.add(mtContra);
							} else if(ctTarget.getPrtnrcode()!=null || loh.getCostcenter()!=null){
								System.out.println("Now insert in COSTTOTAL for SR Contra");
								Costtotal ctContra = new Costtotal();
								BeanUtils.copyProperties(srCostTotal, ctContra);
								ctContra.setCosttotalid(null);
								ctContra = pursr.generateCostTotalContra(ctContra);
								srCTTransactions.add(ctContra);
							}
					
					}
					break;
				case "DNQR":

					for (Transaction source : tempMap.get("DNQR")) {
						Costtotal ctTarget = new Costtotal();
						BeanUtils.copyProperties(source, ctTarget);
						Costtotal dnqrCostTotal = dnqr.generateCostTotal(ctTarget);
						dnqrCTTransactions.add(dnqrCostTotal);
						
						
						List<Orderheader> ohlist=Ohr.findAllByOrdernum(ctTarget.getObjcode(),ctTarget.getObjitnum());
						oh=ohlist.get(0);
						if(oh.getLinkedobjcode()!=null)
						{
						List<Orderheader> Lohlist=Ohr.findAllByOrdernum(oh.getLinkedobjcode(),oh.getObjitnum());
						loh=Lohlist.get(0);}
							if (ctTarget.getItemtype().equals("S")) {
								if ((ctTarget.getLinkedobjcode() != null) && (ctTarget.getLinkedobjitnum() != null)&& loh.getCostcenter()==null) {
									System.out.println("Now insert in MMTOTAL for DNQR Contra");
									Mmtotal mtContra = new Mmtotal();
									BeanUtils.copyProperties(ctTarget, mtContra);
									
									mtContra = dnqr.generateMMTotalContra(mtContra);
									dnqrMTTransactions.add(mtContra);
									//mtr.save(mtContra);
								} else if(ctTarget.getPrtnrcode()!=null ||  loh.getCostcenter()!=null) {

									System.out.println("Now insert in COSTTOTAL for DNQR Contra");
									Costtotal ctContra = new Costtotal();
									BeanUtils.copyProperties(ctTarget, ctContra);
									ctContra.setCosttotalid(null);
									ctContra = dnqr.generateCostTotalContra(ctContra);
									dnqrCTTransactions.add(ctContra);
									//ctr.save(ctContra);

								}
							} else if (ctTarget.getItemtype().equals("M")){
								Mmtotal mmTarget = new Mmtotal();
								BeanUtils.copyProperties(source, mmTarget);
								Mmtotal dnqrMMTotal = dnqr.generateMMTotal(mmTarget);
								dnqrMTTransactions.add(dnqrMMTotal);
							}
						}
					
					break;
				case "CNQR":
					for (Transaction source : tempMap.get("CNQR")) {
						Costtotal ctTarget = new Costtotal();
						BeanUtils.copyProperties(source, ctTarget);
						Costtotal cnqrCostTotal = cnqr.generateCostTotal(ctTarget);
						cnqrCTTransactions.add(cnqrCostTotal);
						List<Orderheader> ohlist=Ohr.findAllByOrdernum(ctTarget.getObjcode(),ctTarget.getObjitnum());
						oh=ohlist.get(0);
						if(oh.getLinkedobjcode()!=null)
						{
						List<Orderheader> Lohlist=Ohr.findAllByOrdernum(oh.getLinkedobjcode(),oh.getObjitnum());
						loh=Lohlist.get(0);}
						if (ctTarget.getItemtype().equals("S")) {
							if ((ctTarget.getLinkedobjcode() != null) && (ctTarget.getLinkedobjitnum() != null) && loh.getCostcenter()==null ) {
								System.out.println("Now insert in MMTOTAL for CNQR Contra");
								Mmtotal mtContra = new Mmtotal();
								BeanUtils.copyProperties(ctTarget, mtContra);
								
								mtContra = cnqr.generateMMTotalContra(mtContra);
								cnqrMTTransactions.add(mtContra);
								//mtr.save(mtContra);
							} else if(ctTarget.getPrtnrcode()!=null || loh.getCostcenter()!=null) {

								System.out.println("Now insert in COSTTOTAL for CNQR Contra");
								Costtotal ctContra = new Costtotal();
								BeanUtils.copyProperties(ctTarget, ctContra);
								ctContra.setCosttotalid(null);
								ctContra = cnqr.generateCostTotalContra(ctContra);
								cnqrCTTransactions.add(ctContra);
								//ctr.save(ctContra);

							}
						} else if (ctTarget.getItemtype().equals("M")){
							Mmtotal mmTarget = new Mmtotal();
							BeanUtils.copyProperties(source, mmTarget);
							Mmtotal cnqrMMTotal = cnqr.generateMMTotal(mmTarget);
							cnqrMTTransactions.add(cnqrMMTotal);
						}
						
					}
					break;
				case "DNVR":

					for (Transaction source : tempMap.get("DNVR")) {
						Costtotal ctTarget = new Costtotal();
						BeanUtils.copyProperties(source, ctTarget);
						Costtotal dnvrCostTotal = dnvr.generateCostTotal(ctTarget);
						dnvrCTTransactions.add(dnvrCostTotal);
						
						
						List<Orderheader> ohlist=Ohr.findAllByOrdernum(ctTarget.getObjcode(),ctTarget.getObjitnum());
						oh=ohlist.get(0);
						if(oh.getLinkedobjcode()!=null)
						{
						List<Orderheader> Lohlist=Ohr.findAllByOrdernum(oh.getLinkedobjcode(),oh.getObjitnum());
						loh=Lohlist.get(0);}
							if (ctTarget.getItemtype().equals("S")) {
								if ((ctTarget.getLinkedobjcode() != null) && (ctTarget.getLinkedobjitnum() != null) && loh.getCostcenter()==null) {
									System.out.println("Now insert in MMTOTAL for DNVR Contra");
									Mmtotal mtContra = new Mmtotal();
									BeanUtils.copyProperties(ctTarget, mtContra);
									
									mtContra = dnvr.generateMMTotalContra(mtContra);
									dnvrMTTransactions.add(mtContra);
									//mtr.save(mtContra);
								} else if(ctTarget.getPrtnrcode()!=null || loh.getCostcenter()!=null) {

									System.out.println("Now insert in COSTTOTAL for DNQR Contra");
									Costtotal ctContra = new Costtotal();
									BeanUtils.copyProperties(ctTarget, ctContra);
									ctContra.setCosttotalid(null);
									ctContra = dnvr.generateCostTotalContra(ctContra);
									dnvrCTTransactions.add(ctContra);
									//ctr.save(ctContra);

								}
							} else if (ctTarget.getItemtype().equals("M")){
								Mmtotal mmTarget = new Mmtotal();
								BeanUtils.copyProperties(source, mmTarget);
								Mmtotal dnvrMMTotal = dnvr.generateMMTotal(mmTarget);
								dnvrMTTransactions.add(dnvrMMTotal);
							}
						}
					
					break;
				case "CNVR":
					for (Transaction source : tempMap.get("CNVR")) {
						Costtotal ctTarget = new Costtotal();
						BeanUtils.copyProperties(source, ctTarget);
						Costtotal cnvrCostTotal = cnvr.generateCostTotal(ctTarget);
						cnvrCTTransactions.add(cnvrCostTotal);
						List<Orderheader> ohlist=Ohr.findAllByOrdernum(ctTarget.getObjcode(),ctTarget.getObjitnum());
						oh=ohlist.get(0);
						if(oh.getLinkedobjcode()!=null)
						{
						List<Orderheader> Lohlist=Ohr.findAllByOrdernum(oh.getLinkedobjcode(),oh.getObjitnum());
						loh=Lohlist.get(0);}
						if (ctTarget.getItemtype().equals("S")) {
							if ((ctTarget.getLinkedobjcode() != null) && (ctTarget.getLinkedobjitnum() != null) && loh.getCostcenter()==null) {
								System.out.println("Now insert in MMTOTAL for CNVR Contra");
								Mmtotal mtContra = new Mmtotal();
								BeanUtils.copyProperties(ctTarget, mtContra);
								
								mtContra = cnvr.generateMMTotalContra(mtContra);
								cnvrMTTransactions.add(mtContra);
								//mtr.save(mtContra);
							} else if(ctTarget.getPrtnrcode()!=null || loh.getCostcenter()!=null) {

								System.out.println("Now insert in COSTTOTAL for CNVR Contra");
								Costtotal ctContra = new Costtotal();
								BeanUtils.copyProperties(ctTarget, ctContra);
								ctContra.setCosttotalid(null);
								ctContra = cnvr.generateCostTotalContra(ctContra);
								cnvrCTTransactions.add(ctContra);
								//ctr.save(ctContra);

							}
						} else if (ctTarget.getItemtype().equals("M")){
							Mmtotal mmTarget = new Mmtotal();
							BeanUtils.copyProperties(source, mmTarget);
							Mmtotal cnvrMMTotal = cnvr.generateMMTotal(mmTarget);
							cnvrMTTransactions.add(cnvrMMTotal);
						}
						
					}
					break;
				default:
					System.out.println("Incorrect event in Purchase!");
				}
			
				List<Numberrange> nrList = nrr.findAllByNumobject("COSTTOTALDOC");
				nr = nrList.get(0);
				BigDecimal currentNum = (nr.getCurrentnumber() == null ? nr.getRangefrom()
						: new BigDecimal(nr.getCurrentnumber().intValue() + 1));
				int objItNum = 10;
				new BigDecimal(0);
				for (Costtotal ct : grCTTransactions) {
					ct.setDocnumber(currentNum);
					ct.setDocitnum(new BigDecimal(objItNum));
					objItNum += 10;
					
					ct = ctr.save(ct);
				}
				for (Mmtotal mt : grMTTransactions) {
					mt.setDocnumber(currentNum);
					mt.setDocitnum(new BigDecimal(objItNum));
					objItNum += 10;
					System.out.println(mt.getTrnsevent());
					mtr.save(mt);
				}
				for (Costtotal ct : ibCTTransactions) {
					ct.setDocnumber(currentNum);
					ct.setDocitnum(new BigDecimal(objItNum));
					objItNum += 10;
					
					ct = ctr.save(ct);
				}
				for (Mmtotal mt : ibMTTransactions) {
					mt.setDocnumber(currentNum);
					mt.setDocitnum(new BigDecimal(objItNum));
					objItNum += 10;
					
					mtr.save(mt);
				}
				for (Costtotal ct : srCTTransactions) {
					ct.setDocnumber(currentNum);
					ct.setDocitnum(new BigDecimal(objItNum));
					objItNum += 10;
					
					ct = ctr.save(ct);
				}
				for (Mmtotal mt : srMTTransactions) {
					mt.setDocnumber(currentNum);
					mt.setDocitnum(new BigDecimal(objItNum));
					objItNum += 10;
					System.out.println(mt.getTrnsevent());
					mtr.save(mt);
				}
				for (Costtotal ct : dnqrCTTransactions) {
					ct.setDocnumber(currentNum);
					ct.setDocitnum(new BigDecimal(objItNum));
					objItNum += 10;
					
					ctr.save(ct);
				}
				
				for (Mmtotal mt : dnqrMTTransactions) {
					mt.setDocnumber(currentNum);
					mt.setDocitnum(new BigDecimal(objItNum));
					objItNum += 10;
					System.out.println(mt.getTrnsevent());
					mtr.save(mt);
				}
				for (Costtotal ct : cnqrCTTransactions) {
					ct.setDocnumber(currentNum);
					ct.setDocitnum(new BigDecimal(objItNum));
					objItNum += 10;
					
					ctr.save(ct);
				}
				
				for (Mmtotal mt : cnqrMTTransactions) {
					mt.setDocnumber(currentNum);
					mt.setDocitnum(new BigDecimal(objItNum));
					objItNum += 10;
					
					mtr.save(mt);
				}
				
				for (Costtotal ct : dnvrCTTransactions) {
					ct.setDocnumber(currentNum);
					ct.setDocitnum(new BigDecimal(objItNum));
					objItNum += 10;
					
					ctr.save(ct);
				}
				
				for (Mmtotal mt : dnvrMTTransactions) {
					mt.setDocnumber(currentNum);
					mt.setDocitnum(new BigDecimal(objItNum));
					objItNum += 10;
					
					mtr.save(mt);
				}
				for (Costtotal ct : cnvrCTTransactions) {
					ct.setDocnumber(currentNum);
					ct.setDocitnum(new BigDecimal(objItNum));
					objItNum += 10;
					
					ctr.save(ct);
				}
				
				for (Mmtotal mt : cnvrMTTransactions) {
					mt.setDocnumber(currentNum);
					mt.setDocitnum(new BigDecimal(objItNum));
					objItNum += 10;
					
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
