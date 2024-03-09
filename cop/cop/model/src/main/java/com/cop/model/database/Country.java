package com.cop.model.database;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the country database table.
 * 
 */
@Entity
@NamedQuery(name="Country.findAll", query="SELECT c FROM Country c where c.is_delete is null")
@NamedQuery(name="Country.softdeleteCountry",query="update Country  set is_delete=:is_delete where countryid=:countryid")
public class Country implements Serializable {
	private static final long serialVersionUID = 1L;
@Id
	private String countryid;

	private String sname;
	
	private String is_delete;
	
	

	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}


	public Country() {
	}

	public String getCountryid() {
		return this.countryid;
	}

	public void setCountryid(String countryid) {
		this.countryid = countryid;
	}

	public String getSname() {
		return this.sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

}