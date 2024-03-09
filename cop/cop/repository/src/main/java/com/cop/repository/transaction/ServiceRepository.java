package com.cop.repository.transaction;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cop.model.database.Bom;
import com.cop.model.database.Serviceitem;
import com.cop.model.database.Serviceitem;

public interface ServiceRepository extends JpaRepository<Serviceitem, String> {
	
	List<Serviceitem> findAllbyItemCode(@Param("sercode") String sercode );
	
	List<Serviceitem> findAllbyItemcodeandPlant(@Param("sercode") String sercode, @Param("plant") String plant);
	
	//Custom query
		 @Query(value = "select * from Serviceitem s where  s.sercode ilike %:keyword%", nativeQuery = true)
		 List<Serviceitem> findByKeyword(@Param("keyword") String keyword);

		 
		 @Modifying
		 @Transactional
		 void softdeleteServiceitem(@Param("sercode") String sercode, @Param("is_delete") String is_delete);
		 
		 @Override
			@Query(value="select c from Serviceitem c where c.is_delete is null")
			 List<Serviceitem> findAll();
}
