package com.cop.repository.transaction;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cop.model.database.Oprnprice;

@Repository
public interface OprnpriceRepository extends JpaRepository<Oprnprice, String> {

	List<Oprnprice> findAllByorgncodecctrandoprn(@Param("orgncode") String orgncode, @Param("cctr") String cctr,
			@Param("oprn") String oprn,@Param("datatyp") String datatyp);
	
	List<Oprnprice> findListofOprnbyorgncodeandcctr(@Param("orgncode") String orgncode, @Param("cctr") String cctr,@Param("datatyp") String datatyp);
	
	List<Oprnprice> findActivityPrice(@Param("orgncode") String orgncode, @Param("year") BigDecimal year,@Param("periodf") BigDecimal periodf,@Param("periodto") BigDecimal periodto);
	
	@Modifying @Transactional
	@Query("delete from Oprnprice where orgncode= :orgncode and year= :year and periodf= :periodf and periodto=:periodto")
	public int delteActivityPrice(@Param("orgncode") String orgncode,@Param ("year") BigDecimal year,@Param ("periodf") BigDecimal periodf ,@Param ("periodto") BigDecimal periodto )  ; 
	
	@Modifying @Transactional
	@Query("insert into Oprnprice (orgncode,year,periodf,periodto,curro,cctr,oprn,datatyp,capqty ,opuom ,opqty,pricebase, fixpr, varprice,totprice, priceuom ,costacc)select orgncode,year,period,period,curro,objcode ,oprn,datatyp,capqty ,oprnuom ,oprnqty ,pricebase, fixedprice , variableprice ,totalprice , priceuom ,oprncostacc from OprnCal where orgncode= :orgncode and year= :year and period= :periodf")
	  public int insertActivityPrice(@Param("orgncode") String orgncode,@Param ("year") BigDecimal year,@Param ("periodf") BigDecimal periodf  )  ; 
	 }

