package com.cop.serviceimpl.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cop.model.database.Currencyexchangerate;
import com.cop.model.database.Mrc;
import com.cop.model.database.Numberrange;
import com.cop.model.database.Orderheader;
import com.cop.repository.transaction.CurrencyExchangeRateRepository;
import com.cop.repository.transaction.MaterialRepository;
import com.cop.repository.transaction.MrcRepository;
import com.cop.repository.transaction.NumberRangeRepository;
import com.cop.repository.transaction.OrderHeaderRepository;
import com.cop.repository.transaction.OrganisationRepository;
import com.cop.repository.transaction.PlantRepository;
import com.cop.repository.transaction.ServiceRepository;
import com.cop.serviceapi.service.OrderHeaderService;
import com.cop.utilities.generic.OHComparatorbyOrdertypeNdSerialNo;

@Service
public class OrderHeaderServiceImpl implements OrderHeaderService {
	SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");

	Numberrange nr;
	Currencyexchangerate cer;
	OHComparatorbyOrdertypeNdSerialNo ohc;
	@Autowired
	private MrcRepository mrcr;
	@Autowired
	private ServiceRepository sr;
	@Autowired
	private OrderHeaderRepository ohr;
	@Autowired
	private PlantRepository pr;
	@Autowired
	private MaterialRepository mr;
	@Autowired
	private OrganisationRepository or;
	@Autowired
	private NumberRangeRepository nrr;
	@Autowired
	private CurrencyExchangeRateRepository cerr;

	private com.cop.model.database.Serviceitem srvc;

	@Override
	public void saveOrderHeaders(List<Orderheader> orderHeaders) {

		try {
			orderHeaders = filterOrderHeaders(orderHeaders);
			for (Orderheader element : orderHeaders)
				saveOrderHeader(element);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void saveOrderHeader(Orderheader oh) {
		
		oh.setOrgncode(pr.findAllByPlant(oh.getPlant()).get(0).getOrgncode().toUpperCase());
		oh.setCurro(or.findAllByOrgnCode(oh.getOrgncode()).get(0).getCurro().toUpperCase());
		
		if (oh.getItemtype().equals("M")) {
			oh.setBuom(mr.findAllByMatlcode(oh.getItemcode()).get(0).getBuom().toUpperCase());
			oh.setProfitcr((mr.findAllByMatlcode(oh.getItemcode())).get(0).getProfitcr().toUpperCase());
		}
		if (oh.getItemtype().equals("S")) {
			oh.setBuom(sr.findAllbyItemCode(oh.getItemcode()).get(0).getBuom().toUpperCase());
		}

		oh.setCreatedby("SYSTEM");

		List<Currencyexchangerate> cerList = cerr.findAllbyOrgncodeNdCurrency(oh.getOrgncode(), oh.getOrdcurrency(),
				oh.getCurro(), new java.util.Date());
		cer = cerList.get(0);
		List<Mrc> MrcList = mrcr.findAllbyItemcodePlantDateMpricetyp(oh.getItemcode(), oh.getPlant(),
				new java.util.Date());
		
		//5th Oct 2020 -- adding field Exchangerate - can either be user entry or calculated from DB
		if(oh.getExchangerate()==null ) {
			oh.setExchangerate(cer.getTcurrv().divide(cer.getScnos()));
			System.out.println("Exchange Rate "+oh.getExchangerate());
			System.out.println("curron and scnos "+cer.getTcurrv() + " " +cer.getScnos());
		}
		if(oh.getExchangerate().equals(new BigDecimal(1)))
			oh.setExchangerate(null);

		if(	(oh.getObjtype().equals("PROD")||oh.getObjtype().equals("STO")||oh.getObjtype().equals("REWORK")||oh.getObjtype().equals("SRO")) && (oh.getGroumrate() == null))
			oh.setGroumrate(MrcList.get(0).getPrice());
		if (oh.getGroumrate() != null)
			oh.setTotcost(oh.getGroumrate().multiply(oh.getOrdmqty()));

		oh.setCurroamount(oh.getTotcost().multiply(cer.getTcurrv().divide(cer.getScnos())));
		// oh.setGroumrate(mr.findAllByMatlcode(oh.getItemcode()).get(0).get);
		oh.setCreatedtime(new Timestamp(new Date().getTime()));
		BigDecimal currentNum;
		if (oh.getObjcode() == null) {
			nr = nrr.findAllByNumobject(oh.getObjtype()).get(0);
			currentNum = (nr.getCurrentnumber() == null ? nr.getRangefrom()
					: new BigDecimal(nr.getCurrentnumber().intValue() + 1));
			oh.setObjcode(currentNum.toString());
			ohr.save(oh);
			nr.setCurrentnumber(currentNum);
			nrr.save(nr);

		} else {
			ohr.save(oh);
		}
	}

	private List<Orderheader> filterOrderHeaders(List<Orderheader> lst) {
		Collections.sort(lst, new OHComparatorbyOrdertypeNdSerialNo());
		Map<String, String> searchMap = new HashMap<>();
		List<String> sameOrderRecords = new ArrayList<>();
		int indexCount = 0;
		for (Orderheader oh : lst) {
			String keywithNum = "";
			String keywithoutNum = "";
			switch (oh.getObjtype()) {
			case "PROD":
				keywithNum = oh.getObjtype() + oh.getPlant() + oh.getBomcode() + ":" + oh.getObjitnum();
				keywithoutNum = oh.getObjtype() + oh.getPlant() + oh.getBomcode() + ":";
				break;
			case "STO":
				keywithNum = oh.getObjtype() + oh.getPlant() + oh.getRplant() + ":" + oh.getObjitnum();
				keywithoutNum = oh.getObjtype() + oh.getPlant() + oh.getRplant() + ":";
				break;
			case "PUR":
				keywithNum = oh.getObjtype() + oh.getPlant() + oh.getVendor() + ":" + oh.getObjitnum();
				keywithoutNum = oh.getObjtype() + oh.getPlant() + oh.getVendor() + ":";
				break;
			case "JOB":
				keywithNum = oh.getObjtype() + oh.getPlant() + oh.getBomcode() + ":" + oh.getObjitnum();
				keywithoutNum = oh.getObjtype() + oh.getPlant() + oh.getBomcode() + ":";
				break;
			case "SALE":
				keywithNum = oh.getObjtype() + oh.getPlant() + oh.getCustomer() + ":" + oh.getObjitnum();
				keywithoutNum = oh.getObjtype() + oh.getPlant() + oh.getCustomer() + ":";
				break;
			case "SRO":
				keywithNum = oh.getObjtype() + oh.getPlant() + oh.getCustomer() + ":" + oh.getObjitnum();
				keywithoutNum = oh.getObjtype() + oh.getPlant() + oh.getCustomer() + ":";
				break;
			}

			if (oh.getObjitnum().intValue() > 10) {
				int objitnum = oh.getObjitnum().intValue();
				boolean rejectFlag = false;
				String sameIndex = "" + indexCount;
				while (objitnum > 10) {
					objitnum -= 10;
					if (!searchMap.containsKey(keywithoutNum + objitnum))
						rejectFlag = true;
					else
						sameIndex = String.join(",", sameIndex,
								searchMap.get(keywithoutNum + objitnum).toString().split("_")[0]);

				}
				if (rejectFlag == true)
					sameIndex = sameIndex + ":REJECT";
				else
					sameIndex = sameIndex + ":SUCCESS";

				for (int i = 0; i < sameOrderRecords.size(); i++)
					if (sameIndex.contains(sameOrderRecords.get(i))) {
						sameOrderRecords.remove(i);
						i--;
					}
				sameOrderRecords.add(sameIndex);
			}
			searchMap.put(keywithNum, indexCount + "_Success");
			indexCount++;

		}

		for (String sameOrderRecord : sameOrderRecords)
			if (sameOrderRecord.split(":")[1].equals("SUCCESS")) {
				String indexList = sameOrderRecord.split(":")[0];

				BigDecimal currentNum = null;
				String arr[] = indexList.split(",");
				for (String index : arr) {
					nr = nrr.findAllByNumobject(lst.get(Integer.parseInt(index)).getObjtype()).get(0);
					currentNum = (nr.getCurrentnumber() == null ? nr.getRangefrom()
							: new BigDecimal(nr.getCurrentnumber().intValue() + 1));
					lst.get(Integer.parseInt(index)).setObjcode(currentNum.toString());
					ohr.save(lst.get(Integer.parseInt(index)));
				}
				nr.setCurrentnumber(currentNum);
				nrr.save(nr);
			}
		for (String sameOrderRecord : sameOrderRecords)
			if (sameOrderRecord.split(":")[1].equals("REJECT")) {
				String indexList = sameOrderRecord.split(":")[0];
				String arr[] = indexList.split(",");
				for (String index : arr)
					lst.get(Integer.parseInt(index)).setObjcode("0");
			}
		return lst;
	}

}
