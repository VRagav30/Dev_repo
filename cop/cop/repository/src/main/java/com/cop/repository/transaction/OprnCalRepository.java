package com.cop.repository.transaction;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cop.model.database.OprnCal;
@Repository
public interface OprnCalRepository extends JpaRepository <OprnCal,String>{
	
	@Procedure(name = "generate_OprnCal")
	   void generate_OprnCal( @Param("iorg") String iorg,
	    		@Param("iyear") BigDecimal iyear, @Param("iperiod") BigDecimal iperiod);

}
