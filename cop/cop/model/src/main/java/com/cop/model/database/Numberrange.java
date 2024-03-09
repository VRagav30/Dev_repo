package com.cop.model.database;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;

/**
 * The persistent class for the numberrange database table.
 * 
 */
@Entity
//@NamedQuery(name = "Numberrange.findAll", query = "SELECT n FROM Numberrange n")
@NamedQuery(name = "Numberrange.findAllbyNumobject", query = "SELECT n FROM Numberrange n where n.numobject = :numobject")
@NamedQuery(name="Numberrange.softdeleteNumberrange",query="update Numberrange  set is_delete=:is_delete where rangeid=:rangeid")
public class Numberrange implements Serializable {
	private static final long serialVersionUID = 1L;

	private BigDecimal currentnumber;

	private String numobject;

	private String numtype;

	private BigDecimal rangefrom;

	@Id
	private BigDecimal rangeid;

	private BigDecimal rangeto;

	private String is_delete;

	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}

	public Numberrange() {
	}

	public BigDecimal getCurrentnumber() {
		return this.currentnumber;
	}

	public void setCurrentnumber(BigDecimal currentnumber) {
		this.currentnumber = currentnumber;
	}

	public String getNumobject() {
		return this.numobject;
	}

	public void setNumobject(String numobject) {
		this.numobject = numobject;
	}

	public String getNumtype() {
		return this.numtype;
	}

	public void setNumtype(String numtype) {
		this.numtype = numtype;
	}

	public BigDecimal getRangefrom() {
		return this.rangefrom;
	}

	public void setRangefrom(BigDecimal rangefrom) {
		this.rangefrom = rangefrom;
	}

	public BigDecimal getRangeid() {
		return this.rangeid;
	}

	public void setRangeid(BigDecimal rangeid) {
		this.rangeid = rangeid;
	}

	public BigDecimal getRangeto() {
		return this.rangeto;
	}

	public void setRangeto(BigDecimal rangeto) {
		this.rangeto = rangeto;
	}

}