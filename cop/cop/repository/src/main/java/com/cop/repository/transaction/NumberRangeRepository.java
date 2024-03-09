package com.cop.repository.transaction;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cop.model.database.Numberrange;



@Repository
public interface NumberRangeRepository extends JpaRepository<Numberrange, String>{
	
	List<Numberrange> findAllByNumobject(@Param("numobject") String numobject);
	
	//Custom query
		 @Query(value = "select * from Numberrange b where  b.numobject ilike %:keyword%", nativeQuery = true)
		 List<Numberrange> findByKeyword(@Param("keyword") String keyword);
		 
		 @Modifying
		 @Transactional
		 void softdeleteNumberrange(@Param("rangeid") BigDecimal rangeid, @Param("is_delete") String is_delete);
		 
		 @Override
			@Query(value="select c from Numberrange c where c.is_delete is null")
			 List<Numberrange> findAll();


}