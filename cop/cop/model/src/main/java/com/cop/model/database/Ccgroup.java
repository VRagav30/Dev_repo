package com.cop.model.database;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;


/**
 * The persistent class for the ccgroup database table.
 * 
 */
@Entity
@NamedQuery(name="Ccgroup.findAll", query="SELECT c FROM Ccgroup c where c.is_delete is null")
@NamedQuery(name="Ccgroup.softdeleteCcgroup",query="update Ccgroup  set is_delete=:is_delete where id=:id")
public class Ccgroup implements Serializable {
	private static final long serialVersionUID = 1L;
@Id
	private BigDecimal ccgpk;
	
	private String grpcode;
	
	private String cctr;

	private String cctrf;

	private String cctrt;

	private String grpd;

	private String orgncode;
	
	private String is_delete;
	
	

	public BigDecimal getCcgpk() {
		return ccgpk;
	}

	public void setCcgpk(BigDecimal ccgpk) {
		this.ccgpk = ccgpk;
	}

	public String getGrpcode() {
		return grpcode;
	}

	public void setGrpcode(String grpcode) {
		this.grpcode = grpcode;
	}

	public String getCctr() {
		return cctr;
	}

	public void setCctr(String cctr) {
		this.cctr = cctr;
	}

	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}


	public Ccgroup() {
	}

	

	public String getCctrf() {
		return this.cctrf;
	}

	public void setCctrf(String cctrf) {
		this.cctrf = cctrf;
	}

	public String getCctrt() {
		return this.cctrt;
	}

	public void setCctrt(String cctrt) {
		this.cctrt = cctrt;
	}

	public String getGrpd() {
		return this.grpd;
	}

	public void setGrpd(String grpd) {
		this.grpd = grpd;
	}

	public String getOrgncode() {
		return this.orgncode;
	}

	public void setOrgncode(String orgncode) {
		this.orgncode = orgncode;
	}

}