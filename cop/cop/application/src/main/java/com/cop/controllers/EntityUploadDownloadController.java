package com.cop.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.cop.model.database.Batch;
import com.cop.model.database.Bom;
import com.cop.model.database.Costcenter;
import com.cop.model.database.Material;
import com.cop.model.database.Mrc;
import com.cop.model.database.Order;
import com.cop.model.database.Prcsstep;
import com.cop.model.database.Prcsstepops;
import com.cop.model.database.Processcard;
import com.cop.model.database.Profitcenter;
import com.cop.repository.transaction.BatchRepository;
import com.cop.repository.transaction.BomRepository;
import com.cop.repository.transaction.CostCenterRepository;
import com.cop.repository.transaction.MaterialRepository;
import com.cop.repository.transaction.MrcRepository;
import com.cop.repository.transaction.PcardRepository;
import com.cop.repository.transaction.PrcsstepRepository;
import com.cop.repository.transaction.PrcsstepopsRepository;
import com.cop.repository.transaction.ProfitcenterRepository;
import com.cop.utilities.file.ExcelUtils;



@Controller
public class EntityUploadDownloadController {
	private static final String MESSAGE = "message";

	@Autowired
	BatchRepository batr;
	@Autowired
	MaterialRepository matr;
	@Autowired
	MrcRepository mrcr;
	@Autowired
	ProfitcenterRepository pcr;
	@Autowired
	CostCenterRepository ccr;
	@Autowired
	BomRepository bomr;
	@Autowired
	PcardRepository pcar;
	@Autowired
	PrcsstepRepository pstepr;
	@Autowired
	PrcsstepopsRepository psor;
	
private static final String EXTENSION = ".xlsx";
private static final String SERVER_LOCATION = "F:\\CoP\\Workspace\\cop\\cop\\cop\\application\\src\\main\\resources\\assests\\tables";

@GetMapping("/downloadBatch")
public ResponseEntity<Resource> download() throws IOException {
	System.out.println("Hi");
    File file = new File(SERVER_LOCATION + File.separator + "Batch" + EXTENSION);

    HttpHeaders header = new HttpHeaders();
    header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=batch.xlsx");
    header.add("Cache-Control", "no-cache, no-store, must-revalidate");
    header.add("Pragma", "no-cache");
    header.add("Expires", "0");

    Path path = Paths.get(file.getAbsolutePath());
    ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

    return ResponseEntity.ok()
            .headers(header)
            .contentLength(file.length())
            .contentType(MediaType.parseMediaType("application/octet-stream"))
            .body(resource);
}

@PostMapping(value = "/uploadEntityFile")
public ModelAndView uploadFile(@RequestParam("fileType") String fileType, @RequestParam("file") MultipartFile file,
		Model model) throws IOException {
	ModelAndView mv = new ModelAndView("admin-home.html");
	File tempFile = null;

	System.out.println("fileType : " + fileType);

	switch (fileType.toUpperCase()) {
	case "BATCH":
		tempFile = convert(file);
		System.out.println(tempFile.getAbsoluteFile());
		List<Batch> bats = ExcelUtils.readFile(new Batch(), tempFile);
		tempFile.delete();
		System.out.println(bats.size());
		System.out.println("Processing Batch");
		batr.saveAll(bats);
		model.addAttribute(MESSAGE, "File Uploaded Successfully!");
		break;
	case "MATERIAL":
		tempFile = convert(file);
		System.out.println(tempFile.getAbsoluteFile());
		List<Material> mats = ExcelUtils.readFile(new Material(), tempFile);
		tempFile.delete();
		System.out.println(mats.size());
		System.out.println("Processing material");
		matr.saveAll(mats);
		model.addAttribute(MESSAGE, "File Uploaded Successfully!");
		break;
	case "MRC":
		tempFile = convert(file);
		System.out.println(tempFile.getAbsoluteFile());
		List<Mrc> mrcs = ExcelUtils.readFile(new Mrc(), tempFile);
		tempFile.delete();
		System.out.println(mrcs.size());
		System.out.println("Processing Mrc");
		mrcr.saveAll(mrcs);
		model.addAttribute(MESSAGE, "File Uploaded Successfully!");
		break;
		
	case "PROFITCENTER":
		tempFile = convert(file);
		System.out.println(tempFile.getAbsoluteFile());
		List<Profitcenter> pcs = ExcelUtils.readFile(new Profitcenter(), tempFile);
		tempFile.delete();
		System.out.println(pcs.size());
		System.out.println("Processing ProfitCenter");
		pcr.saveAll(pcs);
		model.addAttribute(MESSAGE, "File Uploaded Successfully!");
		break;
		
	case "COSTCENTER":
		tempFile = convert(file);
		System.out.println(tempFile.getAbsoluteFile());
		List<Costcenter> ccs = ExcelUtils.readFile(new Costcenter(), tempFile);
		tempFile.delete();
		System.out.println(ccs.size());
		System.out.println("Processing CostCenter");
		ccr.saveAll(ccs);
		model.addAttribute(MESSAGE, "File Uploaded Successfully!");
		break;
		
	case "BOM":
		tempFile = convert(file);
		System.out.println(tempFile.getAbsoluteFile());
		List<Bom> boms = ExcelUtils.readFile(new Bom(), tempFile);
		tempFile.delete();
		System.out.println(boms.size());
		System.out.println("Processing BOM");
		bomr.saveAll(boms);
		model.addAttribute(MESSAGE, "File Uploaded Successfully!");
		break;
	case "PROCESSSTEP":
		tempFile = convert(file);
		System.out.println(tempFile.getAbsoluteFile());
		List<Prcsstep> psteps = ExcelUtils.readFile(new Prcsstep(), tempFile);
		tempFile.delete();
		System.out.println(psteps.size());
		System.out.println("Processing PSTEP");
		pstepr.saveAll(psteps);
		model.addAttribute(MESSAGE, "File Uploaded Successfully!");
		break;
	case "PROCESSCARD":
		tempFile = convert(file);
		System.out.println(tempFile.getAbsoluteFile());
		List<Processcard> pcards = ExcelUtils.readFile(new Processcard(), tempFile);
		tempFile.delete();
		System.out.println(pcards.size());
		System.out.println("Processing PSTEP");
		pcar.saveAll(pcards);
		model.addAttribute(MESSAGE, "File Uploaded Successfully!");
		break;
		
	case "PROCESSSTEPOPS":
		tempFile = convert(file);
		System.out.println(tempFile.getAbsoluteFile());
		List<Prcsstepops> psops = ExcelUtils.readFile(new Prcsstepops(), tempFile);
		tempFile.delete();
		System.out.println(psops.size());
		System.out.println("Processing PSTEP");
		psor.saveAll(psops);
		model.addAttribute(MESSAGE, "File Uploaded Successfully!");
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

