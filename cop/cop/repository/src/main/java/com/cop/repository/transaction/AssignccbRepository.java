package com.cop.repository.transaction;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cop.model.database.Assignccb;
import com.cop.model.database.Assignccb;


public interface AssignccbRepository extends JpaRepository<Assignccb, String> {

	//Custom query
	 @Query(value = "select * from Assignccb a where a.cctr ilike %:keyword%", nativeQuery = true)
	 List<Assignccb> findByKeyword(@Param("keyword") String keyword);
	 
	 @Modifying
	 @Transactional
	 void softdeleteAssignccb(@Param("accbpk") BigDecimal accbpk, @Param("is_delete") String is_delete);
	 
	 @Override
		@Query(value="select b from Assignccb b where b.is_delete is null")
		 List<Assignccb> findAll();
}
