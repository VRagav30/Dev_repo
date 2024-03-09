package com.cop.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cop.model.database.Costtotal;
import com.cop.model.database.Mmtotal;
import com.cop.model.database.Orderheader;
import com.cop.repository.transaction.CostTotalRepository;
import com.cop.repository.transaction.MMTotalRespository;
import com.cop.repository.transaction.OrderHeaderRepository;
import com.cop.utilities.file.ExcelWriter;

@Controller
public class ExcelDownloadController {

	@Autowired
	OrderHeaderRepository ohr;
	@Autowired
	CostTotalRepository ctr;
	@Autowired
	MMTotalRespository mtr;

	@RequestMapping(method = RequestMethod.GET, value = "/downloadExcelFile")
	public void WriteToExcelInMultiSheets() {
		String path = "D:/Transaction Records" + System.currentTimeMillis() + ".xlsx";
		List<Orderheader> ohList = (List<Orderheader>) ohr.findAll();
		List<Costtotal> ctList = ctr.findAll();
		List<Mmtotal> mtList = mtr.findAll();
		ExcelWriter.writeToExcelInMultiSheets(path, "OrderHeader", ohList);
		ExcelWriter.writeToExcelInMultiSheets(path, "CostTotal", ctList);
		ExcelWriter.writeToExcelInMultiSheets(path, "MMtotal", mtList);

	}

}
