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
import com.cop.model.database.Orderheader;
import com.cop.model.database.Transaction;
import com.cop.repository.transaction.BomRepository;
import com.cop.repository.transaction.CostTotalRepository;
import com.cop.repository.transaction.MMTotalRespository;
import com.cop.repository.transaction.NumberRangeRepository;
import com.cop.repository.transaction.OrderHeaderRepository;

@Component
public class Subcon {

	@Autowired
	OrderHeaderRepository Ohr;
	@Autowired
	private InvoiceBooking ib;
	@Autowired
	private ServiceRequest sr;
	@Autowired
	BomRepository bomr;
	private Bom bom;

	@Autowired
	SubconProcessingCharge spc;
	@Autowired
	SubconGoodsIssued sgi;
	@Autowired
	SubconGoodsRecieved sgr;
	@Autowired
	NumberRangeRepository nrr;
	@Autowired
	CostTotalRepository ctr;
	@Autowired
	MMTotalRespository mtr;
	
	@Autowired
	private DNQR dnqr;
	@Autowired
	private DNVR dnvr;
	@Autowired
	private CNQR cnqr;
	@Autowired
	private CNVR cnvr;
	private Numberrange nr;
	private Orderheader oh,loh;
	Costtotal pct;
	Mmtotal gimt;

	public void processSubconRecords(List<Transaction> subconRecords) {
		Map<String, Map<String, List<Transaction>>> map = splitByCobjtypeAndTrnsevent(subconRecords);
		Iterator<String> itr = map.keySet().iterator();

		while (itr.hasNext()) {
			List<Costtotal> ibCTTransactions = new ArrayList<>();
			List<Mmtotal> ibMTTransactions = new ArrayList<>();
			
			List<Costtotal> srCTTransactions = new ArrayList<>();
			List<Mmtotal> srMTTransactions = new ArrayList<>();
			List<Costtotal> pcTransactions = new ArrayList<>();
			List<Mmtotal> giTransactions = new ArrayList<>();
			List<Mmtotal> grTransactions = new ArrayList<>();
			List<Mmtotal> grTransactionAutopost = new ArrayList<>();
			
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
				case "PC":

					for (Transaction source : tempMap.get("PC")) {
						Costtotal target = new Costtotal();
						BeanUtils.copyProperties(source, target);
						pcTransactions.add(target);
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
				case "IB":
					for (Transaction source : tempMap.get("IB")) {
						Costtotal ctTarget = new Costtotal();
						BeanUtils.copyProperties(source, ctTarget);
						Costtotal ibCostTotal = ib.generateCostTotal(ctTarget);
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
									
									mtContra = ib.generateMMTotalContra(mtContra);
									ibMTTransactions.add(mtContra);
									//mtr.save(mtContra);
								} else if(ctTarget.getPrtnrcode()!=null || loh.getCostcenter()!=null) {

									System.out.println("Now insert in COSTTOTAL for ib Contra");
									Costtotal ctContra = new Costtotal();
									BeanUtils.copyProperties(ctTarget, ctContra);
									ctContra.setCosttotalid(null);
									ctContra = ib.generateCostTotalContra(ctContra);
									ibCTTransactions.add(ctContra);
									//ctr.save(ctContra);

								}
							} else if (ctTarget.getItemtype().equals("M")){
								Mmtotal mmTarget = new Mmtotal();
								BeanUtils.copyProperties(ibCostTotal, mmTarget);
								Mmtotal ibMMTotal = ib.generateMMTotal(mmTarget);
								ibMTTransactions.add(ibMMTotal);
							}
						}
					break;
				case "SR":
					for (Transaction source : tempMap.get("SR")) {
							Costtotal ctTarget = new Costtotal();
							BeanUtils.copyProperties(source, ctTarget);
							Costtotal srCostTotal = sr.generateCostTotal(ctTarget);
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
								
								mtContra = sr.generateMMTotalContra(mtContra);
								
								srMTTransactions.add(mtContra);
							} else if(ctTarget.getPrtnrcode()!=null || loh.getCostcenter()!=null){
								System.out.println("Now insert in COSTTOTAL for SR Contra");
								Costtotal ctContra = new Costtotal();
								BeanUtils.copyProperties(srCostTotal, ctContra);
								ctContra.setCosttotalid(null);
								ctContra = sr.generateCostTotalContra(ctContra);
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
					System.out.println("In correct event in Subcon!");
				}
			if (grTransactionAutopost.size() > 0) {
				for (Mmtotal grmmtotal : grTransactionAutopost) {
					System.out.println("Deriving values for : "+grmmtotal.getItemcode());
					pct = new Costtotal();
					List<Orderheader> ohList = Ohr.findAllByOrdernum(grmmtotal.getObjcode(), grmmtotal.getObjitnum());
					oh = ohList.get(0);
					pct.setDate(grmmtotal.getDate());
					pct.setDocdesc("PC AUTOPOST");
					pct.setCobjtype("JOB");
					pct.setObjcode(grmmtotal.getObjcode());
					pct.setItemcode(grmmtotal.getItemcode());
					pct.setItemtype("M");
					pct.setObjitnum(grmmtotal.getObjitnum());
					pct.setDocqty(grmmtotal.getDocqty());
					pct.setTrnsevent("PC");
					pcTransactions.add(pct);
					System.out.println("Derived and saved values for PC : "+pct.getItemcode());
					List<Bom> bomList = bomr.findAllbyBomcodeandItemcode(grmmtotal.getItemcode(), oh.getBomcode(),oh.getPlant());
					for (Bom bom : bomList) 
					{
						gimt = new Mmtotal();
						gimt.setDate(grmmtotal.getDate());
						gimt.setDocdesc("GI AUTOPOST");
						gimt.setCobjtype("JOB");
						gimt.setObjcode(grmmtotal.getObjcode());
						gimt.setItemcode(bom.getImatl().toUpperCase());
						gimt.setObjitnum(grmmtotal.getObjitnum());
						gimt.setDocuom(bom.getIbuom());
						//gimt.setDocqty(grmmtotal.getDocqty());
						//List<Bom> bomList	=bomr.findAllbyBomcodeandImatl(imatl, bomcode, plant)	
						//List<Mmtotal> MmList=mtr.findAllbytrnseventandcobjtypeitemcode(oh.getItemcode(), "JOB", "GR");
						//mt1=MmList.get(0);
						//DOCQTY
						if(oh.getAutopost().equals("YES") && oh.getOrdgruom().equalsIgnoreCase(bom.getPrduom()))
						{
							gimt.setDocqty((bom.getIbuqty().divide(bom.getPrduomqty())).multiply(grmmtotal.getDocqty()));
						}
						gimt.setTrnsevent("GI");
						giTransactions.add(gimt);
						System.out.println("Derived and saved values for GI : "+gimt.getItemcode() +" "+gimt.getDocqty());
					}
					grTransactions.add(grmmtotal);
					System.out.println("Derived and saved values for GR : "+grmmtotal.getItemcode());
				}
			}
		
		List<Numberrange> nrList = nrr.findAllByNumobject("COSTTOTALDOC");
		nr = nrList.get(0);
		BigDecimal currentNum = (nr.getCurrentnumber() == null ? nr.getRangefrom()
				: new BigDecimal(nr.getCurrentnumber().intValue() + 1));
		int objItNum = 10;
		BigDecimal orderAmount = new BigDecimal(0);
		for (Costtotal ct : pcTransactions) {
			ct.setDocnumber(currentNum);
			ct.setDocitnum(new BigDecimal(objItNum));
			objItNum += 10;
			ct = spc.generateCostTotal(ct);
			orderAmount = ct.getOrdamount().add(orderAmount);
			ctr.save(ct);
		}
		for (Mmtotal mt : giTransactions) {
			mt.setDocnumber(currentNum);
			mt.setDocitnum(new BigDecimal(objItNum));
			objItNum += 10;
			mt = sgi.generateMMTotal(mt);
			orderAmount = mt.getOrdamount().add(orderAmount);
			mtr.save(mt);
		}
		for (Mmtotal mt : grTransactions) {
			mt.setDocnumber(currentNum);
			mt.setDocitnum(new BigDecimal(objItNum));
			objItNum += 10;
			mt.setOrdamount(orderAmount);
			mt = sgr.generateMMTotal(mt);
			mtr.save(mt);
		}
		for (Costtotal ct : ibCTTransactions) {
			ct.setDocnumber(currentNum);
			ct.setDocitnum(new BigDecimal(objItNum));
			objItNum += 10;
			System.out.println("Saving IB JOB" + ct.getCobjtype()+ct.getObjcode());
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
					System.out.println("Adding to List : "+transaction.getTrnsevent()+transaction.getObjcode());
					tempList.add(transaction);
					tempMap.put(transaction.getTrnsevent(), tempList);
				} else {
					List<Transaction> tempList = new ArrayList<>();
					tempList.add(transaction);
					System.out.println("Adding to List : "+transaction.getTrnsevent()+transaction.getObjcode());
					tempMap.put(transaction.getTrnsevent(), tempList);
				}
				map.put(transaction.getObjcode(), tempMap);
			} else {
				Map<String, List<Transaction>> tempMap = new HashMap<>();

				List<Transaction> tempList = new ArrayList<>();
				tempList.add(transaction);
				System.out.println("Adding to List : "+transaction.getTrnsevent()+transaction.getObjcode());
				tempMap.put(transaction.getTrnsevent(), tempList);

				map.put(transaction.getObjcode(), tempMap);
			}
		}
		return map;
	}

}
