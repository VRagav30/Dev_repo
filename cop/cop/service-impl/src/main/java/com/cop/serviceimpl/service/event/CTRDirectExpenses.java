package com.cop.serviceimpl.service.event;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cop.model.database.Altuom;
import com.cop.model.database.Caval;
import com.cop.model.database.Costtotal;
import com.cop.model.database.Currencyexchangerate;
import com.cop.model.database.Fydperiod;
import com.cop.model.database.Material;
import com.cop.model.database.Numberrange;
import com.cop.model.database.Organisation;
import com.cop.model.database.Plant;
import com.cop.repository.transaction.AltuomRepository;
import com.cop.repository.transaction.CTRRepository;
import com.cop.repository.transaction.CavalRepository;
import com.cop.repository.transaction.CurrencyExchangeRateRepository;
import com.cop.repository.transaction.FydPeriodRepository;
import com.cop.repository.transaction.MMTotalRespository;
import com.cop.repository.transaction.MaterialRepository;
import com.cop.repository.transaction.MrcRepository;
import com.cop.repository.transaction.NumberRangeRepository;
import com.cop.repository.transaction.OrganisationRepository;
import com.cop.repository.transaction.PlantRepository;
import com.cop.serviceapi.service.CostTotalService;
import com.cop.serviceapi.service.MMTotalService;
// 12/08/2021 - @Autor - RESHMA MD

@Component
public class CTRDirectExpenses {
	
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
	CostTotalService cs;
	@Autowired
	MMTotalRespository mtr;
	@Autowired
	CTRRepository ctrr;
	private Numberrange nr;
	private Material mat;
	private Altuom matalt;
	private Currencyexchangerate cer;
	@Autowired
	CurrencyExchangeRateRepository cerr;
	private Caval caval;
	private Organisation org;
	public Costtotal generateCosttotal(Costtotal ct)
	{	
		
		String plantList=pr.findOrgncode(ct.getPlant());
		ct.setOrgncode(plantList);
		List<Organisation> orgList = this.or.findAllByOrgnCode(ct.getOrgncode());
		this.org = orgList.get(0);
		String fydcode = or.findFydCodeByOrgnCode(ct.getOrgncode());
		System.out.println(fydcode);
		java.sql.Date d = new java.sql.Date((ct.getDate()).getTime());
		System.out.println(d);
		List<Fydperiod> fp = fpr.findAllbyDate(fydcode, d);
		System.out.println(fp.get(0).getPeriod());
		ct.setYear((fp.get(0).getYear()));
		ct.setPeriod(fp.get(0).getPeriod());
		
		ct.setBuom(ct.getDocuom());
		ct.setBuomqty(ct.getDocqty());
		if (ct.getOrdcurrency().equals(org.getCurro())) {
			ct.setAmountincurro(ct.getOrdamount());
		} else {
			
			List<Currencyexchangerate> cerList = this.cerr.findAllbyOrgncodeNdCurrency(ct.getOrgncode(),
					ct.getOrdcurrency(), this.org.getCurro(), new java.sql.Date((ct.getDate()).getTime()));
			this.cer = cerList.get(0);
			ct.setAmountincurro(ct.getOrdamount().multiply(this.cer.getTcurrv().divide(this.cer.getScnos())));
		}
	
				
		return ct;
		
	}

}
