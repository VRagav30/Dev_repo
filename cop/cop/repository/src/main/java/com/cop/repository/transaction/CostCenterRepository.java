package com.cop.repository.transaction;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cop.model.database.Costcenter;

public interface CostCenterRepository extends JpaRepository<Costcenter, String> {
	 //Custom query
	 @Query(value = "select * from Costcenter c where  c.cctr ilike %:keyword%", nativeQuery = true)
	 List<Costcenter> findByKeyword(@Param("keyword") String keyword);
	 
	 @Modifying
	 @Transactional
	 void softdeleteCostcenter(@Param("cctr") String cctr, @Param("is_delete") String is_delete);
	 
	 @Override
		@Query(value="select c from Costcenter c where c.is_delete is null")
		 List<Costcenter> findAll();
		 


}
