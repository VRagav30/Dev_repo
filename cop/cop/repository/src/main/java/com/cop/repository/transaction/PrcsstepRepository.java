package com.cop.repository.transaction;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cop.model.database.Prcsstep;

public interface PrcsstepRepository extends JpaRepository<Prcsstep, String> {
	
	List<Prcsstep>findAllbyprcsnum(@Param("prcsnum") String prcsnum);
	
	//Custom query
		 @Query(value = "select * from Prcsstep p where p.matlcode ilike %:keyword%", nativeQuery = true)
		 List<Prcsstep> findByKeyword(@Param("keyword") String keyword);
		 
		 @Modifying
		 @Transactional
		 void softdeletePrcsstep(@Param("prcsstepcode") BigDecimal prcsstepcode, @Param("is_delete") String is_delete);
		 
		 @Override
			@Query(value="select c from Prcsstep c where c.is_delete is null")
			 List<Prcsstep> findAll();

}
