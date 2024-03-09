package com.cop.model.database;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;

/**
 * The persistent class for the service database table.
 * 
 */
@Entity
//@NamedQuery(name = "Serviceitem.findAll", query = "SELECT s FROM Serviceitem s")
@NamedQuery(name = "Serviceitem.findAllbyItemCode", query = "SELECT s from Serviceitem s where upper(s.sercode)=upper(:sercode)")
@NamedQuery(name = "Serviceitem.findAllbyItemcodeandPlant", query = "SELECT s from Serviceitem s where upper(s.sercode)=upper(:sercode) and upper(s.plant)=upper(:plant)")
@NamedQuery(name="Serviceitem.softdeleteServiceitem",query="update Serviceitem  set is_delete=:is_delete where sercode=:sercode")

public class Serviceitem implements Serializable {
	private static final long serialVersionUID = 1L;

	private String activeind;

	private String buom;

	private BigDecimal coslot;

	private String lname;

	private String oboind;

	private String plant;
	@Id
	private String sercode;

	private String sertyp;

	private String sname;

	private String typed;

	private BigDecimal valgrp;

	private String is_delete;

	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}

	public Serviceitem() {
	}

	public String getActiveind() {
		return this.activeind;
	}

	public void setActiveind(String activeind) {
		this.activeind = activeind;
	}

	public String getBuom() {
		return this.buom;
	}

	public void setBuom(String buom) {
		this.buom = buom;
	}

	public BigDecimal getCoslot() {
		return this.coslot;
	}

	public void setCoslot(BigDecimal coslot) {
		this.coslot = coslot;
	}

	public String getLname() {
		return this.lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getOboind() {
		return this.oboind;
	}

	public void setOboind(String oboind) {
		this.oboind = oboind;
	}

	public String getPlant() {
		return this.plant;
	}

	public void setPlant(String plant) {
		this.plant = plant;
	}

	public String getSercode() {
		return this.sercode;
	}

	public void setSercode(String sercode) {
		this.sercode = sercode;
	}

	public String getSertyp() {
		return this.sertyp;
	}

	public void setSertyp(String sertyp) {
		this.sertyp = sertyp;
	}

	public String getSname() {
		return this.sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getTyped() {
		return this.typed;
	}

	public void setTyped(String typed) {
		this.typed = typed;
	}

	public BigDecimal getValgrp() {
		return this.valgrp;
	}

	public void setValgrp(BigDecimal valgrp) {
		this.valgrp = valgrp;
	}

}