package com.cop.model.database;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.cop.model.converter.DateConverter;
import com.cop.model.converter.StringConverter;
import com.creditdatamw.zerocell.annotation.Column;

/**
 * The persistent class for the mrc database table.
 *
 */
@Entity
//@NamedQuery(name = "Mrc.findAll", query = "SELECT m FROM Mrc m")

@NamedQuery(name = "Mrc.findAllbyValsubbatchocsting", query = "select m from Mrc m where upper(m.itemcode)=upper(:itemcode) and upper(m.plant)=upper(:plant) and :date between m.valfdate and m.valtdate and ((:valsub is null and m.valsub is null) or m.valsub = :valsub) and ((:batchnumber is null and m.batchnumber is null) or m.batchnumber = :batchnumber)")

@NamedQuery(name = "Mrc.findAllbyMpricetypValsub", query = "select m from Mrc m where upper(m.itemcode)=upper(:itemcode) and upper(m.plant)=upper(:plant) and :date between m.valfdate and m.valtdate and upper(m.pricetype)=upper(:pricetype) and ((:valsub is null and m.valsub is null) or m.valsub = :valsub) and ((:batchnumber is null and m.batchnumber is null) or m.batchnumber = :batchnumber)")

@NamedQuery(name="Mrc.softdeleteMrc",query="update Mrc  set is_delete=:is_delete where mrcpk=:mrcpk")
public class Mrc implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private BigDecimal mrcpk;

	@Column(index = 0, name = "itemcode", converterClass = StringConverter.class)
	private String itemcode;
	@Column(index = 1, name = "itemtype", converterClass = StringConverter.class)
	private String itemtype;
	@Column(index = 2, name = "plant", converterClass = StringConverter.class)
	private String plant;
	@Column(index = 3, name = "sname", converterClass = StringConverter.class)
	private String sname;
	@Column(index = 4, name = "pricetype", converterClass = StringConverter.class)
	private String pricetype;
	@Column(index = 5, name = "valfdate", converterClass = DateConverter.class)
	@Temporal(TemporalType.DATE)
	private Date valfdate;
	@Column(index = 6, name = "valtdate", converterClass = DateConverter.class)
	@Temporal(TemporalType.DATE)
	private Date valtdate;
	@Column(index = 7, name = "buom", converterClass = StringConverter.class)
	private String buom;
	@Column(index = 8, name = "buqty", converterClass = BigDecimal.class)
	private BigDecimal buqty;
	@Column(index = 9, name = "price", converterClass = BigDecimal.class)
	private BigDecimal price;
	@Column(index = 10, name = "valsub", converterClass = StringConverter.class)
	private String valsub;
	@Column(index = 11, name = "batchnumber", converterClass = StringConverter.class)
	private String batchnumber;

	private String is_delete;

	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}

	public String getItemcode() {
		return itemcode;
	}

	public void setItemcode(String itemcode) {
		this.itemcode = itemcode;
	}

	public String getPricetype() {
		return pricetype;
	}

	public void setPricetype(String pricetype) {
		this.pricetype = pricetype;
	}

	public String getBatchnumber() {
		return batchnumber;
	}

	public void setBatchnumber(String batchnumber) {
		this.batchnumber = batchnumber;
	}

	public String getValsub() {
		return valsub;
	}

	public void setValsub(String valsub) {
		this.valsub = valsub;
	}

	

	public Mrc() {
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

	public String getMatlcode() {
		return itemcode;
	}

	public void setMatlcode(String matlcode) {
		itemcode = matlcode;
	}

	public String getPricetyp() {
		return pricetype;
	}

	public void setPricetyp(String mpricetyp) {
		pricetype = mpricetyp;
	}

	public String getPlant() {
		return plant;
	}

	public void setPlant(String plant) {
		this.plant = plant;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
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

	public String getItemtype() {
		return itemtype;
	}

	public void setItemtype(String itemtype) {
		this.itemtype = itemtype;
	}
	

	public BigDecimal getMrcpk() {
		return mrcpk;
	}

	public void setMrcpk(BigDecimal mrcpk) {
		this.mrcpk = mrcpk;
	}

}