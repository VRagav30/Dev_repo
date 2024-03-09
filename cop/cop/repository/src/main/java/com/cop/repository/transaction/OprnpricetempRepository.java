package com.cop.repository.transaction;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cop.model.database.Oprnprice;
import com.cop.model.database.OprnpriceTemp;

@Transactional
public interface OprnpricetempRepository extends JpaRepository<OprnpriceTemp, String> {

	 @Procedure(name = "generate_activityprice_temp")
	   void generate_activityprice_temp( @Param("iorg") String iorg,
	    		@Param("iyear") BigDecimal iyear, @Param("iperiod") BigDecimal iperiod);
	 
	 
	 
	 
}
