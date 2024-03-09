package com.cop.model.database;

import java.io.Serializable;
import javax.persistence.*;

import com.cop.model.converter.BigDecimalConverter;
import com.cop.model.converter.DateConverter;
import com.cop.model.converter.StringConverter;
import com.creditdatamw.zerocell.annotation.Column;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the bom database table.
 * 
 */
@Entity
@NamedQuery(name="Bom.findAll", query="SELECT b FROM Bom b where is_delete is null")
@NamedQuery(name="Bom.findAllbyBomcodeandItemcode", query="select b from Bom b where upper(b.bhmatl)=upper(:bhmatl) and b.bomcode=:bomcode and upper(b.plant)=upper(:plant)")
@NamedQuery(name="Bom.findAllbyBomcodeandImatl", query="select b from Bom b where upper(b.imatl)=upper(:imatl) and b.bomcode=:bomcode and upper(b.plant)=upper(:plant)")
@NamedQuery(name="Bom.softdeleteBom",query="update Bom  set is_delete=:is_delete where bompk=:bompk")
public class Bom implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private BigDecimal bompk;
	@Column(index = 0, name = "bomcode", converterClass = StringConverter.class)
	private String bomcode;
	@Column(index = 1, name = "plant", converterClass = StringConverter.class)
	private String plant;
	@Column(index = 2, name = "bhmatl", converterClass = StringConverter.class)
	private String bhmatl;
	@Column(index = 3, name = "buom", converterClass = StringConverter.class)
	private String buom;
	@Column(index = 4, name = "buqty", converterClass = BigDecimalConverter.class)
	private BigDecimal buqty;
	@Column(index = 5, name = "prduom", converterClass = StringConverter.class)
	private String prduom;
	@Column(index =6 , name = "prduomqty", converterClass = BigDecimalConverter.class)
	private BigDecimal prduomqty;
	@Column(index = 7, name = "cnvbuomqty", converterClass = BigDecimalConverter.class)
	private BigDecimal cnvbuomqty;
	@Column(index = 8, name = "version", converterClass = BigDecimalConverter.class)
	private BigDecimal version;
	@Column(index = 9, name = "cnvpuomqty", converterClass = BigDecimalConverter.class)
	private BigDecimal cnvpuomqty;
	@Column(index = 10, name = "valfdate", converterClass =  DateConverter.class)
	@Temporal(TemporalType.DATE)
	private Date valfdate;
	@Column(index = 11, name = "valtdate", converterClass =  DateConverter.class)
	@Temporal(TemporalType.DATE)
	private Date valtdate;
	@Column(index = 12, name = "islnum", converterClass = BigDecimalConverter.class)
	private BigDecimal islnum;
	@Column(index = 13, name = "imatl", converterClass = StringConverter.class)
	private String imatl;
	@Column(index = 14, name = "ibuom", converterClass = StringConverter.class)
	private String ibuom;
	@Column(index = 15, name = "ibuqty", converterClass = BigDecimalConverter.class)
	private BigDecimal ibuqty;
	@Column(index = 16, name = "coprind", converterClass = StringConverter.class)
	private String coprind;
	@Column(index = 17, name = "copratio", converterClass = BigDecimalConverter.class)
	private BigDecimal copratio;
	@Column(index = 18, name = "hmratio", converterClass = BigDecimalConverter.class)
	private BigDecimal hmratio;
	@Column(index = 19, name = "apptype", converterClass = StringConverter.class)
	private String apptype;
	@Column(index = 20, name = "ustyp", converterClass = StringConverter.class)
	private String ustyp;
	@Column(index = 21, name = "ustypd", converterClass = StringConverter.class)
	private String ustypd;
	@Column(index = 22, name = "ostat", converterClass = StringConverter.class)
	private String ostat;
	@Column(index = 23, name = "matlcode", converterClass = StringConverter.class)
	private String is_delete;
	
	

	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}

	public Bom() {
	}

	public String getApptype() {
		return this.apptype;
	}

	public void setApptype(String apptype) {
		this.apptype = apptype;
	}

	public String getBhmatl() {
		return this.bhmatl;
	}

	public void setBhmatl(String bhmatl) {
		this.bhmatl = bhmatl;
	}

	public String getBomcode() {
		return this.bomcode;
	}

	public void setBomcode(String bomcode) {
		this.bomcode = bomcode;
	}

	public BigDecimal getBompk() {
		return this.bompk;
	}

	public void setBompk(BigDecimal bompk) {
		this.bompk = bompk;
	}

	public String getBuom() {
		return this.buom;
	}

	public void setBuom(String buom) {
		this.buom = buom;
	}

	public BigDecimal getBuqty() {
		return this.buqty;
	}

	public void setBuqty(BigDecimal buqty) {
		this.buqty = buqty;
	}

	public BigDecimal getCnvbuomqty() {
		return this.cnvbuomqty;
	}

	public void setCnvbuomqty(BigDecimal cnvbuomqty) {
		this.cnvbuomqty = cnvbuomqty;
	}

	public BigDecimal getCnvpuomqty() {
		return this.cnvpuomqty;
	}

	public void setCnvpuomqty(BigDecimal cnvpuomqty) {
		this.cnvpuomqty = cnvpuomqty;
	}

	public BigDecimal getCopratio() {
		return this.copratio;
	}

	public void setCopratio(BigDecimal copratio) {
		this.copratio = copratio;
	}

	public String getCoprind() {
		return this.coprind;
	}

	public void setCoprind(String coprind) {
		this.coprind = coprind;
	}

	public BigDecimal getHmratio() {
		return this.hmratio;
	}

	public void setHmratio(BigDecimal hmratio) {
		this.hmratio = hmratio;
	}

	public String getIbuom() {
		return this.ibuom;
	}

	public void setIbuom(String ibuom) {
		this.ibuom = ibuom;
	}

	public BigDecimal getIbuqty() {
		return this.ibuqty;
	}

	public void setIbuqty(BigDecimal ibuqty) {
		this.ibuqty = ibuqty;
	}

	public String getImatl() {
		return this.imatl;
	}

	public void setImatl(String imatl) {
		this.imatl = imatl;
	}

	public BigDecimal getIslnum() {
		return this.islnum;
	}

	public void setIslnum(BigDecimal islnum) {
		this.islnum = islnum;
	}

	public String getOstat() {
		return this.ostat;
	}

	public void setOstat(String ostat) {
		this.ostat = ostat;
	}

	public String getPlant() {
		return this.plant;
	}

	public void setPlant(String plant) {
		this.plant = plant;
	}

	public String getPrduom() {
		return this.prduom;
	}

	public void setPrduom(String prduom) {
		this.prduom = prduom;
	}

	public BigDecimal getPrduomqty() {
		return this.prduomqty;
	}

	public void setPrduomqty(BigDecimal prduomqty) {
		this.prduomqty = prduomqty;
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

	public Date getValfdate() {
		return this.valfdate;
	}

	public void setValfdate(Date valfdate) {
		this.valfdate = valfdate;
	}

	public Date getValtdate() {
		return this.valtdate;
	}

	public void setValtdate(Date valtdate) {
		this.valtdate = valtdate;
	}

	public BigDecimal getVersion() {
		return this.version;
	}

	public void setVersion(BigDecimal version) {
		this.version = version;
	}

}