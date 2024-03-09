package com.cop.model.database;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the actualtotallineitem database table.
 * 
 */
@Entity
@NamedQuery(name="Actualtotallineitem.findAll", query="SELECT a FROM Actualtotallineitem a")

@NamedQuery(name="Actualtotallineitem.generateReportByTrnsevent", query="select transtype , sum(totalamount)from Actualtotallineitem a2 where transtype in ('OC','OR','DE','PC') and ((:plant is null and orgncode= :orgncode) or (plant is not null and plant=:plant)) and year= :year and period =:period group by transtype" )

@NamedQuery(name="Actualtotallineitem.getTRSTamount", query="select 'TRST',coalesce (sum(buqty),0),coalesce (sum(totalamount ),0) from Actualtotallineitem a where transtype in ('STO','PT') and (plant = :plant) and plant!=pplant and year= :year and period =:period and excludeorinclude ='Include'")

@NamedQuery(name="Actualtotallineitem.getTOSTamount", query="select 'TOST',coalesce (sum(buqty),0),coalesce (sum(totalamount ),0) from Actualtotallineitem a where transtype in ('STO-CONTRA','PT-CONTRA') and (plant = :plant) and plant!=pplant and year= :year and period =:period and excludeorinclude ='Include' ")


public class Actualtotallineitem implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private BigDecimal atlipk;

	private String batch;

	private String buom;

	private BigDecimal buqty;

	private String cobjtype;

	private String costed;

	private String costtype;

	private String costupdated;

	private String excludeorinclude;

	private String itemcode;

	private String itemexcluded;

	private String mainmaterialprofit;

	private String orgncode;

	private String pbatch;

	private BigDecimal pcb01;

	private BigDecimal pcb02;

	private BigDecimal pcb03;

	private BigDecimal pcb04;

	private BigDecimal pcb05;

	private BigDecimal pcb06;

	private BigDecimal pcb07;

	private BigDecimal pcb08;

	private BigDecimal pcb09;

	private BigDecimal pcb10;

	private BigDecimal pcb11;

	private BigDecimal pcb12;

	private BigDecimal pcb13;

	private BigDecimal pcb14;

	private BigDecimal pcb15;

	private BigDecimal pcb16;

	private BigDecimal pcb17;

	private BigDecimal pcb18;

	private BigDecimal pcb19;

	private BigDecimal pcb20;

	private BigDecimal period;

	private String pitemcode;

	private String plant;

	private String pplant;

	private String psaleorder;

	private String pstocktype;

	private String pvalsub;

	private BigDecimal ratio;

	private String saleorder;

	private String saleorderitem;

	private String service;

	private String sfgprofitcenter;

	private String sourceobjcode;

	private BigDecimal sourceobjitnum;

	private String stagetype;

	private String stocktype;

	private BigDecimal totalamount;

	private String transtype;

	private String valsub;

	private String vendor;

	private BigDecimal year;

	public Actualtotallineitem() {
	}

	public BigDecimal getAtlipk() {
		return this.atlipk;
	}

	public void setAtlipk(BigDecimal atlipk) {
		this.atlipk = atlipk;
	}

	public String getBatch() {
		return this.batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
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

	public String getCobjtype() {
		return this.cobjtype;
	}

	public void setCobjtype(String cobjtype) {
		this.cobjtype = cobjtype;
	}

	public String getCosted() {
		return this.costed;
	}

	public void setCosted(String costed) {
		this.costed = costed;
	}

	public String getCosttype() {
		return this.costtype;
	}

	public void setCosttype(String costtype) {
		this.costtype = costtype;
	}

	public String getCostupdated() {
		return this.costupdated;
	}

	public void setCostupdated(String costupdated) {
		this.costupdated = costupdated;
	}

	public String getExcludeorinclude() {
		return this.excludeorinclude;
	}

	public void setExcludeorinclude(String excludeorinclude) {
		this.excludeorinclude = excludeorinclude;
	}

	public String getItemcode() {
		return this.itemcode;
	}

	public void setItemcode(String itemcode) {
		this.itemcode = itemcode;
	}

	public String getItemexcluded() {
		return this.itemexcluded;
	}

	public void setItemexcluded(String itemexcluded) {
		this.itemexcluded = itemexcluded;
	}

	public String getMainmaterialprofit() {
		return this.mainmaterialprofit;
	}

	public void setMainmaterialprofit(String mainmaterialprofit) {
		this.mainmaterialprofit = mainmaterialprofit;
	}

	public String getOrgncode() {
		return this.orgncode;
	}

	public void setOrgncode(String orgncode) {
		this.orgncode = orgncode;
	}

	public String getPbatch() {
		return this.pbatch;
	}

	public void setPbatch(String pbatch) {
		this.pbatch = pbatch;
	}

	public BigDecimal getPcb01() {
		return this.pcb01;
	}

	public void setPcb01(BigDecimal pcb01) {
		this.pcb01 = pcb01;
	}

	public BigDecimal getPcb02() {
		return this.pcb02;
	}

	public void setPcb02(BigDecimal pcb02) {
		this.pcb02 = pcb02;
	}

	public BigDecimal getPcb03() {
		return this.pcb03;
	}

	public void setPcb03(BigDecimal pcb03) {
		this.pcb03 = pcb03;
	}

	public BigDecimal getPcb04() {
		return this.pcb04;
	}

	public void setPcb04(BigDecimal pcb04) {
		this.pcb04 = pcb04;
	}

	public BigDecimal getPcb05() {
		return this.pcb05;
	}

	public void setPcb05(BigDecimal pcb05) {
		this.pcb05 = pcb05;
	}

	public BigDecimal getPcb06() {
		return this.pcb06;
	}

	public void setPcb06(BigDecimal pcb06) {
		this.pcb06 = pcb06;
	}

	public BigDecimal getPcb07() {
		return this.pcb07;
	}

	public void setPcb07(BigDecimal pcb07) {
		this.pcb07 = pcb07;
	}

	public BigDecimal getPcb08() {
		return this.pcb08;
	}

	public void setPcb08(BigDecimal pcb08) {
		this.pcb08 = pcb08;
	}

	public BigDecimal getPcb09() {
		return this.pcb09;
	}

	public void setPcb09(BigDecimal pcb09) {
		this.pcb09 = pcb09;
	}

	public BigDecimal getPcb10() {
		return this.pcb10;
	}

	public void setPcb10(BigDecimal pcb10) {
		this.pcb10 = pcb10;
	}

	public BigDecimal getPcb11() {
		return this.pcb11;
	}

	public void setPcb11(BigDecimal pcb11) {
		this.pcb11 = pcb11;
	}

	public BigDecimal getPcb12() {
		return this.pcb12;
	}

	public void setPcb12(BigDecimal pcb12) {
		this.pcb12 = pcb12;
	}

	public BigDecimal getPcb13() {
		return this.pcb13;
	}

	public void setPcb13(BigDecimal pcb13) {
		this.pcb13 = pcb13;
	}

	public BigDecimal getPcb14() {
		return this.pcb14;
	}

	public void setPcb14(BigDecimal pcb14) {
		this.pcb14 = pcb14;
	}

	public BigDecimal getPcb15() {
		return this.pcb15;
	}

	public void setPcb15(BigDecimal pcb15) {
		this.pcb15 = pcb15;
	}

	public BigDecimal getPcb16() {
		return this.pcb16;
	}

	public void setPcb16(BigDecimal pcb16) {
		this.pcb16 = pcb16;
	}

	public BigDecimal getPcb17() {
		return this.pcb17;
	}

	public void setPcb17(BigDecimal pcb17) {
		this.pcb17 = pcb17;
	}

	public BigDecimal getPcb18() {
		return this.pcb18;
	}

	public void setPcb18(BigDecimal pcb18) {
		this.pcb18 = pcb18;
	}

	public BigDecimal getPcb19() {
		return this.pcb19;
	}

	public void setPcb19(BigDecimal pcb19) {
		this.pcb19 = pcb19;
	}

	public BigDecimal getPcb20() {
		return this.pcb20;
	}

	public void setPcb20(BigDecimal pcb20) {
		this.pcb20 = pcb20;
	}

	public BigDecimal getPeriod() {
		return this.period;
	}

	public void setPeriod(BigDecimal period) {
		this.period = period;
	}

	public String getPitemcode() {
		return this.pitemcode;
	}

	public void setPitemcode(String pitemcode) {
		this.pitemcode = pitemcode;
	}

	public String getPlant() {
		return this.plant;
	}

	public void setPlant(String plant) {
		this.plant = plant;
	}

	public String getPplant() {
		return this.pplant;
	}

	public void setPplant(String pplant) {
		this.pplant = pplant;
	}

	public String getPsaleorder() {
		return this.psaleorder;
	}

	public void setPsaleorder(String psaleorder) {
		this.psaleorder = psaleorder;
	}

	public String getPstocktype() {
		return this.pstocktype;
	}

	public void setPstocktype(String pstocktype) {
		this.pstocktype = pstocktype;
	}

	public String getPvalsub() {
		return this.pvalsub;
	}

	public void setPvalsub(String pvalsub) {
		this.pvalsub = pvalsub;
	}

	public BigDecimal getRatio() {
		return this.ratio;
	}

	public void setRatio(BigDecimal ratio) {
		this.ratio = ratio;
	}

	public String getSaleorder() {
		return this.saleorder;
	}

	public void setSaleorder(String saleorder) {
		this.saleorder = saleorder;
	}

	public String getSaleorderitem() {
		return this.saleorderitem;
	}

	public void setSaleorderitem(String saleorderitem) {
		this.saleorderitem = saleorderitem;
	}

	public String getService() {
		return this.service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getSfgprofitcenter() {
		return this.sfgprofitcenter;
	}

	public void setSfgprofitcenter(String sfgprofitcenter) {
		this.sfgprofitcenter = sfgprofitcenter;
	}

	public String getSourceobjcode() {
		return this.sourceobjcode;
	}

	public void setSourceobjcode(String sourceobjcode) {
		this.sourceobjcode = sourceobjcode;
	}

	public BigDecimal getSourceobjitnum() {
		return this.sourceobjitnum;
	}

	public void setSourceobjitnum(BigDecimal sourceobjitnum) {
		this.sourceobjitnum = sourceobjitnum;
	}

	public String getStagetype() {
		return this.stagetype;
	}

	public void setStagetype(String stagetype) {
		this.stagetype = stagetype;
	}

	public String getStocktype() {
		return this.stocktype;
	}

	public void setStocktype(String stocktype) {
		this.stocktype = stocktype;
	}

	public BigDecimal getTotalamount() {
		return this.totalamount;
	}

	public void setTotalamount(BigDecimal totalamount) {
		this.totalamount = totalamount;
	}

	public String getTranstype() {
		return this.transtype;
	}

	public void setTranstype(String transtype) {
		this.transtype = transtype;
	}

	public String getValsub() {
		return this.valsub;
	}

	public void setValsub(String valsub) {
		this.valsub = valsub;
	}

	public String getVendor() {
		return this.vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public BigDecimal getYear() {
		return this.year;
	}

	public void setYear(BigDecimal year) {
		this.year = year;
	}

}