package com.cop.model.database;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.repository.Query;

import com.cop.model.generator.StringPrefixedSequenceIdGenerator;

import lombok.Getter;
import lombok.Setter;

/**
 * The persistent class for the mmtotal database table.
 *
 */
@Entity
@Getter
@Setter
@NamedQuery(name = "Mmtotal.findAll", query = "SELECT m FROM Mmtotal m")
//to find the instransitvalue by objcode and objitnum
@NamedQuery(name="Mmtotal.findAllbyObjcodeandObjitnum", query="SELECT m from Mmtotal m where upper(m.objcode)=upper(:objcode) and m.objitnum=:objitnum and m.date<=:date order by m.date desc,m.mmtotalid desc")
@NamedQuery(name="Mmtotal.findAllbytrnseventandcobjtypeitemcode", query="SELECT m from Mmtotal m where upper(itemcode)=(:itemcode) and upper(cobjtype)=(:cobjtype) and upper(trnsevent)=upper(:trnsevent)")
@NamedQuery(name = "Mmtotal.findMaxCstockbyPlant", query = "select m.cstock , m.stockvalue  from Mmtotal m where m.itemcode=:matlcode and m.plant=:plant and m.date<=:date order by m.date desc, m.mmtotalid desc")
@NamedQuery(name = "Mmtotal.findCstockbyPlantandstocktype", query = "select m.cstock , m.stockvalue  from Mmtotal m where m.itemcode=:matlcode and m.plant=:plant and m.date<=:date and m.stocktype=:stocktype order by m.date desc, m.mmtotalid desc ")
@NamedQuery(name = "Mmtotal.findCstockStockValueforOs", query = "select m.cstock , m.stockvalue  from Mmtotal m where m.itemcode=:matlcode and m.plant=:plant and m.date<=:date and m.stocktype=:stocktype or m.vendor=:vendor order by m.date desc, m.mmtotalid desc ")
@NamedQuery(name = "Mmtotal.findAllFromOs", query = "select m from Mmtotal m where m.stocktype ='O' and m.plant=:plant and m.itemcode=:matlcode  and m.date<=:date order by m.date desc, m.mmtotalid desc")
@NamedQuery(name = "Mmtotal.findOrderAmountbytrnsevent", query = "select m.ordamount from Mmtotal m where m.cobjtype=:cobjtype and m.objcode=:objcode and m.trnsevent=:trnsevent order by m.date desc, m.mmtotalid desc")
@NamedQuery(name = "Mmtotal.findCstockStockValuebyEvent", query = "select m.cstock , m.stockvalue  from Mmtotal m where upper(m.itemcode)=upper(:matlcode) and upper(m.plant)=upper(:plant) and m.date<=:date and upper(m.trnsevent)=upper(:trnsevent) order by m.date desc, m.mmtotalid desc ")
@NamedQuery(name = "Mmtotal.findsumofOrdamountbytrnsevent", query = "select SUM(m.ordamount) from Mmtotal m where upper(m.cobjtype)=upper(:cobjtype) and m.objcode=:objcode and upper(m.trnsevent)=(:trnsevent)")

@NamedQuery(name = "Mmtotal.findCstockbyItemcodePlantValsubBatch", query = "select m.cstock , m.stockvalue ,m.bstock from Mmtotal m where m.itemcode=:matlcode "
		+ "and m.plant=:plant and m.date<=:date and (:valsub is null or (m.valsub is not null and m.valsub = :valsub))and "
		+ "(:batchnumber is null or  (m.batchnumber is not null and m.batchnumber = :batchnumber))and ((:saleorder is null and m.saleorder is null) or m.saleorder = :saleorder)  order by m.date desc, m.mmtotalid desc ")

@NamedQuery(name = "Mmtotal.findCstockStockvaluebyGRDateandstocktype", query = "select m from Mmtotal m where m.itemcode=:matlcode and m.plant=:plant and m.date>:date and m.stocktype=:stocktype and (:valsub is null or (m.valsub is not null and m.valsub = :valsub))and (:batchnumber is null or  (m.batchnumber is not null and m.batchnumber = :batchnumber))and ((:saleorder is null and m.saleorder is null) or m.saleorder = :saleorder)   order by m.date , m.mmtotalid   ")

@NamedQuery(name = "Mmtotal.findCstockStockvalueforUpdate", query = "select m from Mmtotal m where m.itemcode=:matlcode and m.plant=:plant and m.date>:date and m.stocktype=:stocktype and (:valsub is null or (m.valsub is not null and m.valsub = :valsub))and (:batchnumber is null or  (m.batchnumber is not null and m.batchnumber = :batchnumber))and m.saleorder=:saleorder order by m.date , m.mmtotalid   ")

//cstock and stockvalue based on batchmangement
@NamedQuery(name = "Mmtotal.findCstockStockBatchmanagement",query="select m.cstock , m.stockvalue, m.bstock  from Mmtotal m where m.itemcode=:matlcode and m.plant=:plant and m.date<=:date and m.stocktype=:stocktype and (:valsub is null or (m.valsub is not null and m.valsub = :valsub))and ((:saleorder is null and m.saleorder is null) or m.saleorder = :saleorder)  order by m.date desc, m.mmtotalid desc ")

@NamedQuery(name = "Mmtotal.findCstockStockwithoutBatch",query="select m.cstock , m.stockvalue ,m.bstock from Mmtotal m where m.itemcode=:matlcode and m.plant=:plant and m.date<=:date and m.stocktype=:stocktype and (:valsub is null or (m.valsub is not null and m.valsub = :valsub))and ((:saleorder is null and m.saleorder is null) or m.saleorder = :saleorder)  order by m.date desc, m.mmtotalid desc ")


@NamedQuery(name = "Mmtotal.findCstockStockwithBatch",query="select m.cstock , m.stockvalue ,m.bstock from Mmtotal m where m.itemcode=:matlcode and m.plant=:plant and m.date<=:date and m.stocktype=:stocktype and (:valsub is null or (m.valsub is not null and m.valsub = :valsub))and (:batchnumber is null or  (m.batchnumber is not null and m.batchnumber = :batchnumber))and ((:saleorder is null and m.saleorder is null) or m.saleorder = :saleorder)  order by m.date desc, m.mmtotalid desc ")


@NamedQuery(name = "Mmtotal.findallbyBatchcosting", query = "select m from Mmtotal m where m.itemcode=:matlcode and m.plant=:plant and m.date>:date and m.stocktype=:stocktype and (:valsub is null or (m.valsub is not null and m.valsub = :valsub)) and ((:saleorder is null and m.saleorder is null) or m.saleorder = :saleorder)   order by m.date , m.mmtotalid   ")

@NamedQuery(name = "Mmtotal.generateConsolidatedReport", query = "select\r\n" + 
		"	m.plant as plant,\r\n" + 
		"	m.itemcode as material,\r\n" + 
		"	m.stocktype as stock_type,\r\n" + 
		"	m.saleorder as sale_order,\r\n" + 
		"	m.valsub as valsub,\r\n" + 
		"	m.batchnumber as batch,\r\n" + 
		"	m.buom as buom,\r\n" + 
		"	sum(case when (m.trnsevent ='OS') then m.buomqty else null end) as openingstock,\r\n" + 
		"	sum (case when (m.cobjtype='PUR' and m.trnsevent ='GR') then m.buomqty else null end) as procurement_receipts,\r\n" + 
		"	sum (case when (m.cobjtype='JOB' and m.trnsevent ='GR') then m.buomqty else null end) as job_receipts ,\r\n" + 
		"	sum (case when (m.cobjtype in ('PROD','REWORK') and m.trnsevent in ('GR','BP')) then m.buomqty else null end) as production_receipts ,\r\n" + 
		"	sum (case when (m.cobjtype='STO' and m.trnsevent ='PR') then m.buomqty else null end) as sto_receipts ,\r\n" + 
		"	sum (case when (m.cobjtype='STO' and m.trnsevent ='STO') then m.buomqty else null end) as stointransit_receipts,\r\n" + 
		"	sum (case when (m.trnsevent in ('BT','MT','PT','ST','SOT')) then m.buomqty else null end) as transfer_recipts,\r\n" + 
		"	sum (case when (m.cobjtype='PUR' and m.trnsevent ='GI') then m.buomqty else null end) as procurement_return,\r\n" + 
		"	sum (case when (m.cobjtype='JOB' and m.trnsevent ='GI') then m.buomqty else null end) as job_consumption ,\r\n" + 
		"	sum (case when (m.cobjtype in ('PROD','REWORK') and m.trnsevent ='GI')then m.buomqty else null end) as production_consumption ,\r\n" + 
		"	sum (case when (m.cobjtype='STO' and m.trnsevent ='STO-CONTRA') then m.buomqty else null end) as sto_delivery ,\r\n" + 
		"	sum (case when ( m.trnsevent in ('BT-CONTRA','MT-CONTRA','PT-CONTRA','ST-CONTRA','SOT-CONTRA')) then m.buomqty else null end) as transfer_delivery,\r\n" + 
		"    sum (case when (m.cobjtype='SALE' and m.trnsevent ='SD') then m.buomqty else null end) as sale_delivery,\r\n" + 
		"    sum (case when (m.cobjtype='CTR' and m.trnsevent ='GI') then m.buomqty else null end) as costcenter_consumption,\r\n" + 
		"    sum(case when(m.cobjtype in ('PUR','JOB','PROD','STO') and m.trnsevent in('GR','PR','BT','MT','PT','ST','SOT'))then m.buomqty else null end) as total_receipts ,\r\n" + 
		"    sum(case when((m.cobjtype in ('PUR','JOB','PROD','STO','SALE','CTR')  and m.trnsevent in ('GI','STO-CONTRA','BT-CONTRA','MT-CONTRA','PT-CONTRA','ST-CONTRA','SOT-CONTRA','SD')))then m.buomqty else null end) as total_delivery,\r\n" + 
		"   ( (sum(case when(m.cobjtype in ('PUR','JOB','PROD','STO') and m.trnsevent in('GR','PR','BT','MT','PT','ST','SOT'))then m.buomqty else null end))-(sum(case when((m.cobjtype in ('PUR','JOB','PROD','STO','SALE','CTR')  and m.trnsevent in ('GI','STO-CONTRA','BT-CONTRA','MT-CONTRA','PT-CONTRA','ST-CONTRA','SOT-CONTRA','SD')))then m.buomqty else null end))) as closingstock\r\n" + 
		"    from\r\n" + 
		"	Mmtotal m\r\n" + 
		"where\r\n" + 
		"	( m.plant in (:plant) or m.plant in (select p.plant from Plant p where p.orgncode=:org))  and m.year=:year and m.period =:period \r\n" + 
		"group by  m.plant,m.itemcode ,m.stocktype,m.saleorder,m.valsub ,\r\n" + 
		"	m.batchnumber, m.buom  order by m.itemcode ")


@NamedQuery(name="Mmtotal.generateDetailedReport", query="select  \r\n" + 
		"			m.docnumber as mmdocnum,  \r\n" + 
		"			m.docitnum  as docitnum,  \r\n" + 
		"			m.date as Date,  \r\n" + 
		"			m.docuom  as docuom,  \r\n" + 
		"			m.docqty  as docuomqty,  \r\n" + 
		"			m.cobjtype as objtype,  \r\n" + 
		"			m.objcode  as objnum,  \r\n" + 
		"			m.objitnum  as objitnum,  \r\n" + 
		"			m.plant as plant,  \r\n" + 
		"			m.itemcode as material,  \r\n" + 
		"			m.stocktype as stock_type,  \r\n" + 
		"			m.saleorder as sale_order,  \r\n" + 
		"			m.valsub as valsub,  \r\n" + 
		"			m.batchnumber as batch,  \r\n" + 
		"			m.buom as buom,  \r\n" + 
		"			sum(case when (m.trnsevent ='OS') then m.buomqty else  null end) as openingstock,  \r\n" + 
		"			sum (case when (m.cobjtype='PUR' and m.trnsevent ='GR') then m.buomqty else  null end) as procurement_receipts,  \r\n" + 
		"			sum (case when (m.cobjtype='JOB' and m.trnsevent ='GR') then m.buomqty else  null end) as job_receipts ,  \r\n" + 
		"			sum (case when (m.cobjtype in ('PROD','REWORK') and m.trnsevent in ('GR','BP')) then m.buomqty else  null end) as production_receipts ,  \r\n" + 
		"			sum (case when (m.cobjtype='STO' and m.trnsevent ='PR') then m.buomqty else  null end) as sto_receipts ,  \r\n" + 
		"			sum (case when (m.cobjtype='STO' and m.trnsevent ='STO') then m.buomqty else  null end) as stointransit_receipts,  \r\n" + 
		"			sum (case when (m.trnsevent in ('BT','MT','PT','ST','SOT')) then m.buomqty else  null end) as transfer_recipts,  \r\n" + 
		"			sum (case when (m.cobjtype='PUR' and m.trnsevent ='GRT') then m.buomqty else  null end) as procurement_return,  \r\n" + 
		"			sum (case when (m.cobjtype='JOB' and m.trnsevent ='GI') then m.buomqty else  null end) as job_consumption ,  \r\n" + 
		"			sum (case when (m.cobjtype in ('PROD','REWORK') and m.trnsevent ='GI') then m.buomqty else  null end) as production_consumption ,  \r\n" + 
		"			sum (case when (m.cobjtype='STO' and m.trnsevent ='STO-CONTRA') then m.buomqty else  null end) as sto_delivery ,  \r\n" + 
		"          	sum (case when ( m.cobjtype='SRO' and  m.trnsevent ='SDT') then m.buomqty else  null end) as sale_return,  \r\n" + 
		"			sum (case when ( m.trnsevent in ('BT-CONTRA','MT-CONTRA','PT-CONTRA','ST-CONTRA','SOT-CONTRA')) then m.buomqty else  null end) as transfer_delivery,  \r\n" + 
		"		    sum (case when (m.cobjtype='SALE' and m.trnsevent ='SD') then m.buomqty else  null end) as sale_delivery,  \r\n" + 
		"    		sum (case when (m.cobjtype='CTR' and m.trnsevent ='GI') then m.buomqty else null end) as costcenter_consumption \r\n" + 
		"		    from 	Mmtotal m where m.trnsevent not in ('IB','IB-CONTRA','DNQR','DNQR-CONTRA','CNVR','CNVR-CONTRA','DNVR','DNVR-CONTRA','SR','SR-CONTRA','CNQR','CNQR-CONTRA')  \r\n" + 
		"			 and ( m.plant in (select plant from Plant where orgncode=:org) or m.plant in (:plant) ) and m.year=:year and m.period= :period   \r\n" + 
		"			group by  m.plant,m.itemcode ,m.stocktype,m.saleorder,m.valsub ,  \r\n" + 
		"			m.batchnumber, m.buom ,m.docnumber ,m.docitnum ,m.date,m.docuom ,m.docqty,m.cobjtype ,m.objcode ,m.objitnum order by m.itemcode")

public class Mmtotal implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mmtotal_seq")
	@GenericGenerator(name = "mmtotal_seq", strategy = "com.cop.model.generator.StringPrefixedSequenceIdGenerator", parameters = {
			@Parameter(name = StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "1"),
			@Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "MM"),
			@Parameter(name = StringPrefixedSequenceIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%09d") })
	private String mmtotalid;

	private String adindicator;

	private BigDecimal amountincurro;

	private String batchnumber;

	private String buom;

	private BigDecimal buomqty;

	private BigDecimal costacc;

	private BigDecimal cstock;

	private String customer;

	@Temporal(TemporalType.DATE)
	private Date date;

	private BigDecimal docitnum;

	private BigDecimal docnumber;

	private BigDecimal docqty;

	private String docuom;

	private BigDecimal intransitqty;

	private BigDecimal intransitvalue;

	private String linkedobjcode;

	private BigDecimal linkedobjitnum;

	private String itemcode;

	private String objcode;

	private BigDecimal objitnum;

	private BigDecimal ordamount;

	private String ordcurrency;

	private String cobjtype;

	private BigDecimal period;

	private String plant;

	private BigDecimal prdamount;

	private BigDecimal prdqty;

	private String sbatch;

	private String smaterial;

	private String splant;

	private String sstype;

	private String stocktype;

	private BigDecimal stockvalue;

	private String svalsub;

	private String svendor;

	private String tbatch;

	private String tmaterial;

	private String tplant;

	private String trnsevent;

	private String tstype;

	private String tvalsub;

	private String tvendor;

	private String valsub;

	private String vendor;

	private BigDecimal year;

	private String saleorder;

	private BigDecimal refdocnum;

	private BigDecimal reversaldocument;

	private String reversalindicator;

	private String docdesc;

	private String usercode;

	private Timestamp createdtime;

	private BigDecimal plnum;

	private String plcomp;

	private BigDecimal exchangerate;
	
	private BigDecimal exchangeratediff;
	
	private BigDecimal unsettledprd;
	
	private String sso;
	
	private String tso;
	
	private BigDecimal bstock;
	
	@Transient
	private BigDecimal price;
	
	private String saleorderitem;
	
	public Mmtotal() {
	}

	@Override
	public String toString() {
		return "Mmtotal [mmtotalid=" + mmtotalid + ", adindicator=" + adindicator + ", amountincurro=" + amountincurro
				+ ", batchnumber=" + batchnumber + ", buom=" + buom + ", buomqty=" + buomqty + ", costacc=" + costacc
				+ ", cstock=" + cstock + ", customer=" + customer + ", date=" + date + ", docitnum=" + docitnum
				+ ", docnumber=" + docnumber + ", docqty=" + docqty + ", docuom=" + docuom + ", intransitqty="
				+ intransitqty + ", intransitvalue=" + intransitvalue + ", linkedobjcode=" + linkedobjcode
				+ ", linkedobjitnum=" + linkedobjitnum + ", itemcode=" + itemcode + ", objcode=" + objcode
				+ ", objitnum=" + objitnum + ", ordamount=" + ordamount + ", ordcurrency=" + ordcurrency + ", cobjtype="
				+ cobjtype + ", period=" + period + ", plant=" + plant + ", prdamount=" + prdamount + ", prdqty="
				+ prdqty + ", sbatch=" + sbatch + ", smaterial=" + smaterial + ", splant=" + splant + ", sstype="
				+ sstype + ", stocktype=" + stocktype + ", stockvalue=" + stockvalue + ", svalsub=" + svalsub
				+ ", svendor=" + svendor + ", tbatch=" + tbatch + ", tmaterial=" + tmaterial + ", tplant=" + tplant
				+ ", trnsevent=" + trnsevent + ", tstype=" + tstype + ", tvalsub=" + tvalsub + ", tvendor=" + tvendor
				+ ", valsub=" + valsub + ", vendor=" + vendor + ", year=" + year + ", saleorder=" + saleorder
				+ ", refdocnum=" + refdocnum + ", reversaldocument=" + reversaldocument + ", reversalindicator="
				+ reversalindicator + ", docdesc=" + docdesc + ", usercode=" + usercode + ", createdtime=" + createdtime
				+ ", plnum=" + plnum + ", plcomp=" + plcomp + ", exchangerate=" + exchangerate + ", exchangeratediff="
				+ exchangeratediff + ", unsettledprd=" + unsettledprd + ", sso=" + sso + ", tso=" + tso + ",saleorderitem=" + saleorderitem +"]";
	}

}