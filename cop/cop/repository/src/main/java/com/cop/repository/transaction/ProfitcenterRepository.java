package com.cop.repository.transaction;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cop.model.database.Profitcenter;

public interface ProfitcenterRepository extends JpaRepository<Profitcenter, String> {
	
	 //Custom query
	 @Query(value = "select * from Profitcenter p where p.profitcr ilike %:keyword%", nativeQuery = true)
	 List<Profitcenter> findByKeyword(@Param("keyword") String keyword);
	 
	 @Modifying
	 @Transactional
	 void softdeleteProfitcenter(@Param("profitcenterpk") BigDecimal profitcenterpk, @Param("is_delete") String is_delete);
	 
	 @Override
		@Query(value="select c from Profitcenter c where c.is_delete is null")
		 List<Profitcenter> findAll();

}
