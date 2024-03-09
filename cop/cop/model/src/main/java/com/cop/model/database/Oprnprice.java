package com.cop.model.database;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;

/**
 * The persistent class for the oprnprice database table.
 *
 */
@Entity
//@NamedQuery(name = "Oprnprice.findAll", query = "SELECT o FROM Oprnprice o")
@NamedQuery(name = "Oprnprice.findAllByorgncodecctrandoprn", query = "select o from Oprnprice o where upper(o.orgncode)=upper(:orgncode) and upper(o.cctr)=upper(:cctr) and upper(o.oprn)=upper(:oprn) and upper(o.datatyp)=upper(:datatyp)")
@NamedQuery(name = "Oprnprice.findListofOprnbyorgncodeandcctr", query = "select o from Oprnprice o where upper(o.orgncode)=upper(:orgncode) and upper(o.cctr)=upper(:cctr) and upper(o.datatyp)=upper(:datatyp)")
@NamedQuery(name = "Oprnprice.findActivityPrice", query = "select o from Oprnprice o where upper(o.orgncode)=upper(:orgncode) and o.year = :year and o.periodf=:periodf and o.periodto=:periodto")
@NamedQuery(name="Oprnprice.softdeleteOprnprice",query="update Oprnprice  set is_delete=:is_delete where oprnpriceid=:oprnpriceid")
//@NamedQuery(name = "Oprnprice.insertActivityPrice",query="insert into oprnprice (select orgncode,year,periodf,periodf,curro,cctr,oprn,datatyp,capqty ,opuom ,opqty,pricebase, fixpr, varprice,totprice, priceuom ,costacc \r\n" + 
// "from OprnCal where orgncode= :orgncode and year= :year and periodf=
// :periodf)")
public class Oprnprice implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private String oprnpriceid;

	private BigDecimal capqty;

	private String cctr;

	private BigDecimal costacc;

	private String curro;

	private String datatyp;

	private BigDecimal fixpr;

	private BigDecimal opqty;

	private String oprn;

	private String opuom;

	private String orgncode;

	private BigDecimal periodf;

	private BigDecimal periodto;

	private String pricebase;

	private BigDecimal priceuom;

	private BigDecimal totprice;

	private BigDecimal varprice;

	private BigDecimal year;

	private String is_delete;

	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}

	public Oprnprice() {
	}

	public BigDecimal getCapqty() {
		return capqty;
	}

	public void setCapqty(BigDecimal capqty) {
		this.capqty = capqty;
	}

	public String getCctr() {
		return cctr;
	}

	public void setCctr(String cctr) {
		this.cctr = cctr;
	}

	public BigDecimal getCostacc() {
		return costacc;
	}

	public void setCostacc(BigDecimal costacc) {
		this.costacc = costacc;
	}

	public String getCurro() {
		return curro;
	}

	public void setCurro(String curro) {
		this.curro = curro;
	}

	public String getDatatyp() {
		return datatyp;
	}

	public void setDatatyp(String datatyp) {
		this.datatyp = datatyp;
	}

	public BigDecimal getFixpr() {
		return fixpr;
	}

	public void setFixpr(BigDecimal fixpr) {
		this.fixpr = fixpr;
	}

	public BigDecimal getOpqty() {
		return opqty;
	}

	public void setOpqty(BigDecimal opqty) {
		this.opqty = opqty;
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

	public BigDecimal getPeriodf() {
		return periodf;
	}

	public void setPeriodf(BigDecimal periodf) {
		this.periodf = periodf;
	}

	public BigDecimal getPeriodto() {
		return periodto;
	}

	public void setPeriodto(BigDecimal periodto) {
		this.periodto = periodto;
	}

	public String getPricebase() {
		return pricebase;
	}

	public void setPricebase(String pricebase) {
		this.pricebase = pricebase;
	}

	public BigDecimal getPriceuom() {
		return priceuom;
	}

	public void setPriceuom(BigDecimal priceuom) {
		this.priceuom = priceuom;
	}

	public BigDecimal getTotprice() {
		return totprice;
	}

	public void setTotprice(BigDecimal totprice) {
		this.totprice = totprice;
	}

	public BigDecimal getVarprice() {
		return varprice;
	}

	public void setVarprice(BigDecimal varprice) {
		this.varprice = varprice;
	}

	public BigDecimal getYear() {
		return year;
	}

	public void setYear(BigDecimal year) {
		this.year = year;
	}

}