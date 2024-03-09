package com.cop.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cop.exception.ResourceNotFoundException;
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
public class EntitySearchController {
	@Autowired
	UserRepository ur;
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
	Fydperiod fp;
	User user;
	@GetMapping("/findBom")
    public ModelAndView displayBom(Model model) {
		ModelAndView mv=new ModelAndView("tables/display/displayBom.html");
		 mv.addObject("boms",bomr.findAll());
        return mv;
    }
	@GetMapping("/findCcb")
    public ModelAndView displayCcb(Model model) {
		ModelAndView mv=new ModelAndView("tables/display/displayCcb.html");
		 mv.addObject("ccbs",ccbr.findAll());
        return mv;
    }
	
	@GetMapping("/findCoac")
    public ModelAndView displayCoac(Model model) {
		ModelAndView mv=new ModelAndView("tables/display/displayCoac.html");
		 mv.addObject("coacs",coacr.findAll());
        return mv;
    }
	
	@GetMapping("/findPlant")
    public ModelAndView displayPlant(Model model) {
		ModelAndView mv=new ModelAndView("tables/display/displayPlant.html");
		 mv.addObject("plants",pr.findAll());
        return mv;
    }
	
	@GetMapping("/findCc")
    public ModelAndView displayCostCenter(Model model) {
		ModelAndView mv=new ModelAndView("tables/display/displayCostCenter.html");
		 mv.addObject("ccs",ccr.findAll());
        return mv;
    }
	
	@GetMapping("/findCountry")
    public ModelAndView displayCountry(Model model) {
		ModelAndView mv=new ModelAndView("tables/display/displayCountry.html");
		 mv.addObject("countries",cunr.findAll());
        return mv;
    }
	@GetMapping("/findOrganisation")
    public ModelAndView displayOrganisation(Model model) {
		ModelAndView mv=new ModelAndView("tables/display/displayOrganisation.html");
		 mv.addObject("orgs",or.findAll());
        return mv;
    }
	
	@GetMapping("/findCurrency")
    public ModelAndView displayCurrency(Model model) {
		ModelAndView mv=new ModelAndView("tables/display/displayCurrency.html");
		 mv.addObject("currs",cr.findAll());
        return mv;
    }
	
	@GetMapping("/findCurrencyER")
    public ModelAndView displayCurrencyER(Model model) {
		ModelAndView mv=new ModelAndView("tables/display/displayCurrencyER.html");
		 mv.addObject("cers",cerr.findAll());
        return mv;
    }
	@GetMapping("/findCustomer")
    public ModelAndView displayCustomer(Model model) {
		ModelAndView mv=new ModelAndView("tables/display/displayCustomer.html");
		 mv.addObject("cuss",cusr.findAll());
        return mv;
    }
	
	@GetMapping("/findFyd")
    public ModelAndView displayFyd(Model model) {
		ModelAndView mv=new ModelAndView("tables/display/displayFyd.html");
		 mv.addObject("fyds",fydr.findAll());
        return mv;
    }

	@GetMapping("/findAltuom")
    public ModelAndView displayAltuom(Model model) {
		ModelAndView mv=new ModelAndView("tables/display/displayAltuom.html");
		 mv.addObject("alts",altr.findAll());
        return mv;
    }
	
	@GetMapping("/findAssignccb")
    public ModelAndView displayAssignccb(Model model) {
		ModelAndView mv=new ModelAndView("tables/display/displayAssignccb.html");
		 mv.addObject("accbs",accbr.findAll());
        return mv;
    }
	
	@GetMapping("/findBatch")
    public ModelAndView displayBatch(Model model) {
		ModelAndView mv=new ModelAndView("tables/display/displayBatch.html");
		 mv.addObject("batches",br.findAll());
        return mv;
    }
	@GetMapping("/findCagroup")
    public ModelAndView displayCagroup(Model model) {
		ModelAndView mv=new ModelAndView("tables/display/displayCagroup.html");
		 mv.addObject("cags",cagr.findAll());
        return mv;
    }
	@GetMapping("/findCaval")
    public ModelAndView displayCaval(Model model) {
		ModelAndView mv=new ModelAndView("tables/display/displayCaval.html");
		 mv.addObject("cavals",cavalr.findAll());
        return mv;
    }
	
	@GetMapping("/findCcgroup")
    public ModelAndView displayCcgroup(Model model) {
		ModelAndView mv=new ModelAndView("tables/display/displayCcgroup.html");
		 mv.addObject("ccgs",ccgr.findAll());
        return mv;
    }
	
	@GetMapping("/findFydPeriod")
    public ModelAndView displayFydPeriod(Model model) {
		ModelAndView mv=new ModelAndView("tables/display/displayFydperiod.html");
		 mv.addObject("fydps",fpr.findAll());
        return mv;
    }
	
	@GetMapping("/findMrc")
    public ModelAndView displayMrc(Model model) {
		ModelAndView mv=new ModelAndView("tables/display/displayMrc.html");
		 mv.addObject("mrcs",mrcr.findAll());
        return mv;
    }
	
	@GetMapping("/findPcorgmap")
    public ModelAndView displayPcorgmap(Model model) {
		ModelAndView mv=new ModelAndView("tables/display/displayPcorgmap.html");
		 mv.addObject("pcorgmaps",pcomr.findAll());
        return mv;
    }	
	
	@GetMapping("/findPGN")
    public ModelAndView displayPGN(Model model) {
		ModelAndView mv=new ModelAndView("tables/display/displayPGN.html");
		 mv.addObject("pgns",pgnr.findAll());
        return mv;
    }	
	
	@GetMapping("/findPcard")
    public ModelAndView displayPcard(Model model) {
		ModelAndView mv=new ModelAndView("tables/display/displayProcesscard.html");
		 mv.addObject("pcards",pcardr.findAll());
        return mv;
    }	
	
	@GetMapping("/findProfitcenter")
    public ModelAndView displayProfitcenter(Model model) {
		ModelAndView mv=new ModelAndView("tables/display/displayProfitcenter.html");
		 mv.addObject("pcs",pcr.findAll());
        return mv;
    }	
	
	@GetMapping("/findProcessstep")
    public ModelAndView displayProcesstep(Model model) {
		ModelAndView mv=new ModelAndView("tables/display/displayPstep.html");
		 mv.addObject("psteps",pstepr.findAll());
        return mv;
    }	
	
	
	@GetMapping("/findNumberRange")
    public ModelAndView displayNumberRange(Model model) {
		ModelAndView mv=new ModelAndView("tables/display/displayNumberRange.html");
		 mv.addObject("nrs",nrr.findAll());
        return mv;
    }
	
	@GetMapping("/findOGN")
    public ModelAndView displayOGN(Model model) {
		ModelAndView mv=new ModelAndView("tables/display/displayOGN.html");
		 mv.addObject("ogns",ognr.findAll());
        return mv;
    }
	
	@GetMapping("/findPstep")
    public ModelAndView displayPstep(Model model) {
		ModelAndView mv=new ModelAndView("tables/display/displayPstep.html");
		 mv.addObject("psteps",pstepr.findAll());
        return mv;
    }
	@GetMapping("/findPstepops")
    public ModelAndView displayPstepops(Model model) {
		ModelAndView mv=new ModelAndView("tables/display/displayPstepops.html");
		 mv.addObject("pops",popsr.findAll());
        return mv;
    }
	
	@GetMapping("/findMaterial")
    public ModelAndView displayMaterial(Model model) {
		ModelAndView mv=new ModelAndView("tables/display/displayMaterial.html");
		 mv.addObject("mats",matr.findAll());
        return mv;
    }
	@GetMapping("/findOpg")
    public ModelAndView displayOpg(Model model) {
		ModelAndView mv=new ModelAndView("tables/display/displayOpgroup.html");
		 mv.addObject("opgs",opgr.findAll());
        return mv;
    }
	@GetMapping("/findOprn")
    public ModelAndView displayOprn(Model model) {
		ModelAndView mv=new ModelAndView("tables/display/displayOprn.html");
		 mv.addObject("oprns",opr.findAll());
        return mv;
    }
	
	@GetMapping("/findPcb")
    public ModelAndView displayPcb(Model model) {
		ModelAndView mv=new ModelAndView("tables/display/displayPcb.html");
		 mv.addObject("pcbs",pcbr.findAll());
        return mv;
    }
	@GetMapping("/findServiceitem")
    public ModelAndView displayServiceitem(Model model) {
		ModelAndView mv=new ModelAndView("tables/display/displayServiceitem.html");
		 mv.addObject("sis",serr.findAll());
        return mv;
    }
	@GetMapping("/findUom")
    public ModelAndView displaUom(Model model) {
		ModelAndView mv=new ModelAndView("tables/display/displayUom.html");
		 mv.addObject("uoms",uomr.findAll());
        return mv;
    }
	@GetMapping("/findVendor")
    public ModelAndView displayVendor(Model model) {
		ModelAndView mv=new ModelAndView("tables/display/displayVendor.html");
		 mv.addObject("vendors",vr.findAll());
        return mv;
    }
	@GetMapping("/findUser")
    public ModelAndView displayUser(Model model) {
		ModelAndView mv=new ModelAndView("tables/display/displayUser.html");
		 mv.addObject("Users",ur.findAll());
        return mv;
    }
	
	 
    @GetMapping("/searchFyd")
    public ModelAndView searchFyd(Fyd fyd,Model model, String keyword)
        {
    	ModelAndView mv=new ModelAndView("tables/display/displayFyd.html");
		 mv.addObject("fyds",fydr.findByKeyword(keyword));
    		return mv;
    		}
    
    @GetMapping("/searchUser")
    public ModelAndView searchUser(User user,Model model, String keyword)
        {
    	ModelAndView mv=new ModelAndView("tables/display/displayUser.html");
		 mv.addObject("users",ur.findByKeyword(keyword));
    		return mv;
    		}
    
    @GetMapping("/searchCcb")
    public ModelAndView searchCcb(Ccb ccb,Model model, String keyword)
        {
    	ModelAndView mv=new ModelAndView("tables/display/displayCcb.html");
		 mv.addObject("ccbs",ccbr.findByKeyword(keyword));
    		return mv;
    		}
    @GetMapping("/searchAltuom")
    public ModelAndView searchAltuom(Altuom alt,Model model, String keyword)
        {
    	ModelAndView mv=new ModelAndView("tables/display/displayAltuom.html");
		 mv.addObject("alts",altr.findByKeyword(keyword));
    		return mv;
    		}
    
    @GetMapping("/searchBom")
    public ModelAndView searchBom(Bom bom,Model model, String keyword)
        {
    	ModelAndView mv=new ModelAndView("tables/display/displayBom.html");
		 mv.addObject("boms",bomr.findByKeyword(keyword));
    		return mv;
    		}
    @GetMapping("/searchOprn")
    public ModelAndView searchOprn(Oprn oprn,Model model, String keyword)
        {
    	ModelAndView mv=new ModelAndView("tables/display/displayOprn.html");
		 mv.addObject("oprns",opr.findByKeyword(keyword));
    		return mv;
    		}
    
    @GetMapping("/searchOpg")
    public ModelAndView searchOpg(Opgroup opg,Model model, String keyword)
        {
    	ModelAndView mv=new ModelAndView("tables/display/displayOpgroup.html");
		 mv.addObject("opgs",opgr.findByKeyword(keyword));
    		return mv;
    		}
    @GetMapping("/searchPlant")
    public ModelAndView searchPlant(Plant plant,Model model, String keyword)
        {
    	ModelAndView mv=new ModelAndView("tables/display/displayPlant.html");
		 mv.addObject("plants",pr.findByKeyword(keyword));
    		return mv;
    		}
    
    @GetMapping("/searchPcard")
    public ModelAndView searchPcard(Processcard pcard,Model model, String keyword)
        {
    	ModelAndView mv=new ModelAndView("tables/display/displayProcesscard.html");
		 mv.addObject("pcards",pcardr.findByKeyword(keyword));
    		return mv;
    		}
    
    @GetMapping("/searchPstep")
    public ModelAndView searchPstep(Prcsstep pstep,Model model, String keyword)
        {
    	ModelAndView mv=new ModelAndView("tables/display/displayPstep.html");
		 mv.addObject("psteps",pstepr.findByKeyword(keyword));
    		return mv;
    		}
    
    @GetMapping("/searchPstepops")
    public ModelAndView searchPstepops(Prcsstepops pstep,Model model, String keyword)
        {
    	ModelAndView mv=new ModelAndView("tables/display/displayPstepops.html");
		 mv.addObject("pops",popsr.findByKeyword(keyword));
    		return mv;
    		}
    @GetMapping("/searchBatch")
    public ModelAndView searchBatch(Batch bat,Model model, String keyword)
        {
    	ModelAndView mv=new ModelAndView("tables/display/displayBatch.html");
		 mv.addObject("batches",br.findByKeyword(keyword));
    		return mv;
    		}
    @GetMapping("/searchCurrencyER")
    public ModelAndView searchCurrencyER(Currencyexchangerate cer,Model model, String keyword)
        {
    	ModelAndView mv=new ModelAndView("tables/display/displayCurrencyER.html");
		 mv.addObject("cers",cerr.findByKeyword(keyword));
    		return mv;
    		}
    @GetMapping("/searchUom")
    public ModelAndView searchUom(Uom uom,Model model, String keyword)
        {
    	ModelAndView mv=new ModelAndView("tables/display/displayUom.html");
		 mv.addObject("uoms",uomr.findByKeyword(keyword));
    		return mv;
    		}
    
    @GetMapping("/searchVendor")
    public ModelAndView searchVendor(Vendor vendor,Model model, String keyword)
        {
    	ModelAndView mv=new ModelAndView("tables/display/displayVendor.html");
		 mv.addObject("vendors",vr.findByKeyword(keyword));
    		return mv;
    		}
    
    @GetMapping("/searchMaterial")
    public ModelAndView searchMaterial(Material mat,Model model, String keyword)
        {
    	ModelAndView mv=new ModelAndView("tables/display/displayMaterial.html");
		 mv.addObject("mats",matr.findByKeyword(keyword));
    		return mv;
    		}
    
    @GetMapping("/searchMrc")
    public ModelAndView searchMrc(Mrc mrc,Model model, String keyword)
        {
    	ModelAndView mv=new ModelAndView("tables/display/displayMrc.html");
		 mv.addObject("mrcs",mrcr.findByKeyword(keyword));
    		return mv;
    		}
    
    @GetMapping("/searchOrg")
    public ModelAndView searchOrg(Organisation org,Model model, String keyword)
        {
    	ModelAndView mv=new ModelAndView("tables/display/displayOrganisation.html");
		 mv.addObject("orgs",or.findByKeyword(keyword));
    		return mv;
    		}
    
    @GetMapping("/searchOgn")
    public ModelAndView searchOrg(Organisationgovtnum ogn,Model model, String keyword)
        {
    	ModelAndView mv=new ModelAndView("tables/display/displayOGN.html");
		 mv.addObject("ogns",ognr.findByKeyword(keyword));
    		return mv;
    		}
    
    @GetMapping("/searchServiceitem")
    public ModelAndView searchServiceitem(Serviceitem ser,Model model, String keyword)
        {
    	ModelAndView mv=new ModelAndView("tables/display/displayServiceitem.html");
		 mv.addObject("sis",serr.findByKeyword(keyword));
    		return mv;
    		}
    @GetMapping("/searchNumberRange")
    public ModelAndView searchNumberRange(Numberrange nr,Model model, String keyword)
        {
    	ModelAndView mv=new ModelAndView("tables/display/displayNumberRange.html");
		 mv.addObject("nrs",nrr.findByKeyword(keyword));
    		return mv;
    		}
    @GetMapping("/searchCustomer")
    public ModelAndView searchCustomer(Customer cus,Model model, String keyword)
        {
    	ModelAndView mv=new ModelAndView("tables/display/displayCustomer.html");
		 mv.addObject("cuss",cusr.findByKeyword(keyword));
    		return mv;
    		}
    @GetMapping("/searchCaval")
    public ModelAndView searchCaval(Caval caval,Model model, String keyword)
        {
    	ModelAndView mv=new ModelAndView("tables/display/displayCaval.html");
		 mv.addObject("cavals",cavalr.findByKeyword(keyword));
    		return mv;
    		}
    @GetMapping("/searchCoac")
    public ModelAndView searchCoac(Coac Coac,Model model, String keyword)
        {
    	ModelAndView mv=new ModelAndView("tables/display/displayCoac.html");
		 mv.addObject("Coacs",coacr.findByKeyword(keyword));
    		return mv;
    		}
    
    @GetMapping("/searchCc")
    public ModelAndView searchCc(Costcenter Cc,Model model, String keyword)
        {
    	ModelAndView mv=new ModelAndView("tables/display/displayCostcenter.html");
		 mv.addObject("ccs",ccr.findByKeyword(keyword));
    		return mv;
    		}
    
    @GetMapping("/searchCcgroup")
    public ModelAndView searchCc(Ccgroup Ccg,Model model, String keyword)
        {
    	ModelAndView mv=new ModelAndView("tables/display/displayCcgroup.html");
		 mv.addObject("ccgs",ccgr.findByKeyword(keyword));
    		return mv;
    		}
    @GetMapping("/searchCagroup")
    public ModelAndView searchCc(Cagroup Cag,Model model, String keyword)
        {
    	ModelAndView mv=new ModelAndView("tables/display/displayCagroup.html");
		 mv.addObject("cags",cagr.findByKeyword(keyword));
    		return mv;
    		}
    
    @GetMapping("/searchProfitcenter")
    public ModelAndView searchCc(Profitcenter pc,Model model, String keyword)
        {
    	ModelAndView mv=new ModelAndView("tables/display/displayProfitcenter.html");
		 mv.addObject("pcs",pcr.findByKeyword(keyword));
    		return mv;
    		}
    
    @GetMapping("/searchPGN")
    public ModelAndView searchPGN(Plantgovtnum pgn,Model model, String keyword)
        {
    	ModelAndView mv=new ModelAndView("tables/display/displayPGN.html");
		 mv.addObject("pgns",pgnr.findByKeyword(keyword));
    		return mv;
    		}
    
    @GetMapping("/searchPcorgmap")
    public ModelAndView searchPcorgmap(Profitcenterorgmap pcom,Model model, String keyword)
        {
    	ModelAndView mv=new ModelAndView("tables/display/displayPcorgmap.html");
		 mv.addObject("pcoms",pcomr.findByKeyword(keyword));
    		return mv;
    		}
    
    @GetMapping("/searchCurrency")
    public ModelAndView searchCurrency(Currency cur,Model model, String keyword)
        {
    	ModelAndView mv=new ModelAndView("tables/display/displayCurrency.html");
		 mv.addObject("currs",cr.findByKeyword(keyword));
    		return mv;
    		}
    @GetMapping("/searchCountry")
    public ModelAndView searchCountry(Country cun,Model model, String keyword)
        {
    	ModelAndView mv=new ModelAndView("tables/display/displayCountry.html");
		 mv.addObject("countries",cunr.findByKeyword(keyword));
    		return mv;
    		}
    @GetMapping("/searchFydPeriod")
    public ModelAndView searchFydPeriod(Fydperiod fydp,Model model, String keyword)
        {
    	ModelAndView mv=new ModelAndView("tables/display/displayFydperiod.html");
		 mv.addObject("fydps",fpr.findByKeyword(keyword));
    		return mv;
    		}
    @GetMapping("/searchPcb")
    public ModelAndView searchPcb(Pcb pcb,Model model, String keyword)
        {
    	ModelAndView mv=new ModelAndView("tables/display/displayPcb.html");
		 mv.addObject("pcbs",pcbr.findByKeyword(keyword));
    		return mv;
    		}
    
    @GetMapping("/searchAssignccb")
    public ModelAndView searchAssignccb(Assignccb accb,Model model, String keyword)
        {
    	ModelAndView mv=new ModelAndView("tables/display/displayAssignccb.html");
		 mv.addObject("accbs",accbr.findByKeyword(keyword));
    		return mv;
    		}
    
    
}
