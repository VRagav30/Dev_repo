package com.cop.model.database;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;

/**
 * The persistent class for the opgroup database table.
 * 
 */
@Entity
//@NamedQuery(name = "Opgroup.findAll", query = "SELECT o FROM Opgroup o")
@NamedQuery(name="Opgroup.softdeleteOpgroup",query="update Opgroup  set is_delete=:is_delete where oprn=:oprn")
public class Opgroup implements Serializable {
	private static final long serialVersionUID = 1L;

	private String allocmethod;

	private String grpd;

	private String opgrp;
	@Id
	private String oprn;

	private String orgncode;

	private String sname;

	private BigDecimal value;

	private String is_delete;

	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}

	public Opgroup() {
	}

	public String getAllocmethod() {
		return this.allocmethod;
	}

	public void setAllocmethod(String allocmethod) {
		this.allocmethod = allocmethod;
	}

	public String getGrpd() {
		return this.grpd;
	}

	public void setGrpd(String grpd) {
		this.grpd = grpd;
	}

	public String getOpgrp() {
		return this.opgrp;
	}

	public void setOpgrp(String opgrp) {
		this.opgrp = opgrp;
	}

	public String getOprn() {
		return this.oprn;
	}

	public void setOprn(String oprn) {
		this.oprn = oprn;
	}

	public String getOrgncode() {
		return this.orgncode;
	}

	public void setOrgncode(String orgncode) {
		this.orgncode = orgncode;
	}

	public String getSname() {
		return this.sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public BigDecimal getValue() {
		return this.value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

}