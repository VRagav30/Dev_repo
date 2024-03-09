package com.cop.controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cop.repository.transaction.MMTotalRespository;
import com.cop.repository.transaction.MaterialRepository;
import com.cop.repository.transaction.OrderHeaderRepository;
import com.cop.repository.transaction.OrganisationRepository;
import com.cop.repository.transaction.PlantRepository;

@Controller
public class GenerateCostingController {


	@Autowired
	OrganisationRepository orgr;
	
	@Autowired
	MMTotalRespository mtr;

	
	@RequestMapping("/generateCosting")
	public ModelAndView generateReport(@RequestParam(value = "org", required = false) String org,
			@RequestParam(value = "plant", required = false) String plant,
			@RequestParam(value="period", required=false) BigDecimal period,
			@RequestParam(value="year", required=false) BigDecimal year,Model model) {
		ModelAndView mv = new ModelAndView("generateCosting");
		mv.addObject("orgs", orgr.findAll());
		try
		{
		mtr.generateall_periodicCosting(org, year, period);
		}
		catch (Exception e) {
		
		System.out.println(e.getMessage());}
		mv.addObject("success", "Costing Done Sucessfully!");
		return mv;

	}
	
}
