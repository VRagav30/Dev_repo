package com.cop.repository.transaction;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cop.model.database.Profitcenterorgmap;

public interface ProfitcenterorgmapRepository extends JpaRepository<Profitcenterorgmap, String> {
	
	 //Custom query
	 @Query(value = "select * from Profitcenterorgmap p where  p.orgncode  ilike %:keyword%", nativeQuery = true)
	 List<Profitcenterorgmap> findByKeyword(@Param("keyword") String keyword);
	 
	 @Modifying
	 @Transactional
	 void softdeleteProfitcenterorgmap(@Param("pcompk") BigDecimal pcompk, @Param("is_delete") String is_delete);
	 
	 @Override
		@Query(value="select c from Profitcenterorgmap c where c.is_delete is null")
		 List<Profitcenterorgmap> findAll();

}
