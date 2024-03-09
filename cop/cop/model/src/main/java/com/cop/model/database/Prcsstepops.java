package com.cop.model.database;

import java.io.Serializable;
import javax.persistence.*;

import com.cop.model.converter.BigDecimalConverter;
import com.cop.model.converter.StringConverter;
import com.creditdatamw.zerocell.annotation.Column;

import java.math.BigDecimal;

/**
 * The persistent class for the prcsstepops database table.
 * 
 */
@Entity
@Table(name = "prcsstepops")
//@NamedQuery(name = "Prcsstepops.findAll", query = "SELECT p FROM Prcsstepops p")
@NamedQuery(name = "Prcsstepops.findAllbyoprn", query = "Select p from Prcsstepops p where upper(p.opname)=upper(:opname) and upper(p.prcsnum)=upper(:prcsnum)")
@NamedQuery(name="Prcsstepops.softdeletePrcsstepops",query="update Prcsstepops  set is_delete=:is_delete where prcsstepopscode=:prcsstepopscode")

public class Prcsstepops implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prcsstepops_seq")
	@SequenceGenerator(name = "prcsstepops_seq", sequenceName = "prcsstepops_seq", allocationSize = 1)
	private BigDecimal prcsstepopscode;
	@Column(index = 0, name = "prcsstepcode", converterClass = BigDecimalConverter.class)
	private BigDecimal prcsstepcode;
	@Column(index = 0, name = "prcsnum", converterClass = StringConverter.class)
	private String prcsnum;
	@Column(index = 0, name = "opname", converterClass = StringConverter.class)
	private String opname;
	@Column(index = 0, name = "opuom", converterClass = StringConverter.class)
	private String opuom;
	@Column(index = 0, name = "opqty", converterClass = BigDecimalConverter.class)
	private BigDecimal opqty;
	
	private String is_delete;

	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}

	

	public Prcsstepops() {
	}

	public String getOpname() {
		return this.opname;
	}

	public void setOpname(String opname) {
		this.opname = opname;
	}

	public String getPrcsnum() {
		return prcsnum;
	}

	public void setPrcsnum(String prcsnum) {
		this.prcsnum = prcsnum;
	}

	public BigDecimal getOpqty() {
		return this.opqty;
	}

	public void setOpqty(BigDecimal opqty) {
		this.opqty = opqty;
	}

	public String getOpuom() {
		return this.opuom;
	}

	public void setOpuom(String opuom) {
		this.opuom = opuom;
	}

	public BigDecimal getPrcsstepcode() {
		return this.prcsstepcode;
	}

	public void setPrcsstepcode(BigDecimal prcsstepcode) {
		this.prcsstepcode = prcsstepcode;
	}

	public BigDecimal getPrcsstepopscode() {
		return this.prcsstepopscode;
	}

	public void setPrcsstepopscode(BigDecimal prcsstepopscode) {
		this.prcsstepopscode = prcsstepopscode;
	}

}