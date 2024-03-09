package com.cop.model.database;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the currency database table.
 * 
 */
@Entity
@NamedQuery(name="Currency.findAll", query="SELECT c FROM Currency c where c.is_delete is null")
@NamedQuery(name="Currency.softdeleteCurrency",query="update Currency  set is_delete=:is_delete where currc=:currc")
public class Currency implements Serializable {
	private static final long serialVersionUID = 1L;
@Id
	private String currc;

	private BigDecimal currd;

	private String currn;
	
	private String is_delete;
	
	

	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}


	public Currency() {
	}

	public String getCurrc() {
		return this.currc;
	}

	public void setCurrc(String currc) {
		this.currc = currc;
	}

	public BigDecimal getCurrd() {
		return this.currd;
	}

	public void setCurrd(BigDecimal currd) {
		this.currd = currd;
	}

	public String getCurrn() {
		return this.currn;
	}

	public void setCurrn(String currn) {
		this.currn = currn;
	}

}