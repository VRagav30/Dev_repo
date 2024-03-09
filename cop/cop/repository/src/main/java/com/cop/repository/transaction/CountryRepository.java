package com.cop.repository.transaction;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cop.model.database.Bom;
import com.cop.model.database.Country;

public interface CountryRepository extends JpaRepository<Country, String> {
	
	 //Custom query
	 @Query(value = "select * from Country c where c.countryid ilike %:keyword% or c.sname ilike %:keyword%", nativeQuery = true)
	 List<Bom> findByKeyword(@Param("keyword") String keyword);
	 
	 @Modifying
	 @Transactional
	 void softdeleteCountry(@Param("countryid") String countryid, @Param("is_delete") String is_delete);
	 
	 @Override
		@Query(value="select c from Country c where c.is_delete is null")
		 List<Country> findAll();
		 


}
