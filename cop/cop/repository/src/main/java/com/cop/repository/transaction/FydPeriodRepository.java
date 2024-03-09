package com.cop.repository.transaction;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.cop.model.database.Fydperiod;

public interface FydPeriodRepository extends CrudRepository<Fydperiod, String>{
	
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("select f from Fydperiod f where f.fydcode = ?1 and ?2 between f.fromdate and f.todate")
	List <Fydperiod> findAllbyDate ( String fydcode,Date date);
	
	List<Fydperiod> findbyPeriod(@Param("period")BigDecimal period);
	
	List<Long> findYear(@Param("orgncode") String orgncode);
	
	List<Fydperiod> findPeriodsbyYear(@Param("orgncode") String orgncode,@Param("year") BigDecimal year);
	
	 //Custom query
	 @Query(value = "select * from Fydperiod f where f.fydcode ilike %:keyword%", nativeQuery = true)
	 List<Fydperiod> findByKeyword(@Param("keyword") String keyword);
	 
	 @Modifying
	 @Transactional
	 void softdeleteFydperiod(@Param("fydperiodpk") BigDecimal fydperiodpk, @Param("is_delete") String is_delete);
	 
	 @Override
		@Query(value="select c from Fydperiod c where c.is_delete is null")
		 List<Fydperiod> findAll();
}
