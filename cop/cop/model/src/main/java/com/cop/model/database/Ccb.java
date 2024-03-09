package com.cop.model.database;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;


/**
 * The persistent class for the ccb database table.
 * 
 */
@Entity
@NamedQuery(name="Ccb.findAll", query="SELECT c FROM Ccb c where c.is_delete is null")

@NamedQuery(name="Ccb.softdeleteCcb",query="update Ccb  set is_delete=:is_delete where ccbn=:ccbn")
public class Ccb implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private BigDecimal ccbpk;

	private String cagrd;

	private String cagrp;

	private String ccbgrm;

	private BigDecimal ccbn;

	private String ccbcode;

	private String opgrp;
	
	private String is_delete;
	
	
	

	public BigDecimal getCcbpk() {
		return ccbpk;
	}

	public void setCcbpk(BigDecimal ccbpk) {
		this.ccbpk = ccbpk;
	}

	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}


	public Ccb() {
	}

	public String getCagrd() {
		return this.cagrd;
	}

	public void setCagrd(String cagrd) {
		this.cagrd = cagrd;
	}

	public String getCagrp() {
		return this.cagrp;
	}

	public void setCagrp(String cagrp) {
		this.cagrp = cagrp;
	}

	public String getCcbgrm() {
		return this.ccbgrm;
	}

	public void setCcbgrm(String ccbgrm) {
		this.ccbgrm = ccbgrm;
	}

	

	public BigDecimal getCcbn() {
		return ccbn;
	}

	public void setCcbn(BigDecimal ccbn) {
		this.ccbn = ccbn;
	}

	public String getCcbcode() {
		return this.ccbcode;
	}

	public void setCcbcode(String ccbcode) {
		this.ccbcode = ccbcode;
	}

	public String getOpgrp() {
		return this.opgrp;
	}

	public void setOpgrp(String opgrp) {
		this.opgrp = opgrp;
	}

}