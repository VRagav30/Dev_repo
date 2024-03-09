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
 * The persistent class for the orderheader database table.
 *
 */
@Entity
@Getter
@Setter
@NamedQuery(name = "Orderheader.findAll", query = "SELECT o FROM Orderheader o")
@NamedQuery(name = "Orderheader.findAllByOrgncode", query = "SELECT o FROM Orderheader o where o.orgncode=:orgncode")
@NamedQuery(name = "Orderheader.findAllByOrdernum", query = "SELECT o FROM Orderheader o where o.objcode=:objcode and o.objitnum=:objitnum")
@NamedQuery(name="Orderheader.findAllByLinkedobjcode", query="SELECT o FROM Orderheader o where upper(o.linkedobjcode)=upper(:linkedobjcode)")
//@NamedQuery(name="Orderheader.findAllBysoassignment", query="SELECT o FROM Orderheader o where upper(o.soassignment)=upper(:soassignment)")
public class Orderheader implements Serializable {
	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.DATE)
	private Date acdate;

	@Temporal(TemporalType.DATE)
	private Date asdate;

	private String bomcode;

	private String buom;

	private BigDecimal copratio;

	private String nocomp;

	private String autopost;

	private String createdby;

	private Timestamp createdtime;

	private String curro;

	private String customer;

	private BigDecimal hmratio;

	private String itemcode;

	private String modifiedby;

	private Timestamp modifiedtime;

	private BigDecimal objitnum;

	private String ordcurrency;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderheader_seq")
	@GenericGenerator(name = "orderheader_seq", strategy = "com.cop.model.generator.StringPrefixedSequenceIdGenerator", parameters = {
			@Parameter(name = StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "1"),
			@Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "OHD"),
			@Parameter(name = StringPrefixedSequenceIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%09d") })
	private String orderheadercode;

	private String objcode;

	private String objtype;

	private BigDecimal ordgrqty;

	private String ordgruom;

	private BigDecimal ordmqty;

	private String ordsts;

	private String orgncode;

	@Temporal(TemporalType.DATE)
	private Date planedate;

	@Temporal(TemporalType.DATE)
	private Date planfdate;

	private String plant;

	private String profitcr;

	private String prscnum;

	private BigDecimal groumrate;

	private String rplant;

	private String soassignment;

	private BigDecimal totcost;

	private BigDecimal totgramount;

	private String varcal;

	private String vendor;

	private BigDecimal wipamount;

	private String costcenter;
	
	private BigDecimal plqty;

	private BigDecimal curroamount;
	
	private String itemtype;
	
	private String linkedobjcode;
	
	private BigDecimal linkedobjitnum;
	
	private String valsub;
	
	private BigDecimal exchangerate;
	
	private String batchnumber;

	public Orderheader() {
	}

	@Override
	public String toString() {
		return "Orderheader [acdate=" + acdate + ", asdate=" + asdate + ", bomcode=" + bomcode + ", buom=" + buom
				+ ", copratio=" + copratio + ", nocomp=" + nocomp + ", autopost=" + autopost + ", createdby="
				+ createdby + ", createdtime=" + createdtime + ", curro=" + curro + ", customer=" + customer
				+ ", hmratio=" + hmratio + ", itemcode=" + itemcode + ", modifiedby=" + modifiedby + ", modifiedtime="
				+ modifiedtime + ", objitnum=" + objitnum + ", ordcurrency=" + ordcurrency + ", orderheadercode="
				+ orderheadercode + ", objcode=" + objcode + ", objtype=" + objtype + ", ordgrqty=" + ordgrqty
				+ ", ordgruom=" + ordgruom + ", ordmqty=" + ordmqty + ", ordsts=" + ordsts + ", orgncode=" + orgncode
				+ ", planedate=" + planedate + ", planfdate=" + planfdate + ", plant=" + plant + ", profitcr="
				+ profitcr + ", prscnum=" + prscnum + ", groumrate=" + groumrate + ", rplant=" + rplant
				+ ", soassignment=" + soassignment + ", totcost=" + totcost + ", totgramount=" + totgramount
				+ ", varcal=" + varcal + ", vendor=" + vendor + ", wipamount=" + wipamount + ", costcenter="
				+ costcenter + ", plqty=" + plqty + ", curroamount=" + curroamount + ", itemtype=" + itemtype
				+ ", linkedobjcode=" + linkedobjcode + ", linkedobjitnum=" + linkedobjitnum + ", valsub=" + valsub
				+ ", exchangerate=" + exchangerate + ", batchnumber=" + batchnumber + "]";
	}

	
}