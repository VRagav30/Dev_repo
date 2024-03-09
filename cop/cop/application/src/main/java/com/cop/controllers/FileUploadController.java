package com.cop.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.cop.model.database.CostingUpload;
import com.cop.model.database.Order;
import com.cop.model.database.Orderheader;
import com.cop.model.database.Transaction;
import com.cop.repository.transaction.OrderHeaderRepository;
import com.cop.serviceapi.service.OrderTransactionService;
import com.cop.serviceapi.service.costing.ActualTotalService;
import com.cop.serviceimpl.service.CostTotalServiceImpl;
import com.cop.serviceimpl.service.MMTotalServiceImpl;
import com.cop.utilities.file.ExcelReader;
import com.cop.utilities.file.ExcelUtils;

@Controller
public class FileUploadController {

	private static final String MESSAGE = "message";
	@Value("${order.header.columns}")
	private String OrderHeaderColumns;

	@Value("${cost.total.columns}")
	private String CostTotalColumns;

	@Value("${mm.total.columns}")
	private String MMTotalColumns;

	Orderheader oh;

	@Autowired
	OrderTransactionService ots;
	
	@Autowired
	ActualTotalService ats;

	@Autowired
	OrderHeaderRepository ohr;

	@Autowired
	CostTotalServiceImpl ctsi;

	@Autowired
	MMTotalServiceImpl mtsi;
	@Autowired
	ExcelReader er;

	//@GetMapping("/")
	public String uploadFile() {
		return "uploadTransactionFile";
	}

	@GetMapping("/uploadTransFile")
	public String uploadTransactionFiles() {
		return "uploadTransactionFile";

	}
	
	@GetMapping("/uploadActivityPrice")
	public String uploadActivityPrice() {
		return "uploadActivityPrice";

	}

	@PostMapping(value = "/uploadTransFile")
	public String uploadFile(@RequestParam("fileType") String fileType, @RequestParam("file") MultipartFile file,
			Model model) throws IOException {
		File tempFile = null;

		System.out.println("fileType : " + fileType);
		System.out.println("from properties :" + OrderHeaderColumns);

		switch (fileType.toUpperCase()) {
		case "ORDERS":
			tempFile = convert(file);
			System.out.println(tempFile.getAbsoluteFile());
			List<Order> orders = ExcelUtils.readFile(new Order(), tempFile);
			tempFile.delete();
			System.out.println(orders.size());
			System.out.println("Processing Order Header");
			ots.createAndSaveOrders(orders);
			model.addAttribute(MESSAGE, "File Uploaded Successfully!");
			break;
		case "TRANSACTIONS":
			tempFile = convert(file);
			List<Transaction> transactions = ExcelUtils.readFile(new Transaction(), tempFile);
			tempFile.delete();
			System.out.println("Processing Transaction Upload");
			System.out.println("from properties :" + CostTotalColumns);
			ots.createAndSaveOrderTransactions(transactions);
			model.addAttribute(MESSAGE, "File Uploaded Successfully!");

			break;
			
		case "COSTING":
			tempFile = convert(file);
			List<CostingUpload> cuTransactions = ExcelUtils.readFile(new CostingUpload(), tempFile);
			tempFile.delete();
			System.out.println("Processing Actual Total");
			//System.out.println("from properties :" + CostTotalColumns);
			ats.performActualCosting(cuTransactions);
			model.addAttribute(MESSAGE, "File Uploaded Successfully!");

			break;
		/*
		 * case "MM TOTAL": System.out.println("procesing MM total");
		 * System.out.println("from properties:"+ MMTotalColumns); List<String>
		 * MMheaders = er.readExcelHeader(file.getInputStream());
		 * System.out.println(String.join(",", MMheaders));
		 * if(MMTotalColumns.equalsIgnoreCase(String.join(",", MMheaders))) {
		 * List<Map<String, Object>> records =
		 * er.readExcelRecords(file.getInputStream(),MMheaders);
		 * System.out.println("Total records detected : "+records.size());
		 * model.addAttribute(MESSAGE, "File Uploaded Successfully!");
		 * mtsi.createAndSaveMmTotal(records); } else {
		 * model.addAttribute(MESSAGE,"The Headers dont match, file not uploaded" ); }
		 * break;
		 */
		default:
			model.addAttribute(MESSAGE, "The Headers dont match, file not uploaded");
		}

		return "uploadTransactionFile";

	}
	
	@PostMapping(value = "/uploadActivityPrice")
	public ModelAndView uploadActivityPrice(@RequestParam("fileType") String fileType, @RequestParam("file") MultipartFile file,
			Model model) throws IOException {
		ModelAndView mv = new ModelAndView("uploadActivityPrice");
		File tempFile = null;

		System.out.println("fileType : " + fileType);
		System.out.println("from properties :" + OrderHeaderColumns);

		switch (fileType.toUpperCase()) {
		case "TRANSACTIONS":
			tempFile = convert(file);
			List<Transaction> transactions = ExcelUtils.readFile(new Transaction(), tempFile);
			tempFile.delete();
			System.out.println("Processing Transaction Upload");
			System.out.println("from properties :" + CostTotalColumns);
			ots.createAndSaveOrderTransactions(transactions);
			model.addAttribute(MESSAGE, "File Uploaded Successfully!");
			
			//mv.addObject("dResults", mr.generateDetailedReport(Arrays.asList(plant.split(",")), new BigDecimal(2020),
			//		new BigDecimal(1), new BigDecimal(12)));
			break;
		default:
			model.addAttribute(MESSAGE, "The Headers dont match, file not uploaded");
		}

		return mv;

	}

	public File convert(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}
}