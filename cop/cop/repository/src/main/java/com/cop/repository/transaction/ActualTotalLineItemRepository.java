package com.cop.repository.transaction;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.cop.model.database.Actualtotal;
import com.cop.model.database.Actualtotallineitem;
@Repository
public interface  ActualTotalLineItemRepository extends JpaRepository<Actualtotallineitem, String> 
{
//find totalamount of Oc OR DE PC and feed to report	
	List<Object[]> generateReportByTrnsevent(@Param("orgncode") String orgncode,@Param("plant") List<String> plant,@Param("year") BigDecimal year, @Param("period") BigDecimal period);
	
	List<Object[]> getTRSTamount(@Param("plant") List<String> plant,@Param("year") BigDecimal year, @Param("period") BigDecimal period);
	
	List<Object[]> getTOSTamount(@Param("plant") List<String> plant,@Param("year") BigDecimal year, @Param("period") BigDecimal period);
	
}