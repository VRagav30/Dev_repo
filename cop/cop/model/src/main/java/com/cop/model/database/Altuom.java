package com.cop.model.database;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

/**
 * The persistent class for the altuom database table.
 *
 */
@Entity
@NamedQuery(name = "Altuom.findAll", query = "SELECT m FROM Altuom m where m.is_delete is null")
@NamedQuery(name = "Altuom.findAltuombyMatCodePlantAltUOM", query = "SELECT m FROM Altuom m where upper(m.plant)=upper(:plant) and upper(m.itemcode)=upper(:itemcode) and upper(m.altuom)=upper(:altuom) and upper(m.buom)=upper(:buom)")
@NamedQuery(name="Altuom.softdeleteAltuom",query="update Altuom  set is_delete=:is_delete where altuomid=:altuomid")
public class Altuom implements Serializable {
	private static final long serialVersionUID = 1L;

	private String altuom;

	@Override
	public String toString() {
		return "Altuom [altuom=" + altuom + ", altuomqty=" + altuomqty + ", buom=" + buom + ", buqty=" + buqty
				+ ", itemcode=" + itemcode + ", itemtype=" + itemtype + ", orgncode=" + orgncode + ", plant=" + plant
				+ "]";
	}

	private BigDecimal altuomqty;

	private String buom;

	private BigDecimal buqty;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private BigDecimal altuomid;
	
	public BigDecimal getAltuomid() {
		return altuomid;
	}

	public void setAltuomid(BigDecimal altuomid) {
		this.altuomid = altuomid;
	}

	private String itemcode;

	private String itemtype;

	private String orgncode;

	private String plant;
	
	private String is_delete;
	
	

	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}

	public Altuom() {
	}

	public String getAltuom() {
		return altuom;
	}

	public void setAltuom(String altuom) {
		this.altuom = altuom;
	}

	public BigDecimal getAltuomqty() {
		return altuomqty;
	}

	public void setAltuomqty(BigDecimal altuomqty) {
		this.altuomqty = altuomqty;
	}

	public String getBuom() {
		return buom;
	}

	public void setBuom(String buom) {
		this.buom = buom;
	}

	public BigDecimal getBuqty() {
		return buqty;
	}

	public void setBuqty(BigDecimal buqty) {
		this.buqty = buqty;
	}

	public String getItemcode() {
		return itemcode;
	}

	public void setItemcode(String itemcode) {
		this.itemcode = itemcode;
	}

	public String getOrgncode() {
		return orgncode;
	}

	public void setOrgncode(String orgncode) {
		this.orgncode = orgncode;
	}

	public String getPlant() {
		return plant;
	}

	public void setPlant(String plant) {
		this.plant = plant;
	}

	public String getItemtype() {
		return itemtype;
	}

	public void setItemtype(String itemtype) {
		this.itemtype = itemtype;
	}

}