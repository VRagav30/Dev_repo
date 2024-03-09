package com.cop.model.database;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;

/**
 * The persistent class for the uom database table.
 * 
 */
@Entity
//@NamedQuery(name="Uom.findAll", query="SELECT u FROM Uom u")
@NamedQuery(name="Uom.softdeleteUom",query="update Uom  set is_delete=:is_delete where uom=:uom")

public class Uom implements Serializable {
	private static final long serialVersionUID = 1L;

	private String sname;
	@Id
	private String uom;

	private BigDecimal uomd;

	private String is_delete;

	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}

	public Uom() {
	}

	public String getSname() {
		return this.sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getUom() {
		return this.uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public BigDecimal getUomd() {
		return this.uomd;
	}

	public void setUomd(BigDecimal uomd) {
		this.uomd = uomd;
	}

}