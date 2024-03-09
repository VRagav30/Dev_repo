package com.cop.model.database;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.cop.model.converter.BigDecimalConverter;
import com.cop.model.converter.DateConverter;
import com.cop.model.converter.StringConverter;
import com.creditdatamw.zerocell.annotation.Column;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Transaction implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(index = 0, name = "DATE", converterClass = DateConverter.class)
	private Date date;

	@Column(index = 1, name = "PLANT", converterClass = StringConverter.class)
	private String plant;

	@Column(index = 2, name = "DOCUMENTDESCRIPTION", converterClass = StringConverter.class)
	private String docdesc;

	@Column(index = 3, name = "REFRENCEDOCNUMBER", converterClass = BigDecimalConverter.class)
	private BigDecimal refdocnum;

	@Column(index = 4, name = "COBJTYPE", converterClass = StringConverter.class)
	private String cobjtype;

	@Column(index = 5, name = "OBJCODE", converterClass = StringConverter.class)
	private String objcode;

	@Column(index = 6, name = "ITEMCODE", converterClass = StringConverter.class)
	private String itemcode;
	
	@Column(index = 7, name = "ITEMTYPE", converterClass = StringConverter.class)
	private String itemtype;

	@Column(index = 8, name = "VENDOR", converterClass = StringConverter.class)
	private String vendor;

	@Column(index = 9, name = "OBJITNUM", converterClass = BigDecimalConverter.class)
	private BigDecimal objitnum;

	@Column(index = 10, name = "DOCQTY", converterClass = BigDecimalConverter.class)
	private BigDecimal docqty;

	@Column(index = 11, name = "DOCUOM", converterClass = StringConverter.class)
	private String docuom;

	@Column(index = 12, name = "BUOM", converterClass = StringConverter.class)
	private String buom;

	@Column(index = 13, name = "BUOMQTY", converterClass = BigDecimalConverter.class)
	private BigDecimal buomqty;

	@Column(index = 14, name = "ORDCURRENCY", converterClass = StringConverter.class)
	private String ordcurrency;

	@Column(index = 15, name = "ORDAMOUNT", converterClass = BigDecimalConverter.class)
	private BigDecimal ordamount;

	@Column(index = 16, name = "AMOUNTINCURRO", converterClass = BigDecimalConverter.class)
	private BigDecimal amountincurro;

	@Column(index = 17, name = "PRTNRCODE", converterClass = StringConverter.class)
	private String prtnrcode;

	@Column(index = 18, name = "TRNSEVENT", converterClass = StringConverter.class)
	private String trnsevent;

	@Column(index = 19, name = "COSTACC", converterClass = BigDecimalConverter.class)
	private BigDecimal costacc;

	@Column(index = 20, name = "REVERSALINDICATOR", converterClass = StringConverter.class)
	private String reversalindicator;

	@Column(index = 21, name = "REVERSALDOCUMENT", converterClass = BigDecimalConverter.class)
	private BigDecimal reversaldocument;

	@Column(index = 22, name = "BATCHNUMBER", converterClass = StringConverter.class)
	private String batchnumber;

	@Column(index = 23, name = "VALSUB", converterClass = StringConverter.class)
	private String valsub;

	@Column(index = 24, name = "SALEORDER", converterClass = StringConverter.class)
	private String saleorder;
	
	@Column(index=25, name="SALEORDERITEM", converterClass=StringConverter.class)
	private String saleorderitem;

	@Column(index = 26, name = "STOCKTYPE", converterClass = StringConverter.class)
	private String stocktype;

	@Column(index = 27, name = "SSTYPE", converterClass = StringConverter.class)
	private String sstype;
	
	@Column(index = 28, name = "PLNUM", converterClass = BigDecimalConverter.class)
	private BigDecimal plnum;
	
	@Column(index = 29, name = "PLCOMP", converterClass = StringConverter.class)
	private String plcomp;
	
	@Column (index=30, name="LINKEDOBJCODE", converterClass = StringConverter.class)
	private String linkedobjcode;
	
	@Column (index=31, name="LINKEDOBJITNUM", converterClass = BigDecimalConverter.class)
	private BigDecimal linkedobjitnum;
	
	@Column (index=32, name="EXCHANGERATE", converterClass = BigDecimalConverter.class)
	private BigDecimal exchangerate;
	
	@Column (index=33, name="SBATCH", converterClass = StringConverter.class)
	private String sbatch;
	
	@Column (index=34, name="SMATERIAL", converterClass = StringConverter.class)
	private String smaterial;
	
	@Column (index=35, name="SVALSUB", converterClass = StringConverter.class)
	private String svalsub;
	
	@Column (index=36, name="SPLANT", converterClass = StringConverter.class)
	private String splant;
	
	@Column (index=37, name="SVENDOR", converterClass = StringConverter.class)
	private String svendor;
	
	@Column (index=38, name="SSO", converterClass = StringConverter.class)
	private String sso;
	
	@Column(index = 39, name = "TSTYPE", converterClass = StringConverter.class)
	private String tstype;
	
	
	@Column (index=40, name="TBATCH", converterClass = StringConverter.class)
	private String tbatch;
	
	@Column (index=41, name="TMATERIAL", converterClass = StringConverter.class)
	private String tmaterial;
	
	@Column (index=42, name="TVALSUB", converterClass = StringConverter.class)
	private String tvalsub;
	
	@Column (index=43, name="TVENDOR", converterClass = StringConverter.class)
	private String tvendor;
	
	@Column (index=44, name="TPLANT", converterClass = StringConverter.class)
	private String tplant;
		
	
	@Column (index=45, name="TSO", converterClass = StringConverter.class)
	private String tso;

	
}