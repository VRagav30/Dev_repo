package com.cop.repository.transaction;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cop.model.database.Opgroup;


public interface OpgroupRepository extends JpaRepository<Opgroup, String> { 
	@Query(value = "select * from Opgroup o where upper( o.oprn) like upper(:keyword)", nativeQuery = true)
List<Opgroup> findByKeyword(@Param("keyword") String keyword);
	
	@Modifying
	 @Transactional
	 void softdeleteOpgroup(@Param("opgrp") String opgrp, @Param("is_delete") String is_delete);
	 
	 @Override
		@Query(value="select c from Opgroup c where c.is_delete is null")
		 List<Opgroup> findAll();
	
	

}
