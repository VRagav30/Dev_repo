package com.cop.model.database;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;


/**
 * The persistent class for the cagroup database table.
 * 
 */
@Entity
@NamedQuery(name="Cagroup.findAll", query="SELECT c FROM Cagroup c where c.is_delete is null")
@NamedQuery(name="Cagroup.softdeleteCagroup",query="update Cagroup  set is_delete=:is_delete where cagrpid=:cagrpid")
public class Cagroup implements Serializable {
	private static final long serialVersionUID = 1L;
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
	private BigDecimal cagrpid;

	private String cagrp;

	private String costaccd;

	private String orgncode;
	
	private String is_delete;
	
	public BigDecimal getCagrpid() {
	return cagrpid;
}

public void setCagrpid(BigDecimal cagrpid) {
	this.cagrpid = cagrpid;
}

	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}


	public Cagroup() {
	}

	public String getCagrp() {
		return this.cagrp;
	}

	public void setCagrp(String cagrp) {
		this.cagrp = cagrp;
	}

	public String getCostaccd() {
		return this.costaccd;
	}

	public void setCostaccd(String costaccd) {
		this.costaccd = costaccd;
	}

	public String getOrgncode() {
		return this.orgncode;
	}

	public void setOrgncode(String orgncode) {
		this.orgncode = orgncode;
	}

}