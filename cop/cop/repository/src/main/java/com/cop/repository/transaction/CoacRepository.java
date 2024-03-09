package com.cop.repository.transaction;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cop.model.database.Coac;

public interface CoacRepository extends JpaRepository<Coac, String> {
	
	 //Custom query
	 @Query(value = "select * from Coac c where  c.costacc like %:keyword%", nativeQuery = true)
	 List<Coac> findByKeyword(@Param("keyword") String keyword);
	 
	 @Modifying
	 @Transactional
	 void softdeleteCoac(@Param("costacc") BigDecimal costacc, @Param("is_delete") String is_delete);
	 
	 @Override
		@Query(value="select c from Coac c where c.is_delete is null")
		 List<Coac> findAll();
		 


}
