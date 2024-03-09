package com.cop.model.database;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * The persistent class for the organisationdgovtnum database table.
 * 
 */
@Entity
//@NamedQuery(name = "Organisationgovtnum.findAll", query = "SELECT o FROM Organisationgovtnum o")
@NamedQuery(name="Organisationgovtnum.softdeleteOrganisationgovtnum",query="update Organisationgovtnum  set is_delete=:is_delete where ognPk=:ognPk")
public class Organisationgovtnum implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ogn_pk")
	private BigDecimal ognPk;

	private String orgncode;

	@Temporal(TemporalType.DATE)
	private Date seton;

	private String type;

	private String value;

	public Organisationgovtnum() {
	}

	public BigDecimal getOgnPk() {
		return this.ognPk;
	}

	public void setOgnPk(BigDecimal ognPk) {
		this.ognPk = ognPk;
	}

	public String getOrgncode() {
		return this.orgncode;
	}

	public void setOrgncode(String orgncode) {
		this.orgncode = orgncode;
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

	private String is_delete;

	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}
}