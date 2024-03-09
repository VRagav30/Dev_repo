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
public class CostingUpload implements Serializable {
	
	@Column(index = 0, name = "DOCNUM", converterClass = BigDecimalConverter.class)
	private BigDecimal docnum;
	
	@Column(index = 1, name = "DOCITNUM", converterClass = BigDecimalConverter.class)
	private BigDecimal docitnum;
	
	@Column(index = 2, name = "AMOUNTINCURRO", converterClass = BigDecimalConverter.class)
	private BigDecimal amountincurro;
	
	@Column(index = 3, name = "QUANTITYTYPE", converterClass = StringConverter.class)
	private String quantitytype;
	
	@Column(index = 4, name = "PCBCODE1", converterClass = BigDecimalConverter.class)
	private BigDecimal pcbcode1;
	
	@Column(index = 5, name = "PCBCODE2", converterClass = BigDecimalConverter.class)
	private BigDecimal pcbcode2;
	
	@Column(index = 6, name = "PCBCODE3", converterClass = BigDecimalConverter.class)
	private BigDecimal pcbcode3;
	
	@Column(index = 7, name = "PCBCODE4", converterClass = BigDecimalConverter.class)
	private BigDecimal pcbcode4;
	
	@Column(index = 8, name = "PCBCODE5", converterClass = BigDecimalConverter.class)
	private BigDecimal pcbcode5;
	
	@Column(index = 9, name = "PCBCODE6", converterClass = BigDecimalConverter.class)
	private BigDecimal pcbcode6;
	
	@Column(index = 10, name = "PCBCODE7", converterClass = BigDecimalConverter.class)
	private BigDecimal pcbcode7;
	
	@Column(index = 11, name = "PCBCODE8", converterClass = BigDecimalConverter.class)
	private BigDecimal pcbcode8;
	
	@Column(index = 12, name = "PCBCODE9", converterClass = BigDecimalConverter.class)
	private BigDecimal pcbcode9;
	
	@Column(index = 13, name = "PCBCODE10", converterClass = BigDecimalConverter.class)
	private BigDecimal pcbcode10;
	
	@Column(index = 14, name = "PCBCODE11", converterClass = BigDecimalConverter.class)
	private BigDecimal pcbcode11;
	
	@Column(index = 15, name = "PCBCODE12", converterClass = BigDecimalConverter.class)
	private BigDecimal pcbcode12;
	
	@Column(index = 16, name = "PCBCODE13", converterClass = BigDecimalConverter.class)
	private BigDecimal pcbcode13;
	
	@Column(index = 17, name = "PCBCODE14", converterClass = BigDecimalConverter.class)
	private BigDecimal pcbcode14;
	@Column(index = 18, name = "PCBCODE15", converterClass = BigDecimalConverter.class)
	private BigDecimal pcbcode15;
	@Column(index = 19, name = "PCBCODE16", converterClass = BigDecimalConverter.class)
	private BigDecimal pcbcode16;
	@Column(index = 20, name = "PCBCODE17", converterClass = BigDecimalConverter.class)
	private BigDecimal pcbcode17;
	@Column(index = 21, name = "PCBCODE18", converterClass = BigDecimalConverter.class)
	private BigDecimal pcbcode18;
	@Column(index = 22, name = "PCBCODE19", converterClass = BigDecimalConverter.class)
	private BigDecimal pcbcode19;
	@Column(index = 23, name = "PCBCODE20", converterClass = BigDecimalConverter.class)
	private BigDecimal pcbcode20;
	@Column(index=24, name="TOTALCOST", converterClass=BigDecimalConverter.class)
	private BigDecimal totalcost;
	
	
	
}
