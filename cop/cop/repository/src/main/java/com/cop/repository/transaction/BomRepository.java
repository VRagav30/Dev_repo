package com.cop.repository.transaction;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cop.model.database.Altuom;
import com.cop.model.database.Bom;

public interface BomRepository extends JpaRepository<Bom, String> {
	
	List<Bom> findAllbyBomcodeandItemcode(@Param("bhmatl") String bhmatl, @Param("bomcode") String bomcode,@Param("plant") String plant);
	List<Bom> findAllbyBomcodeandImatl(@Param("imatl") String imatl, @Param("bomcode") String bomcode,@Param("plant") String plant);
	@Modifying
	@Query("delete from Bom b where b.bomcode=:bomcode and b.islnum=:islnum")
	void deleteBom(@Param("bomcode") BigDecimal bomcode, @Param("islnum") BigDecimal islnum);
	
	 //Custom query
	 @Query(value = "select * from Bom b where  b.imatl ilike %:keyword%", nativeQuery = true)
	 List<Bom> findByKeyword(@Param("keyword") String keyword);
	@Override
	@Query(value="select b from Bom b where b.is_delete is null")
	 List<Bom> findAll();
	 
	 @Modifying
	 @Transactional
	 void softdeleteBom(@Param("bompk") BigDecimal bompk, @Param("is_delete") String is_delete);
}
