package com.cop.repository.transaction;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cop.model.database.Bom;
import com.cop.model.database.Organisation;
import com.cop.model.database.Organisation;

@Repository
public interface OrganisationRepository extends JpaRepository<Organisation, String>{

	List<Organisation> findAllByOrgnCode(@Param("orgncode") String orgncode);
	
	String findFydCodeByOrgnCode (@Param("orgncode") String orgncode);
	
	String findCurro (@Param("orgncode") String orgncode);
	
	//Custom query
		 @Query(value = "select * from Organisation o where  o.sname ilike %:keyword% or o.city ilike %:keyword% or o.state ilike %:keyword%", nativeQuery = true)
		 List<Organisation> findByKeyword(@Param("keyword") String keyword);
		 
		 @Modifying
		 @Transactional
		 void softdeleteOrganisation(@Param("orgncode") String orgncode, @Param("is_delete") String is_delete);
		 
		 @Override
			@Query(value="select c from Organisation c where c.is_delete is null")
			 List<Organisation> findAll();

}
