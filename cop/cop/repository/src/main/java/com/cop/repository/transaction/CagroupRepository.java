package com.cop.repository.transaction;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cop.model.database.Cagroup;
import com.cop.model.database.Cagroup;

public interface CagroupRepository extends JpaRepository<Cagroup, String> {
	
	 //Custom query
	 @Query(value = "select * from Cagroup c where c.cagrp ilike %:keyword%", nativeQuery = true)
	 List<Cagroup> findByKeyword(@Param("keyword") String keyword);
	 
	 @Modifying
	 @Transactional
	 void softdeleteCagroup(@Param("cagrpid") BigDecimal cagrpid, @Param("is_delete") String is_delete);

	 @Override
		@Query(value="select c from Cagroup c where c.is_delete is null")
		 List<Cagroup> findAll();
		 
		
}