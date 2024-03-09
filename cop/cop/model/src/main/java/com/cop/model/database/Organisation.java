package com.cop.model.database;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the organisation database table.
 * 
 */
@Entity
//@NamedQuery(name = "Organisation.findAll", query = "SELECT o FROM Organisation o")
@NamedQuery(name = "Organisation.findCurro", query = "SELECT o.curro from Organisation o where upper(o.orgncode) = upper(:orgncode)")
@NamedQuery(name = "Organisation.findAllByOrgnCode", query = "SELECT o FROM Organisation o where upper(o.orgncode) = upper(:orgncode)")
@NamedQuery(name = "Organisation.findFydCodeByOrgnCode", query = "SELECT o.fydcode FROM Organisation o where upper(o.orgncode) = upper(:orgncode)")
@NamedQuery(name="Organisation.softdeleteOrganisation",query="update Organisation  set is_delete=:is_delete where orgncode=:orgncode")
public class Organisation implements Serializable {
	private static final long serialVersionUID = 1L;

	private String address1;

	private String address2;

	private String city;

	private String countryid;

	private String curra1;

	private String curra2;

	private String curro;

	private String datetyp;

	private String fydcode;

	private String fydtyp;

	private String govrn1;

	private String govrn2;

	private String govrn3;

	private String lname;
	@Id
	private String orgncode;

	private String ostat;

	private String pincode;

	private String sname;

	private String state;

	private String is_delete;

	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}

	public Organisation() {
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

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountryid() {
		return this.countryid;
	}

	public void setCountryid(String countryid) {
		this.countryid = countryid;
	}

	public String getCurra1() {
		return this.curra1;
	}

	public void setCurra1(String curra1) {
		this.curra1 = curra1;
	}

	public String getCurra2() {
		return this.curra2;
	}

	public void setCurra2(String curra2) {
		this.curra2 = curra2;
	}

	public String getCurro() {
		return this.curro;
	}

	public void setCurro(String curro) {
		this.curro = curro;
	}

	public String getDatetyp() {
		return this.datetyp;
	}

	public void setDatetyp(String datetyp) {
		this.datetyp = datetyp;
	}

	public String getFydcode() {
		return this.fydcode;
	}

	public void setFydcode(String fydcode) {
		this.fydcode = fydcode;
	}

	public String getFydtyp() {
		return this.fydtyp;
	}

	public void setFydtyp(String fydtyp) {
		this.fydtyp = fydtyp;
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

	public String getGovrn3() {
		return this.govrn3;
	}

	public void setGovrn3(String govrn3) {
		this.govrn3 = govrn3;
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

	public String getPincode() {
		return this.pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getSname() {
		return this.sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

}