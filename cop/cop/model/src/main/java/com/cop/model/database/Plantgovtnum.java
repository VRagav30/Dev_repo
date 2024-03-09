package com.cop.model.database;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * The persistent class for the plantgovtnum database table.
 * 
 */
@Entity
//@NamedQuery(name = "Plantgovtnum.findAll", query = "SELECT p FROM Plantgovtnum p")
@NamedQuery(name="Plantgovtnum.softdeletePlantgovtnum",query="update Plantgovtnum  set is_delete=:is_delete where pgnPk=:pgnPk")

public class Plantgovtnum implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "pgn_pk")
	private BigDecimal pgnPk;
	@Id
	private String plant;

	@Temporal(TemporalType.DATE)
	private Date seton;

	private String type;

	private String value;

	private String is_delete;

	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}

	public Plantgovtnum() {
	}

	public BigDecimal getPgnPk() {
		return this.pgnPk;
	}

	public void setPgnPk(BigDecimal pgnPk) {
		this.pgnPk = pgnPk;
	}

	public String getPlant() {
		return this.plant;
	}

	public void setPlant(String plant) {
		this.plant = plant;
	}

	public Date getSeton() {
		return this.seton;
	}

	public void setSeton(Date seton) {
		this.seton = seton;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}