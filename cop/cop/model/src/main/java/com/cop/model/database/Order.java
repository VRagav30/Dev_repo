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
public class Order implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(index = 0, name = "ORGNCODE", converterClass = StringConverter.class)
	private String orgncode;

	@Column(index = 1, name = "OBJCODE", converterClass = BigDecimalConverter.class)
	private BigDecimal objcode;

	@Column(index = 2, name = "OBJTYPE", converterClass = StringConverter.class)
	private String objtype;

	@Column(index = 3, name = "PLANT", converterClass = StringConverter.class)
	private String plant;

	@Column(index = 4, name = "RPLANT", converterClass = StringConverter.class)
	private String rplant;

	@Column(index = 5, name = "ITEMCODE", converterClass = StringConverter.class)
	private String itemcode;
	
	@Column(index = 6, name = "ITEMTYPE", converterClass = StringConverter.class)
	private String itemtype;
	
	@Column(index = 7, name = "BUOM", converterClass = StringConverter.class)
	private String buom;

	@Column(index = 8, name = "PFTCENTC", converterClass = StringConverter.class)
	private String profitcr;

	@Column(index = 9, name = "BOMCODE", converterClass = StringConverter.class)
	private String bomcode;

	@Column(index = 10, name = "PRCSNUM", converterClass = StringConverter.class)
	private String prscnum;

	@Column(index = 11, name = "NOCOMP", converterClass = StringConverter.class)
	private String nocomp;

	@Column(index = 12, name = "AUTOPOST", converterClass = StringConverter.class)
	private String autopost;

	@Column(index = 13, name = "PLANFDATE", converterClass = DateConverter.class)
	private Date planfdate;

	@Column(index = 14, name = "PLANEDATE", converterClass = DateConverter.class)
	private Date planedate;

	@Column(index = 15, name = "ASDATE", converterClass = DateConverter.class)
	private Date asdate;

	@Column(index = 16, name = "ACDATE", converterClass = DateConverter.class)
	private Date acdate;

	@Column(index = 17, name = "VARCAL", converterClass = StringConverter.class)
	private String varcal;

	@Column(index = 18, name = "VENDOR", converterClass = StringConverter.class)
	private String vendor;

	@Column(index = 19, name = "CUSTOMER", converterClass = StringConverter.class)
	private String customer;

	@Column(index = 20, name = "ORDMQTY", converterClass = BigDecimalConverter.class)
	private BigDecimal ordmqty;

	@Column(index = 21, name = "ORDGRQTY", converterClass = BigDecimalConverter.class)
	private BigDecimal ordgrqty;

	@Column(index = 22, name = "ORDSTS", converterClass = StringConverter.class)
	private String ordsts;

	@Column(index = 23, name = "TOTCOST", converterClass = BigDecimalConverter.class)
	private BigDecimal totcost;

	@Column(index = 24, name = "TOTGRAMOUNT", converterClass = BigDecimalConverter.class)
	private BigDecimal totgramount;

	@Column(index = 25, name = "WIPAMOUNT", converterClass = BigDecimalConverter.class)
	private BigDecimal wipamount;

	@Column(index = 26, name = "OBJITNUM", converterClass = BigDecimalConverter.class)
	private BigDecimal objitnum;

	@Column(index = 27, name = "ORDGRUOM", converterClass = StringConverter.class)
	private String ordgruom;

	@Column(index = 28, name = "GRUOMRATE", converterClass = BigDecimalConverter.class)
	private BigDecimal groumrate;

	@Column(index = 29, name = "ORDCURRENCY", converterClass = StringConverter.class)
	private String ordcurrency;

	@Column(index = 30, name = "CURRO", converterClass = StringConverter.class)
	private String curro;

	@Column(index = 31, name = "HMRATIO", converterClass = BigDecimalConverter.class)
	private BigDecimal hmratio;

	@Column(index = 32, name = "COPRATIO", converterClass = BigDecimalConverter.class)
	private BigDecimal copratio;

	@Column(index = 33, name = "SOASSIGNMENT", converterClass = StringConverter.class)
	private String soassignment;
	
	@Column(index = 34, name = "COSTCENTER", converterClass = StringConverter.class)
	private String costcenter;
	
	@Column (index=35, name="LINKEDOBJCODE", converterClass = StringConverter.class)
	private String linkedobjcode;
	
	@Column (index=36, name="LINKEDOBJITNUM", converterClass = BigDecimalConverter.class)
	private BigDecimal linkedobjitnum;
	
	@Column (index=37, name="PLQTY", converterClass = BigDecimalConverter.class)
	private BigDecimal plqty;
	
	@Column(index = 38, name = "VALSUB", converterClass = StringConverter.class)
	private String valsub;
	
	@Column (index=39, name="EXCHANGERATE", converterClass = BigDecimalConverter.class)
	private BigDecimal exchangerate;
	
	@Column(index = 40, name = "BATCHNUMBER", converterClass = StringConverter.class)
	private String batchnumber;
	public Order() {
	}

	@Override
	public String toString() {
		return "Order [orgncode=" + orgncode + ", objcode=" + objcode + ", objtype=" + objtype + ", plant=" + plant
				+ ", rplant=" + rplant + ", itemcode=" + itemcode + ", itemtype=" + itemtype + ", buom=" + buom
				+ ", profitcr=" + profitcr + ", bomcode=" + bomcode + ", prscnum=" + prscnum + ", nocomp=" + nocomp
				+ ", autopost=" + autopost + ", planfdate=" + planfdate + ", planedate=" + planedate + ", asdate="
				+ asdate + ", acdate=" + acdate + ", varcal=" + varcal + ", vendor=" + vendor + ", customer=" + customer
				+ ", ordmqty=" + ordmqty + ", ordgrqty=" + ordgrqty + ", ordsts=" + ordsts + ", totcost=" + totcost
				+ ", totgramount=" + totgramount + ", wipamount=" + wipamount + ", objitnum=" + objitnum + ", ordgruom="
				+ ordgruom + ", groumrate=" + groumrate + ", ordcurrency=" + ordcurrency + ", curro=" + curro
				+ ", hmratio=" + hmratio + ", copratio=" + copratio + ", soassignment=" + soassignment + ", costcenter="
				+ costcenter + ", linkedobjcode=" + linkedobjcode + ", linkedobjitnum=" + linkedobjitnum + ", plqty="
				+ plqty + ", valsub=" + valsub + ", exchangerate=" + exchangerate + ", batchnumber=" + batchnumber
				+ "]";
	}

}