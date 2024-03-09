package com.cop.model.database;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.cop.model.converter.DateConverter;
import com.cop.model.converter.StringConverter;
import com.creditdatamw.zerocell.annotation.Column;

/**
 * The persistent class for the batch database table.
 * 
 */
@Entity
//@NamedQuery(name="Batch.findAll", query="SELECT b FROM Batch b where b.is_delete is null")
@NamedQuery(name="Batch.softdeleteBatch",query="update Batch  set is_delete=:is_delete where batch=:batch")
public class Batch implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(index = 0, name = "batch", converterClass = StringConverter.class)
	private String batch;
	@Column(index = 1, name = "batchcosting", converterClass = StringConverter.class)
	private String batchcosting;
	@Column(index = 2, name = "deletionindicator", converterClass = StringConverter.class)
	private String deletionindicator;
	@Column(index = 3, name = "itemcode", converterClass = StringConverter.class)
	private String itemcode;
	@Column(index = 4, name = "orgncode", converterClass = StringConverter.class)
	private String orgncode;
	@Column(index = 5, name = "soassignment", converterClass = StringConverter.class)
	private BigDecimal soassignment;
	
	@Temporal(TemporalType.DATE)
	@Column(index = 6, name = "valfdate", converterClass = DateConverter.class)
	private Date valfdate;
	@Column(index = 7, name = "valsub", converterClass = StringConverter.class)
	private String valsub;
	
	private String is_delete;
	
	

	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}

	public Batch() {
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

	public String getDeletionindicator() {
		return this.deletionindicator;
	}

	public void setDeletionindicator(String deletionindicator) {
		this.deletionindicator = deletionindicator;
	}

	public String getItemcode() {
		return this.itemcode;
	}

	public void setItemcode(String itemcode) {
		this.itemcode = itemcode;
	}

	public String getOrgncode() {
		return this.orgncode;
	}

	public void setOrgncode(String orgncode) {
		this.orgncode = orgncode;
	}

	public BigDecimal getSoassignment() {
		return this.soassignment;
	}

	public void setSoassignment(BigDecimal soassignment) {
		this.soassignment = soassignment;
	}

	public Date getValfdate() {
		return this.valfdate;
	}

	public void setValfdate(Date valfdate) {
		this.valfdate = valfdate;
	}

	public String getValsub() {
		return this.valsub;
	}

	public void setValsub(String valsub) {
		this.valsub = valsub;
	}

}