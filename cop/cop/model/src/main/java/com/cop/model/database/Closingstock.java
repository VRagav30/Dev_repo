package com.cop.model.database;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the closingstock database table.
 * 
 */
@Entity
@NamedQuery(name="Closingstock.findAll", query="SELECT c FROM Closingstock c")
public class Closingstock implements Serializable {
	private static final long serialVersionUID = 1L;

	private String batchnumber;

	private String coverage;

	private String createdtime;

	@Id
	private BigDecimal cspk;

	private BigDecimal csqty;

	private String itemcode;

	private BigDecimal period;

	private String plant;

	private String saleorder;

	private String status;

	private String stocktype;

	private String usercode;

	private String valsub;

	private BigDecimal year;
	
	private String is_delete;
	
	

	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}


	public Closingstock() {
	}

	public String getBatchnumber() {
		return this.batchnumber;
	}

	public void setBatchnumber(String batchnumber) {
		this.batchnumber = batchnumber;
	}

	public String getCoverage() {
		return this.coverage;
	}

	public void setCoverage(String coverage) {
		this.coverage = coverage;
	}

	public String getCreatedtime() {
		return this.createdtime;
	}

	public void setCreatedtime(String createdtime) {
		this.createdtime = createdtime;
	}

	public BigDecimal getCspk() {
		return this.cspk;
	}

	public void setCspk(BigDecimal cspk) {
		this.cspk = cspk;
	}

	public BigDecimal getCsqty() {
		return this.csqty;
	}

	public void setCsqty(BigDecimal csqty) {
		this.csqty = csqty;
	}

	public String getItemcode() {
		return this.itemcode;
	}

	public void setItemcode(String itemcode) {
		this.itemcode = itemcode;
	}

	public BigDecimal getPeriod() {
		return this.period;
	}

	public void setPeriod(BigDecimal period) {
		this.period = period;
	}

	public String getPlant() {
		return this.plant;
	}

	public void setPlant(String plant) {
		this.plant = plant;
	}

	public String getSaleorder() {
		return this.saleorder;
	}

	public void setSaleorder(String saleorder) {
		this.saleorder = saleorder;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStocktype() {
		return this.stocktype;
	}

	public void setStocktype(String stocktype) {
		this.stocktype = stocktype;
	}

	public String getUsercode() {
		return this.usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

	public String getValsub() {
		return this.valsub;
	}

	public void setValsub(String valsub) {
		this.valsub = valsub;
	}

	public BigDecimal getYear() {
		return this.year;
	}

	public void setYear(BigDecimal year) {
		this.year = year;
	}

}