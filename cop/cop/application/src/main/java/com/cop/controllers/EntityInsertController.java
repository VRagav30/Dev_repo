package com.cop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cop.model.database.Altuom;
import com.cop.model.database.Assignccb;
import com.cop.model.database.Batch;
import com.cop.model.database.Bom;
import com.cop.model.database.Cagroup;
import com.cop.model.database.Caval;
import com.cop.model.database.Ccb;
import com.cop.model.database.Ccgroup;
import com.cop.model.database.Coac;
import com.cop.model.database.Costcenter;
import com.cop.model.database.Country;
import com.cop.model.database.Currency;
import com.cop.model.database.Currencyexchangerate;
import com.cop.model.database.Customer;
import com.cop.model.database.Fyd;
import com.cop.model.database.Fydperiod;
import com.cop.model.database.Material;
import com.cop.model.database.Mrc;
import com.cop.model.database.Numberrange;
import com.cop.model.database.Opgroup;
import com.cop.model.database.Oprn;
import com.cop.model.database.Organisation;
import com.cop.model.database.Organisationgovtnum;
import com.cop.model.database.Pcb;
import com.cop.model.database.Plant;
import com.cop.model.database.Plantgovtnum;
import com.cop.model.database.Prcsstep;
import com.cop.model.database.Prcsstepops;
import com.cop.model.database.Processcard;
import com.cop.model.database.Profitcenter;
import com.cop.model.database.Profitcenterorgmap;
import com.cop.model.database.Serviceitem;
import com.cop.model.database.Uom;
import com.cop.model.security.User;
import com.cop.model.database.Vendor;
import com.cop.repository.transaction.AltuomRepository;
import com.cop.repository.transaction.AssignccbRepository;
import com.cop.repository.transaction.BatchRepository;
import com.cop.repository.transaction.BomRepository;
import com.cop.repository.transaction.CagroupRepository;
import com.cop.repository.transaction.CavalRepository;
import com.cop.repository.transaction.CcbRepository;
import com.cop.repository.transaction.CcgroupRepository;
import com.cop.repository.transaction.CoacRepository;
import com.cop.repository.transaction.CostCenterRepository;
import com.cop.repository.transaction.CountryRepository;
import com.cop.repository.transaction.CurrencyExchangeRateRepository;
import com.cop.repository.transaction.CurrencyRepository;
import com.cop.repository.transaction.CustomerRepository;
import com.cop.repository.transaction.FydPeriodRepository;
import com.cop.repository.transaction.FydRepository;
import com.cop.repository.transaction.MaterialRepository;
import com.cop.repository.transaction.MrcRepository;
import com.cop.repository.transaction.NumberRangeRepository;
import com.cop.repository.transaction.OgnRepository;
import com.cop.repository.transaction.OpgroupRepository;
import com.cop.repository.transaction.OprnRepository;
import com.cop.repository.transaction.OrganisationRepository;
import com.cop.repository.transaction.PcardRepository;
import com.cop.repository.transaction.PcbRepository;
import com.cop.repository.transaction.PgnRepository;
import com.cop.repository.transaction.PlantRepository;
import com.cop.repository.transaction.PrcsstepRepository;
import com.cop.repository.transaction.PrcsstepopsRepository;
import com.cop.repository.transaction.ProfitcenterRepository;
import com.cop.repository.transaction.ProfitcenterorgmapRepository;
import com.cop.repository.transaction.ServiceRepository;
import com.cop.repository.transaction.UomRepository;
import com.cop.repository.transaction.UserRepository;
import com.cop.repository.transaction.VendorRepository;


@Controller
public class EntityInsertController {
	@Autowired
	FydRepository fydr;
	@Autowired
	CurrencyRepository cr;
	@Autowired
	CountryRepository cunr;
	@Autowired
	CurrencyExchangeRateRepository cerr;
	@Autowired
	UomRepository uomr;
	@Autowired
	VendorRepository vr;
	@Autowired
	CustomerRepository cusr;
	@Autowired
	MaterialRepository matr;
	@Autowired
	CostCenterRepository ccr;
	@Autowired
	CoacRepository coacr;
	@Autowired
	BomRepository bomr;
	@Autowired
	ServiceRepository serr;
	@Autowired
	PcbRepository pcbr;
	@Autowired
	CcbRepository ccbr;
	@Autowired
	OpgroupRepository opgr;
	@Autowired
	OprnRepository opr;
	@Autowired
	OrganisationRepository or;
	@Autowired
	OgnRepository ognr;
	@Autowired
	PgnRepository pgnr;
	
	@Autowired
	PlantRepository pr;
	Plant pl;
	@Autowired
	MrcRepository mrcr;
	Mrc mrc;
	@Autowired
	AltuomRepository altr;
	Altuom alt;
	@Autowired
	BatchRepository br;
	Batch bat;
	Fyd fyd;
	Currency cu;
	Country country;
	Currencyexchangerate cer;
	Uom uomt;
	Vendor  ven;
	Customer cus;
	Material mat;
	Costcenter cc;
	Coac ca;
	Bom  bom;
	Serviceitem ser;
	Pcb pcb;
	Ccb ccb;
	Opgroup opg;
	Oprn operation;
	Organisation org;
	Organisationgovtnum ogn;
	Plantgovtnum pgn;
	User user;
	
	@Autowired
	UserRepository ur;
	
	@Autowired
	CavalRepository cavalr;
	Caval caval;
	
	@Autowired
	PcardRepository pcardr;
	Processcard pcard;
	
	
	@Autowired
	NumberRangeRepository nrr;
	Numberrange nr;
	
	@Autowired
	AssignccbRepository accbr;
	Assignccb accb;
	
	@Autowired
	CcgroupRepository ccgr;
	Ccgroup ccg;
	
	@Autowired
	ProfitcenterRepository pcr;
	Profitcenter pc;
	
	@Autowired
	ProfitcenterorgmapRepository pcomr;
	Profitcenterorgmap pcom;
	
	@Autowired
	CagroupRepository cagr;
	Cagroup cag;
	
	@Autowired
	PrcsstepRepository pstepr;
	Prcsstep pstep;
	
	@Autowired
	PrcsstepopsRepository popsr;
		
	@Autowired
	FydPeriodRepository fpr;
	
	
	@RequestMapping("/insertFydperiod")
	public ModelAndView InsertFydperiod(
			Fydperiod fydp,
			@RequestParam(value = "mode", required = false, defaultValue = "") String mode,
			Model model) {
		ModelAndView mv = new ModelAndView("tables/display/displayFydperiod.html");
		System.out.println("mode : "+mode);
		switch(mode) {
		case "save":
			System.out.println("save");
			fpr.save(fydp);
			mv.addObject("mode", "edit");
			break;
		case "edit":
			System.out.println("edit");
			mv = new ModelAndView("tables/insertFydperiod.html");
			mv.addObject("mode", "save");
			break;
		case "delete":
			System.out.println("delete");
			fpr.softdeleteFydperiod(fydp.getFydperiodpk(), "user"+System.currentTimeMillis());;
			break;
		default:
			System.out.println("default");
			model.addAttribute(fydp);
			mv = new ModelAndView("tables/insertFydperiod.html");
			mv.addObject("mode", "save");
		}
		mv.addObject("fydp", fydp);
		mv.addObject("fydps",fpr.findAll());
		return mv;
	}
	
	@RequestMapping("/insertPstepops")
	public ModelAndView InsertPstepops(
			Prcsstepops pop,
			@RequestParam(value = "mode", required = false, defaultValue = "") String mode,
			Model model) {
		ModelAndView mv = new ModelAndView("tables/display/displayPstepops.html");
		System.out.println("mode : "+mode);
		switch(mode) {
		case "save":
			System.out.println("save");
			popsr.save(pop);
			mv.addObject("mode", "edit");
			break;
		case "edit":
			System.out.println("edit");
			mv = new ModelAndView("tables/insertPstepops.html");
			mv.addObject("mode", "save");
			break;
		case "delete":
			System.out.println("delete");
			popsr.softdeletePrcsstepops(pop.getPrcsstepopscode(), "user"+System.currentTimeMillis());;
			break;
		default:
			System.out.println("default");
			model.addAttribute(pop);
			mv = new ModelAndView("tables/insertPstepops.html");
			mv.addObject("mode", "save");
		}
		mv.addObject("pop", pop);
		mv.addObject("pops",popsr.findAll());
		return mv;
	}
	
	@RequestMapping("/insertPstep")
	public ModelAndView InsertPstep(
			Prcsstep pstep,
			@RequestParam(value = "mode", required = false, defaultValue = "") String mode,
			Model model) {
		ModelAndView mv = new ModelAndView("tables/display/displayPstep.html");
		System.out.println("mode : "+mode);
		switch(mode) {
		case "save":
			System.out.println("save");
			pstepr.save(pstep);
			mv.addObject("mode", "edit");
			break;
		case "edit":
			System.out.println("edit");
			mv = new ModelAndView("tables/insertPstep.html");
			mv.addObject("mode", "save");
			break;
		case "delete":
			System.out.println("delete");
			pstepr.softdeletePrcsstep(pstep.getPrcsstepcode(), "user"+System.currentTimeMillis());;
			break;
		default:
			System.out.println("default");
			model.addAttribute(pstep);
			mv = new ModelAndView("tables/insertPstep.html");
			mv.addObject("mode", "save");
		}
		mv.addObject("pstep", pstep);
		mv.addObject("psteps",pstepr.findAll());
		return mv;
	}
	
	@RequestMapping("/insertPcard")
	public ModelAndView InsertPcard(
			Processcard pcard,
			@RequestParam(value = "mode", required = false, defaultValue = "") String mode,
			Model model) {
		ModelAndView mv = new ModelAndView("tables/display/displayPcard.html");
		System.out.println("mode : "+mode);
		switch(mode) {
		case "save":
			System.out.println("save");
			pcardr.save(pcard);
			mv.addObject("mode", "edit");
			break;
		case "edit":
			System.out.println("edit");
			mv = new ModelAndView("tables/insertPcard.html");
			mv.addObject("mode", "save");
			break;
		case "delete":
			System.out.println("delete");
			pcardr.softdeleteProcesscard(pcard.getPrscnum(), "user"+System.currentTimeMillis());;
			break;
		default:
			System.out.println("default");
			model.addAttribute(pcard);
			mv = new ModelAndView("tables/insertPcard.html");
			mv.addObject("mode", "save");
		}
		mv.addObject("pcard", pcard);
		mv.addObject("pcards",pcardr.findAll());
		return mv;
	}
	
	@RequestMapping("/insertCagroup")
	public ModelAndView InsertCagroup(
			Cagroup cag,
			@RequestParam(value = "mode", required = false, defaultValue = "") String mode,
			Model model) {
		ModelAndView mv = new ModelAndView("tables/display/displayCagroup.html");
		System.out.println("mode : "+mode);
		switch(mode) {
		case "save":
			System.out.println("save");
			cagr.save(cag);
			mv.addObject("mode", "edit");
			break;
		case "edit":
			System.out.println("edit");
			mv = new ModelAndView("tables/insertCagroup.html");
			mv.addObject("mode", "save");
			break;
		case "delete":
			System.out.println("delete");
			cagr.softdeleteCagroup(cag.getCagrpid(), "user"+ System.currentTimeMillis());;
			break;
		default:
			System.out.println("default");
			model.addAttribute(cag);
			mv = new ModelAndView("tables/insertCagroup.html");
			mv.addObject("mode", "save");
		}
		mv.addObject("cag", cag);
		mv.addObject("cags",cagr.findAll());
		return mv;
	}
	
	@RequestMapping("/insertPcorgmap")
	public ModelAndView InsertPcorgmap(
			Profitcenterorgmap pcom,
			@RequestParam(value = "mode", required = false, defaultValue = "") String mode,
			Model model) {
		ModelAndView mv = new ModelAndView("tables/display/displayPcorgmap.html");
		System.out.println("mode : "+mode);
		switch(mode) {
		case "save":
			System.out.println("save");
			pcomr.save(pcom);
			mv.addObject("mode", "edit");
			break;
		case "edit":
			System.out.println("edit");
			mv = new ModelAndView("tables/insertPcorgmap.html");
			mv.addObject("mode", "save");
			break;
		case "delete":
			System.out.println("delete");
			pcomr.softdeleteProfitcenterorgmap(pcom.getPcompk(), "user"+System.currentTimeMillis());;
			break;
		default:
			System.out.println("default");
			model.addAttribute(pcom);
			mv = new ModelAndView("tables/insertPcorgmap.html");
			mv.addObject("mode", "save");
		}
		mv.addObject("pcom", pcom);
		mv.addObject("pcoms",pcomr.findAll());
		return mv;
	}
	
	@RequestMapping("/insertProfitcenter")
	public ModelAndView InsertProfitcenter(
			Profitcenter pc,
			@RequestParam(value = "mode", required = false, defaultValue = "") String mode,
			Model model) {
		ModelAndView mv = new ModelAndView("tables/display/displayProfitcenter.html");
		System.out.println("mode : "+mode);
		switch(mode) {
		case "save":
			System.out.println("save");
			pcr.save(pc);
			mv.addObject("mode", "edit");
			break;
		case "edit":
			System.out.println("edit");
			mv = new ModelAndView("tables/insertProfitcenter.html");
			mv.addObject("mode", "save");
			break;
		case "delete":
			System.out.println("delete");
			pcr.softdeleteProfitcenter(pc.getProfitcenterpk(), "user"+System.currentTimeMillis());;
			break;
		default:
			System.out.println("default");
			model.addAttribute(pc);
			mv = new ModelAndView("tables/insertProfitcenter.html");
			mv.addObject("mode", "save");
		}
		mv.addObject("pc", pc);
		mv.addObject("pcs",pcr.findAll());
		return mv;
	}
	
	@RequestMapping("/insertCcgroup")
	public ModelAndView InsertCcgroup(
			Ccgroup ccg,
			@RequestParam(value = "mode", required = false, defaultValue = "") String mode,
			Model model) {
		ModelAndView mv = new ModelAndView("tables/display/displayCcgroup.html");
		System.out.println("mode : "+mode);
		switch(mode) {
		case "save":
			System.out.println("save");
			ccgr.save(ccg);
			mv.addObject("mode", "edit");
			break;
		case "edit":
			System.out.println("edit");
			mv = new ModelAndView("tables/insertCcgroup.html");
			mv.addObject("mode", "save");
			break;
		case "delete":
			System.out.println("delete");
			ccgr.softdeleteCcgroup(ccg.getCcgpk(), "user"+System.currentTimeMillis());;
			break;
		default:
			System.out.println("default");
			model.addAttribute(ccg);
			mv = new ModelAndView("tables/insertCcgroup.html");
			mv.addObject("mode", "save");
		}
		mv.addObject("ccg", ccg);
		mv.addObject("ccgs",ccgr.findAll());
		return mv;
	}
	
	@RequestMapping("/insertAssignccb")
	public ModelAndView InsertAssignccb(
			Assignccb accb,
			@RequestParam(value = "mode", required = false, defaultValue = "") String mode,
			Model model) {
		ModelAndView mv = new ModelAndView("tables/display/displayAssignccb.html");
		System.out.println("mode : "+mode);
		switch(mode) {
		case "save":
			System.out.println("save");
			accbr.save(accb);
			mv.addObject("mode", "edit");
			break;
		case "edit":
			System.out.println("edit");
			mv = new ModelAndView("tables/insertAssignccb.html");
			mv.addObject("mode", "save");
			break;
		case "delete":
			System.out.println("delete");
			accbr.softdeleteAssignccb(accb.getAccbpk(), "user"+ System.currentTimeMillis());
			break;
		default:
			System.out.println("default");
			model.addAttribute(accb);
			mv = new ModelAndView("tables/insertAssignccb.html");
			mv.addObject("mode", "save");
		}
		mv.addObject("accb", accb);
		mv.addObject("accbs",accbr.findAll());
		return mv;
	}
	
	@RequestMapping("/insertNumberRange")
	public ModelAndView InsertNumberRange(
			Numberrange nr,
			@RequestParam(value = "mode", required = false, defaultValue = "") String mode,
			Model model) {
		ModelAndView mv = new ModelAndView("tables/display/displayNumberRange.html");
		System.out.println("mode : "+mode);
		switch(mode) {
		case "save":
			System.out.println("save");
			nrr.save(nr);
			mv.addObject("mode", "edit");
			break;
		case "edit":
			System.out.println("edit");
			mv = new ModelAndView("tables/insertNumberRange.html");
			mv.addObject("mode", "save");
			break;
		case "delete":
			System.out.println("delete");
			nrr.softdeleteNumberrange(nr.getRangeid(), "user"+System.currentTimeMillis());;
			break;
		default:
			System.out.println("default");
			model.addAttribute(nr);
			mv = new ModelAndView("tables/insertNumberRange.html");
			mv.addObject("mode", "save");
		}
		mv.addObject("nr", nr);
		mv.addObject("nrs",nrr.findAll());
		return mv;
	}
	@RequestMapping("/insertCaval")
	public ModelAndView InsertCaval(
			Caval caval,
			@RequestParam(value = "mode", required = false, defaultValue = "") String mode,
			Model model) {
		ModelAndView mv = new ModelAndView("tables/display/displayCaval.html");
		System.out.println("mode : "+mode);
		switch(mode) {
		case "save":
			System.out.println("save");
			cavalr.save(caval);
			mv.addObject("mode", "edit");
			break;
		case "edit":
			System.out.println("edit");
			mv = new ModelAndView("tables/insertCaval.html");
			mv.addObject("mode", "save");
			break;
		case "delete":
			System.out.println("delete");
			cavalr.softdeleteCaval(caval.getCavalpk(), "user"+ System.currentTimeMillis());
			break;
		default:
			System.out.println("default");
			model.addAttribute(caval);
			mv = new ModelAndView("tables/insertCaval.html");
			mv.addObject("mode", "save");
		}
		mv.addObject("caval", caval);
		mv.addObject("cavals",cavalr.findAll());
		return mv;
	}
	
	@RequestMapping("/insertFyd")
	public ModelAndView InsertFyd(
			Fyd fyd,
			@RequestParam(value = "mode", required = false, defaultValue = "") String mode,
			Model model) {
		ModelAndView mv = new ModelAndView("tables/display/displayFyd.html");
		//ModelAndView insertView = new ModelAndView("tables/insertFyd.html");
		//ModelAndView displayView=new ModelAndView("tables/display/displayFyd.html");
		System.out.println("mode : "+mode);
		switch(mode) {
		case "save":
			System.out.println("save");
			fydr.save(fyd);
			mv.addObject("mode", "edit");
			break;
		case "edit":
			System.out.println("edit");
			mv = new ModelAndView("tables/insertFyd.html");
			mv.addObject("mode", "save");
			break;
		case "delete":
			System.out.println("delete");
			fydr.softdeleteFyd(fyd.getFydpk(), "user"+System.currentTimeMillis());;
			break;
		default:
			System.out.println("default");
			model.addAttribute(fyd);
			mv = new ModelAndView("tables/insertFyd.html");
			mv.addObject("mode", "save");
		}
		mv.addObject("fyd", fyd);
		mv.addObject("fyds",fydr.findAll());
		return mv;
	}

	@RequestMapping("/insertCurrency")
	public ModelAndView InsertCurrency(
			Currency cu,
			@RequestParam(value = "mode", required = false, defaultValue = "") String mode,
			Model model) {
		ModelAndView mv = new ModelAndView("tables/display/displayCurrency.html");
		System.out.println("mode : "+mode);
		switch(mode) {
		case "save":
			System.out.println("save");
			cr.save(cu);
			mv.addObject("mode", "edit");
			break;
		case "edit":
			System.out.println("edit");
			mv = new ModelAndView("tables/insertCurrency.html");
			mv.addObject("mode", "save");
			break;
		case "delete":
			System.out.println("delete");
			cr.softdeleteCurrency(cu.getCurrc(), "user"+System.currentTimeMillis());;
			break;
		default:
			System.out.println("default");
			model.addAttribute(cu);
			mv = new ModelAndView("tables/insertCurrency.html");
			mv.addObject("mode", "save");
		}
		mv.addObject("cu", cu);
		mv.addObject("cus",cr.findAll());
		return mv;
	}

	@RequestMapping("/insertCountry")
	public ModelAndView InsertCountry(
			Country cun,
			@RequestParam(value = "mode", required = false, defaultValue = "") String mode,
			Model model) {
		ModelAndView mv = new ModelAndView("tables/display/displayCountry.html");
		System.out.println("mode : "+mode);
		switch(mode) {
		case "save":
			System.out.println("save");
			cunr.save(cun);
			mv.addObject("mode", "edit");
			break;
		case "edit":
			System.out.println("edit");
			mv = new ModelAndView("tables/insertCountry.html");
			mv.addObject("mode", "save");
			break;
		case "delete":
			System.out.println("delete");
			cunr.softdeleteCountry(cun.getCountryid(), "user"+System.currentTimeMillis());;
			break;
		default:
			System.out.println("default");
			model.addAttribute(cun);
			mv = new ModelAndView("tables/insertCountry.html");
			mv.addObject("mode", "save");
		}
		mv.addObject("cun", cun);
		mv.addObject("cuns",cunr.findAll());
		return mv;
	}

	@RequestMapping("/insertCurrencyER")
public ModelAndView InsertCurrencyER(
		Currencyexchangerate cer,
		@RequestParam(value = "mode", required = false, defaultValue = "") String mode,
		Model model) {
	ModelAndView mv = new ModelAndView("tables/display/displayCurrencyER.html");
	System.out.println("mode : "+mode);
	switch(mode) {
	case "save":
		System.out.println("save");
		cerr.save(cer);
		mv.addObject("mode", "edit");
		break;
	case "edit":
		System.out.println("edit");
		mv = new ModelAndView("tables/insertCurrencyER.html");
		mv.addObject("mode", "save");
		break;
	case "delete":
		System.out.println("delete");
		cerr.softdeleteCurrencyexchangerate(cer.getCurexchratepk(), "user"+System.currentTimeMillis());;
		break;
	default:
		System.out.println("default");
		model.addAttribute(cer);
		mv = new ModelAndView("tables/insertCurrencyER.html");
		mv.addObject("mode", "save");
	}
	mv.addObject("cer", cer);
	mv.addObject("cers",cerr.findAll());
	return mv;
}

@RequestMapping("/insertUom")
public ModelAndView InsertUom(
		Uom uomt,
		@RequestParam(value = "mode", required = false, defaultValue = "") String mode,
		Model model) {
	ModelAndView mv = new ModelAndView("tables/display/displayUom.html");
	System.out.println("mode : "+mode);
	switch(mode) {
	case "save":
		System.out.println("save");
		uomr.save(uomt);
		mv.addObject("mode", "edit");
		break;
	case "edit":
		System.out.println("edit");
		mv = new ModelAndView("tables/insertUom.html");
		mv.addObject("mode", "save");
		break;
	case "delete":
		System.out.println("delete");
		uomr.softdeleteUom(uomt.getUom(), "user"+System.currentTimeMillis());;
		break;
	default:
		System.out.println("default");
		model.addAttribute(uomt);
		mv = new ModelAndView("tables/insertUom.html");
		mv.addObject("mode", "save");
	}
	mv.addObject("uomt", uomt);
	mv.addObject("uoms",uomr.findAll());
	return mv;
}

@RequestMapping("/insertAltuom")
public ModelAndView InsertAltuom(
		Altuom alt,
		@RequestParam(value = "mode", required = false, defaultValue = "") String mode,
		Model model) {
	ModelAndView mv = new ModelAndView("tables/display/displayAltuom.html");
	System.out.println("mode : "+mode);
	switch(mode) {
	case "save":
		System.out.println("save");
		altr.save(alt);
		mv.addObject("mode", "edit");
		break;
	case "edit":
		System.out.println("edit");
		mv = new ModelAndView("tables/insertAltuom.html");
		mv.addObject("mode", "save");
		break;
	case "delete":
		System.out.println("delete");
		altr.softdeleteAltuom(alt.getAltuomid(),  "user"+ System.currentTimeMillis());
		break;
	default:
		System.out.println("default");
		model.addAttribute(alt);
		mv = new ModelAndView("tables/insertAltuom.html");
		mv.addObject("mode", "save");
	}
	mv.addObject("alt", alt);
	mv.addObject("alts",altr.findAll());
	return mv;
}

@RequestMapping("/insertVendor")
public ModelAndView InsertVendor(
		Vendor ven,
		@RequestParam(value = "mode", required = false, defaultValue = "") String mode,
		Model model) {
	ModelAndView mv = new ModelAndView("tables/display/displayVendor.html");
	System.out.println("mode : "+mode);
	switch(mode) {
	case "save":
		System.out.println("save");
		vr.save(ven);
		mv.addObject("mode", "edit");
		break;
	case "edit":
		System.out.println("edit");
		mv = new ModelAndView("tables/insertVendor.html");
		mv.addObject("mode", "save");
		break;
	case "delete":
		System.out.println("delete");
		vr.softdeleteVendor(ven.getVendor(), "user"+System.currentTimeMillis());;
		break;
	default:
		System.out.println("default");
		model.addAttribute(ven);
		mv = new ModelAndView("tables/insertVendor.html");
		mv.addObject("mode", "save");
	}
	mv.addObject("ven", ven);
	mv.addObject("vens",vr.findAll());
	return mv;
}

@RequestMapping("/insertCustomer")
public ModelAndView InsertCustomer(
		Customer cus,
		@RequestParam(value = "mode", required = false, defaultValue = "") String mode,
		Model model) {
	ModelAndView mv = new ModelAndView("tables/display/displayCustomer.html");
	System.out.println("mode : "+mode);
	switch(mode) {
	case "save":
		System.out.println("save");
		cusr.save(cus);
		mv.addObject("mode", "edit");
		break;
	case "edit":
		System.out.println("edit");
		mv = new ModelAndView("tables/insertCustomer.html");
		mv.addObject("mode", "save");
		break;
	case "delete":
		System.out.println("delete");
		cusr.softdeleteCustomer(cus.getCustomer(), "user"+System.currentTimeMillis());;
		break;
	default:
		System.out.println("default");
		model.addAttribute(cus);
		mv = new ModelAndView("tables/insertCustomer.html");
		mv.addObject("mode", "save");
	}
	mv.addObject("cus", cus);
	mv.addObject("cuss",cusr.findAll());
	return mv;
}

@RequestMapping("/insertCc")
public ModelAndView InsertCc(
		Costcenter cc,
		@RequestParam(value = "mode", required = false, defaultValue = "") String mode,
		Model model) {
	ModelAndView mv = new ModelAndView("tables/display/displayCostcenter.html");
	System.out.println("mode : "+mode);
	switch(mode) {
	case "save":
		System.out.println("save");
		ccr.save(cc);
		mv.addObject("mode", "edit");
		break;
	case "edit":
		System.out.println("edit");
		mv = new ModelAndView("tables/insertCostcenter.html");
		mv.addObject("mode", "save");
		break;
	case "delete":
		System.out.println("delete");
		ccr.softdeleteCostcenter(cc.getCctr(), "user"+System.currentTimeMillis());
		break;
	default:
		System.out.println("default");
		model.addAttribute(cc);
		mv = new ModelAndView("tables/insertCostcenter.html");
		mv.addObject("mode", "save");
	}
	mv.addObject("cc", cc);
	mv.addObject("ccs",ccr.findAll());
	return mv;
}

@RequestMapping("/insertMaterial")
public ModelAndView InsertMaterial(
		Material mat,
		@RequestParam(value = "mode", required = false, defaultValue = "") String mode,
		Model model) {
	ModelAndView mv = new ModelAndView("tables/display/displayMaterial.html");
	System.out.println("mode : "+mode);
	switch(mode) {
	case "save":
		System.out.println("save");
		matr.save(mat);
		mv.addObject("mode", "edit");
		break;
	case "edit":
		System.out.println("edit");
		mv = new ModelAndView("tables/insertMaterial.html");
		mv.addObject("mode", "save");
		break;
	case "delete":
		System.out.println("delete");
		matr.softdeleteMaterial(mat.getMatpk(), "user"+System.currentTimeMillis());;
		break;
	default:
		System.out.println("default");
		model.addAttribute(mat);
		mv = new ModelAndView("tables/insertMaterial.html");
		mv.addObject("mode", "save");
	}
	mv.addObject("mat", mat);
	mv.addObject("mats",matr.findAll());
	return mv;
}

@RequestMapping("/insertCoac")
public ModelAndView InsertCoac(
		Coac coac,
		@RequestParam(value = "mode", required = false, defaultValue = "") String mode,
		Model model) {
	ModelAndView mv = new ModelAndView("tables/display/displayCoac.html");
	System.out.println("mode : "+mode);
	switch(mode) {
	case "save":
		System.out.println("save");
		coacr.save(coac);
		mv.addObject("mode", "edit");
		break;
	case "edit":
		System.out.println("edit");
		mv = new ModelAndView("tables/insertCoac.html");
		mv.addObject("mode", "save");
		break;
	case "delete":
		System.out.println("delete");
		coacr.softdeleteCoac(coac.getCostacc(), "user"+System.currentTimeMillis());
		break;
	default:
		System.out.println("default");
		model.addAttribute(coac);
		mv = new ModelAndView("tables/insertCoac.html");
		mv.addObject("mode", "save");
	}
	mv.addObject("coac", coac);
	mv.addObject("coacs",coacr.findAll());
	return mv;
}

@RequestMapping("/insertBom")
public ModelAndView InsertBom(
		Bom bom,
		@RequestParam(value = "mode", required = false, defaultValue = "") String mode,
		Model model) {
	ModelAndView mv = new ModelAndView("tables/display/displayBom.html");
	System.out.println("mode : "+mode);
	switch(mode) {
	case "save":
		System.out.println("save");
		bomr.save(bom);
		mv.addObject("mode", "edit");
		break;
	case "edit":
		System.out.println("edit");
		mv = new ModelAndView("tables/insertBom.html");
		mv.addObject("mode", "save");
		break;
	case "delete":
		
		System.out.println(bom.getBompk());
		bomr.softdeleteBom(bom.getBompk(), "user"+ System.currentTimeMillis());
		break;
	default:
		System.out.println("default");
		model.addAttribute(bom);
		mv = new ModelAndView("tables/insertBom.html");
		mv.addObject("mode", "save");
	}
	mv.addObject("bom", bom);
	mv.addObject("boms",bomr.findAll());
	return mv;
}

@RequestMapping("/insertServiceitem")
public ModelAndView InsertServiceitem(
		Serviceitem ser,
		@RequestParam(value = "mode", required = false, defaultValue = "") String mode,
		Model model) {
	ModelAndView mv = new ModelAndView("tables/display/displayServiceitem.html");
	System.out.println("mode : "+mode);
	switch(mode) {
	case "save":
		System.out.println("save");
		serr.save(ser);
		mv.addObject("mode", "edit");
		break;
	case "edit":
		System.out.println("edit");
		mv = new ModelAndView("tables/insertServiceitem.html");
		mv.addObject("mode", "save");
		break;
	case "delete":
		System.out.println("delete");
		serr.softdeleteServiceitem(ser.getSercode(), "user"+System.currentTimeMillis());;
		break;
	default:
		System.out.println("default");
		model.addAttribute(ser);
		mv = new ModelAndView("tables/insertServiceitem.html");
		mv.addObject("mode", "save");
	}
	mv.addObject("ser", ser);
	mv.addObject("sis",serr.findAll());
	return mv;
}

@RequestMapping("/insertPcb")
public ModelAndView InsertPcb(
		Pcb pcb,
		@RequestParam(value = "mode", required = false, defaultValue = "") String mode,
		Model model) {
	ModelAndView mv = new ModelAndView("tables/display/displayPcb.html");
	System.out.println("mode : "+mode);
	switch(mode) {
	case "save":
		System.out.println("save");
		pcbr.save(pcb);
		mv.addObject("mode", "edit");
		break;
	case "edit":
		System.out.println("edit");
		mv = new ModelAndView("tables/insertPcb.html");
		mv.addObject("mode", "save");
		break;
	case "delete":
		System.out.println("delete");
		pcbr.softdeletePcb(pcb.getPcbpk(), "user"+System.currentTimeMillis());
		break;
	default:
		System.out.println("default");
		model.addAttribute(pcb);
		mv = new ModelAndView("tables/insertPcb.html");
		mv.addObject("mode", "save");
	}
	mv.addObject("pcb", pcb);
	mv.addObject("pcbs",pcbr.findAll());
	return mv;
}

@RequestMapping("/insertOpg")
public ModelAndView InsertOpg(
		Opgroup opg,
		@RequestParam(value = "mode", required = false, defaultValue = "") String mode,
		Model model) {
	ModelAndView mv = new ModelAndView("tables/display/displayOpg.html");
	System.out.println("mode : "+mode);
	switch(mode) {
	case "save":
		System.out.println("save");
		opgr.save(opg);
		mv.addObject("mode", "edit");
		break;
	case "edit":
		System.out.println("edit");
		mv = new ModelAndView("tables/insertOpg.html");
		mv.addObject("mode", "save");
		break;
	case "delete":
		System.out.println("delete");
		opgr.softdeleteOpgroup(opg.getOpgrp(), "user"+System.currentTimeMillis());;
		break;
	default:
		System.out.println("default");
		model.addAttribute(opg);
		mv = new ModelAndView("tables/insertOpg.html");
		mv.addObject("mode", "save");
	}
	mv.addObject("opg", opg);
	mv.addObject("opgs",opgr.findAll());
	return mv;
}

@RequestMapping("/insertCcb")
public ModelAndView InsertCcb(
		Ccb ccb,
		@RequestParam(value = "mode", required = false, defaultValue = "") String mode,
		Model model) {
	ModelAndView mv = new ModelAndView("tables/display/displayCcb.html");
	System.out.println("mode : "+mode);
	switch(mode) {
	case "save":
		System.out.println("save");
		ccbr.save(ccb);
		mv.addObject("mode", "edit");
		break;
	case "edit":
		System.out.println("edit");
		mv = new ModelAndView("tables/insertCcb.html");
		mv.addObject("mode", "save");
		break;
	case "delete":
		System.out.println("delete");
		ccbr.softdeleteCcb(ccb.getCcbpk(), "user"+ System.currentTimeMillis());;
		break;
	default:
		System.out.println("default");
		model.addAttribute(ccb);
		mv = new ModelAndView("tables/insertCcb.html");
		mv.addObject("mode", "save");
	}
	mv.addObject("ccb", ccb);
	mv.addObject("ccbs",ccbr.findAll());
	return mv;
}

@RequestMapping("/insertOprn")
public ModelAndView InsertOprn(
		Oprn oprn,
		@RequestParam(value = "mode", required = false, defaultValue = "") String mode,
		Model model) {
	ModelAndView mv = new ModelAndView("tables/display/displayOprn.html");
	System.out.println("mode : "+mode);
	switch(mode) {
	case "save":
		System.out.println("save");
		opr.save(oprn);
		mv.addObject("mode", "edit");
		break;
	case "edit":
		System.out.println("edit");
		mv = new ModelAndView("tables/insertOprn.html");
		mv.addObject("mode", "save");
		break;
	case "delete":
		System.out.println("delete");
		opr.softdeleteOprn(oprn.getOprn(), "user"+System.currentTimeMillis());;
		break;
	default:
		System.out.println("default");
		model.addAttribute(oprn);
		mv = new ModelAndView("tables/insertOprn.html");
		mv.addObject("mode", "save");
	}
	mv.addObject("oprn", oprn);
	mv.addObject("oprns",opr.findAll());
	return mv;
}

@RequestMapping("/insertOrganisation")
public ModelAndView InsertOrganisation(
		Organisation org,
		@RequestParam(value = "mode", required = false, defaultValue = "") String mode,
		Model model) {
	ModelAndView mv = new ModelAndView("tables/display/displayOrganisation.html");
	System.out.println("mode : "+mode);
	switch(mode) {
	case "save":
		System.out.println("save");
		or.save(org);
		mv.addObject("mode", "edit");
		break;
	case "edit":
		System.out.println("edit");
		mv = new ModelAndView("tables/insertOrganisation.html");
		mv.addObject("mode", "save");
		break;
	case "delete":
		System.out.println("delete");
		or.softdeleteOrganisation(org.getOrgncode(), "user"+System.currentTimeMillis());;
		break;
	default:
		System.out.println("default");
		model.addAttribute(org);
		mv = new ModelAndView("tables/insertOrganisation.html");
		mv.addObject("mode", "save");
	}
	mv.addObject("org", org);
	mv.addObject("orgs",or.findAll());
	return mv;
}

@RequestMapping("/insertPgn")
public ModelAndView InsertPgn(
		Plantgovtnum pgn,
		@RequestParam(value = "mode", required = false, defaultValue = "") String mode,
		Model model) {
	ModelAndView mv = new ModelAndView("tables/display/displayPGN.html");
	System.out.println("mode : "+mode);
	switch(mode) {
	case "save":
		System.out.println("save");
		pgnr.save(pgn);
		mv.addObject("mode", "edit");
		break;
	case "edit":
		System.out.println("edit");
		mv = new ModelAndView("tables/insertPGN.html");
		mv.addObject("mode", "save");
		break;
	case "delete":
		System.out.println("delete");
		pgnr.softdeletePlantgovtnum(pgn.getPgnPk(), "user"+System.currentTimeMillis());;
		break;
	default:
		System.out.println("default");
		model.addAttribute(pgn);
		mv = new ModelAndView("tables/insertPGN.html");
		mv.addObject("mode", "save");
	}
	mv.addObject("pgn", pgn);
	mv.addObject("pgns",pgnr.findAll());
	return mv;
}

@RequestMapping("/insertOgn")
public ModelAndView InsertOgn(
		Organisationgovtnum ogn,
		@RequestParam(value = "mode", required = false, defaultValue = "") String mode,
		Model model) {
	ModelAndView mv = new ModelAndView("tables/display/displayOgn.html");
	System.out.println("mode : "+mode);
	switch(mode) {
	case "save":
		System.out.println("save");
		ognr.save(ogn);
		mv.addObject("mode", "edit");
		break;
	case "edit":
		System.out.println("edit");
		mv = new ModelAndView("tables/insertOgn.html");
		mv.addObject("mode", "save");
		break;
	case "delete":
		System.out.println("delete");
		ognr.softdeleteOrganisationgovtnum(ogn.getOgnPk(), "user"+System.currentTimeMillis());;
		break;
	default:
		System.out.println("default");
		model.addAttribute(ogn);
		mv = new ModelAndView("tables/insertOgn.html");
		mv.addObject("mode", "save");
	}
	mv.addObject("ogn", ogn);
	mv.addObject("ogns",ognr.findAll());
	return mv;
}

@RequestMapping("/insertPlant")
public ModelAndView InsertPlant(
		Plant pl,
		@RequestParam(value = "mode", required = false, defaultValue = "") String mode,
		Model model) {
	ModelAndView mv = new ModelAndView("tables/display/displayPlant.html");
	System.out.println("mode : "+mode);
	switch(mode) {
	case "save":
		System.out.println("save");
		pr.save(pl);
		mv.addObject("mode", "edit");
		break;
	case "edit":
		System.out.println("edit");
		mv = new ModelAndView("tables/insertPlant.html");
		mv.addObject("mode", "save");
		break;
	case "delete":
		System.out.println("delete");
		pr.softdeletePlant(pl.getPlant(), "user"+System.currentTimeMillis());;
		break;
	default:
		System.out.println("default");
		model.addAttribute(pl);
		mv = new ModelAndView("tables/insertPlant.html");
		mv.addObject("mode", "save");
	}
	mv.addObject("pl", pl);
	mv.addObject("pls",pr.findAll());
	return mv;
}


@RequestMapping("/insertBatch")
public ModelAndView InsertBatch(
		Batch bat,
		@RequestParam(value = "mode", required = false, defaultValue = "") String mode,
		Model model) {
	ModelAndView mv = new ModelAndView("tables/display/displayBatch.html");
	System.out.println("mode : "+mode);
	switch(mode) {
	case "save":
		System.out.println("save");
		br.save(bat);
		mv.addObject("mode", "edit");
		break;
	case "edit":
		System.out.println("edit");
		mv = new ModelAndView("tables/insertBatch.html");
		mv.addObject("mode", "save");
		break;
	case "delete":
		System.out.println("delete");
		br.softdeleteBatch(bat.getBatch(), "user"+ System.currentTimeMillis());;
		break;
	default:
		System.out.println("default");
		System.out.println(bat.getValfdate());
		model.addAttribute(bat);
		mv = new ModelAndView("tables/insertBatch.html");
		mv.addObject("mode", "save");
	}
	mv.addObject("bat", bat);
	mv.addObject("batches",br.findAll());
	return mv;
}

@RequestMapping("/insertMrc")
public ModelAndView InsertMrc(
		Mrc mrc,
		@RequestParam(value = "mode", required = false, defaultValue = "") String mode,
		Model model) {
	ModelAndView mv = new ModelAndView("tables/display/displayMrc.html");
	System.out.println("mode : "+mode);
	switch(mode) {
	case "save":
		System.out.println("save");
		mrcr.save(mrc);
		mv.addObject("mode", "edit");
		break;
	case "edit":
		System.out.println("edit");
		mv = new ModelAndView("tables/insertMrc.html");
		mv.addObject("mode", "save");
		break;
	case "delete":
		System.out.println("delete");
		mrcr.softdeleteMrc(mrc.getMrcpk(), "user"+System.currentTimeMillis());;
		break;
	default:
		System.out.println("default");
		model.addAttribute(mrc);
		mv = new ModelAndView("tables/insertMrc.html");
		mv.addObject("mode", "save");
	}
	mv.addObject("mrc", mrc);
	mv.addObject("mrcs",mrcr.findAll());
	return mv;
}


@RequestMapping("/insertUser")
public ModelAndView InsertUser(
		User user,
		@RequestParam(value = "mode", required = false, defaultValue = "") String mode,
		Model model) {
	ModelAndView mv = new ModelAndView("tables/display/displayUser.html");
	System.out.println("mode : "+mode);
	switch(mode) {
	case "save":
		System.out.println("save");
		ur.save(user);
		mv.addObject("mode", "edit");
		break;
	case "edit":
		System.out.println("edit");
		mv = new ModelAndView("tables/insertUser.html");
		mv.addObject("mode", "save");
		break;
	case "delete":
		System.out.println("delete");
		ur.softdeleteUser(user.getUserId(), "user"+System.currentTimeMillis());;
		break;
	default:
		System.out.println("default");
		model.addAttribute(user);
		mv = new ModelAndView("tables/insertUser.html");
		mv.addObject("mode", "save");
	}
	mv.addObject("user", user);
	mv.addObject("urs",ur.findAll());
	return mv;
}
}
