package com.cop.controllers;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cop.model.database.Fydperiod;
import com.cop.model.database.Plant;
import com.cop.repository.transaction.ActualTotalLineItemRepository;
import com.cop.repository.transaction.ActualTotalRepository;
import com.cop.repository.transaction.FydPeriodRepository;
import com.cop.repository.transaction.MMTotalRespository;
import com.cop.repository.transaction.MaterialRepository;
import com.cop.repository.transaction.OprnCalRepository;
import com.cop.repository.transaction.OprnpriceRepository;
import com.cop.repository.transaction.OprnpricetempRepository;
import com.cop.repository.transaction.OrderHeaderRepository;
import com.cop.repository.transaction.OrganisationRepository;
import com.cop.repository.transaction.PlantRepository;

@Controller
public class GenerateReportController {
	@Autowired
	FydPeriodRepository fpr;

	@Autowired
	OrganisationRepository orgr;
	
	@Autowired
	OprnpricetempRepository optr;
	
	@Autowired
	OprnCalRepository opcalr;
	
	@Autowired
	OprnpriceRepository opr;

	@Autowired
	OrderHeaderRepository ohr;

	@Autowired
	PlantRepository pr;
	@Autowired
	MaterialRepository matr;

	@Autowired
	MMTotalRespository mr;
	@Autowired
	ActualTotalRepository atr;
	@Autowired
	ActualTotalLineItemRepository atlir;
	@Autowired
	OrganisationRepository or;
	String currency;
	String curro;

	public static String currencyWithChosenLocalisation(BigDecimal value, Locale locale) {
		NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
		return nf.format(value);
	}

	
	@RequestMapping("/loadConsolidatedReport")
	public ModelAndView loadConsolidatedReport(@RequestParam(value = "org", required = false) String org,
			@RequestParam(value = "plant", required = false) String plant,
			@RequestParam(value = "from", required = false) BigDecimal from,
			@RequestParam(value = "to", required = false) BigDecimal to,
			@RequestParam(value = "year", required = false) String year,
			@RequestParam(value = "mat", required = false) String mat,
			@RequestParam(value = "stocktype", required = false) String stocktype,
			@RequestParam(value = "soa", required = false) String soa, Model model) {
		ModelAndView mv = new ModelAndView("GenConsolidatedReport.html");
		System.out.println("selected org is - " + org);
		mv.addObject("orgs", orgr.findAll());
		mv.addObject("plants", pr.findAll());
		mv.addObject("mats", matr.findAll());
		return mv;

	}
	@RequestMapping("/loadDetailedReport")
	public ModelAndView loadDetailedReport(@RequestParam(value = "org", required = false) String org,
			@RequestParam(value = "plant", required = false) String plant,
			@RequestParam(value = "from", required = false) BigDecimal from,
			@RequestParam(value = "to", required = false) BigDecimal to,
			@RequestParam(value = "year", required = false) String year,
			@RequestParam(value = "mat", required = false) String mat,
			@RequestParam(value = "stocktype", required = false) String stocktype,
			@RequestParam(value = "soa", required = false) String soa, Model model) {
		ModelAndView mv = new ModelAndView("GenDetailedReport.html");
		System.out.println("selected org is - " + org);
		mv.addObject("orgs", orgr.findAll());
		mv.addObject("plants", pr.findAll());
		mv.addObject("mats", matr.findAll());
		return mv;

	}
	
	@RequestMapping("/loadReconReport")
	public ModelAndView loadReconReport(@RequestParam(value = "org", required = false) String org,
			@RequestParam(value = "plant", required = false) String plant,
			@RequestParam(value = "from", required = false) BigDecimal from,
			@RequestParam(value = "to", required = false) BigDecimal to,
			@RequestParam(value = "year", required = false) String year,
			@RequestParam(value = "mat", required = false) String mat,
			@RequestParam(value = "stocktype", required = false) String stocktype,
			@RequestParam(value = "soa", required = false) String soa, Model model) {
		ModelAndView mv = new ModelAndView("GenReconReport.html");
		System.out.println("selected org is - " + org);
		mv.addObject("orgs", orgr.findAll());
		mv.addObject("plants", pr.findAll());
		mv.addObject("mats", matr.findAll());
		return mv;

	}
	

	@RequestMapping(value = "/plant")
	@ResponseBody
	public Set<String> getRegions(@RequestParam String org) {
		List<Plant> results = pr.findAllbyOrgncode(org);
		Set<String> plants = new HashSet<String>();
		for (Plant plant : results) {
			plants.add(plant.getPlant());
		}
	    return plants;
	}
	
	@RequestMapping(value = "/year")
	@ResponseBody
	public Set<String> getYears(@RequestParam String org) {
		List<Long> results = fpr.findYear(org);
		Set<String> years = new HashSet<String>();
		for (Long year : results) {
			years.add(year.toString());
		}
	    return years;
	}
	
	@RequestMapping(value = "/period")
	@ResponseBody
	public List<Fydperiod> getFYDPeriods(@RequestParam String org, @RequestParam String year) {
		List<Fydperiod> results = fpr.findPeriodsbyYear(org, new BigDecimal(year));
	    return results;
	}
	
	@RequestMapping("/generateReport")
	public ModelAndView generateReport(@RequestParam(value = "org", required = false) String org,
			@RequestParam(value = "plant", required = false) String plant,
			@RequestParam(value = "year", required = false) BigDecimal year,
			@RequestParam(value = "period", required = false) BigDecimal period,
			@RequestParam(value = "mat", required = false) String mat,
			@RequestParam(value = "stocktype", required = false) String stocktype,
			@RequestParam(value = "soa", required = false) String soa, Model model) {
		ModelAndView mv = new ModelAndView("generateReport");
		System.out.println("selected org is - " + org);
		mv.addObject("orgs", orgr.findAll());
		mv.addObject("plants", pr.findAll());
		mv.addObject("mats", matr.findAll());
		mv.addObject("results", mr.generateConsolidatedReport(org,Arrays.asList(plant.split(",")), (year),period));
		return mv;

	}
	
	@RequestMapping("/generateDetailedReport")
	public ModelAndView generateDetailedReport(@RequestParam(value = "org", required = false) String org,
			@RequestParam(value = "plant", required = false) String plant,
			@RequestParam(value = "year", required = false) BigDecimal year,
			@RequestParam(value = "period", required = false) BigDecimal period,
			@RequestParam(value = "mat", required = false) String mat,
			@RequestParam(value = "stocktype", required = false) String stocktype,
			@RequestParam(value = "soa", required = false) String soa, Model model) {
		ModelAndView mv = new ModelAndView("detailedReport");
		System.out.println("selected org is - " + org);
		mv.addObject("orgs", orgr.findAll());
		mv.addObject("plants", pr.findAll());
		mv.addObject("mats", matr.findAll());
		mv.addObject("dResults", mr.generateDetailedReport(org,Arrays.asList(plant.split(",")), year, period));
		mv.addObject("year",year);
		mv.addObject("period",period);
		mv.addObject("mat",mat);
		mv.addObject("selectedOrg", org);
		mv.addObject("selectedPlant", plant);
		return mv;

	}
	@RequestMapping("/generateConsolidatedReport")
	public ModelAndView generateConsolidatedReport(@RequestParam(value = "org", required = false) String org,
			@RequestParam(value = "plant", required = false) String plant,
			@RequestParam(value = "year", required = false) BigDecimal year,
			@RequestParam(value = "period", required = false) BigDecimal period,
			@RequestParam(value = "mat", required = false) String mat,
			@RequestParam(value = "stocktype", required = false) String stocktype,
			@RequestParam(value = "soa", required = false) String soa, Model model) {
		ModelAndView mv = new ModelAndView("consolidatedReport");
		System.out.println("selected org is - " + org);
		mv.addObject("orgs", orgr.findAll());
		mv.addObject("plants", pr.findAll());
		mv.addObject("mats", matr.findAll());
		mv.addObject("results", mr.generateConsolidatedReport(org,Arrays.asList(plant.split(",")), year,period));
		mv.addObject("org",org);
		mv.addObject("plant",plant);
		mv.addObject("year",year);
		mv.addObject("period",period);
		mv.addObject("mat",mat);
		return mv;

	}

	@RequestMapping("/generateQtyTypeReport")
	public ModelAndView generateQtyTypeReport(@RequestParam(value = "qtyType", required = false) String qtyType,
			@RequestParam(value = "org", required = false) String org, 
			@RequestParam(value = "plant", required = false) String plant,
			@RequestParam(value = "year", required = false) String year,
			@RequestParam(value = "period", required = false) String period, Model model) {
		ModelAndView mv = new ModelAndView("quantitytype");
		System.out.println("selected qtyType is - " + qtyType);
		System.out.println("selected org is - " + org);
		mv.addObject("org",org);
		mv.addObject("plant",plant);
		mv.addObject("year",year);
		mv.addObject("period",period);
		mv.addObject("qtyType",qtyType);
		mv.addObject("qResults", atr.generateQuantityTypeReport(org, Arrays.asList(plant.split(",")), new BigDecimal(year), new BigDecimal(period), qtyType));
		return mv;

	}
	
	@RequestMapping("/generateMaterialReport")
	public ModelAndView generateMaterialReport(@RequestParam(value = "qtyType", required = false) String qtyType,
			@RequestParam(value = "org", required = false) String org, 
			@RequestParam(value = "plant", required = false) String plant,
			@RequestParam(value = "year", required = false) BigDecimal year,
			@RequestParam(value = "period", required = false) BigDecimal period, 
			@RequestParam(value = "mat", required = false) String mat,
			@RequestParam(value = "stocktype", required = false) String stocktype,
			@RequestParam(value = "soa", required = false) String soa, Model model) {
		ModelAndView mv = new ModelAndView("material.html");
		System.out.println("selected qtyType is - " + qtyType);
		System.out.println("selected org is - " + org);
		mv.addObject("org",org);
		mv.addObject("plant",plant);
		mv.addObject("year",year);
		mv.addObject("period",period);
		mv.addObject("mat",mat);
		//mv.addObject("qResults", atr.generateMaterialReport(org, Arrays.asList(plant.split(",")),mat,stocktype,soa, new BigDecimal(year), new BigDecimal(period)));
		
		plant=plant.equals("Please Select...")?null:plant;
		stocktype=stocktype.equals("Please Select...")?null:stocktype;
		soa=soa.equals("Please Select...")?null:soa;
		soa=soa.trim().equals("")?null:soa;
		mat=mat.equals("Please select...")?null:mat;
		System.out.println("input : "+mat+" "+soa+" "+stocktype);
		Map<String, BigDecimal> matTotalQtyMap = (atr.generateMaterialReport(org,
				plant == null ? null : Arrays.asList(plant.split(",")), mat, stocktype, soa,
				year,	
						period
				)).stream()
						.collect(Collectors.toMap(o -> (String) o[0], o -> new BigDecimal(o[1].toString())));
		mv.addObject("matTotalQtyMap", matTotalQtyMap);
		Map<String, BigDecimal> matTotalAmtMap = (atr.generateMaterialReport(org,
				plant == null ? null : Arrays.asList(plant.split(",")), mat,stocktype, soa, year, period)).stream()
						.collect(Collectors.toMap(o -> (String) o[0], o -> new BigDecimal(o[2].toString())));
		mv.addObject("matTotalAmtMap", matTotalAmtMap);

		if (org != null)
			curro = or.findCurro(org);
		else {
			String orgnisation = pr.findOrgCodeByPlantId(plant);
			curro = or.findCurro(orgnisation);
		}

		if (curro.equals("INR"))
			currency = "Rs:";
		if (curro.equals("USD"))
			currency = "$";
		
		mv.addObject("currency", currency);
		
		System.out.println(currency);

		System.out.println("Recon" + matTotalAmtMap);
		Map<String, BigDecimal> liTotalAmountMap = (atlir.generateReportByTrnsevent(org,
				plant == null ? null : Arrays.asList(plant.split(",")),year,period)).stream()
						.collect(Collectors.toMap(o -> (String) o[0], o -> new BigDecimal(o[1].toString())));
		mv.addObject("liTotalAmountMap", liTotalAmountMap);

		BigDecimal rawTotalCost = atr.getTotalCostbyMattype(org, plant, "RAW",year,period);
		// String rawCost=currencyWithChosenLocalisation(rawTotalCost,loc);
		mv.addObject("rawTotalCost", rawTotalCost);

		BigDecimal fgTotalCost = atr.getTotalCostbyMattype(org, plant, "FG",year,period);
		mv.addObject("fgTotalCost", fgTotalCost);

		BigDecimal sfgTotalCost = atr.getTotalCostbyMattype(org, plant, "SFG",year,period);
		mv.addObject("sfgTotalCost", sfgTotalCost);

		BigDecimal consTotalCost = atr.getTotalCostbyMattype(org, plant, "CONS",year,period);
		mv.addObject("consTotalCost", consTotalCost);

		System.out.println("rawTotalCost"+ rawTotalCost +"fgTotalCost"+ fgTotalCost);
		
		Map<String, BigDecimal> tostTotalQtyMap = (atlir.getTOSTamount(
				plant == null ? null : Arrays.asList(plant.split(",")),year,period)).stream()
				.collect(Collectors.toMap(o -> (String) o[0], o -> new BigDecimal(o[1].toString())));
		mv.addObject("tostTotalQtyMap", tostTotalQtyMap);
		
		Map<String, BigDecimal> tostTotalAmountMap = (atlir.getTOSTamount(
				plant == null ? null : Arrays.asList(plant.split(",")),year,period)).stream()
						.collect(Collectors.toMap(o -> (String) o[0], o -> new BigDecimal(o[2].toString())));
		System.out.println("RESHMA" + tostTotalAmountMap);
		mv.addObject("tostTotalAmountMap", tostTotalAmountMap);
		
		Map<String, BigDecimal> trstTotalQtyMap = (atlir.getTRSTamount(
				plant == null ? null : Arrays.asList(plant.split(",")),year,period)).stream()
				.collect(Collectors.toMap(o -> (String) o[0], o -> new BigDecimal(o[1].toString())));
		mv.addObject("trstTotalQtyMap", trstTotalQtyMap);

		Map<String, BigDecimal> trstTotalAmountMap = (atlir.getTRSTamount(
				plant == null ? null : Arrays.asList(plant.split(",")),year,period)).stream()
						.collect(Collectors.toMap(o -> (String) o[0], o -> new BigDecimal(o[2].toString())));
		System.out.println(trstTotalAmountMap);
		mv.addObject("trstTotalAmountMap", trstTotalAmountMap);

		BigDecimal OPST, MOST, WOST, BMGR, BMSR, BMVD, BSVD, BMNA, BSNA, BSQD,SRST,WIST,TOST,TRST,SSVD,SMNA,SVNA,SMSR,SVGR,SMVD;
		
		if (matTotalAmtMap.get("SVNA") == null)
			SVNA = new BigDecimal(0);
		else
			SVNA = matTotalAmtMap.get("SVNA");
		mv.addObject("SVNA",SVNA);
		
		if (matTotalAmtMap.get("OPST") == null)
			OPST = new BigDecimal(0);
		else
			OPST = matTotalAmtMap.get("OPST");
		mv.addObject("OPST",OPST);
		if (matTotalAmtMap.get("MOST") == null)
			MOST = new BigDecimal(0);
		else
			MOST = matTotalAmtMap.get("MOST");
		mv.addObject("MOST",MOST);
		if (matTotalAmtMap.get("WOST") == null)
			WOST = new BigDecimal(0);
		else
			WOST = matTotalAmtMap.get("WOST");
		mv.addObject("WOST",WOST);

		if (matTotalAmtMap.get("BMGR") == null)
			BMGR = new BigDecimal(0);
		else
			BMGR = matTotalAmtMap.get("BMGR"); 
		
		mv.addObject("BMGR",BMGR);
		if (matTotalAmtMap.get("BMSR") == null)
			BMSR = new BigDecimal(0);
		else
			BMSR = matTotalAmtMap.get("BMSR");
		mv.addObject("BMSR",BMSR);
		System.out.println("BMSR"+BMSR);
		if (matTotalAmtMap.get("BMVD") == null)
			BMVD = new BigDecimal(0);
		else
			BMVD = matTotalAmtMap.get("BMVD");
		if (matTotalAmtMap.get("BSVD") == null)
			BSVD = new BigDecimal(0);
		else
			BSVD = matTotalAmtMap.get("BSVD");
		if (matTotalAmtMap.get("BMNA") == null)
			BMNA = new BigDecimal(0);
		else
			BMNA = matTotalAmtMap.get("BMNA");
		if (matTotalAmtMap.get("BSNA") == null)
			BSNA = new BigDecimal(0);
		else
			BSNA = matTotalAmtMap.get("BSNA");
		if (matTotalAmtMap.get("BSQD") == null)
			BSQD = new BigDecimal(0);
		else
			BSQD = matTotalAmtMap.get("BSQD");
		
		if (matTotalAmtMap.get("SRST") == null)
			SRST = new BigDecimal(0);
		else
			SRST = matTotalAmtMap.get("SRST");
		
		if (matTotalAmtMap.get("WIST") == null)
			WIST = new BigDecimal(0);
		else
			WIST = matTotalAmtMap.get("WIST"); 
		
		if (tostTotalAmountMap.get("TOST") == null)
		TOST = new BigDecimal(0);
		else
		TOST = tostTotalAmountMap.get("TOST"); 
		
		if (trstTotalAmountMap.get("TRST") == null)
			TRST = new BigDecimal(0);
		else
			TRST = trstTotalAmountMap.get("TRST"); 
		
		if (matTotalAmtMap.get("SSVD") == null)
			SSVD = new BigDecimal(0);
		else
			SSVD = matTotalAmtMap.get("SSVD");
		mv.addObject("SSVD",SSVD);
		
		if (matTotalAmtMap.get("SMNA") == null)
			SMNA = new BigDecimal(0);
		else
			SMNA = matTotalAmtMap.get("SMNA");
		mv.addObject("SMNA",SMNA);
		System.out.println(matTotalAmtMap);
		BigDecimal procAdd=BMGR.add(BMSR).add(BMVD).add(BSVD);
		BigDecimal procSub=BMNA.add(BSNA).add(BSQD);
		BigDecimal netProcValue = (BMGR.add(BMSR).add(BMVD).add(BSVD)).subtract(BMNA.add(BSNA).add(BSQD));
		System.out.println(netProcValue);
		mv.addObject("netProcValue", netProcValue);
		mv.addObject("procAdd",procAdd);
		mv.addObject("procSub",procSub);
		
		BigDecimal pc, oc, or, de;
		if (liTotalAmountMap.get("PC") == null)
			pc = new BigDecimal(0);
		else
			pc = liTotalAmountMap.get("PC");
		if (liTotalAmountMap.get("OC") == null)
			oc = new BigDecimal(0);
		else
			oc = liTotalAmountMap.get("OC");
		if (liTotalAmountMap.get("OR") == null)
			or = new BigDecimal(0);
		else
			or = liTotalAmountMap.get("OR");
		if (liTotalAmountMap.get("DE") == null)
			de = new BigDecimal(0);
		else
			de = liTotalAmountMap.get("DE");

		BigDecimal  SSNA, SSQD, ILST, SDST;
		if (matTotalAmtMap.get("SMSR") == null)
			SMSR = new BigDecimal(0);
		else
			SMSR = matTotalAmtMap.get("SMSR");
		if (matTotalAmtMap.get("SVGR") == null)
			SVGR = new BigDecimal(0);
		else
			SVGR = matTotalAmtMap.get("SVGR");
		if (matTotalAmtMap.get("SMVD") == null)
			SMVD = new BigDecimal(0);
		else
			SMVD = matTotalAmtMap.get("SMVD");
		if (matTotalAmtMap.get("SSNA") == null)
			SSNA = new BigDecimal(0);
		else
			SSNA = matTotalAmtMap.get("SSNA");
		if (matTotalAmtMap.get("SSQD") == null)
			SSQD = new BigDecimal(0);
		else
			SSQD = matTotalAmtMap.get("SSQD");
		if (matTotalAmtMap.get("ILST") == null)
			ILST = new BigDecimal(0);
		else
			ILST = matTotalAmtMap.get("ILST");
		if (matTotalAmtMap.get("SDST") == null)
			SDST = new BigDecimal(0);
		else
			SDST = matTotalAmtMap.get("SDST");
		if (matTotalAmtMap.get("SRST") == null)
			SRST = new BigDecimal(0);
		else
			SRST = matTotalAmtMap.get("SRST");
		BigDecimal subconAdd=( SMSR) .add (SVGR) .add (SMVD) .add (SSVD);
		BigDecimal subconSub=( SMNA).add(SSQD).add(SVNA).add(SSNA);
		BigDecimal netsubconcharge = ( SMSR) .add (SVGR) .add (SMVD) .add (SSVD) .subtract( SMNA).subtract(SSQD).subtract(SVNA).subtract(SSNA);
		BigDecimal totalInputCost = (OPST) .add (MOST) .add (WOST) .add (netProcValue) .add (netsubconcharge) .add (oc) .add (or).add(SRST).add(de).add(TRST);
		BigDecimal totalOutputCost = (SDST ).add (ILST).add( rawTotalCost ).add( sfgTotalCost ).add( fgTotalCost).add(WIST).add(consTotalCost).add(TOST);

		mv.addObject("subconAdd",subconAdd);
		mv.addObject("subconSub",subconSub);
		mv.addObject("netsubconcharge", netsubconcharge);
		mv.addObject("totalInputCost", totalInputCost);
		mv.addObject("totalOutputCost", totalOutputCost);

		return mv;
	

	}
	
	@RequestMapping("/generatePcbValuesReport")
	public ModelAndView generatePcbValuesReport(@RequestParam(value = "qtyType", required = false) String qtyType,
			@RequestParam(value = "org", required = false) String org, 
			@RequestParam(value = "plant", required = false) String plant,
			@RequestParam(value = "year", required = false) String year,
			@RequestParam(value = "period", required = false) String period,
			@RequestParam(value = "atpk", required = false) String atpk, Model model) {
		ModelAndView mv = new ModelAndView("pcbValues");
		System.out.println("selected qtyType is - " + qtyType);
		System.out.println("selected org is - " + org);
		mv.addObject("org",org);
		mv.addObject("plant",plant);
		mv.addObject("year",year);
		mv.addObject("period",period);
		mv.addObject("qtyType",qtyType);
		mv.addObject("atpk",atpk);
		mv.addObject("qResults", atr.generatePcbValuesReport(atpk, qtyType));
		return mv;

	}
	
	@RequestMapping("/generateReconReport")
	public ModelAndView generateReconReport(@RequestParam(value = "org", required = false) String org,
			@RequestParam(value = "plant", required = false) String plant,
			@RequestParam(value = "period", required = false) BigDecimal period,
			@RequestParam(value = "year", required = false) BigDecimal year,
			@RequestParam(value = "mat", required = false) String mat,
			@RequestParam(value = "stocktype", required = false) String stocktype,
			@RequestParam(value = "soa", required = false) String soa, Model model) {
		ModelAndView mv = new ModelAndView("reconReport");
		
		System.out.println("selected org is - " + org);
		mv.addObject("selectedOrg", org);
		mv.addObject("orgs", orgr.findAll());
		System.out.println("selected plant is - " + plant);
		mv.addObject("selectedPlant", plant);
		mv.addObject("plants", pr.findAllbyOrgncode(org));
		System.out.println("selected year is - " + year);
		mv.addObject("selectedYear", year);
		mv.addObject("years",fpr.findYear(org));
		System.out.println("selected period is - " + period);
		mv.addObject("selectedPeriod", period);
		mv.addObject("periods",fpr.findPeriodsbyYear(org, year));
		mv.addObject("mats",matr.findAll());
		mv.addObject("selectedMaterial",mat);
		mv.addObject("selectedStocktype",stocktype);
		mv.addObject("selectedSoa",soa);
		/*
		 * mv.addObject("orgs", orgr.findAll()); mv.addObject("plants", pr.findAll());
		 * mv.addObject("mats",matr.findAll()); mv.addObject("results",
		 * mr.generateConsolidatedReport(Arrays.asList(plant.split(",")),new
		 * BigDecimal(2020),new BigDecimal(1),new BigDecimal(12)));
		 */
		plant=plant.equals("Please Select...")?null:plant;
		stocktype=stocktype.equals("Please Select...")?null:stocktype;
		soa=soa.equals("Please Select...")?null:soa;
		soa=soa.trim().equals("")?null:soa;
		mat=mat.equals("Please select...")?null:mat;
		System.out.println("input : "+mat+" "+soa+" "+stocktype);
		Map<String, BigDecimal> reconTotalQtyMap = (atr.generateReconReport(org,
				plant == null ? null : Arrays.asList(plant.split(",")), mat, stocktype, soa,
				year,	
						period
				)).stream()
						.collect(Collectors.toMap(o -> (String) o[0], o -> new BigDecimal(o[1].toString())));
		mv.addObject("reconTotalQtyMap", reconTotalQtyMap);
		Map<String, BigDecimal> reconTotalAmtMap = (atr.generateReconReport(org,
				plant == null ? null : Arrays.asList(plant.split(",")), mat,stocktype, soa, year, period)).stream()
						.collect(Collectors.toMap(o -> (String) o[0], o -> new BigDecimal(o[2].toString())));
		mv.addObject("reconTotalAmtMap", reconTotalAmtMap);

		if (org != null)
			curro = or.findCurro(org);
		else {
			String orgnisation = pr.findOrgCodeByPlantId(plant);
			curro = or.findCurro(orgnisation);
		}

		if (curro.equals("INR"))
			currency = "Rs:";
		if (curro.equals("USD"))
			currency = "$";
		
		mv.addObject("currency", currency);
		
		System.out.println(currency);

		System.out.println("Recon" + reconTotalAmtMap);
		Map<String, BigDecimal> liTotalAmountMap = (atlir.generateReportByTrnsevent(org,
				plant == null ? null : Arrays.asList(plant.split(",")),year,period)).stream()
						.collect(Collectors.toMap(o -> (String) o[0], o -> new BigDecimal(o[1].toString())));
		mv.addObject("liTotalAmountMap", liTotalAmountMap);

		BigDecimal rawTotalCost = atr.getTotalCostbyMattype(org, plant, "RAW",year,period);
		// String rawCost=currencyWithChosenLocalisation(rawTotalCost,loc);
		mv.addObject("rawTotalCost", rawTotalCost);

		BigDecimal fgTotalCost = atr.getTotalCostbyMattype(org, plant, "FG",year,period);
		mv.addObject("fgTotalCost", fgTotalCost);

		BigDecimal sfgTotalCost = atr.getTotalCostbyMattype(org, plant, "SFG",year,period);
		mv.addObject("sfgTotalCost", sfgTotalCost);

		BigDecimal consTotalCost = atr.getTotalCostbyMattype(org, plant, "CONS",year,period);
		mv.addObject("consTotalCost", consTotalCost);

		System.out.println("rawTotalCost"+ rawTotalCost +"fgTotalCost"+ fgTotalCost);
		
		Map<String, BigDecimal> tostTotalAmountMap = (atlir.getTOSTamount(
				plant == null ? null : Arrays.asList(plant.split(",")),year,period)).stream()
						.collect(Collectors.toMap(o -> (String) o[0], o -> new BigDecimal(o[1].toString())));
		System.out.println("RESHMA" + tostTotalAmountMap);
		mv.addObject("tostTotalAmountMap", tostTotalAmountMap);

		Map<String, BigDecimal> trstTotalAmountMap = (atlir.getTRSTamount(
				plant == null ? null : Arrays.asList(plant.split(",")),year,period)).stream()
						.collect(Collectors.toMap(o -> (String) o[0], o -> new BigDecimal(o[1].toString())));
		System.out.println(trstTotalAmountMap);
		mv.addObject("trstTotalAmountMap", trstTotalAmountMap);

		BigDecimal OPST, MOST, WOST, BMGR, BMSR, BMVD, BSVD, BMNA, BSNA, BSQD,SRST,WIST,TOST,TRST,SSVD,SMNA,SVNA;
		
		if (reconTotalAmtMap.get("SVNA") == null)
			SVNA = new BigDecimal(0);
		else
			SVNA = reconTotalAmtMap.get("SVNA");
		mv.addObject("SVNA",SVNA);
		
		if (reconTotalAmtMap.get("OPST") == null)
			OPST = new BigDecimal(0);
		else
			OPST = reconTotalAmtMap.get("OPST");
		mv.addObject("OPST",OPST);
		if (reconTotalAmtMap.get("MOST") == null)
			MOST = new BigDecimal(0);
		else
			MOST = reconTotalAmtMap.get("MOST");
		mv.addObject("MOST",MOST);
		if (reconTotalAmtMap.get("WOST") == null)
			WOST = new BigDecimal(0);
		else
			WOST = reconTotalAmtMap.get("WOST");
		mv.addObject("WOST",WOST);

		if (reconTotalAmtMap.get("BMGR") == null)
			BMGR = new BigDecimal(0);
		else
			BMGR = reconTotalAmtMap.get("BMGR"); 
		
		mv.addObject("BMGR",BMGR);
		if (reconTotalAmtMap.get("BMSR") == null)
			BMSR = new BigDecimal(0);
		else
			BMSR = reconTotalAmtMap.get("BMSR");
		mv.addObject("BMSR",BMSR);
		System.out.println("BMSR"+BMSR);
		if (reconTotalAmtMap.get("BMVD") == null)
			BMVD = new BigDecimal(0);
		else
			BMVD = reconTotalAmtMap.get("BMVD");
		if (reconTotalAmtMap.get("BSVD") == null)
			BSVD = new BigDecimal(0);
		else
			BSVD = reconTotalAmtMap.get("BSVD");
		if (reconTotalAmtMap.get("BMNA") == null)
			BMNA = new BigDecimal(0);
		else
			BMNA = reconTotalAmtMap.get("BMNA");
		if (reconTotalAmtMap.get("BSNA") == null)
			BSNA = new BigDecimal(0);
		else
			BSNA = reconTotalAmtMap.get("BSNA");
		if (reconTotalAmtMap.get("BSQD") == null)
			BSQD = new BigDecimal(0);
		else
			BSQD = reconTotalAmtMap.get("BSQD");
		
		if (reconTotalAmtMap.get("SRST") == null)
			SRST = new BigDecimal(0);
		else
			SRST = reconTotalAmtMap.get("SRST");
		
		if (reconTotalAmtMap.get("WIST") == null)
			WIST = new BigDecimal(0);
		else
			WIST = reconTotalAmtMap.get("WIST"); 
		
		if (tostTotalAmountMap.get("TOST") == null)
		TOST = new BigDecimal(0);
		else
		TOST = tostTotalAmountMap.get("TOST"); 
		
		if (trstTotalAmountMap.get("TRST") == null)
			TRST = new BigDecimal(0);
		else
			TRST = trstTotalAmountMap.get("TRST"); 
		
		if (reconTotalAmtMap.get("SSVD") == null)
			SSVD = new BigDecimal(0);
		else
			SSVD = reconTotalAmtMap.get("SSVD");
		mv.addObject("SSVD",SSVD);
		
		if (reconTotalAmtMap.get("SMNA") == null)
			SMNA = new BigDecimal(0);
		else
			SMNA = reconTotalAmtMap.get("SMNA");
		mv.addObject("SMNA",SMNA);
		System.out.println(reconTotalAmtMap);
		BigDecimal netProcValue = (BMGR.add(BMSR).add(BMVD).add(BSVD)).subtract(BMNA.add(BSNA).add(BSQD));
		System.out.println(netProcValue);
		mv.addObject("netProcValue", netProcValue);

		BigDecimal pc, oc, or, de;
		if (liTotalAmountMap.get("PC") == null)
			pc = new BigDecimal(0);
		else
			pc = liTotalAmountMap.get("PC");
		if (liTotalAmountMap.get("OC") == null)
			oc = new BigDecimal(0);
		else
			oc = liTotalAmountMap.get("OC");
		if (liTotalAmountMap.get("OR") == null)
			or = new BigDecimal(0);
		else
			or = liTotalAmountMap.get("OR");
		if (liTotalAmountMap.get("DE") == null)
			de = new BigDecimal(0);
		else
			de = liTotalAmountMap.get("DE");

		BigDecimal SMSR, SVGR, SMVD, SSNA, SSQD, ILST, SDST;
		if (reconTotalAmtMap.get("SMSR") == null)
			SMSR = new BigDecimal(0);
		else
			SMSR = reconTotalAmtMap.get("SMSR");
		if (reconTotalAmtMap.get("SVGR") == null)
			SVGR = new BigDecimal(0);
		else
			SVGR = reconTotalAmtMap.get("SVGR");
		if (reconTotalAmtMap.get("SMVD") == null)
			SMVD = new BigDecimal(0);
		else
			SMVD = reconTotalAmtMap.get("SMVD");
		if (reconTotalAmtMap.get("SSNA") == null)
			SSNA = new BigDecimal(0);
		else
			SSNA = reconTotalAmtMap.get("SSNA");
		if (reconTotalAmtMap.get("SSQD") == null)
			SSQD = new BigDecimal(0);
		else
			SSQD = reconTotalAmtMap.get("SSQD");
		if (reconTotalAmtMap.get("ILST") == null)
			ILST = new BigDecimal(0);
		else
			ILST = reconTotalAmtMap.get("ILST");
		if (reconTotalAmtMap.get("SDST") == null)
			SDST = new BigDecimal(0);
		else
			SDST = reconTotalAmtMap.get("SDST");
		if (reconTotalAmtMap.get("SRST") == null)
			SRST = new BigDecimal(0);
		else
			SRST = reconTotalAmtMap.get("SRST");
		
		BigDecimal netsubconcharge = pc.add( SMSR) .add (SVGR) .add (SMVD) .add (SSVD) .subtract( SMNA).subtract(SSQD).subtract(SVNA).subtract(SSNA);
		BigDecimal totalInputCost = (OPST) .add (MOST) .add (WOST) .add (netProcValue) .add (netsubconcharge) .add (oc) .add (or).add(SRST).add(de).add(TRST);
		BigDecimal totalOutputCost = (SDST ).add (ILST).add( rawTotalCost ).add( sfgTotalCost ).add( fgTotalCost).add(WIST).add(consTotalCost).add(TOST);

		mv.addObject("netsubconcharge", netsubconcharge);
		mv.addObject("totalInputCost", totalInputCost);
		mv.addObject("totalOutputCost", totalOutputCost);

		return mv;

	}
	
	@RequestMapping("/loadActivityPrice")
	public ModelAndView loadActivityPrice(
			@RequestParam(value = "org", required = false) String org, 
			@RequestParam(value = "plant", required = false) String plant,
			@RequestParam(value = "year", required = false) BigDecimal year,
			@RequestParam(value = "period", required = false) BigDecimal period,
			 Model model) {
		ModelAndView mv = new ModelAndView("loadActivityPrice");
		//System.out.println("selected qtyType is - " + qtyType);
		System.out.println("selected org is - " + org);
		mv.addObject("orgs", orgr.findAll());
		mv.addObject("plants", pr.findAll());
		mv.addObject("mats", matr.findAll());
		
		return mv;
	}
	
	@RequestMapping("/loadMaterialReport")
	public ModelAndView loadMaterialReport(
			@RequestParam(value = "org", required = false) String org, 
			@RequestParam(value = "plant", required = false) String plant,
			@RequestParam(value = "year", required = false) BigDecimal year,
			@RequestParam(value = "period", required = false) BigDecimal period,
			 Model model) {
		ModelAndView mv = new ModelAndView("generateMaterialReport.html");
		//System.out.println("selected qtyType is - " + qtyType);
		System.out.println("selected org is - " + org);
		mv.addObject("orgs", orgr.findAll());
		mv.addObject("plants", pr.findAll());
		mv.addObject("mats", matr.findAll());
		
		return mv;
	}
	
		@RequestMapping("/generateActivityPrice")
		public ModelAndView generateActivityPrice(
				@RequestParam(value = "org", required = false) String org, 
				@RequestParam(value = "plant", required = false) String plant,
				@RequestParam(value = "year", required = false) BigDecimal year,
				@RequestParam(value = "period", required = false) BigDecimal period,
				Model model) {
			ModelAndView mv = new ModelAndView("oprnpricetemp");
			//System.out.println("selected qtyType is - " + qtyType);
			System.out.println("selected org is - " + org);
			mv.addObject("org",org);
			mv.addObject("plant",plant);
			mv.addObject("year",year);
			mv.addObject("period",period);
			System.out.println("selected org is - " + org);
			mv.addObject("selectedOrg", org);
			
			System.out.println("selected plant is - " + plant);
			mv.addObject("selectedPlant", plant);
		
			System.out.println("selected year is - " + year);
			mv.addObject("selectedYear", year);
			
			System.out.println("selected period is - " + period);
			mv.addObject("selectedPeriod", period);
			
			
			opcalr.generate_OprnCal(org, year, period);
			mv.addObject("results", opcalr.findAll());
			return mv;

		}
		
		@RequestMapping("/saveActivityPrice")
		public ModelAndView saveActivityPrice(
				@RequestParam(value = "org", required = false) String org, 
				@RequestParam(value = "plant", required = false) String plant,
				@RequestParam(value = "year", required = false) BigDecimal year,
				@RequestParam(value = "period", required = false) BigDecimal period,
				Model model) {
			ModelAndView mv = new ModelAndView("displayOprnPrice");
			mv.addObject("org",org);
			mv.addObject("plant",plant);
			mv.addObject("year",year);
			mv.addObject("period",period);
			opr.delteActivityPrice(org, year, period,period);
			mv.addObject("results",opr.insertActivityPrice(org, year, period));
			
			mv.addObject("oprnprice",opr.findActivityPrice(org, year, period,period));
			return mv;
		}
	}

