package com.cop.model.database;

import java.io.Serializable;
import javax.persistence.*;

import com.cop.model.converter.BigDecimalConverter;
import com.cop.model.converter.StringConverter;
import com.creditdatamw.zerocell.annotation.Column;

import java.math.BigDecimal;

/**
 * The persistent class for the material database table.
 * 
 */
@Entity
//@NamedQuery(name="Material.findAll", query="SELECT m FROM Material m")
@NamedQuery(name = "Material.findAllByMatlcode", query = "SELECT m FROM Material m where upper(m.matlcode)=upper(:matlcode)")
//@NamedQuery(name="Material.findAllByMatlcodeandPlantValsub", query="SELECT m FROM Material m where(upper(m.matlcode))=(upper(:matlcode)) and upper(m.plant)=upper(:plant)")
//@NamedQuery(name="Material.findAllByMatlcodeandPlantValsub", query="SELECT m FROM Material m where ( m.matlcode is null or (upper(m.matlcode))=(upper(:matlcode)) ) and (m.plant is null or  upper(m.plant)=upper(:plant)) and (m.valsub is null or  upper(m.valsub)=upper(:valsub))")

@NamedQuery(name="Material.softdeleteMaterial",query="update Material  set is_delete=:is_delete where matpk=:matpk")

public class Material implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private BigDecimal matpk;
	@Column(index = 0, name = "matlcode", converterClass = StringConverter.class)
	private String matlcode;
	@Column(index = 1, name = "sname", converterClass = StringConverter.class)
	private String sname;
	@Column(index = 2, name = "lname", converterClass = StringConverter.class)
	private String lname;
	@Column(index = 3, name = "mattyp", converterClass = StringConverter.class)
	private String mattyp;
	@Column(index = 4, name = "typed", converterClass = StringConverter.class)
	private String typed;
	@Column(index = 5, name = "plant", converterClass = StringConverter.class)
	private String plant;
	@Column(index = 6, name = "prdind", converterClass = StringConverter.class)
	private String prdind;
	@Column(index = 7, name = "bomal", converterClass = StringConverter.class)
	private String bomal;
	@Column(index = 8, name = "prcsind", converterClass = StringConverter.class)
	private String prcsind;
	@Column(index = 9, name = "oboind", converterClass = StringConverter.class)
	private String oboind;
	@Column(index = 10, name = "scallow", converterClass = StringConverter.class)
	private String scallow;
	@Column(index = 11, name = "utpcost", converterClass = StringConverter.class)
	private String utpcost;
	@Column(index = 12, name = "trplant", converterClass = StringConverter.class)
	private String trplant;
	@Column(index = 13, name = "ignocost", converterClass = StringConverter.class)
	private String ignocost;
	@Column(index = 14, name = "coprind", converterClass = StringConverter.class)
	private String coprind;
	@Column(index = 15, name = "byproduct", converterClass = StringConverter.class)
	private String byproduct;
	@Column(index = 16, name = "ostat", converterClass = StringConverter.class)
	private String ostat;
	@Column(index = 17, name = "buom", converterClass = StringConverter.class)
	private String buom;
	@Column(index = 18, name = "profitcr", converterClass = StringConverter.class)
	private String profitcr;
	@Column(index = 19, name = "coslot", converterClass = BigDecimalConverter.class)
	private BigDecimal coslot;
	@Column(index = 20, name = "valgrp", converterClass = BigDecimalConverter.class)
	private BigDecimal valgrp;
	@Column(index = 21, name = "valsub", converterClass = StringConverter.class)
	private String valsub;
	@Column(index = 22, name = "batch", converterClass = StringConverter.class)
	private String batch;
	@Column(index = 23, name = "batchcosting", converterClass = StringConverter.class)
	private String batchcosting;

	private String is_delete;

	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}

	public BigDecimal getMatpk() {
		return matpk;
	}

	public void setMatpk(BigDecimal matpk) {
		this.matpk = matpk;
	}

	public Material() {
	}

	public String getBatch() {
		return this.batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public String getBatchcosting() {
		return this.batchcosting;
	}

	public void setBatchcosting(String batchcosting) {
		this.batchcosting = batchcosting;
	}

	public String getBomal() {
		return this.bomal;
	}

	public void setBomal(String bomal) {
		this.bomal = bomal;
	}

	public String getBuom() {
		return this.buom;
	}

	public void setBuom(String buom) {
		this.buom = buom;
	}

	public String getByproduct() {
		return this.byproduct;
	}

	public void setByproduct(String byproduct) {
		this.byproduct = byproduct;
	}

	public String getCoprind() {
		return this.coprind;
	}

	public void setCoprind(String coprind) {
		this.coprind = coprind;
	}

	public BigDecimal getCoslot() {
		return this.coslot;
	}

	public void setCoslot(BigDecimal coslot) {
		this.coslot = coslot;
	}

	public String getIgnocost() {
		return this.ignocost;
	}

	public void setIgnocost(String ignocost) {
		this.ignocost = ignocost;
	}

	public String getLname() {
		return this.lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getMatlcode() {
		return this.matlcode;
	}

	public void setMatlcode(String matlcode) {
		this.matlcode = matlcode;
	}

	public String getMattyp() {
		return this.mattyp;
	}

	public void setMattyp(String mattyp) {
		this.mattyp = mattyp;
	}

	public String getOboind() {
		return this.oboind;
	}

	public void setOboind(String oboind) {
		this.oboind = oboind;
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

	public String getPrcsind() {
		return this.prcsind;
	}

	public void setPrcsind(String prcsind) {
		this.prcsind = prcsind;
	}

	public String getPrdind() {
		return this.prdind;
	}

	public void setPrdind(String prdind) {
		this.prdind = prdind;
	}

	public String getProfitcr() {
		return this.profitcr;
	}

	public void setProfitcr(String profitcr) {
		this.profitcr = profitcr;
	}

	public String getScallow() {
		return this.scallow;
	}

	public void setScallow(String scallow) {
		this.scallow = scallow;
	}

	public String getSname() {
		return this.sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getTrplant() {
		return this.trplant;
	}

	public void setTrplant(String trplant) {
		this.trplant = trplant;
	}

	public String getTyped() {
		return this.typed;
	}

	public void setTyped(String typed) {
		this.typed = typed;
	}

	public String getUtpcost() {
		return this.utpcost;
	}

	public void setUtpcost(String utpcost) {
		this.utpcost = utpcost;
	}

	public BigDecimal getValgrp() {
		return this.valgrp;
	}

	public void setValgrp(BigDecimal valgrp) {
		this.valgrp = valgrp;
	}

	public String getValsub() {
		return this.valsub;
	}

	public void setValsub(String valsub) {
		this.valsub = valsub;
	}

}