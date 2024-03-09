package com.cop.serviceimpl.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cop.model.database.Altuom;
import com.cop.model.database.Costtotal;
import com.cop.model.database.Currencyexchangerate;
import com.cop.model.database.Fydperiod;
import com.cop.model.database.Material;
import com.cop.model.database.Mmtotal;
import com.cop.model.database.Numberrange;
import com.cop.model.database.Orderheader;
import com.cop.model.database.Organisation;
import com.cop.repository.transaction.AltuomRepository;
import com.cop.repository.transaction.CostTotalRepository;
import com.cop.repository.transaction.CurrencyExchangeRateRepository;
import com.cop.repository.transaction.FydPeriodRepository;
import com.cop.repository.transaction.MMTotalRespository;
import com.cop.repository.transaction.MaterialRepository;
import com.cop.repository.transaction.NumberRangeRepository;
import com.cop.repository.transaction.OrderHeaderRepository;
import com.cop.repository.transaction.OrganisationRepository;
import com.cop.repository.transaction.PlantRepository;
import com.cop.serviceapi.service.MMTotalService;
import com.cop.serviceimpl.service.event.CTRGoodsIssue;
import com.cop.serviceimpl.service.event.OpeningStock;
import com.cop.serviceimpl.service.event.ProdByProduct;
import com.cop.serviceimpl.service.event.ProdGoodIssued;
import com.cop.serviceimpl.service.event.ProdGoodReceived;
import com.cop.serviceimpl.service.event.PurchaseDNQR;
import com.cop.serviceimpl.service.event.ReworkGoodReceived;
import com.cop.serviceimpl.service.event.ReworkGoodsIssued;
import com.cop.serviceimpl.service.event.SaleOrder;
import com.cop.serviceimpl.service.event.StockTransportOrder;
import com.cop.serviceimpl.service.event.SubconGoodsIssued;
import com.cop.serviceimpl.service.event.SubconGoodsRecieved;
import com.cop.serviceimpl.service.event.VendorStock;
import com.cop.utils.TransactionUtils;

@Service
public class MMTotalServiceImpl implements MMTotalService {
	@Autowired 
	TransactionUtils transUtil;
	@Autowired
	MaterialRepository mr;
	@Autowired
	PlantRepository pr;
	@Autowired
	FydPeriodRepository fpr;
	@Autowired
	OrderHeaderRepository Ohr;
	@Autowired
	CostTotalRepository ctr;
	@Autowired
	MMTotalRespository mtr;
	@Autowired
	NumberRangeRepository nrr;
	@Autowired
	CurrencyExchangeRateRepository cerr;
	@Autowired
	OrganisationRepository or;

	@Autowired
	OpeningStock os;
	@Autowired
	VendorStock vs;
	@Autowired
	SubconGoodsRecieved sgr;
	@Autowired
	SubconGoodsIssued sgi;
	@Autowired
	ProdGoodIssued pgi;
	@Autowired
	ProdGoodReceived pgr;
	@Autowired
	ProdByProduct pbp;
	@Autowired
	StockTransportOrder sto;
	@Autowired
	SaleOrder so;
	@Autowired
	ReworkGoodsIssued rgi;
	@Autowired
	ReworkGoodReceived rgr;
	@Autowired
	CTRGoodsIssue ctri;
	@Autowired
	PurchaseDNQR dnqr;
	@Autowired
	AltuomRepository mar;
	private Material mat;
	private Organisation org;
	private Mmtotal mt;
	private Numberrange nr;
	private Costtotal ct;
	private Orderheader oh;
	private Currencyexchangerate cer;
	private Altuom matalt;


	@Override
	public void saveMMTotalTransactionsFromCostTotal(List<Costtotal> costTotalTransactions) {
		System.out.println("Welcome to MMTOTAL");
		Iterator<Costtotal> itr = costTotalTransactions.iterator();

		try {
			
			while (itr.hasNext()) {
				String costTotalID = itr.next().getCosttotalid();
				List<String> it = new ArrayList<>();
				it.add(costTotalID);
				mt = new Mmtotal();
				// TODO Auto-generated method stub
				// PERIOD,Date,YEAR,PLANT,Docnumber,DOCITNUM,Stocktype,Batch
				// number,Valsub,MATLCODE,Vendor,SALEORDER,Doc QTY,DocUOM,
				// BUOMQTY,BUOM,COBJTYP/ORDERTYP,OBJCODE/ORDERNUM,OBJITNUM,TRNSEVENT,
				List<Costtotal> ctList = ctr.findAllById(it);
				System.out.println(ctList.toString());
				ct = ctList.get(0);
				System.out.println(ct.toString());

				mt.setPeriod(ct.getPeriod());
				mt.setDate(ct.getDate());
				mt.setYear(ct.getYear());
				mt.setPlant(ct.getPlant());
				mt.setDocnumber(ct.getDocnumber());
				mt.setDocitnum(ct.getDocitnum());
				mt.setStocktype(ct.getStocktype());
				mt.setBatchnumber(ct.getBatchnumber());
				mt.setItemcode(ct.getItemcode());
				mt.setVendor(ct.getVendor());
				// SALEORDER TO BE DETERMINED
				mt.setDocqty(ct.getDocqty());
				mt.setDocuom(ct.getDocuom());
				mt.setBuomqty(ct.getBuomqty());
				mt.setBuom(ct.getBuom());
				mt.setCobjtype(ct.getCobjtype());
				mt.setObjcode(ct.getObjcode());
				mt.setObjitnum(ct.getObjitnum());
				mt.setTrnsevent(ct.getTrnsevent());
				System.out.println("Mt Transevent " + mt.getTrnsevent());
				// ORDCURRENCY,Ord AMOUNT,Amount in curro,COSTACC, CSTOCK(buom),Stock Value
				mt.setOrdcurrency(ct.getOrdcurrency());
				mt.setOrdamount(ct.getOrdamount());
				List<Orderheader> ohList = Ohr.findAllByOrdernum(ct.getObjcode(), ct.getObjitnum());
				oh = ohList.get(0);
				List<Organisation> orgList = this.or.findAllByOrgnCode(ct.getOrgncode());
				this.org = orgList.get(0);
				if (mt.getOrdcurrency().equals(org.getCurro())) {
				mt.setAmountincurro(mt.getOrdamount());
			} else {
				
				List<Currencyexchangerate> cerList = this.cerr.findAllbyOrgncodeNdCurrency(oh.getOrgncode(),
						mt.getOrdcurrency(), this.org.getCurro(), new java.sql.Date((mt.getDate()).getTime()));
				this.cer = cerList.get(0);
				mt.setAmountincurro(mt.getOrdamount().multiply(this.cer.getTcurrv().divide(this.cer.getScnos())));
			}
				
				mt.setCostacc(ct.getCostacc());
				// tempcstockval = tempcstockval.add(ct.getAmountincurro());
				// mt.setStockvalue(tempcstockval);
				// tempcstock = tempcstock.add(ct.getBuomqty());
				// mt.setCstock(tempcstock);
				mt.setAdindicator("+");

				// PRDAMOUNT (CURRO), PRDQTY(BUOM) ,
				
				
				mt.setValsub(oh.getValsub());
				System.out.println();
				System.out.println("ct.getOrgncode(), ct.getOrdcurrency(), org.getCurro(), ct.getDate() "
						+ ct.getOrgncode() + ct.getOrdcurrency() + org.getCurro() + ct.getDate());
				List<Currencyexchangerate> cerList = cerr.findAllbyOrgncodeNdCurrency(ct.getOrgncode(),
						ct.getOrdcurrency(), org.getCurro(), ct.getDate());
				cer = cerList.get(0);
				System.out.println(ct.getOrdamount());
				// System.out.println(PurchaseGoodsRecieve.currencExchangeRateValue);
				
				// PRODAMOUNT
				// Diffamount ((IBAMOUNT - (IBDOCQTY *GRRATEPERUOM ))* EXCHANGE RATE )
				BigDecimal ER = cer.getTcurrv().divide(cer.getScnos());
				System.out.println(oh.getGroumrate());
				BigDecimal qtygroum = mt.getDocqty().multiply(oh.getGroumrate());
				BigDecimal Amount = ct.getOrdamount().subtract(qtygroum);
				mt.setPrdamount(Amount.multiply(ER));
				mt.setPrdqty(ct.getBuomqty());
				mt.setValsub(oh.getValsub());

				if (mt.getTrnsevent().equals("IB")) {
					System.out.println("Entering IB loop");
					/*// PRODAMOUNT
					// Diffamount ((IBAMOUNT - (IBDOCQTY *GRRATEPERUOM ))* EXCHANGE RATE )
					BigDecimal ER = cer.getTcurrv().divide(cer.getScnos());
					System.out.println(oh.getGroumrate());
					BigDecimal qtygroum = mt.getDocqty().multiply(oh.getGroumrate());
					BigDecimal Amount = ct.getOrdamount().subtract(qtygroum);
					mt.setPrdamount(Amount.multiply(ER));
					mt.setPrdqty(ct.getBuomqty());
					// PRDQTY (ammountincurro -(ct.docqty*groumrate*exchangerate) */

					BigDecimal[] stock;
					List<BigDecimal[]> mtrList = mtr.findCstockbyItemcodePlantValsubBatch(mt.getItemcode(), mt.getPlant(),
							mt.getDate(),oh.getValsub(),mt.getBatchnumber(),mt.getSaleorder());

					if (mtrList.isEmpty()) {
						mt.setCstock(new BigDecimal(0));
						mt.setStockvalue(mt.getPrdamount());
						System.out.println("CSTOCK = " + mt.getCstock() + " | StockValue = " + mt.getStockvalue());

					} else {
						stock = mtrList.get(0);
						BigDecimal cstock = stock[0];
						BigDecimal stockvalue = stock[1];
						mt.setCstock(cstock);

						//mt.setStockvalue((ct.getAmountincurro().subtract((qtygroum.multiply(ER)))).add(stockvalue));
						mt.setStockvalue(stockvalue.add(mt.getPrdamount()));

						System.out.println("CSTOCK = " + mt.getCstock() + " | StockValue = " + mt.getStockvalue());

					}
					List<Mmtotal> mtrLst = mtr.findCstockStockvaluebyGRDateandstocktype(mt.getItemcode(), mt.getPlant(),
							mt.getDate(),mt.getStocktype(),mt.getValsub(),mt.getBatchnumber(),mt.getSaleorder());
					for (Mmtotal mmtotal : mtrLst) {
						System.out.println("Updating " + mmtotal.getMmtotalid());
						//mmtotal.setCstock(mmtotal.getCstock().add(mmtotal.getBuomqty()));
						mmtotal.setStockvalue(mmtotal.getStockvalue().add(mmtotal.getAmountincurro()));
						mtr.save(mmtotal);
					}
				} else if (mt.getTrnsevent().equals("GR")) {

					BigDecimal[] stock;
					List<BigDecimal[]> mtrList = mtr.findCstockbyItemcodePlantValsubBatch(mt.getItemcode(), mt.getPlant(),
							mt.getDate(),oh.getValsub(),mt.getBatchnumber(),mt.getSaleorder());

					if (mtrList.isEmpty()) {
						mt.setCstock(mt.getBuomqty());
						mt.setStockvalue(mt.getAmountincurro());
						System.out.println("IF CSTOCK = " + mt.getCstock() + " | StockValue = " + mt.getStockvalue());

					} else {
						stock = mtrList.get(0);
						BigDecimal cstock = stock[0];
						BigDecimal stockvalue = stock[1];
						mt.setCstock(cstock.add(mt.getBuomqty()));
						mt.setStockvalue(stockvalue.add(mt.getAmountincurro()));
						System.out.println("else CSTOCK = " + mt.getCstock() + " | StockValue = " + mt.getStockvalue());

					}
					List<Mmtotal> mtrLst = mtr.findCstockStockvaluebyGRDateandstocktype(mt.getItemcode(), mt.getPlant(),
							mt.getDate(),mt.getStocktype(),mt.getValsub(),mt.getBatchnumber(),mt.getSaleorder());
					for (Mmtotal mmtotal : mtrLst) {
						System.out.println("Updating " + mmtotal.getMmtotalid());
						mmtotal.setCstock(mmtotal.getCstock().add(mt.getBuomqty()));
						mmtotal.setStockvalue(mmtotal.getStockvalue().add(mt.getAmountincurro()));
						mtr.save(mmtotal);
					}

				}

				mt.setUsercode("SYSTEM");
				mt.setCreatedtime(new Timestamp(new Date().getTime()));
				mtr.save(mt);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void saveMMTotalTransactions(List<Mmtotal> mmTotalTranactions) {
		Iterator<Mmtotal> itr = mmTotalTranactions.iterator();
		Map<String, Integer> docnummap = new HashMap<>();
		Map<String, Integer> docitnummap = new HashMap<>();
		try {

			while (itr.hasNext()) {
				mt = itr.next();
				// common values for VS and OS
				if (mt.getObjcode() != null) {
					List<Orderheader> ohList = Ohr.findAllByOrdernum(mt.getObjcode(), mt.getObjitnum());
					oh = ohList.get(0);
				}
				if (mt.getPlant() == null)
					mt.setPlant(oh.getPlant());
				if (mt.getItemcode() == null)
					mt.setItemcode(oh.getItemcode());
				if (mt.getDocuom() == null)
					mt.setDocuom(oh.getOrdgruom());
				// Equal to system year or system year minus one year
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
				String key;

				if ((mt.getCobjtype() != null) && mt.getCobjtype().equals("JOB"))
					key = mt.getPlant().toString() + mt.getObjcode().toString() + mt.getCobjtype();
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
				// BUOM
				List<Material> matList = mr.findAllByMatlcodeandPlantValsub(mt.getItemcode(),mt.getPlant(),mt.getValsub());
				mat = matList.get(0);
				mt.setBuom(mat.getBuom().toUpperCase());
				// BUOMQTY
				List<Altuom> altList = mar.findAltuombyMatCodePlantAltUOM(mt.getPlant(), mt.getItemcode(),
						mt.getDocuom(), mat.getBuom());
				matalt = altList.get(0);
				if (mt.getDocuom().toString().toUpperCase().equals(matalt.getBuom().toUpperCase()))
					mt.setBuomqty(mt.getDocqty());
				else {
					BigDecimal qty = matalt.getBuqty().divide(matalt.getAltuomqty());
					System.out.println("buqty " + matalt.getBuqty() + "altuomgty " + matalt.getAltuomqty()
							+ "Quantity ratio " + qty);
					mt.setBuomqty(mt.getDocqty().multiply(qty));
				}
				
				// UserCOde
				mt.setUsercode("SYSTEM"); // hardcode as SYSTEM - not needed from excel

				// time stamp
				mt.setCreatedtime(new Timestamp(new Date().getTime()));

				// reversal indicator
				// System automatic WHEN THE DOCUMENT IS REVERSED ; Reversal allowed only if IB
				// not done for PO
				// mt.setReversalindicator("R");

				// Reversal document
				// Populate with new document number when document is reversed
				// if(mt.getReversalindicator().equals("R"))
				// mt.setReversaldocumnet(new BigDecimal(nr.getCurrentnumber().intValue()+1));

				// Required User entry when batch management allowed /costing selected

				List<Mmtotal> mmlist = new ArrayList<>();
				switch (mt.getTrnsevent()) {
				case "OS":
					mt = os.generateMMTotal(mt);
					break;
				case "VS":
					mmlist = vs.generateMMTotal(mt);
					break;

				default:
					// System.out.println("MMTotal Exception : Event Match not found!");
					// throw new Exception();

				}

				for (Mmtotal vs : mmlist)
					mtr.save(vs);

				switch (mt.getCobjtype() + ":" + mt.getTrnsevent()) {
				case "JOB:GR":
					mt = sgr.generateMMTotal(mt);
					break;
				case "JOB:GI":
					mt = sgi.generateMMTotal(mt);
					break;

				case "PROD:GR":
					mt = pgi.generateMMTotal(mt);
					break;
				case "PROD:GI":
					mt = pgr.generateMMTotal(mt);
					break;
				case "PROD:BP":
					mt = pbp.generateMMTotal(mt);
					break;
					
				case "REWORK:GR":
					mt = rgi.generateMMTotal(mt);
					break;
				case "REOWRK:GI":
					mt = rgr.generateMMTotal(mt);
					break;
					
				case "STO:STO":
					mt=sto.generateMMTotal(mt);
					break;
				case "SALE:SD":
					mt=so.generateMMTotal(mt);
					break;
				case "STO:PR":
				mt=sto.generateMMTotalPurchaseRecipet(mt);
				break;
				case"CTR:GI":
					mt=ctri.generateMMTotal(mt);
					break;
					
				case "PUR:DNQR":
					mt=dnqr.generateMMTotal(mt);
					break;
				default:
					// System.out.println("MMTotal Exception : Event Match not found!");
					// throw new Exception();
				}
				System.out.println("end of mmtotal");
				mtr.save(mt);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Mmtotal intializeMMTotal(Mmtotal mmTotal) {
		try {

			
			mt = mmTotal;
			
			// common values for VS and OS
			if (mt.getObjcode() != null ) {
				List<Orderheader> ohList = Ohr.findAllByOrdernum(mt.getObjcode(), mt.getObjitnum());
				oh = ohList.get(0);
			}
			
			
			
			
			if(mt.getObjcode()!=null)
					{
					if(mt.getCobjtype().equalsIgnoreCase(oh.getObjtype()) )
						System.out.println("Correct objtype");
							else
								mt.setCobjtype(oh.getObjtype());
					}
			
			if(mt.getObjcode()!=null) {
			List<Organisation> orgList = this.or.findAllByOrgnCode(oh.getOrgncode());
			this.org = orgList.get(0);}
			else
				
				{String Orgncode = pr.findOrgCodeByPlantId(mt.getPlant());
			List<Organisation> olist = or.findAllByOrgnCode(Orgncode);
			org=olist.get(0);}
			//mt.setOrdcurrency(olist.get(0).getCurro());
			
			
			
			if (mt.getPlant()==null)
				mt.setPlant(oh.getPlant());
			if (mt.getItemcode()==(null))
				mt.setItemcode(oh.getItemcode());
			
		
			if (mt.getDocuom() == null)
				mt.setDocuom(oh.getOrdgruom());
			// Equal to system year or system year minus one year
			
			if(mt.getOrdcurrency()==null && mt.getObjcode()!=null)
				mt.setOrdcurrency(oh.getOrdcurrency());
			//else 
				//mt.setOrdcurrency(org.getCurro());
			
			
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
			
			if(mt.getStocktype()==null)
			{
			if (oh.getSoassignment() != null)
				mt.setStocktype("S");
			else
				mt.setStocktype("O");}
			
			//DOCQTY
			
			// BUOM
			// BUOM
 			List<Material> matList = mr.findAllByMatlcodeandPlantValsub(mt.getItemcode(),mt.getPlant(),oh.getValsub());
 			mat = matList.get(0);
			mt.setBuom(mat.getBuom().toUpperCase());
			// BUOMQTY
			List<Altuom> altList = mar.findAltuombyMatCodePlantAltUOM(mt.getPlant(), mt.getItemcode(), mt.getDocuom(),
					mt.getBuom());
			matalt = altList.get(0);
			if(mt.getDocqty()!=null)
			{
			if (mt.getDocuom().toString().toUpperCase().equals(matalt.getBuom().toUpperCase()))
				mt.setBuomqty(mt.getDocqty());
			else {
				BigDecimal qty = matalt.getBuqty().divide(matalt.getAltuomqty());
				System.out.println(
						"buqty " + matalt.getBuqty() + "altuomgty " + matalt.getAltuomqty() + "Quantity ratio " + qty);
				mt.setBuomqty(mt.getDocqty().multiply(qty));
			}}
			
			if ( (mat.getBatch().equalsIgnoreCase("YES")) && mat.getBatchcosting().equalsIgnoreCase("NO"))
				{mt.setBstock(mt.getBuomqty());}

			if(mt.getBatchnumber()==null && oh!=null && mt.getItemcode().equals(oh.getItemcode()))
			mt.setBatchnumber(oh.getBatchnumber());
			if(mt.getObjcode()!=null)
			{
			mt.setLinkedobjcode(oh.getLinkedobjcode());
			mt.setLinkedobjitnum(oh.getLinkedobjitnum());
			mt.setValsub(oh.getValsub());
			if(mt.getValsub()==null)
				mt.setValsub(oh.getValsub());
			}
			mt.setUsercode("SYSTEM");
			mt.setCreatedtime(new Timestamp(new Date().getTime()));
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mt;
	}

}
