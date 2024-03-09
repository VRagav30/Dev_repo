package com.cop.serviceimpl.service.event;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cop.model.database.Caval;
import com.cop.model.database.Costtotal;
import com.cop.model.database.Currencyexchangerate;
import com.cop.model.database.Altuom;
import com.cop.model.database.Material;
import com.cop.model.database.Orderheader;
import com.cop.model.database.Organisation;
import com.cop.repository.transaction.CavalRepository;
import com.cop.repository.transaction.CurrencyExchangeRateRepository;
import com.cop.repository.transaction.AltuomRepository;
import com.cop.repository.transaction.MaterialRepository;
import com.cop.repository.transaction.OrderHeaderRepository;
import com.cop.repository.transaction.OrganisationRepository;
import com.cop.serviceapi.service.CostTotalService;

@Component
public class SubconProcessingCharge {

	Costtotal ct;
	@Autowired
	AltuomRepository mar;
	@Autowired
	OrderHeaderRepository Ohr;
	@Autowired
	OrganisationRepository or;
	@Autowired
	CurrencyExchangeRateRepository cerr;
	@Autowired
	CavalRepository cvr;
	@Autowired
	MaterialRepository mr;
	@Autowired
	CostTotalService cs;

	private Caval caval;
	private Material mat;
	private Currencyexchangerate cer;
	private Organisation org;
	private Orderheader oh;
	Altuom matalt;

	public Costtotal generateCostTotal(Costtotal ct) {

		ct = this.cs.initializeCostTotal(ct);
		

		List<Orderheader> ohList = this.Ohr.findAllByOrdernum(ct.getObjcode(), ct.getObjitnum());
		this.oh = ohList.get(0);

		List<Material> matList = this.mr.findAllByMatlcodeandPlantValsub(ct.getItemcode(),oh.getPlant(),oh.getValsub());
		this.mat = matList.get(0);
		System.out.println("enetering subcon purchase charge");

		// BUOMQTY
		List<Altuom> altList = this.mar.findAltuombyMatCodePlantAltUOM(this.oh.getPlant(), ct.getItemcode(),
				ct.getDocuom(), ct.getBuom());
		this.matalt = altList.get(0);

		System.out.println("BUOM from MATALT" + this.matalt.getBuom().toUpperCase());
		System.out.println("ct buomqty" + ct.getDocqty());

		if (ct.getDocuom().toString().toUpperCase().equals(this.matalt.getBuom().toUpperCase())) {
			ct.setBuomqty(ct.getDocqty());
		} // need to derive
		else {
			BigDecimal qty = this.matalt.getBuqty().divide(this.matalt.getAltuomqty());
			System.out.println("buqty " + this.matalt.getBuqty() + "altuomgty " + this.matalt.getAltuomqty()
					+ "Quantity ratio " + qty);
			ct.setBuomqty(ct.getDocqty().multiply(qty));
		}

		// ORDAMOUNT
		ct.setOrdamount(ct.getDocqty().multiply(this.oh.getGroumrate()));

		// AmountInCurro
		List<Organisation> orgList = this.or.findAllByOrgnCode(ct.getOrgncode());
		this.org = orgList.get(0);
		System.out.println(
				"ct.getOrgncode(), ct.getOrdcurrency(), org.getCurro(), new java.sql.Date((ct.getDate()).getTime() "
						+ ct.getOrgncode() + ct.getOrdcurrency() + this.org.getCurro() + ct.getDate());
		List<Currencyexchangerate> cerList = this.cerr.findAllbyOrgncodeNdCurrency(ct.getOrgncode(),
				ct.getOrdcurrency(), this.org.getCurro(), new java.sql.Date((ct.getDate()).getTime()));
		this.cer = cerList.get(0);
		ct.setAmountincurro(ct.getOrdamount().multiply(this.cer.getTcurrv().divide(this.cer.getScnos())));
		// costacc
		List<Caval> cvList = this.cvr.findAllbyOrgnCodeMatValGrp("PC", this.oh.getOrgncode(), this.mat.getValgrp());
		this.caval = cvList.get(0);
		ct.setCostacc(this.caval.getCostacc());

		// REVERSALINDICATOR

		// REVERSALDOCUMENT

		return ct;

	}
}
