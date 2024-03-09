package com.cop.model.database;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

import com.cop.model.converter.BigDecimalConverter;
import com.cop.model.converter.StringConverter;
import com.creditdatamw.zerocell.annotation.Column;

/**
 * The persistent class for the prcsstep database table.
 *
 */
@Entity
//@NamedQuery(name = "Prcsstep.findAll", query = "SELECT p FROM Prcsstep p")
@NamedQuery(name = "Prcsstep.findAllbyprcsnum", query = "select p from Prcsstep p where upper(p.prscnum)=upper(:prcsnum)")
@NamedQuery(name="Prcsstep.softdeletePrcsstep",query="update Prcsstep  set is_delete=:is_delete where prcsstepcode=:prcsstepcode")

public class Prcsstep implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private BigDecimal prcsstepcode;
	@Column(index = 0, name = "prscnum", converterClass = StringConverter.class)
	private String prscnum;
	@Column(index = 1, name = "matlcode", converterClass = StringConverter.class)
	private String matlcode;
	@Column(index = 2, name = "prresou", converterClass = StringConverter.class)
	private String prresou;
	@Column(index = 3, name = "stepnum", converterClass = BigDecimalConverter.class)
	private BigDecimal stepnum;
	@Column(index = 4, name = "prcsuom", converterClass = StringConverter.class)
	private String prcsuom;
	@Column(index = 5, name = "prcsuomqty", converterClass = BigDecimalConverter.class)
	private BigDecimal prcsuomqty;
	@Column(index = 6, name = "prcsctr", converterClass = StringConverter.class)
	private String prcsctr;
	@Column(index = 7, name = "buom", converterClass = StringConverter.class)
	private String buom;
	@Column(index = 8, name = "cnvbuomqty", converterClass = BigDecimalConverter.class)
	private BigDecimal cnvbuomqty;
	@Column(index = 9, name = "setup", converterClass = StringConverter.class)
	private String setup;
	@Column(index = 10, name = "interop", converterClass = StringConverter.class)
	private String interop;
	@Column(index = 11, name = "runop", converterClass = StringConverter.class)
	private String runop;


	
	
	private String is_delete;

	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}

	public Prcsstep() {
	}

	public String getBuom() {
		return buom;
	}

	public void setBuom(String buom) {
		this.buom = buom;
	}

	public BigDecimal getCnvbuomqty() {
		return cnvbuomqty;
	}

	public void setCnvbuomqty(BigDecimal cnvbuomqty) {
		this.cnvbuomqty = cnvbuomqty;
	}

	public String getInterop() {
		return interop;
	}

	public void setInterop(String interop) {
		this.interop = interop;
	}

	public String getMatlcode() {
		return matlcode;
	}

	public void setMatlcode(String matlcode) {
		this.matlcode = matlcode;
	}

	public String getPrcsctr() {
		return prcsctr;
	}

	public void setPrcsctr(String prcsctr) {
		this.prcsctr = prcsctr;
	}

	public BigDecimal getPrcsstepcode() {
		return prcsstepcode;
	}

	public void setPrcsstepcode(BigDecimal prcsstepcode) {
		this.prcsstepcode = prcsstepcode;
	}

	public String getPrcsuom() {
		return prcsuom;
	}

	public void setPrcsuom(String prcsuom) {
		this.prcsuom = prcsuom;
	}

	public BigDecimal getPrcsuomqty() {
		return prcsuomqty;
	}

	public void setPrcsuomqty(BigDecimal prcsuomqty) {
		this.prcsuomqty = prcsuomqty;
	}

	public String getPrresou() {
		return prresou;
	}

	public void setPrresou(String prresou) {
		this.prresou = prresou;
	}

	public String getPrscnum() {
		return prscnum;
	}

	public void setPrscnum(String prscnum) {
		this.prscnum = prscnum;
	}

	public String getRunop() {
		return runop;
	}

	public void setRunop(String runop) {
		this.runop = runop;
	}

	public String getSetup() {
		return setup;
	}

	public void setSetup(String setup) {
		this.setup = setup;
	}

	public BigDecimal getStepnum() {
		return stepnum;
	}

	public void setStepnum(BigDecimal stepnum) {
		this.stepnum = stepnum;
	}

}