package com.cop.repository.transaction;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cop.model.database.Ccgroup;


public interface CcgroupRepository extends JpaRepository<Ccgroup, String> {

	 //Custom query
	 @Query(value = "select * from Ccgroup c where  c.cctr ilike %:keyword%", nativeQuery = true)
	 List<Ccgroup> findByKeyword(@Param("keyword") String keyword);
	 
	 @Modifying
	 @Transactional
	 void softdeleteCcgroup(@Param("cagrpid") BigDecimal cagrpid, @Param("is_delete") String is_delete);

	 @Override
		@Query(value="select c from Ccgroup c where c.is_delete is null")
		 List<Ccgroup> findAll();
		 
}
