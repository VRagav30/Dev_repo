package com.cop.repository.transaction;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.cop.model.database.Currencyexchangerate;

public interface CurrencyExchangeRateRepository extends CrudRepository<Currencyexchangerate, String> {

	List<Currencyexchangerate> findAllbyOrgncodeNdCurrency(String orgncode, String scurr, String tcurr, Date orddate);

	// Custom query
	@Query(value = "select * from Currencyexchangerate c where  c.scurr ilike %:keyword% or c.tcurr ilike %:keyword%", nativeQuery = true)
	List<Currencyexchangerate> findByKeyword(@Param("keyword") String keyword);
	
	@Modifying
	 @Transactional
	 void softdeleteCurrencyexchangerate(@Param("curexchratepk") BigDecimal curexchratepk, @Param("is_delete") String is_delete);
	
	@Override
	@Query(value="select c from Currencyexchangerate c where c.is_delete is null")
	List<Currencyexchangerate> findAll();
}
