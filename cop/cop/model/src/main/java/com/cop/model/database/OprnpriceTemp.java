package com.cop.model.database;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the oprnprice_temp database table.
 * 
 */

@NamedStoredProcedureQuery(
        name = "generate_activityprice_temp",
        procedureName = "generate_activityprice_temp",
        parameters = {
        		@StoredProcedureParameter(type = String.class, mode = ParameterMode.IN),
        		@StoredProcedureParameter(type = BigDecimal.class, mode = ParameterMode.IN),
        		@StoredProcedureParameter(type = BigDecimal .class, mode = ParameterMode.IN)
        })
@Entity
@Table(name="oprnprice_temp")
@NamedQuery(name="OprnpriceTemp.findAll", query="SELECT o FROM OprnpriceTemp o")

public class OprnpriceTemp implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private BigDecimal optpk;

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

	private String transactionid;

	public BigDecimal getOptpk() {
		return optpk;
	}

	public void setOptpk(BigDecimal optpk) {
		this.optpk = optpk;
	}

	private BigDecimal varprice;

	private BigDecimal year;

	public OprnpriceTemp() {
	}

	public BigDecimal getCapqty() {
		return this.capqty;
	}

	public void setCapqty(BigDecimal capqty) {
		this.capqty = capqty;
	}

	public String getCctr() {
		return this.cctr;
	}

	public void setCctr(String cctr) {
		this.cctr = cctr;
	}

	public BigDecimal getCostacc() {
		return this.costacc;
	}

	public void setCostacc(BigDecimal costacc) {
		this.costacc = costacc;
	}

	public String getCurro() {
		return this.curro;
	}

	public void setCurro(String curro) {
		this.curro = curro;
	}

	public String getDatatyp() {
		return this.datatyp;
	}

	public void setDatatyp(String datatyp) {
		this.datatyp = datatyp;
	}

	public BigDecimal getFixpr() {
		return this.fixpr;
	}

	public void setFixpr(BigDecimal fixpr) {
		this.fixpr = fixpr;
	}

	public BigDecimal getOpqty() {
		return this.opqty;
	}

	public void setOpqty(BigDecimal opqty) {
		this.opqty = opqty;
	}

	public String getOprn() {
		return this.oprn;
	}

	public void setOprn(String oprn) {
		this.oprn = oprn;
	}

	public String getOpuom() {
		return this.opuom;
	}

	public void setOpuom(String opuom) {
		this.opuom = opuom;
	}

	public String getOrgncode() {
		return this.orgncode;
	}

	public void setOrgncode(String orgncode) {
		this.orgncode = orgncode;
	}

	public BigDecimal getPeriodf() {
		return this.periodf;
	}

	public void setPeriodf(BigDecimal periodf) {
		this.periodf = periodf;
	}

	public BigDecimal getPeriodto() {
		return this.periodto;
	}

	public void setPeriodto(BigDecimal periodto) {
		this.periodto = periodto;
	}

	public String getPricebase() {
		return this.pricebase;
	}

	public void setPricebase(String pricebase) {
		this.pricebase = pricebase;
	}

	public BigDecimal getPriceuom() {
		return this.priceuom;
	}

	public void setPriceuom(BigDecimal priceuom) {
		this.priceuom = priceuom;
	}

	public BigDecimal getTotprice() {
		return this.totprice;
	}

	public void setTotprice(BigDecimal totprice) {
		this.totprice = totprice;
	}

	public String getTransactionid() {
		return this.transactionid;
	}

	public void setTransactionid(String transactionid) {
		this.transactionid = transactionid;
	}

	public BigDecimal getVarprice() {
		return this.varprice;
	}

	public void setVarprice(BigDecimal varprice) {
		this.varprice = varprice;
	}

	public BigDecimal getYear() {
		return this.year;
	}

	public void setYear(BigDecimal year) {
		this.year = year;
	}

}