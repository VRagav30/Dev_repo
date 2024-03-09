package com.cop.model.database;

import java.io.Serializable;
import javax.persistence.*;

import com.cop.model.converter.DateConverter;
import com.cop.model.converter.StringConverter;
import com.creditdatamw.zerocell.annotation.Column;

import java.util.Date;


/**
 * The persistent class for the costcenter database table.
 * 
 */
@Entity
@NamedQuery(name="Costcenter.findAll", query="SELECT c FROM Costcenter c where c.is_delete is null")
@NamedQuery(name="Costcenter.findOrgncode", query="SELECT c.orgncode FROM Costcenter c where upper(c.cctr)=upper(:cctr) and c.is_delete is null")
@NamedQuery(name="Costcenter.softdeleteCostcenter",query="update Costcenter  set is_delete=:is_delete where cctr=:cctr")
public class Costcenter implements Serializable {
	private static final long serialVersionUID = 1L;

@Id
@Column(index = 0, name = "cctr", converterClass = StringConverter.class)
	private String cctr;
@Column(index = 1, name = "sname", converterClass = StringConverter.class)
	private String sname;
@Column(index = 2, name = "lname", converterClass = StringConverter.class)
	private String lname;
@Column(index = 3, name = "valfdate", converterClass = DateConverter.class)
	@Temporal(TemporalType.DATE)
	private Date valfdate;
@Column(index = 4, name = "valtdate", converterClass = DateConverter.class)
	@Temporal(TemporalType.DATE)
	private Date valtdate;
@Column(index = 5, name = "ostat", converterClass = StringConverter.class)
	private String ostat;
@Column(index = 6, name = "orgncode", converterClass = StringConverter.class)	
	private String orgncode;
@Column(index = 7, name = "profitcr", converterClass = StringConverter.class)	
	private String profitcr;
@Column(index = 8, name = "plant", converterClass = StringConverter.class)
	private String plant;
@Column(index = 9, name = "function", converterClass = StringConverter.class)
	private String function;

	
	
	private String is_delete;
	
	

	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}


	

	public Costcenter() {
	}

	public String getCctr() {
		return this.cctr;
	}

	public void setCctr(String cctr) {
		this.cctr = cctr;
	}

	public String getFunction() {
		return this.function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public String getLname() {
		return this.lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getOrgncode() {
		return this.orgncode;
	}

	public void setOrgncode(String orgncode) {
		this.orgncode = orgncode;
	}

	public String getOstat() {
		return this.ostat;
	}

	public void setOstat(String ostat) {
		this.ostat = ostat;
	}

	public String getPlant() {
		return this.plant;
	}

	public void setPlant(String plant) {
		this.plant = plant;
	}

	public String getProfitcr() {
		return this.profitcr;
	}

	public void setProfitcr(String profitcr) {
		this.profitcr = profitcr;
	}

	public String getSname() {
		return this.sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public Date getValfdate() {
		return this.valfdate;
	}

	public void setValfdate(Date valfdate) {
		this.valfdate = valfdate;
	}

	public Date getValtdate() {
		return this.valtdate;
	}

	public void setValtdate(Date valtdate) {
		this.valtdate = valtdate;
	}

}