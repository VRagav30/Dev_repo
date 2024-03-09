
package com.cop.model.database;

import java.io.Serializable;
import javax.persistence.*;
//import javax.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.Date;

/**
 * y The persistent class for the fydperiod database table.
 * 
 */
@Entity
//@NamedQuery(name = "Fydperiod.findAll", query = "SELECT f FROM Fydperiod f")

@NamedQuery(name = "Fydperiod.findbyPeriod", query = "SELECT f from Fydperiod f where f.period=:period")

@NamedQuery(name = "Fydperiod.findYear", query = "Select distinct year from Fydperiod f where fydcode=(select fydcode from Organisation o2 where orgncode = :orgncode)")

@NamedQuery(name = "Fydperiod.findPeriodsbyYear", query = "Select f from Fydperiod f where fydcode=(select fydcode from Organisation o2 where orgncode = :orgncode) and year=:year")

@NamedQuery(name="Fydperiod.softdeleteFydperiod",query="update Fydperiod  set is_delete=:is_delete where fydperiodpk=:fydperiodpk")
public class Fydperiod implements Serializable {
	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.DATE)
	private Date fromdate;

	// @Size(min=2, max=4)
	private String fydcode;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private BigDecimal fydperiodpk;

	private BigDecimal period;

	@Temporal(TemporalType.DATE)
	private Date todate;

	private BigDecimal year;

	private String is_delete;

	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}

	public Fydperiod() {
	}

	public Date getFromdate() {
		return this.fromdate;
	}

	public void setFromdate(Date fromdate) {
		this.fromdate = fromdate;
	}

	public String getFydcode() {
		return this.fydcode;
	}

	public void setFydcode(String fydcode) {
		this.fydcode = fydcode;
	}

	public BigDecimal getPeriod() {
		return this.period;
	}

	public void setPeriod(BigDecimal period) {
		this.period = period;
	}

	public Date getTodate() {
		return this.todate;
	}

	public void setTodate(Date todate) {
		this.todate = todate;
	}

	public BigDecimal getYear() {
		return this.year;
	}

	public void setYear(BigDecimal year) {
		this.year = year;
	}

	public BigDecimal getFydperiodpk() {
		return fydperiodpk;
	}

	public void setFydperiodpk(BigDecimal fydperiodpk) {
		this.fydperiodpk = fydperiodpk;
	}

}