package com.cop.repository.transaction;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cop.model.database.Uom;

public interface UomRepository extends JpaRepository<Uom,String> {
	
	//Custom query
		 @Query(value = "select * from Uom u where u.uom ilike %:keyword%", nativeQuery = true)
		 List<Uom> findByKeyword(@Param("keyword") String keyword);
		 
		 @Modifying
		 @Transactional
		 void softdeleteUom(@Param("uom") String uom, @Param("is_delete") String is_delete);
		
		 
		 @Override
			@Query(value="select u from Uom u where u.is_delete is null")
			List<Uom> findAll();

}
