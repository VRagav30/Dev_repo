package com.cop.model.database;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the plant database table.
 * 
 */
@Entity
//@NamedQuery(name = "Plant.findAll", query = "SELECT p FROM Plant p")
@NamedQuery(name = "Plant.findAllByPlant", query = "SELECT p FROM Plant p where upper(p.plant) = upper(:plant)")

@NamedQuery(name = "Plant.findOrgCodeByPlantId", query = "SELECT p.orgncode FROM Plant p where upper(p.plant) = upper(:plant) ")
@NamedQuery(name="Plant.softdeletePlant",query="update Plant  set is_delete=:is_delete where plant=:plant")

public class Plant implements Serializable {
	private static final long serialVersionUID = 1L;

	private String address1;

	private String address2;

	private String address3;

	private String govrn1;

	private String govrn2;

	private String orgncode;

	@Id
	private String plant;

	private String sname;

	private String ustyp;

	private String ustypd;

	private String is_delete;

	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}

	public Plant() {
	}

	public String getAddress1() {
		return this.address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return this.address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress3() {
		return this.address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public String getGovrn1() {
		return this.govrn1;
	}

	public void setGovrn1(String govrn1) {
		this.govrn1 = govrn1;
	}

	public String getGovrn2() {
		return this.govrn2;
	}

	public void setGovrn2(String govrn2) {
		this.govrn2 = govrn2;
	}

	public String getOrgncode() {
		return this.orgncode;
	}

	public void setOrgncode(String orgncode) {
		this.orgncode = orgncode;
	}

	public String getPlant() {
		return this.plant;
	}

	public void setPlant(String plant) {
		this.plant = plant;
	}

	public String getSname() {
		return this.sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getUstyp() {
		return this.ustyp;
	}

	public void setUstyp(String ustyp) {
		this.ustyp = ustyp;
	}

	public String getUstypd() {
		return this.ustypd;
	}

	public void setUstypd(String ustypd) {
		this.ustypd = ustypd;
	}

}