package com.cop.repository.transaction;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cop.model.database.Oprn;
import com.cop.model.database.Oprn;
import com.cop.model.database.Oprn;


public interface OprnRepository extends JpaRepository<Oprn, String> {
	
	List<Oprn> findAllByOprn(@Param("oprn") String oprn);
	
	 //Custom query
	 @Query(value = "select * from Oprn o where upper( o.oprn) like upper(:keyword)", nativeQuery = true)
	 List<Oprn> findByKeyword(@Param("keyword") String keyword);
	
	 
	 @Modifying
	 @Transactional
	 void softdeleteOprn(@Param("oprn") String oprn, @Param("is_delete") String is_delete);
	 
	 @Override
		@Query(value="select c from Oprn c where c.is_delete is null")
		 List<Oprn> findAll();
	
}
