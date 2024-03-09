package com.cop.model.database;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.cop.model.generator.StringPrefixedSequenceIdGenerator;

import lombok.Getter;
import lombok.Setter;

/**
 * The persistent class for the costtotal database table.
 *
 */
@Entity
@Getter
@Setter
@NamedQuery(name = "Costtotal.findAll", query = "SELECT c FROM Costtotal c")
@NamedQuery(name = "Costtotal.findAllbyRefdocnum", query = "SELECT c FROM  Costtotal c where c.docnumber = :refdocnum")
@NamedQuery(name = "Costtotal.findOrderAmountbytrnsevent", query = "select c.ordamount from Costtotal c where c.cobjtype=:cobjtype and c.objcode=:objcode and c.trnsevent=:trnsevent order by c.costtotalid desc")

@NamedQuery(name = "Costtotal.findsumofOrdamountbytrnsevent", query = "select SUM(c.ordamount) from Costtotal c where upper(c.cobjtype)=upper(:cobjtype) and c.objcode=:objcode and upper(c.trnsevent)=(:trnsevent)")

@NamedQuery(name = "Costtotal.findAllBytrnsevent", query = "select c from Costtotal c where upper(c.plant)=upper(:plant) and upper(c.itemcode)=upper(:itemcode) and upper(c.trnsevent)=upper(:trnsevent) and c.date<=:date order by c.costtotalid desc")

@NamedQuery(name = "Costtotal.findbyObjcode", query = "select c from Costtotal c where c.objcode=:objcode  and c.objitnum=:objitnum  and c.trnsevent=:trnsevent order by c.costtotalid desc")
public class Costtotal implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "costtotal_seq")
	@GenericGenerator(name = "costtotal_seq", strategy = "com.cop.model.generator.StringPrefixedSequenceIdGenerator", parameters = {
			@Parameter(name = StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "1"),
			@Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "COST"),
			@Parameter(name = StringPrefixedSequenceIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%09d") })
	private String costtotalid;

	private BigDecimal amountincurro;

	private String batchnumber;

	private String buom;

	private BigDecimal buomqty;

	private String cobjtype;

	private BigDecimal costacc;

	private String customer;

	@Temporal(TemporalType.DATE)
	private Date date;

	private Timestamp createdtime;

	private String docdesc;

	private BigDecimal docnumber;

	private BigDecimal docitnum;

	private BigDecimal docqty;

	private String docuom;

	private String itemtype;

	private String itemcode;

	private String objcode;

	private BigDecimal objitnum;

	private BigDecimal ordamount;

	private String ordcurrency;

	private String orgncode;

	private BigDecimal period;

	private String plant;

	private String prtnrcode;

	private BigDecimal refdocnum;

	private BigDecimal reversaldocument;

	private String reversalindicator;

	private String rplant;

	private String saleorder;

	private String stocktype;

	private String trnsevent;

	private String usercode;

	private String valsub;

	private String vendor;

	private BigDecimal year;

	private String linkedobjcode;

	private BigDecimal linkedobjitnum;

	private BigDecimal plnum;

	private String plcomp;

	private BigDecimal exchangerate;

	private BigDecimal exchangeratediff;

}