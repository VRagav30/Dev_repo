package com.cop.model.database;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.cop.model.generator.StringPrefixedSequenceIdGenerator;

import lombok.Getter;
import lombok.Setter;



/**
 * The persistent class for the actualtotal database table.
 * 
 */
@Entity
@Getter
@Setter
@NamedQuery(name="Actualtotal.findAll", query="SELECT a FROM Actualtotal a")

@NamedQuery(name="Actualtotal.generateReconReport",query="select  \r\n" + 
		"	quantitytype, \r\n" + 
		"	coalesce(sum(buqty ),0) , coalesce(sum(totalcost),0)  from Actualtotal a "
		+ "where (((:plant is null) and (a.plant in (select p.plant from Plant p where p.orgncode= :org) )) or (:plant is not null and a.plant in (:plant))) "
		+ " and (itemcode = :itemcode  or :itemcode is null) and (stocktype =:stocktype or :stocktype is null) and (:saleorder is null or saleorder=:saleorder ) "
		+ " and year = :year and period = :period and a.quantitytype "
		+ " in (	'OPST' ,'MOST' ,'WOST' ,'BMGR' ,'BMSR' ,'BMVD' ,'BSVD' ,'BMNA' ,'BSNA' ,'BSQD' ,'SRST' ,'SMSR' ,'SVGR','SMVD' ,'SSNA','ILST','SDST','WIST','SSVD','SMNA','SSQD','SVNA') group by a.quantitytype \r\n" + 
		"	")

@NamedQuery(name="Actualtotal.generateMaterialReport",query="select  \r\n" + 
		"	quantitytype, \r\n" + 
		"	coalesce(sum(buqty ),0) , coalesce(sum(totalcost),0)  from Actualtotal a "
		+ "where (((:plant is null) and (a.plant in (select p.plant from Plant p where p.orgncode= :org) )) or (:plant is not null and a.plant in (:plant))) "
		+ " and (itemcode = :itemcode) and (stocktype =:stocktype or :stocktype is null) and (:saleorder is null or saleorder=:saleorder ) "
		+ " and year = :year and period = :period and a.quantitytype "
		+ " in (	'OPST' ,'MOST' ,'WOST' ,'BMGR' ,'BMSR' ,'BMVD' ,'BSVD' ,'BMNA' ,'BSNA' ,'BSQD' ,'SRST' ,'SMSR' ,'SVGR','SMVD' ,'SSNA','ILST','SDST','WIST','SSVD','SMNA','SSQD','SVNA') group by a.quantitytype \r\n" + 
		"	")

@NamedQuery(name="Actualtotal.generateQuantityTypeReport",query ="select itemcode,valsub,batchnumber ,saleorder ,stocktype ,plant ,buqty,totalcost,atpk from Actualtotal a where ((:plant is null and a.plant in (select p.plant from Plant p where p.orgncode= :org )) or (:plant is not null and a.plant in (:plant))) and year = :year and period = :period and a.quantitytype =:quantitytype")


@NamedQuery(name="Actualtotal.generatePcbValuesReport",query =" SELECT pcbcode1, pcbcode2, pcbcode3, pcbcode4, pcbcode5, pcbcode6, pcbcode7, pcbcode8, pcbcode9, pcbcode10, pcbcode11, pcbcode12, pcbcode13, pcbcode14, pcbcode15, pcbcode16, pcbcode17, pcbcode18, pcbcode19, pcbcode20\r\n" + 
		"FROM Actualtotal \r\n" + 
		"WHERE atpk=:atpk and quantitytype =:quantitytype")


@NamedQuery(name="Actualtotal.getTotalCostbyMattype",query="select coalesce(sum(totalcost),0) from Actualtotal a  where quantitytype ='MCST' and ((:plant is null and a.plant in (select p.plant from Plant p where p.orgncode= :org )) or (:plant is not null and a.plant in (:plant))) and year = :year and period = :period and a.itemcode in (select m.matlcode from Material m where m.mattyp = :matType)")
// @NamedQuery(name="Actualtotal.getTotalCostforAllMattype",query="select sum(totalcost), m.mattyp from Actualtotal a , Material m where quantitytype ='MCST' and a.itemcode =m.matlcode group by m.mattyp")

public class Actualtotal implements Serializable {
	private static final long serialVersionUID = 1L;
@Id
@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "actualtotal_seq")
@GenericGenerator(name = "actualtotal_seq", strategy = "com.cop.model.generator.StringPrefixedSequenceIdGenerator", parameters = {
		@Parameter(name = StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "1"),
		@Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "AT"),
		@Parameter(name = StringPrefixedSequenceIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%09d") })
	private String atpk;

	private String batchnumber;

	private String buom;

	private BigDecimal buqty;

	private String counter;

	private String itemcode;

	private BigDecimal pcbcode1;

	private BigDecimal pcbcode10;

	private BigDecimal pcbcode11;

	private BigDecimal pcbcode12;

	private BigDecimal pcbcode13;

	private BigDecimal pcbcode14;

	private BigDecimal pcbcode15;

	private BigDecimal pcbcode16;

	private BigDecimal pcbcode17;

	private BigDecimal pcbcode18;

	private BigDecimal pcbcode19;

	private BigDecimal pcbcode2;

	private BigDecimal pcbcode20;

	private BigDecimal pcbcode3;

	private BigDecimal pcbcode4;

	private BigDecimal pcbcode5;

	private BigDecimal pcbcode6;

	private BigDecimal pcbcode7;

	private BigDecimal pcbcode8;

	private BigDecimal pcbcode9;

	private BigDecimal period;

	private String plant;

	private String quantitytype;

	private String saleorder;

	private String stocktype;

	private String tmaterial;

	private BigDecimal totalcost;

	private String valsub;

	private String vendor;

	private BigDecimal year;

	public Actualtotal() {
	}
	
	
}