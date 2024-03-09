package com.cop.repository.transaction;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cop.model.database.Processcard;

public interface PcardRepository extends JpaRepository<Processcard,String> {

	//Custom query
		 @Query(value = "select * from Processcard p where  p.matlcode ilike %:keyword% or p.plant ilike  %:keyword% ", nativeQuery = true)
		 List<Processcard> findByKeyword(@Param("keyword") String keyword);
		 
		 @Modifying
		 @Transactional
		 void softdeleteProcesscard(@Param("prscnum") String prscnum, @Param("is_delete") String is_delete);
		 
		 @Override
			@Query(value="select c from Processcard c where c.is_delete is null")
			 List<Processcard> findAll();
}
