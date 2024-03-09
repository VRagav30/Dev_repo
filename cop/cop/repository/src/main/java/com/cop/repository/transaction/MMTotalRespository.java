package com.cop.repository.transaction;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cop.model.database.Mmtotal;
import com.cop.model.database.ConsolidatedReport;

@Repository
public interface MMTotalRespository extends JpaRepository<Mmtotal, String> {
	
	List<Mmtotal> findAllbyObjcodeandObjitnum(@Param ("objcode") String objcode, @Param("objitnum") BigDecimal objitnum ,@Param("date") Date date);
	List<BigDecimal[]> findCstockbyPlantandstocktype(@Param("matlcode") String matlcode, @Param("plant") String plant,
			@Param("date") Date date, @Param("stocktype") String stocktype);

	List<BigDecimal[]> findMaxCstockbyPlant(@Param("matlcode") String matlcode, @Param("plant") String plant,
			@Param("date") Date date);

	List<BigDecimal[]> findCstockStockValueforOs(@Param("matlcode") String matlcode, @Param("plant") String plant,
			@Param("date") Date date, @Param("stocktype") String stocktype, @Param("vendor") String vendor);

	List<Mmtotal> findAllFromOs(@Param("plant") String plant, @Param("matlcode") String matlcode,
			@Param("date") Date date);

	List<BigDecimal> findOrderAmountbytrnsevent(@Param("cobjtype") String cobjtype,
			@Param("objcode") BigDecimal objcode, @Param("trnsevent") String trnsevent);

	List<BigDecimal[]> findCstockStockValuebyEvent(@Param("matlcode") String matlcode, @Param("plant") String plant,
			@Param("date") Date date, @Param("trnsevent") String trnsevent);

	BigDecimal findsumofOrdamountbytrnsevent(@Param("cobjtype") String cobjtype, @Param("objcode") BigDecimal objcode,
			@Param("trnsevent") String trnsevent);

	List<BigDecimal[]> findCstockbyItemcodePlantValsubBatch(@Param("matlcode") String matlcode, @Param("plant") String plant,
			@Param("date") Date date,@Param ("valsub") String valsub ,@Param("batchnumber") String batchnumber, @Param("saleorder") String saleorder);

	List<Mmtotal> findCstockStockvaluebyGRDateandstocktype(@Param("matlcode") String matlcode, @Param("plant") String plant,
			@Param("date") Date date ,@Param("stocktype") String stocktype,@Param("valsub") String valsub, @Param("batchnumber") String batchnumber, @Param("saleorder") String saleorder);
	
	List<Mmtotal> findAllbytrnseventandcobjtypeitemcode(@Param("itemcode") String itemcode, @Param("cobjtype") String cobjtype,@Param("trnsevent") String trnsevent);

	List<Mmtotal> findCstockStockvalueforUpdate(@Param("matlcode") String matlcode, @Param("plant") String plant,
			@Param("date") Date date ,@Param("stocktype") String stocktype,@Param("valsub") String valsub, @Param("batchnumber") String batchnumber , @Param("saleorder") String saleorder);
	
	//10/11/2020 get cstock and stock value based on bm=yes or no
	List<BigDecimal[]> findCstockStockBatchmanagement(@Param("matlcode") String matlcode, @Param("plant") String plant,
			@Param("date") Date date ,@Param("stocktype") String stocktype,@Param("valsub") String valsub,  @Param("saleorder") String saleorder);

	List<Mmtotal> findallbyBatchcosting(@Param("matlcode") String matlcode, @Param("plant") String plant,
			@Param("date") Date date ,@Param("stocktype") String stocktype,@Param("valsub") String valsub, @Param("saleorder") String saleorder);
// Using this for batchmanagement 
	List<BigDecimal[]> findCstockStockwithoutBatch(@Param("matlcode") String matlcode, @Param("plant") String plant,
			@Param("date") Date date ,@Param("stocktype") String stocktype,@Param("valsub") String valsub,  @Param("saleorder") String saleorder);

	List<BigDecimal[]> findCstockStockwithBatch(@Param("matlcode") String matlcode, @Param("plant") String plant,
			@Param("date") Date date ,@Param("stocktype") String stocktype,@Param("valsub") String valsub, @Param("batchnumber") String batchnumber , @Param("saleorder") String saleorder);

	List<Object[]> generateConsolidatedReport(@Param("org") String org,@Param("plant") List<String> plant,@Param("year") BigDecimal year,@Param("period") BigDecimal period);


List<Object[]> generateDetailedReport(@Param("org") String org,@Param("plant") List<String> plant,@Param("year") BigDecimal year,@Param("period") BigDecimal period );


@Query("select m from Mmtotal m where m.docnumber=?1 and m.docitnum=?2")
List<Mmtotal> getMmtotalbydocnum(@Param("docnumber") BigDecimal docnumber,@Param("docitnum") BigDecimal docitnum);



@Procedure(name = "generateall_periodicCosting")
void generateall_periodicCosting( @Param("iorg") String iorg,
 		@Param("iyear") BigDecimal iyear, @Param("iperiod") BigDecimal iperiod);
}