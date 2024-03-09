package com.cop.model.database;

import java.io.Serializable;
import javax.persistence.*;

import com.cop.model.converter.DateConverter;
import com.cop.model.converter.StringConverter;
import com.creditdatamw.zerocell.annotation.Column;

import java.math.BigDecimal;
import java.util.Date;

/**
 * The persistent class for the profitcenter database table.
 * 
 */
@Entity
//@NamedQuery(name = "Profitcenter.findAll", query = "SELECT p FROM Profitcenter p")
@NamedQuery(name="Profitcenter.softdeleteProfitcenter",query="update Profitcenter  set is_delete=:is_delete where profitcenterpk=:profitcenterpk")

public class Profitcenter implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private BigDecimal profitcenterpk;
	
	@Column(index = 0, name = "profitcr", converterClass = StringConverter.class)
	private String profitcr;
	@Column(index = 1, name = "sname", converterClass = StringConverter.class)
	private String sname;
	@Column(index = 2, name = "lname", converterClass = StringConverter.class)
	private String lname;
	@Column(index = 3, name = "valfdate", converterClass = DateConverter.class)
	@Temporal(TemporalType.DATE)
	private Date valfdate;
	@Column(index = 4, name = "valtdate", converterClass = DateConverter.class)
	@Temporal(TemporalType.DATE)
	private Date valtdate;
	@Column(index = 5, name = "ostat", converterClass = StringConverter.class)
	private String ostat;
	
	

	private String is_delete;

	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}

	

	public Profitcenter() {
	}

	public String getLname() {
		return this.lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getOstat() {
		return this.ostat;
	}

	public void setOstat(String ostat) {
		this.ostat = ostat;
	}

	public BigDecimal getProfitcenterpk() {
		return this.profitcenterpk;
	}

	public void setProfitcenterpk(BigDecimal profitcenterpk) {
		this.profitcenterpk = profitcenterpk;
	}

	public String getProfitcr() {
		return this.profitcr;
	}

	public void setProfitcr(String profitcr) {
		this.profitcr = profitcr;
	}

	public String getSname() {
		return this.sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
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