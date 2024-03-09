package com.cop.repository.transaction;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cop.model.database.Bom;
import com.cop.model.database.Plant;
import com.cop.model.database.Plant;

@Repository
public interface PlantRepository extends JpaRepository<Plant, String>{
	
	 List<Plant> findAllByPlant(@Param("plant") String plant);
	 
	 String findOrgCodeByPlantId(@Param("plant") String plant);
	 
	 @Query("select p from Plant p where p.orgncode=?1")
	 List<Plant> findAllbyOrgncode(@Param("orgncode") String orgncode);
	 
	 @Query("select p.orgncode from Plant p where p.plant=?1")
	 String findOrgncode(@Param("plant") String plant);
	
	 @Query(value = "select * from Plant p where  p.plant  ilike %:keyword% or p.orgncode ilike %:keyword%", nativeQuery = true)
	 List<Plant> findByKeyword(@Param("keyword") String keyword);
	 
	 @Modifying
	 @Transactional
	 void softdeletePlant(@Param("plant") String plant, @Param("is_delete") String is_delete);
	 
	 @Override
		@Query(value="select c from Plant c where c.is_delete is null")
		 List<Plant> findAll();

	
}
