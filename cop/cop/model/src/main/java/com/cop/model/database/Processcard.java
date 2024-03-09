package com.cop.model.database;

import java.io.Serializable;
import javax.persistence.*;

import com.cop.model.converter.DateConverter;
import com.cop.model.converter.StringConverter;
import com.creditdatamw.zerocell.annotation.Column;

import java.util.Date;

/**
 * The persistent class for the processcard database table.
 * 
 */
@Entity
//@NamedQuery(name = "Processcard.findAll", query = "SELECT p FROM Processcard p")
@NamedQuery(name="Processcard.softdeleteProcesscard",query="update Processcard  set is_delete=:is_delete where prscnum=:prscnum")

public class Processcard implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(index = 0, name = "prscnum", converterClass = StringConverter.class)
	private String prscnum;
	@Column(index = 2, name = "plant", converterClass = StringConverter.class)
	private String plant;
	@Column(index = 3, name = "matlcode", converterClass = StringConverter.class)
	private String matlcode;
	@Temporal(TemporalType.DATE)
	@Column(index = 4, name = "valfdate", converterClass = DateConverter.class)
	private Date valfdate;
	@Temporal(TemporalType.DATE)
	@Column(index = 5, name = "valtdate", converterClass = DateConverter.class)
	private Date valtdate;
	@Column(index = 6, name = "ostat", converterClass = StringConverter.class)
	private String ostat;

	
	

	

	private String is_delete;

	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}

	public Processcard() {
	}

	public String getMatlcode() {
		return this.matlcode;
	}

	public void setMatlcode(String matlcode) {
		this.matlcode = matlcode;
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

	public String getPrscnum() {
		return this.prscnum;
	}

	public void setPrscnum(String prscnum) {
		this.prscnum = prscnum;
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