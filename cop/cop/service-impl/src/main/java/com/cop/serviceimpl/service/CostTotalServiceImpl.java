package com.cop.serviceimpl.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cop.model.database.Altuom;
import com.cop.model.database.Caval;
import com.cop.model.database.Costtotal;
import com.cop.model.database.Currencyexchangerate;
import com.cop.model.database.Fydperiod;
import com.cop.model.database.Material;
import com.cop.model.database.Numberrange;
import com.cop.model.database.Orderheader;
import com.cop.model.database.Organisation;
import com.cop.model.database.Serviceitem;
import com.cop.repository.transaction.AltuomRepository;
import com.cop.repository.transaction.CavalRepository;
import com.cop.repository.transaction.CostTotalRepository;
import com.cop.repository.transaction.CurrencyExchangeRateRepository;
import com.cop.repository.transaction.FydPeriodRepository;
import com.cop.repository.transaction.MaterialRepository;
import com.cop.repository.transaction.NumberRangeRepository;
import com.cop.repository.transaction.OrderHeaderRepository;
import com.cop.repository.transaction.OrganisationRepository;
import com.cop.repository.transaction.PlantRepository;
import com.cop.repository.transaction.ServiceRepository;
import com.cop.serviceapi.service.CostTotalService;
import com.cop.serviceimpl.service.event.ProdOperationalCost;
import com.cop.serviceimpl.service.event.PurchaseCNQR;
import com.cop.serviceimpl.service.event.PurchaseDNQR;
import com.cop.serviceimpl.service.event.PurchaseDNVR;
import com.cop.serviceimpl.service.event.PurchaseGoodsRecieve;
import com.cop.serviceimpl.service.event.PurchaseInvoiceBooking;
import com.cop.serviceimpl.service.event.PurchaseServiceRequest;
import com.cop.serviceimpl.service.event.StockTransportOrder;
import com.cop.serviceimpl.service.event.SubconProcessingCharge;
import com.cop.utils.TransactionUtils;

@Service
public class CostTotalServiceImpl implements CostTotalService {
	@Autowired 
	TransactionUtils transUtil;
	Costtotal ct;
	@Autowired
	OrganisationRepository or;
	@Autowired
	CavalRepository cvr;
	@Autowired
	CostTotalRepository ctr;
	@Autowired
	PlantRepository pr;
	@Autowired
	OrderHeaderRepository Ohr;
	@Autowired
	FydPeriodRepository fpr;
	@Autowired
	MaterialRepository mr;
	@Autowired
	AltuomRepository mar;
	@Autowired
	CurrencyExchangeRateRepository cerr;
	@Autowired
	NumberRangeRepository nrr;
	@Autowired
	ServiceRepository sr;
	@Autowired
	PurchaseGoodsRecieve pgr;
	@Autowired
	PurchaseInvoiceBooking pib;
	@Autowired
	PurchaseServiceRequest psr;
	@Autowired
	SubconProcessingCharge pc;
	@Autowired
	ProdOperationalCost oc;
	@Autowired
	StockTransportOrder sto;
	
	
	@Autowired
	PurchaseDNQR dnqr;
	@Autowired
	PurchaseDNVR dnvr;
	@Autowired
	PurchaseCNQR cnqr;
	private Orderheader oh;
	private Material mat;
	private Numberrange nr;
    private Organisation org;
	private com.cop.model.database.Serviceitem srvc;
	private List<String> costTotalIDs;
	private Currencyexchangerate cer;

	// DOCNUMBER,
	// REFERENCEDOCNUMBER,OBJCODE,VENDOR,CUSTOMER,
	// ITEMTYPE,DOCQTY,DOCUOM,BUOM,BUOMQTY,ORDCURRENCY,ORDAMOUNT,
	// AMOUNTINCURRO,PRTNRCODE,TRNSEVENT,COSTACC,USERCODE,
	// REVERSALDOCUMENT,SALEORDER

	@Override
	public List<String> createAndSaveCostTotal(List<Map<String, Object>> records) {
		Iterator<Map<String, Object>> itr = records.iterator();
		Map<String, Integer> docnummap = new HashMap<>();
		Map<String, Integer> docitnummap = new HashMap<>();
		System.out.println(itr.toString());
		costTotalIDs = new ArrayList<>();
		try {
			while (itr.hasNext()) {
				Map<String, Object> map = itr.next();
				ct = new Costtotal();// Multiple options - PRDORD,PURORD,JOBORD,SALEORD,CODOC,RWORD ,STOORDER

				/* Mandatory From File Upload */
				/*-------------------------------------------------------------------------*/

				// type of order Multiple options - PRDORD,PURORD,JOBORD,SALEORD,CODOC,RWORD
				// ,STOORDER
				if (map.get("COBJTYPE") != null)
					ct.setCobjtype((map.get("COBJTYPE").toString()));

				if (map.get("TRNSEVENT") != null)
					ct.setTrnsevent((map.get("TRNSEVENT").toString()));

				// Ordernumber alias OBJCODE
				if (map.get("OBJCODE") != null)
					ct.setObjcode(map.get("OBJCODE").toString());

				if (map.get("OBJITNUM") != null)
					ct.setObjitnum(new BigDecimal(map.get("OBJITNUM").toString()));

				/*
				 * TODO add validation to check if combination of OBJCODE + OBJITNUM + COBJTYP
				 * exist in OrderHeader
				 */

				String key = ct.getObjcode().toString() + ct.getObjitnum().toString();
				if (docnummap.containsKey(key)) {

					ct.setDocnumber(new BigDecimal(docnummap.get(key)));

					int newitemnum = docitnummap.get(key) + 10;
					docitnummap.put(key, newitemnum);
					ct.setDocitnum(new BigDecimal(newitemnum));
				} else {

					// DOCNUM
					List<Numberrange> nrList = nrr.findAllByNumobject("COSTTOTALDOC");
					nr = nrList.get(0);
					BigDecimal currentNum = (nr.getCurrentnumber() == null ? nr.getRangefrom()
							: new BigDecimal(nr.getCurrentnumber().intValue() + 1));
					nr.setCurrentnumber(currentNum);
					ct.setDocnumber(currentNum);
					docnummap.put(key, ct.getDocnumber().intValue());
					docitnummap.put(key, 10);
					ct.setDocitnum(new BigDecimal(10));
				}

				if (map.get("DATE") != null) {
					ct.setDate(new SimpleDateFormat("yyyy-MM-dd").parse((map.get("DATE").toString())));
					java.util.Date d = ct.getDate();
					System.out.println(d);
				}

				if (map.get("REFERENCEDOCNUMBER") != null)
					ct.setRefdocnum(new BigDecimal(map.get("REFERENCEDOCNUMBER").toString()));

				if (map.get("DOCUMENTDESCRIPTION") != null)
					ct.setDocdesc(map.get("DOCUMENTDESCRIPTION").toString());

				/*-------------------------------------------------------------------------*/

				/* --------------------------USING ORDERHEADER ---------------------------- */
				List<Orderheader> ohList = Ohr.findAllByOrdernum(ct.getObjcode(), ct.getObjitnum());
				oh = ohList.get(0);

				if (oh.getSoassignment() != null)
					ct.setStocktype("S");
				else
					ct.setStocktype("O");

				// derive from orderheader using ordernum/objcode and objitnum
				ct.setOrgncode(oh.getOrgncode());

				// derive from orderheader using ordernum/objcode and objitnum
				ct.setPlant(oh.getPlant());

				// Equal to system year or system year minus one year
				if ((ct.getPlant() != null) && (ct.getDate() != null)) {

					String plant = ct.getPlant();
					String orgCode = pr.findOrgCodeByPlantId(plant);
					System.out.println(orgCode);
					String fydcode = or.findFydCodeByOrgnCode(orgCode);
					System.out.println(fydcode);
					java.sql.Date d = new java.sql.Date((ct.getDate()).getTime());
					System.out.println(d);
					List<Fydperiod> fp = fpr.findAllbyDate(fydcode, d);
					System.out.println(fp.get(0).getPeriod());
					ct.setYear((fp.get(0).getYear()));
					ct.setPeriod(fp.get(0).getPeriod());
				}

				// As per OBJCODE - OBJITNUM vendor
				ct.setVendor(oh.getVendor());

				// ORDCURRENCY
				ct.setOrdcurrency(oh.getOrdcurrency()); // derive from order header
				// DOCUOM
				ct.setDocuom(oh.getOrdgruom()); // derive from order header

				//
				ct.setItemcode(oh.getItemcode());

				/*
				 * ----------------------------USING MATERIAL
				 * MASTER-----------------------------
				 */
				List<Material> matList = mr.findAllByMatlcodeandPlantValsub(ct.getItemcode(),ct.getPlant(),oh.getValsub());
				mat = matList.get(0);

				System.out.println("Plant" + oh.getPlant() + "Matlcode" + ct.getItemcode());

				// Material (M)or service(S) or expenses(E)
				if (mat.getMattyp().toUpperCase() == "SER")
					ct.setItemtype("S");
				else
					ct.setItemtype("M");// derive from material master using material code

				// BUOM
				ct.setBuom(mat.getBuom().toUpperCase()); // derive from material master using material code

				System.out.println("buom" + ct.getBuom());

				/*------------------------------------Derived------------------------------*/

				ct.setUsercode("SYSTEM"); // hardcode as SYSTEM - not needed from excel

				// time stamp
				ct.setCreatedtime(new Timestamp(new Date().getTime()));

				// costacc
				List<Caval> cvList = cvr.findAllbyOrgnCodeMatValGrp("INV", oh.getOrgncode(), mat.getValgrp());
				ct.setCostacc(cvList.get(0).getCostacc());

				switch (ct.getCobjtype() + ":" + ct.getTrnsevent()) {
				case "PUR:GR":
					ct = pgr.generateCostTotal(ct);
					break;
				case "PUR:IB":
					ct = pib.generateCostTotal(ct);
					break;
				default:
					System.out.println("CostTotal Exception : OrderType and Event do not match!");
					throw new Exception();
				}

				nrr.save(nr);

				ctr.save(ct);
				System.out.println("CostTotal ID : " + ct.getCosttotalid());
				costTotalIDs.add(ct.getCosttotalid());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return costTotalIDs;
	}

	@Override
	public List<Costtotal> saveCostTotalTransactions(List<Costtotal> transactions) {
		Iterator<Costtotal> itr = transactions.iterator();
		Map<String, Integer> docnummap = new HashMap<>();
		Map<String, Integer> docitnummap = new HashMap<>();
		System.out.println(itr.toString());
		List<Costtotal> transactionsOut = new ArrayList<>();
		try {
			while (itr.hasNext()) {
				ct = itr.next();

				String key = ct.getObjcode().toString() + ct.getObjitnum().toString();
				if (docnummap.containsKey(key)) {

					ct.setDocnumber(new BigDecimal(docnummap.get(key)));

					int newitemnum = docitnummap.get(key) + 10;
					docitnummap.put(key, newitemnum);
					ct.setDocitnum(new BigDecimal(newitemnum));
				} else {

					// DOCNUM
					List<Numberrange> nrList = nrr.findAllByNumobject("COSTTOTALDOC");
					nr = nrList.get(0);
					BigDecimal currentNum = (nr.getCurrentnumber() == null ? nr.getRangefrom()
							: new BigDecimal(nr.getCurrentnumber().intValue() + 1));
					nr.setCurrentnumber(currentNum);
					ct.setDocnumber(currentNum);
					docnummap.put(key, ct.getDocnumber().intValue());
					docitnummap.put(key, 10);
					ct.setDocitnum(new BigDecimal(10));
				}

				ct = saveCostTotalTransaction(ct);
				nrr.save(nr);
				System.out.println("CostTotal ID : " + ct.getCosttotalid());
				transactionsOut.add(ct);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transactionsOut;
	}

	@Override
	public Costtotal saveCostTotalTransaction(Costtotal ct) {

		try {

			/*
			 * TODO add validation to check if combination of OBJCODE + OBJITNUM + COBJTYP
			 * exist in OrderHeader
			 */

			ct.getObjcode().toString();
			ct.getObjitnum().toString();

			/* --------------------------USING ORDERHEADER ---------------------------- */
			List<Orderheader> ohList = Ohr.findAllByOrdernum(ct.getObjcode(), ct.getObjitnum());
			oh = ohList.get(0);

			if (oh.getSoassignment() != null)
				ct.setStocktype("S");
			else
				ct.setStocktype("O");

			// derive from orderheader using ordernum/objcode and objitnum
			ct.setOrgncode(oh.getOrgncode());

			// derive from orderheader using ordernum/objcode and objitnum
			ct.setPlant(oh.getPlant());

			// Equal to system year or system year minus one year
			if ((ct.getPlant() != null) && (ct.getDate() != null)) {

				String plant = ct.getPlant();
				String orgCode = pr.findOrgCodeByPlantId(plant);
				System.out.println(orgCode);
				String fydcode = or.findFydCodeByOrgnCode(orgCode);
				System.out.println(fydcode);
				java.sql.Date d = new java.sql.Date((ct.getDate()).getTime());
				System.out.println(d);
				List<Fydperiod> fp = fpr.findAllbyDate(fydcode, d);
				System.out.println(fp.get(0).getPeriod());
				ct.setYear((fp.get(0).getYear()));
				ct.setPeriod(fp.get(0).getPeriod());
			}

			// As per OBJCODE - OBJITNUM vendor
			ct.setVendor(oh.getVendor());

			// ORDCURRENCY
			if(ct.getOrdcurrency()==null && ct.getObjcode()!=null)
				ct.setOrdcurrency(oh.getOrdcurrency()); // derive from order header
			// DOCUOM
			ct.setDocuom(oh.getOrdgruom()); // derive from order header

			//
			ct.setItemcode(oh.getItemcode());
			ct.setPrtnrcode(oh.getCostcenter());
			if (ct.getItemtype().equals("M")) {

				List<Material> matList = mr.findAllByMatlcodeandPlantValsub(ct.getItemcode(), oh.getPlant(),oh.getValsub());
				mat = matList.get(0);

				System.out.println("Plant" + oh.getPlant() + "Matlcode" + ct.getItemcode());

				// BUOM
				ct.setBuom(mat.getBuom().toUpperCase()); // derive from material master using material code
				// costacc
				List<Caval> cvList = cvr.findAllbyOrgnCodeMatValGrp("INV", oh.getOrgncode(), mat.getValgrp());
				ct.setCostacc(cvList.get(0).getCostacc());
			} else if (ct.getItemtype().equals("S")) {
				List<Serviceitem> matList = sr.findAllbyItemCode(ct.getItemcode());
				srvc = matList.get(0);

				System.out.println("Plant" + oh.getPlant() + "Service code" + ct.getItemcode());

				// BUOM
				ct.setBuom(srvc.getBuom().toUpperCase());
			}

			/*------------------------------------Derived------------------------------*/

			ct.setUsercode("SYSTEM"); // hardcode as SYSTEM - not needed from excel

			// time stamp
			ct.setCreatedtime(new Timestamp(new Date().getTime()));

			switch (ct.getCobjtype() + ":" + ct.getTrnsevent()) {
			case "PUR:GR":
				ct = pgr.generateCostTotal(ct);
				break;
			case "PUR:IB":
				ct = pib.generateCostTotal(ct);
				break;
			case "JOB:PC":
				ct = pc.generateCostTotal(ct);
				break;
			case "PROD:OC":
				ct = oc.generateCostTotal(ct);
				break;
			case "REWORK:OC":
				ct=oc.generateCostTotal(ct);
				break;
			case "PUR:SR":
				ct = psr.generateCostTotal(ct);
				break;
			case "STO:DE":
				ct=sto.generateCostTotal(ct);
				break;
			case "PUR:DNQR":
				ct=dnqr.generateCostTotal(ct);
				break;
			case "PUR:CNQR":
				ct=cnqr.generateCostTotal(ct);
				break;
			case "PUR:DNVR":
				ct=dnvr.generateCostTotal(ct);
				break;
			default:
				// System.out.println("CostTotal Exception : OrderType and Event do not
				// match!");
				// throw new Exception();
			}

			if (ct.getDocnumber() == null) {

				// DOCNUM
				List<Numberrange> nrList = nrr.findAllByNumobject("COSTTOTALDOC");
				nr = nrList.get(0);
				BigDecimal currentNum = (nr.getCurrentnumber() == null ? nr.getRangefrom()
						: new BigDecimal(nr.getCurrentnumber().intValue() + 1));
				nr.setCurrentnumber(currentNum);
				ct.setDocnumber(currentNum);
				ct.setDocitnum(new BigDecimal(10));
				nrr.save(nr);
				ctr.save(ct);
			} else
				ctr.save(ct);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ct;
	}

	@Override
	public Costtotal initializeCostTotal(Costtotal ct) {

		try {

			/* --------------------------USING ORDERHEADER ---------------------------- */
			List<Orderheader> ohList = Ohr.findAllByOrdernum(ct.getObjcode(), ct.getObjitnum());
			oh = ohList.get(0);
			

			// derive from orderheader using ordernum/objcode and objitnum
			ct.setOrgncode(oh.getOrgncode());

			// derive from orderheader using ordernum/objcode and objitnum
			ct.setPlant(oh.getPlant());
	
			if(ct.getDocqty()==null)
				ct.setDocqty(oh.getOrdmqty());
			if(oh.getValsub()!=null)
				ct.setValsub(oh.getValsub());
			if(oh.getLinkedobjcode()!=null)
				ct.setLinkedobjcode(oh.getLinkedobjcode());
			if(oh.getLinkedobjitnum()!=null)
				ct.setLinkedobjitnum(oh.getLinkedobjitnum());
			// Equal to system year or system year minus one year
			if ((ct.getPlant() != null) && (ct.getDate() != null)) {

				String plant = ct.getPlant();
				String orgCode = pr.findOrgCodeByPlantId(plant);
				System.out.println(orgCode);
				String fydcode = or.findFydCodeByOrgnCode(orgCode);
				System.out.println(fydcode);
				java.sql.Date d = new java.sql.Date((ct.getDate()).getTime());
				System.out.println(d);
				List<Fydperiod> fp = fpr.findAllbyDate(fydcode, d);
				System.out.println(fp.get(0).getPeriod());
				ct.setYear((fp.get(0).getYear()));
				ct.setPeriod(fp.get(0).getPeriod());
			}

			// As per OBJCODE - OBJITNUM vendor
			ct.setVendor(oh.getVendor());
			
			if(ct.getBatchnumber()==null)
				ct.setBatchnumber(oh.getBatchnumber());

			// ORDCURRENCY
			ct.setOrdcurrency(oh.getOrdcurrency()); // derive from order header
			// DOCUOM
			if (ct.getDocuom() == null)
				ct.setDocuom(oh.getOrdgruom()); // derive from order header

			//
			if (ct.getItemcode() == null)
				ct.setItemcode(oh.getItemcode());
			
			if(oh.getCostcenter()!=null)
				ct.setPrtnrcode(oh.getCostcenter());
			
			if(ct.getItemtype().equals("M"))
			{
			if (oh.getSoassignment() != null )
				ct.setStocktype("S");
			else
				ct.setStocktype("O");
			}
			/*
			 * ----------------------------USING MATERIAL
			 * MASTER-----------------------------
			 */
			if (ct.getItemtype().equals("M")) {
				List<Material> matList = mr.findAllByMatlcodeandPlantValsub(ct.getItemcode(),ct.getPlant(),ct.getValsub());
				mat = matList.get(0);
				System.out.println("Plant" + oh.getPlant() + "Matlcode" + ct.getItemcode());
				// BUOM
				ct.setBuom(mat.getBuom().toUpperCase()); // derive from material master using material code
				

			}
			if (ct.getItemtype().equals("S")) {
				List<Serviceitem> SrvcList = sr.findAllbyItemCode(ct.getItemcode());
				srvc = SrvcList.get(0);
				ct.setBuom(srvc.getBuom().toUpperCase());
				List<Caval> cvList = cvr.findAllbyOrgnCodeMatValGrp("SR", oh.getOrgncode(), srvc.getValgrp());
				ct.setCostacc(cvList.get(0).getCostacc());

			}

			if (ct.getItemtype().equals("C")) {
				System.out.println("Itemcode of type CTR C");

			}

			/*------------------------------------Derived------------------------------*/

			ct.setUsercode("SYSTEM"); // hardcode as SYSTEM - not needed from excel

			// time stamp
			ct.setCreatedtime(new Timestamp(new Date().getTime()));
			List<Organisation> orgList = or.findAllByOrgnCode(ct.getOrgncode());
			org = orgList.get(0);
			
			List<Currencyexchangerate> cerList = cerr.findAllbyOrgncodeNdCurrency(oh.getOrgncode(), oh.getOrdcurrency(),
					oh.getCurro(), new java.util.Date());
			cer = cerList.get(0);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ct;
	}
	
	public BigDecimal unitConvertor(String plant, String itemCode, String baseUnit, String fromUnit, String toUnit, BigDecimal fromValue) {
		if(fromUnit.equals(toUnit)) {
			return fromValue;
		}
		BigDecimal toValue = null;
		List<Altuom> altList = this.mar.findAltuombyMatCodePlantAltUOM(plant, itemCode,
				fromUnit, baseUnit);
		Altuom fromAltuom = altList.get(0);
		altList = this.mar.findAltuombyMatCodePlantAltUOM(plant, itemCode,
				toUnit, baseUnit);
		Altuom toAltuom = altList.get(0);
		toValue=fromValue.multiply((fromAltuom.getBuqty().divide(fromAltuom.getAltuomqty())).multiply((toAltuom.getAltuomqty().divide(toAltuom.getBuqty()))));
		return toValue;
	}
}