package com.cop.repository.transaction;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cop.model.database.Fyd;
import com.cop.model.database.Fyd;

public interface FydRepository extends JpaRepository<Fyd,String>{
	
	
	
	@Transactional
	@Query("select f from Fyd f where f.fydcode=:fydcode")
	List<Fyd> findAllByFydCode(String fydcode);
	
	@Transactional
	@Query("delete from Fyd f where f.fydcode= :fydcode")
	void deleteFyd(@Param ("fydcode") String fydcode);
	
	 //Custom query
	 @Query(value = "select * from Fyd f where f.fydcode like %:keyword% ", nativeQuery = true)
	 List<Fyd> findByKeyword(@Param("keyword") String keyword);
	 
	 @Modifying
	 @Transactional
	 void softdeleteFyd(@Param("fydpk") BigDecimal fydpk, @Param("is_delete") String is_delete);
	 
	 @Override
		@Query(value="select c from Fyd c where c.is_delete is null")
		 List<Fyd> findAll();
	}


