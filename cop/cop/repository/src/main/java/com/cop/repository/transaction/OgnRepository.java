package com.cop.repository.transaction;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cop.model.database.Organisationgovtnum;

public interface OgnRepository extends JpaRepository<Organisationgovtnum, String> {
	
	//Custom query
		 @Query(value = "select * from Organisationgovtnum o where  o.orgncode ilike %:keyword%", nativeQuery = true)
		 List<Organisationgovtnum> findByKeyword(@Param("keyword") String keyword);
		 
		 @Modifying
		 @Transactional
		 void softdeleteOrganisationgovtnum(@Param("ogn_pk") BigDecimal ogn_pk, @Param("is_delete") String is_delete);
		 
		 @Override
			@Query(value="select c from Organisationgovtnum c where c.is_delete is null")
			 List<Organisationgovtnum> findAll();

}
