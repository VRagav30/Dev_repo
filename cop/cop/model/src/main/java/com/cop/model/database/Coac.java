package com.cop.model.database;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the coac database table.
 * 
 */
@Entity
@NamedQuery(name="Coac.findAll", query="SELECT c FROM Coac c where c.is_delete is null")
@NamedQuery(name="Coac.softdeleteCoac",query="update Coac  set is_delete=:is_delete where costacc=:costacc")
public class Coac implements Serializable {
	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.DATE)
	private Date cafrom;

	private String cagrp;

	@Temporal(TemporalType.DATE)
	private Date cato;

	private String ccbgrm;
@Id
	private BigDecimal costacc;

	private String coyn;

	private String opgrp;

	private String orgncode;

	private String pcagrp;

	private String sname;
	
	private String is_delete;
	
	

	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}


	public Coac() {
	}

	public Date getCafrom() {
		return this.cafrom;
	}

	public void setCafrom(Date cafrom) {
		this.cafrom = cafrom;
	}

	public String getCagrp() {
		return this.cagrp;
	}

	public void setCagrp(String cagrp) {
		this.cagrp = cagrp;
	}

	public Date getCato() {
		return this.cato;
	}

	public void setCato(Date cato) {
		this.cato = cato;
	}

	public String getCcbgrm() {
		return this.ccbgrm;
	}

	public void setCcbgrm(String ccbgrm) {
		this.ccbgrm = ccbgrm;
	}

	public BigDecimal getCostacc() {
		return this.costacc;
	}

	public void setCostacc(BigDecimal costacc) {
		this.costacc = costacc;
	}

	public String getCoyn() {
		return this.coyn;
	}

	public void setCoyn(String coyn) {
		this.coyn = coyn;
	}

	public String getOpgrp() {
		return this.opgrp;
	}

	public void setOpgrp(String opgrp) {
		this.opgrp = opgrp;
	}

	public String getOrgncode() {
		return this.orgncode;
	}

	public void setOrgncode(String orgncode) {
		this.orgncode = orgncode;
	}

	public String getPcagrp() {
		return this.pcagrp;
	}

	public void setPcagrp(String pcagrp) {
		this.pcagrp = pcagrp;
	}

	public String getSname() {
		return this.sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

}