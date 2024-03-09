package com.cop.repository.transaction;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cop.model.database.Costtotal;




	@Repository
	public interface CostTotalRepository extends JpaRepository<Costtotal, String>{
    List <Costtotal> findAll ();
   List<Costtotal> findAllbyRefdocnum(BigDecimal refdocnum);
   
   List<BigDecimal> findOrderAmountbytrnsevent(@Param("cobjtype") String cobjtype,
			@Param("objcode") String objcode, @Param("trnsevent") String trnsevent);
   
   BigDecimal findsumofOrdamountbytrnsevent(@Param("cobjtype") String cobjtype,
			@Param("objcode") BigDecimal objcode, @Param("trnsevent") String trnsevent);
   List<Costtotal>  findAllBytrnsevent(@Param("plant") String plant,@Param ("itemcode") String itemcode,@Param("trnsevent") String trnsevent, @Param("date") Date date);
   
   List<Costtotal> findbyObjcode(@Param("objcode") String objcode, @Param("objitnum") BigDecimal objitnum ,  @Param("trnsevent") String trnsevent);
	}

