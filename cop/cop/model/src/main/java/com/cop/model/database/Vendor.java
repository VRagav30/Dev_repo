package com.cop.model.database;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the vendor database table.
 * 
 */
@Entity
//@NamedQuery(name="Vendor.findAll", query="SELECT v FROM Vendor v")
@NamedQuery(name="Vendor.softdeleteVendor",query="update Vendor  set is_delete=:is_delete where vendor=:vendor")

public class Vendor implements Serializable {
	private static final long serialVersionUID = 1L;

	private String address1;

	private String address2;

	private String address3;

	private String address4;

	private String govrn1;

	private String lname;

	private String orgncode;

	private String ostat;

	private String partnfunc;

	private String plant;

	private String sname;
	@Id
	private String vendor;

	private String vendortyp;

	private String vendortypd;

	private String vengrp;

	private String is_delete;

	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}

	public Vendor() {
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

	public String getAddress4() {
		return this.address4;
	}

	public void setAddress4(String address4) {
		this.address4 = address4;
	}

	public String getGovrn1() {
		return this.govrn1;
	}

	public void setGovrn1(String govrn1) {
		this.govrn1 = govrn1;
	}

	public String getLname() {
		return this.lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getOrgncode() {
		return this.orgncode;
	}

	public void setOrgncode(String orgncode) {
		this.orgncode = orgncode;
	}

	public String getOstat() {
		return this.ostat;
	}

	public void setOstat(String ostat) {
		this.ostat = ostat;
	}

	public String getPartnfunc() {
		return this.partnfunc;
	}

	public void setPartnfunc(String partnfunc) {
		this.partnfunc = partnfunc;
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

	public String getVendor() {
		return this.vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getVendortyp() {
		return this.vendortyp;
	}

	public void setVendortyp(String vendortyp) {
		this.vendortyp = vendortyp;
	}

	public String getVendortypd() {
		return this.vendortypd;
	}

	public void setVendortypd(String vendortypd) {
		this.vendortypd = vendortypd;
	}

	public String getVengrp() {
		return this.vengrp;
	}

	public void setVengrp(String vengrp) {
		this.vengrp = vengrp;
	}

}