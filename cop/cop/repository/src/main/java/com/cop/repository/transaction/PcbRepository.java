package com.cop.repository.transaction;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cop.model.database.Pcb;

public interface PcbRepository extends JpaRepository<Pcb, String> {
	//Custom query
	 @Query(value = "select * from Pcb p where p.pcbcode ilike %:keyword%", nativeQuery = true)
	 List<Pcb> findByKeyword(@Param("keyword") String keyword);
	 
	 @Modifying
	 @Transactional
	 void softdeletePcb(@Param("pcbpk") BigDecimal pcbpk, @Param("is_delete") String is_delete);
	 
	 @Override
		@Query(value="select c from Pcb c where c.is_delete is null")
		 List<Pcb> findAll();

}
