package com.cop.model.database;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the currencyexchangerate database table.
 * 
 */
@Entity
//@NamedQuery(name="Currencyexchangerate.findAll", query="SELECT c FROM Currencyexchangerate c where c.is_delete is null")
@NamedQuery(name="Currencyexchangerate.findAllbyOrgncodeNdCurrency", query="select c from Currencyexchangerate c where upper(c.orgncode)=upper(:orgncode) and upper(c.scurr)=upper(:scurr)\r\n" + 
		"and upper(c.tcurr)=upper(:tcurr) and :orddate between c.valfdate and c.valtdate")

@NamedQuery(name="Currencyexchangerate.softdeleteCurrencyexchangerate",query="update Currencyexchangerate  set is_delete=:is_delete where curexchratepk=:curexchratepk")
public class Currencyexchangerate implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private BigDecimal curexchratepk;

	private String orgncode;

	private BigDecimal scnos;

	private String scurr;

	private String tcurr;

	private BigDecimal tcurrv;

	@Temporal(TemporalType.DATE)
	private Date valfdate;

	@Temporal(TemporalType.DATE)
	private Date valtdate;
	
	private String is_delete;
	
	

	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}


	public Currencyexchangerate() {
	}

	public BigDecimal getCurexchratepk() {
		return this.curexchratepk;
	}

	public void setCurexchratepk(BigDecimal curexchratepk) {
		this.curexchratepk = curexchratepk;
	}

	public String getOrgncode() {
		return this.orgncode;
	}

	public void setOrgncode(String orgncode) {
		this.orgncode = orgncode;
	}

	public BigDecimal getScnos() {
		return this.scnos;
	}

	public void setScnos(BigDecimal scnos) {
		this.scnos = scnos;
	}

	public String getScurr() {
		return this.scurr;
	}

	public void setScurr(String scurr) {
		this.scurr = scurr;
	}

	public String getTcurr() {
		return this.tcurr;
	}

	public void setTcurr(String tcurr) {
		this.tcurr = tcurr;
	}

	public BigDecimal getTcurrv() {
		return this.tcurrv;
	}

	public void setTcurrv(BigDecimal tcurrv) {
		this.tcurrv = tcurrv;
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