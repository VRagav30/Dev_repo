package com.cop.model.database;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the assignccb database table.
 * 
 */
@Entity
@NamedQuery(name="Assignccb.findAll", query="SELECT a FROM Assignccb a where a.is_delete is null")
@NamedQuery(name="Assignccb.softdeleteAssignccb",query="update Assignccb  set is_delete=:is_delete where accbpk=:accbpk")
public class Assignccb implements Serializable {
	private static final long serialVersionUID = 1L;
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
	private BigDecimal accbpk;

	private String ccbcode;

	private String cctr;

	private String orgncode;

	@Temporal(TemporalType.DATE)
	private Date valfdate;

	@Temporal(TemporalType.DATE)
	private Date valtdate;

	
private String is_delete;
	
	

	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}
	public Assignccb() {
	}

	public BigDecimal getAccbpk() {
		return this.accbpk;
	}

	public void setAccbpk(BigDecimal accbpk) {
		this.accbpk = accbpk;
	}

	public String getCcbcode() {
		return this.ccbcode;
	}

	public void setCcbcode(String ccbcode) {
		this.ccbcode = ccbcode;
	}

	public String getCctr() {
		return this.cctr;
	}

	public void setCctr(String cctr) {
		this.cctr = cctr;
	}

	public String getOrgncode() {
		return this.orgncode;
	}

	public void setOrgncode(String orgncode) {
		this.orgncode = orgncode;
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