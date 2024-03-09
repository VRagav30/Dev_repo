package com.cop.repository.transaction;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cop.model.database.Ccb;
import com.cop.model.database.Ccb;


public interface CcbRepository extends JpaRepository<Ccb, String> {
	
	//Custom query
	@Transactional
		 @Query(value = "select * from Ccb c where (c.cagrp like %:keyword% )or (c.opgrp like %:keyword%) ", nativeQuery = true)
		 List<Ccb> findByKeyword(@Param("keyword") String keyword);
	@Modifying
	 @Transactional
	 void softdeleteCcb(@Param(" ccbpk") BigDecimal ccbpk, @Param("is_delete") String is_delete);
	
	

	 @Override
		@Query(value="select c from Ccb c where c.is_delete is null")
		 List<Ccb> findAll();
		 
		}


