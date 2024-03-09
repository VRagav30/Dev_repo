package com.cop.repository.transaction;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.cop.model.database.Actualtotal;
@Repository
public interface  ActualTotalRepository extends JpaRepository<Actualtotal, String> 
{
	//List<Actualtotal>findTotalAmountbyPlant(@Param ("plant") String plant);
	
	List<Object[]> generateReconReport(@Param("org") String org, @Param("plant") List<String> plant,@Param("itemcode") String itemcode, @Param("stocktype") String stocktype, @Param("saleorder") String saleorder, @Param("year") BigDecimal year, @Param("period") BigDecimal period);
	
	List<Object[]> generateMaterialReport(@Param("org") String org, @Param("plant") List<String> plant,@Param("itemcode") String itemcode, @Param("stocktype") String stocktype, @Param("saleorder") String saleorder, @Param("year") BigDecimal year, @Param("period") BigDecimal period);
	
	
	BigDecimal getTotalCostbyMattype(@Param("org") String org, @Param ("plant") String plant, @Param("matType") String matType,@Param("year") BigDecimal year, @Param("period") BigDecimal period);
	
	List<Object[]>generateQuantityTypeReport(@Param("org") String org, @Param("plant") List<String> plant,@Param("year") BigDecimal year, @Param("period") BigDecimal period, @Param("quantitytype") String quantitytype);
	//List<Object[]> getTotalCostforAllMattype(@Param("plant") List<String> plant);
	
	List<Object[]>generatePcbValuesReport(@Param("atpk") String atpk,@Param("quantitytype") String quantitytype);
	


}