package com.cop.repository.transaction;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cop.model.database.Prcsstepops;

public interface PrcsstepopsRepository extends JpaRepository<Prcsstepops,String> {

	List<Prcsstepops> findByOpname(String opname);
	List<Prcsstepops> findAllbyoprn(@Param("opname") String opname, @Param("prcsnum") String prcsnum);
	
	//Custom query
	 @Query(value = "select * from Prcsstepops p where p.opname ilike %:keyword%", nativeQuery = true)
	 List<Prcsstepops> findByKeyword(@Param("keyword") String keyword);
	 
	 @Modifying
	 @Transactional
	 void softdeletePrcsstepops(@Param("prcsstepopscode") BigDecimal prcsstepopscode, @Param("is_delete") String is_delete);
	 
	 @Override
		@Query(value="select c from Prcsstepops c where c.is_delete is null")
		 List<Prcsstepops> findAll();
}
