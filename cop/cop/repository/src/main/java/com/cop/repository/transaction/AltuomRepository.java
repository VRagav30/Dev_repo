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
import com.cop.model.database.Fyd;

public interface AltuomRepository extends JpaRepository<Altuom, String> {

	List<Altuom> findAltuombyMatCodePlantAltUOM(@Param("plant") String plant, @Param("itemcode") String itemcode,
			@Param("altuom") String altuom, @Param("buom") String buom);
	
	 //Custom query
	 @Query(value = "select * from Altuom a where upper( a.itemcode) like upper(:keyword)", nativeQuery = true)
	 List<Altuom> findByKeyword(@Param("keyword") String keyword);
	 @Override
		@Query(value="select b from Altuom b where b.is_delete is null")
		 List<Altuom> findAll();
	 
	 @Modifying
	 @Transactional
	 void softdeleteAltuom(@Param("altuomid") BigDecimal altuomid, @Param("is_delete") String is_delete);

}
