package com.cop.model.database;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the pcb database table.
 * 
 */
/**
 * @author Reshma
 *
 */
@Entity
//@NamedQuery(name = "Pcb.findAll", query = "SELECT p FROM Pcb p")
@NamedQuery(name="Pcb.softdeletePcb",query="update Pcb  set is_delete=:is_delete where pcbpk=:pcbpk")

public class Pcb implements Serializable {
	private static final long serialVersionUID = 1L;

	private BigDecimal pcbpk;

	private String fieldmap;

	private String orgncode;

	private String pcagrp;

	private String pcbcode;

	private String pcbgrd;
	@Id
	private String pcbgrn;

	private String is_delete;

	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}

	@Temporal(TemporalType.DATE)
	private Date valfdate;

	@Temporal(TemporalType.DATE)
	private Date valtdate;

	public BigDecimal getPcbpk() {
		return pcbpk;
	}

	public void setPcbpk(BigDecimal pcbpk) {
		this.pcbpk = pcbpk;
	}

	public Pcb() {
	}

	public String getFieldmap() {
		return this.fieldmap;
	}

	public void setFieldmap(String fieldmap) {
		this.fieldmap = fieldmap;
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

	public String getPcbcode() {
		return this.pcbcode;
	}

	public void setPcbcode(String pcbcode) {
		this.pcbcode = pcbcode;
	}

	public String getPcbgrd() {
		return this.pcbgrd;
	}

	public void setPcbgrd(String pcbgrd) {
		this.pcbgrd = pcbgrd;
	}

	public String getPcbgrn() {
		return this.pcbgrn;
	}

	public void setPcbgrn(String pcbgrn) {
		this.pcbgrn = pcbgrn;
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