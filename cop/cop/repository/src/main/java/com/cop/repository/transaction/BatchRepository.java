package com.cop.repository.transaction;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cop.model.database.Batch;
import com.cop.model.database.Batch;

public interface BatchRepository extends JpaRepository<Batch, String> {
	
	@Query("select count(b) from Batch b where b.batch=(?1) and upper(b.itemcode)=upper(?2)")
	Long findAllbyBatch(@Param ("batch") String batch, @Param ("itemcode") String itemcode);
	
	//Custom query
		 @Query(value = "select * from Batch b where  b.itemcode ilike %:keyword% or b.batch ilike %:keyword%", nativeQuery = true)
		 List<Batch> findByKeyword(@Param("keyword") String keyword);
		 
		 @Modifying
		 @Transactional
		 void softdeleteBatch(@Param("batch") String batch, @Param("is_delete") String is_delete);
		 
		 @Override
			@Query(value="select b from Batch b where b.is_delete is null")
			 List<Batch> findAll();
	

}
