package com.cop.model.database;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the oprn database table.
 *
 */
@Entity
//@NamedQuery(name = "Oprn.findAll", query = "SELECT o FROM Oprn o")
@NamedQuery(name = "Oprn.findAllByOprn", query = "select o from Oprn o where upper(o.oprn)=upper(:oprn)")
@NamedQuery(name="Oprn.softdeleteOprn",query="update Oprn  set is_delete=:is_delete where oprn=:oprn")
public class Oprn implements Serializable {
	private static final long serialVersionUID = 1L;

	private String is_delete;

	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}

	private BigDecimal costacc;

	private String lname;
	@Id
	private String oprn;

	private String opuom;

	private String orgncode;

	private String ostat;

	private String pricebase;

	private String sname;

	@Temporal(TemporalType.DATE)
	private Date valfdate;

	@Temporal(TemporalType.DATE)
	private Date valtdate;

	public Oprn() {
	}

	public BigDecimal getCostacc() {
		return costacc;
	}

	public void setCostacc(BigDecimal costacc) {
		this.costacc = costacc;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getOprn() {
		return oprn;
	}

	public void setOprn(String oprn) {
		this.oprn = oprn;
	}

	public String getOpuom() {
		return opuom;
	}

	public void setOpuom(String opuom) {
		this.opuom = opuom;
	}

	public String getOrgncode() {
		return orgncode;
	}

	public void setOrgncode(String orgncode) {
		this.orgncode = orgncode;
	}

	public String getOstat() {
		return ostat;
	}

	public void setOstat(String ostat) {
		this.ostat = ostat;
	}

	public String getPricebase() {
		return pricebase;
	}

	public void setPricebase(String pricebase) {
		this.pricebase = pricebase;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public Date getValfdate() {
		return valfdate;
	}

	public void setValfdate(Date valfdate) {
		this.valfdate = valfdate;
	}

	public Date getValtdate() {
		return valtdate;
	}

	public void setValtdate(Date valtdate) {
		this.valtdate = valtdate;
	}

}