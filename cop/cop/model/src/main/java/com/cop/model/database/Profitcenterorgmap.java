package com.cop.model.database;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

/**
 * The persistent class for the profitcenterorgmap database table.
 * 
 */
@Entity
//@NamedQuery(name = "Profitcenterorgmap.findAll", query = "SELECT p FROM Profitcenterorgmap p")
@NamedQuery(name="Profitcenterorgmap.softdeleteProfitcenterorgmap",query="update Profitcenterorgmap  set is_delete=:is_delete where pcompk=:pcompk")

public class Profitcenterorgmap implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private BigDecimal pcompk;

	private String orgncode;

	private String profitcr;

	private String is_delete;

	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}

	public Profitcenterorgmap() {
	}

	public String getOrgncode() {
		return this.orgncode;
	}

	public void setOrgncode(String orgncode) {
		this.orgncode = orgncode;
	}

	public String getProfitcr() {
		return this.profitcr;
	}

	public void setProfitcr(String profitcr) {
		this.profitcr = profitcr;
	}

	public BigDecimal getPcompk() {
		return pcompk;
	}

	public void setPcompk(BigDecimal pcompk) {
		this.pcompk = pcompk;
	}

}