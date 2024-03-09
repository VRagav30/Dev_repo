package com.cop.repository.transaction;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cop.model.database.Bom;
import com.cop.model.database.Caval;
import com.cop.model.database.Caval;

public interface CavalRepository extends JpaRepository<Caval, String>{
	
	 @SuppressWarnings("unchecked")
	Caval save(Caval entity);
	List<Caval> findAllbyOrgnCodeMatValGrp(@Param("vevent") String vevent,@Param("orgncode") String orgncode,@Param("valgrp") BigDecimal valgrp);
	List<Caval> findAllbyMatlcode(@Param("vevent") String vevent,@Param("valgrp") BigDecimal valgrp);
	//Custom query
	 @Query(value = "select * from Caval c where  c.costacc ilike %:keyword%", nativeQuery = true)
	 List<Caval> findByKeyword(@Param("keyword") String keyword);
	 
	 @Modifying
	 @Transactional
	 void softdeleteCaval(@Param("cavalpk") BigDecimal cavalpk, @Param("is_delete") String is_delete);
	 


	 @Override
		@Query(value="select c from Caval c where c.is_delete is null order by cavalpk")
		 List<Caval> findAll();
		 
}
