package com.cop.repository.transaction;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cop.model.database.Mrc;

public interface MrcRepository extends JpaRepository<Mrc, String> {
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("select m from Mrc m where upper(m.itemcode)=upper(?1) and upper(m.plant)=upper(?2) and ?3 between m.valfdate and m.valtdate ")
	List<Mrc> findAllbyItemcodePlantDateMpricetyp(String itemcode, String plant, Date date);

	@Query("select m from Mrc m where upper(m.itemcode)=upper(?1) and upper(m.plant)=upper(?2) and ?3 between m.valfdate and m.valtdate and upper(m.pricetype)=upper(?4) ")
	List<Mrc> findAllbyMatlcodePlantDateMpricetyp(String itemcode, String plant, Date date, String pricetype);
	
	
	List<Mrc> findAllbyMpricetypValsub(@Param ("itemcode") String itemcode, @Param("plant")String plant,@Param("date") Date date, @Param("pricetype") String pricetype, @Param("valsub") String valsub,@Param("batchnumber") String batchnumber);

	List<Mrc> findAllbyValsubbatchocsting(@Param ("itemcode") String itemcode, @Param("plant")String plant,@Param("date") Date date, @Param("valsub") String valsub,@Param("batchnumber") String batchnumber);

	//Custom query
		 @Query(value = "select * from Mrc m where  m.itemcode  ilike %:keyword% or m.plant  ilike %:keyword%", nativeQuery = true)
		 List<Mrc> findByKeyword(@Param("keyword") String keyword);
		 
		 @Modifying
		 @Transactional
		 void softdeleteMrc(@Param("mrcpk") BigDecimal mrcpk, @Param("is_delete") String is_delete);
		 
		 @Override
			@Query(value="select c from Mrc c where c.is_delete is null")
			 List<Mrc> findAll();
}
