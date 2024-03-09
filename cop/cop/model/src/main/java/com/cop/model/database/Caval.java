package com.cop.model.database;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the caval database table.
 * 
 */
@Entity
@NamedQuery(name="Caval.findAll", query="SELECT c FROM Caval c where c.is_delete is null")
@NamedQuery(name="Caval.findAllbyMatlcode", query="select c from Caval c where upper(c.vevent) =upper(:vevent) and  (c.valgrp) = (:valgrp)")
@NamedQuery(name="Caval.findAllbyOrgnCodeMatValGrp", query="select c From Caval c where upper(c.vevent) =upper(:vevent) and upper(c.orgncode) = upper(:orgncode) and (c.valgrp) = (:valgrp)")
@NamedQuery(name="Caval.softdeleteCaval",query="update Caval  set is_delete=:is_delete where cavalpk=:cavalpk")

public class Caval implements Serializable {
	private static final long serialVersionUID = 1L;
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
	private BigDecimal cavalpk;

	private BigDecimal costacc;

	private String orgncode;

	private BigDecimal valgrp;

	private String vevent;
	
	private String is_delete;
	
	

	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}


	public Caval() {
	}

	public BigDecimal getCavalpk() {
		return this.cavalpk;
	}

	public void setCavalpk(BigDecimal cavalpk) {
		this.cavalpk = cavalpk;
	}

	public BigDecimal getCostacc() {
		return this.costacc;
	}

	public void setCostacc(BigDecimal costacc) {
		this.costacc = costacc;
	}

	public String getOrgncode() {
		return this.orgncode;
	}

	public void setOrgncode(String orgncode) {
		this.orgncode = orgncode;
	}

	public BigDecimal getValgrp() {
		return this.valgrp;
	}

	public void setValgrp(BigDecimal valgrp) {
		this.valgrp = valgrp;
	}

	public String getVevent() {
		return this.vevent;
	}

	public void setVevent(String vevent) {
		this.vevent = vevent;
	}

}