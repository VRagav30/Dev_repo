package com.cop.repository.transaction;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cop.model.database.Bom;
import com.cop.model.database.Currency;
import com.cop.model.database.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, String> {
	
	 //Custom query
	 @Query(value = "select * from Currency c where c.currc ilike %:keyword%", nativeQuery = true)
	 List<Currency> findByKeyword(@Param("keyword") String keyword);
	 
	 @Modifying
	 @Transactional
	 void softdeleteCurrency(@Param("currc") String currc, @Param("is_delete") String is_delete);
	 
	 @Override
		@Query(value="select c from Currency c where c.is_delete is null")
		 List<Currency> findAll();
		 


}
