package com.cop.model.database;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the oprn_cal database table.
 * 
 */
@NamedStoredProcedureQuery(
        name = "generate_OprnCal",
        procedureName = "generate_OprnCal",
        parameters = {
        		@StoredProcedureParameter(type = String.class, mode = ParameterMode.IN),
        		@StoredProcedureParameter(type = BigDecimal.class, mode = ParameterMode.IN),
        		@StoredProcedureParameter(type = BigDecimal .class, mode = ParameterMode.IN)
        })
@Entity
@Table(name="oprn_cal")
@NamedQuery(name="OprnCal.findAll", query="SELECT o FROM OprnCal o")
public class OprnCal implements Serializable {
	private static final long serialVersionUID = 1L;

	private String allocatedfrom;

	private BigDecimal allocatedfromamount;

	private String allocatedto;

	private BigDecimal allocatedtoamount;

	private String cagrp;

	private BigDecimal capqty;
	
	private String curro;
	
	private String pricebase;

	private String ccgrm;

	private String cobjtype;

	private BigDecimal costacc;

	private BigDecimal fixedamount;

	private BigDecimal fixedprice;

	@Id
	private String id;

	private String objcode;
	
	private String datatyp;

	private BigDecimal opgamount;

	private String opgrp;

	private String oprn;

	private String oprnavailability;

	private BigDecimal oprncostacc;

	private BigDecimal oprnqty;

	private String oprnuom;

	private String orgncode;

	private BigDecimal period;

	private BigDecimal postedamount;

	private BigDecimal totalamount;

	private BigDecimal totalprice;

	private String trnsevent;

	private BigDecimal variableamount;

	private BigDecimal variableprice;

	private BigDecimal year;
	
	private BigDecimal priceuom;

	public OprnCal() {
	}

	public String getAllocatedfrom() {
		return this.allocatedfrom;
	}

	public void setAllocatedfrom(String allocatedfrom) {
		this.allocatedfrom = allocatedfrom;
	}

	public BigDecimal getAllocatedfromamount() {
		return this.allocatedfromamount;
	}

	public void setAllocatedfromamount(BigDecimal allocatedfromamount) {
		this.allocatedfromamount = allocatedfromamount;
	}

	public String getAllocatedto() {
		return this.allocatedto;
	}

	public void setAllocatedto(String allocatedto) {
		this.allocatedto = allocatedto;
	}

	public BigDecimal getAllocatedtoamount() {
		return this.allocatedtoamount;
	}

	public void setAllocatedtoamount(BigDecimal allocatedtoamount) {
		this.allocatedtoamount = allocatedtoamount;
	}

	public String getCagrp() {
		return this.cagrp;
	}

	public void setCagrp(String cagrp) {
		this.cagrp = cagrp;
	}

	public BigDecimal getCapqty() {
		return this.capqty;
	}

	public void setCapqty(BigDecimal capqty) {
		this.capqty = capqty;
	}

	public String getCcgrm() {
		return this.ccgrm;
	}

	public void setCcgrm(String ccgrm) {
		this.ccgrm = ccgrm;
	}

	public String getCobjtype() {
		return this.cobjtype;
	}

	public void setCobjtype(String cobjtype) {
		this.cobjtype = cobjtype;
	}

	public BigDecimal getCostacc() {
		return this.costacc;
	}

	public void setCostacc(BigDecimal costacc) {
		this.costacc = costacc;
	}

	public BigDecimal getFixedamount() {
		return this.fixedamount;
	}

	public void setFixedamount(BigDecimal fixedamount) {
		this.fixedamount = fixedamount;
	}

	public BigDecimal getFixedprice() {
		return this.fixedprice;
	}

	public void setFixedprice(BigDecimal fixedprice) {
		this.fixedprice = fixedprice;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getObjcode() {
		return this.objcode;
	}

	public void setObjcode(String objcode) {
		this.objcode = objcode;
	}

	public BigDecimal getOpgamount() {
		return this.opgamount;
	}

	public void setOpgamount(BigDecimal opgamount) {
		this.opgamount = opgamount;
	}

	public String getOpgrp() {
		return this.opgrp;
	}

	public void setOpgrp(String opgrp) {
		this.opgrp = opgrp;
	}

	public String getOprn() {
		return this.oprn;
	}

	public void setOprn(String oprn) {
		this.oprn = oprn;
	}

	public String getOprnavailability() {
		return this.oprnavailability;
	}

	public void setOprnavailability(String oprnavailability) {
		this.oprnavailability = oprnavailability;
	}

	public BigDecimal getOprncostacc() {
		return this.oprncostacc;
	}

	public void setOprncostacc(BigDecimal oprncostacc) {
		this.oprncostacc = oprncostacc;
	}

	public BigDecimal getOprnqty() {
		return this.oprnqty;
	}

	public void setOprnqty(BigDecimal oprnqty) {
		this.oprnqty = oprnqty;
	}

	public String getOprnuom() {
		return this.oprnuom;
	}

	public void setOprnuom(String oprnuom) {
		this.oprnuom = oprnuom;
	}

	public String getOrgncode() {
		return this.orgncode;
	}

	public void setOrgncode(String orgncode) {
		this.orgncode = orgncode;
	}

	public BigDecimal getPeriod() {
		return this.period;
	}

	public void setPeriod(BigDecimal period) {
		this.period = period;
	}

	public BigDecimal getPostedamount() {
		return this.postedamount;
	}

	public void setPostedamount(BigDecimal postedamount) {
		this.postedamount = postedamount;
	}

	public BigDecimal getTotalamount() {
		return this.totalamount;
	}

	public void setTotalamount(BigDecimal totalamount) {
		this.totalamount = totalamount;
	}

	public BigDecimal getTotalprice() {
		return this.totalprice;
	}

	public void setTotalprice(BigDecimal totalprice) {
		this.totalprice = totalprice;
	}

	public String getTrnsevent() {
		return this.trnsevent;
	}

	public void setTrnsevent(String trnsevent) {
		this.trnsevent = trnsevent;
	}

	public BigDecimal getVariableamount() {
		return this.variableamount;
	}

	public void setVariableamount(BigDecimal variableamount) {
		this.variableamount = variableamount;
	}

	public BigDecimal getVariableprice() {
		return this.variableprice;
	}

	public void setVariableprice(BigDecimal variableprice) {
		this.variableprice = variableprice;
	}

	public BigDecimal getYear() {
		return this.year;
	}

	public void setYear(BigDecimal year) {
		this.year = year;
	}

	public String getCurro() {
		return curro;
	}

	public void setCurro(String curro) {
		this.curro = curro;
	}

	public String getPricebase() {
		return pricebase;
	}

	public void setPricebase(String pricebase) {
		this.pricebase = pricebase;
	}
	
	

	public String getDatatyp() {
		return datatyp;
	}

	public void setDatatyp(String datatyp) {
		this.datatyp = datatyp;
	}

	public BigDecimal getPriceuom() {
		return priceuom;
	}

	public void setPriceuom(BigDecimal priceuom) {
		this.priceuom = priceuom;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

}