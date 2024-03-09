package com.cop.test.fileUploads;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.WebApplicationContext;

import com.cop.model.database.Costtotal;
import com.cop.serviceimpl.service.CostTotalServiceImpl;

//@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = MyWebConfig.class, initializers = { ConfigFileApplicationContextInitializer.class })

public class OrderUploadTest {

	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;
	File file;
	MockMultipartFile mockMulitpartFile;
	String fileName;
	String fileType;
	@Autowired
	CostTotalServiceImpl cti;

	@BeforeEach
	public void setup() {
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(wac);
		mockMvc = builder.build();
	}
	@Disabled
	@Test
	public void unitConvTest() {
		System.out.println(cti.unitConvertor("IN000002DOLV", "OXYGEN", "KGS", "M3", "LOT", new BigDecimal(1)));
	}

	
	@Test

	public void uploadOrderTransactions() throws Exception {
		String fileList[] = { 
				"ORDERS:0_0_Orders -ALL.xlsx"
				,"ORDERS:0_1_Orders.xlsx"
				,"ORDERS:0_2_Orders.xlsx"
				,"ORDERS:0_3_CNQR Orders .xlsx"
				,"ORDERS:0_4_CNQR Orders.xlsx"
				,"ORDERS:0_5_order_batch.xlsx"
				,"ORDERS:0_6_SALE Orders.xlsx"
				,"ORDERS:0_7_subcon_orders.xlsx"
				,"ORDERS:0_8_Order.xlsx"
				,"TRANSACTIONS:1_PUR GR.xlsx"
				, "TRANSACTIONS:2_PUR IB.xlsx"
				, "TRANSACTIONS:4_VS.xlsx"
				, "TRANSACTIONS:3_OS.xlsx"
				, "TRANSACTIONS:5_SUBCON_GI_PC_GR.xlsx"
				, "TRANSACTIONS:6_PROD_GI_OC_GR.xlsx"
				, "TRANSACTIONS:7_PUR SR.xlsx"
				, "TRANSACTIONS:8_STO.xlsx"
				, "TRANSACTIONS:9_SALE TRANSACTION.xlsx"
				//, "TRANSACTIONS:10_SUBCON_AUTOPOST_TRANSACTION.xlsx"
				, "TRANSACTIONS:11_PROD_AUTO_TRANSACTION.xlsx"
				, "TRANSACTIONS:12_STO-PR.xlsx"
				, "TRANSACTIONS:13_STO-PR2.xlsx"
				, "TRANSACTIONS:14_STO-PR3.xlsx"
				, "TRANSACTIONS:15_PROD_BYPROD_Transaction.xlsx"
				, "TRANSACTIONS:16_PRIB_Transaction.xlsx"
				, "TRANSACTIONS:17_Rework_Transaction.xlsx"
				, "TRANSACTIONS:18_CTR_TRANS.xlsx"
				, "TRANSACTIONS:19_PROD_GI_OC_GR - WIP.xlsx"
				, "TRANSACTIONS:20_dqnr_TRNS.xlsx"
				, "TRANSACTIONS:21_dqnr_TRNS _S.xlsx"
				, "TRANSACTIONS:22_dqnr_TRNS _S2.xlsx"
				, "TRANSACTIONS:23_CNQR_TRNS.xlsx"
				, "TRANSACTIONS:24_CNQR_TRANS_S.xlsx"
				, "TRANSACTIONS:25_CNQR_TRANS_S2.xlsx"
				, "TRANSACTIONS:26_DNVR.xlsx"
				, "TRANSACTIONS:27_DNVR -S.xlsx"
				, "TRANSACTIONS:28_DNVR -S2.xlsx"
				, "TRANSACTIONS:29_dqnr_TRNS _S3.xlsx"
				, "TRANSACTIONS:30_CNVR.xlsx"
				, "TRANSACTIONS:31_CNVR_S.xlsx"
				, "TRANSACTIONS:32_CNVR-S2.xlsx"
				, "TRANSACTIONS:33_PUR_GR_ctr.xlsx"
				,"TRANSACTIONS:34_dqnr_Oxygen.xlsx"
				, "TRANSACTIONS:35_dqnr_OxygenLoading.xlsx"
				, "TRANSACTIONS:36_dqnr_OxygenLoading -Mmtotal.xlsx"
				, "TRANSACTIONS:37_Transfer.xlsx"	
				, "TRANSACTIONS:38_OR.xlsx"
				, "TRANSACTIONS:39_Transfer - OS.xlsx"
				, "TRANSACTIONS:40_BATCH_GR.xlsx"
				, "TRANSACTIONS:41_VS_batch.xlsx"
				, "TRANSACTIONS:42_SUBCON_GI_PC_GR -batch.xlsx"
				, "TRANSACTIONS:43_PROD_GI_OC_GR_batch.xlsx"
				, "TRANSACTIONS:44_Rework_Transaction -batch.xlsx"
				, "TRANSACTIONS:45_STO_batch.xlsx"
				,"TRANSACTIONS:46_OS_O_OV_S_SV.xlsx"
				,"TRANSACTIONS:48_OS_O_OV_S_SV.xlsx"
				,"TRANSACTIONS:49_subcon_vr_qr.xlsx"
				,"TRANSACTIONS:50_subcon_ib.xlsx"
				,"TRANSACTIONS:51_subcon_sr.xlsx"
				,"TRANSACTIONS:52_Rework_2.xlsx"
				,"TRANSACTIONS:53_prod.xlsx"
				,"TRANSACTIONS:54_TransferDeadLock.xlsx"
				,"TRANSACTIONS:55_IL.xlsx"
				,"TRANSACTIONS:56_SDT.xlsx"
				,"TRANSACTIONS:57_IG.xlsx"
				,"TRANSACTIONS:58_WIP-S.xlsx"
				,"TRANSACTIONS:59_WIP.xlsx"
				};
		for (String fileMetadata : fileList) {
			fileName = fileMetadata.split(":")[1];
			fileType = fileMetadata.split(":")[0];
			System.out.println("Testing File Upload : " + fileType + " : " + fileName + " START");
			file = ResourceUtils.getFile("classpath:testUploadFiles/" + fileName);
			mockMulitpartFile = new MockMultipartFile("file", fileName, "multipart/form-data",
					new FileInputStream(file));
			mockMvc.perform(MockMvcRequestBuilders.multipart("/uploadTransFile").file(mockMulitpartFile)
					.param("fileType", fileType).characterEncoding("UTF-8"))
					.andExpect(MockMvcResultMatchers.status().isOk());
			System.out.println("Testing File Upload : " + fileType + " : " + fileName + " END");
		}

	}
}
