package com.cop.repository.transaction;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cop.model.database.Plantgovtnum;
import com.cop.model.database.Plantgovtnum;

public interface PgnRepository extends JpaRepository<Plantgovtnum, String> {
	
	 //Custom query
	 @Query(value = "select * from Plantgovtnum p where  p.plant ilike %:keyword%", nativeQuery = true)
	 List<Plantgovtnum> findByKeyword(@Param("keyword") String keyword);
	 
	 @Modifying
	 @Transactional
	 void softdeletePlantgovtnum(@Param("pgn_pk") BigDecimal pgn_pk, @Param("is_delete") String is_delete);
	 
	 @Override
		@Query(value="select c from Plantgovtnum c where c.is_delete is null")
		 List<Plantgovtnum> findAll();

}
