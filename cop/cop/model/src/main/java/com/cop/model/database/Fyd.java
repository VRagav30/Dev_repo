package com.cop.model.database;

import java.io.Serializable;
import javax.persistence.*;
//import javax.validation.constraints.*;

import java.math.BigDecimal;

/**
 * The persistent class for the fyd database table.
 * 
 */
@Entity
//@NamedQuery(name = "Fyd.findAll", query = "SELECT f FROM Fyd f")
@NamedQuery(name="Fyd.softdeleteFyd",query="update Fyd  set is_delete=:is_delete where fydpk=:fydpk")
public class Fyd implements Serializable {
	private static final long serialVersionUID = 1L;

	private String frommonth;

//@NotEmpty
//@Size(max=4,message="size.fyd.fydcode")
	private String fydcode;
//@NotEmpty
	private String fydtyp;
//@Size(max=2)
	private BigDecimal numper;

	private String tomonth;

	private BigDecimal year;
	@Id
	// @NotEmpty
	@GeneratedValue(strategy = GenerationType.AUTO)
	private BigDecimal fydpk;

	private String is_delete;

	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}

	public Fyd() {
	}

	public String getFrommonth() {
		return this.frommonth;
	}

	public void setFrommonth(String frommonth) {
		this.frommonth = frommonth;
	}

	public String getFydcode() {
		return this.fydcode;
	}

	public void setFydcode(String fydcode) {
		this.fydcode = fydcode;
	}

	public String getFydtyp() {
		return this.fydtyp;
	}

	public void setFydtyp(String fydtyp) {
		this.fydtyp = fydtyp;
	}

	public BigDecimal getNumper() {
		return this.numper;
	}

	public void setNumper(BigDecimal numper) {
		this.numper = numper;
	}

	public String getTomonth() {
		return this.tomonth;
	}

	public void setTomonth(String tomonth) {
		this.tomonth = tomonth;
	}

	public BigDecimal getYear() {
		return this.year;
	}

	public void setYear(BigDecimal year) {
		this.year = year;
	}

	public BigDecimal getFydpk() {
		return fydpk;
	}

	public void setFydpk(BigDecimal fydpk) {
		this.fydpk = fydpk;
	}

}