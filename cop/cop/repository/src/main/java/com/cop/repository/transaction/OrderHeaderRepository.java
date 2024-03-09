package com.cop.repository.transaction;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cop.model.database.Orderheader;

@Repository
public interface OrderHeaderRepository extends CrudRepository<Orderheader, String> {

	List<Orderheader> findAll(@Param("orgncode") String orgncode);
	
	List<Orderheader> findAllByOrgncode(@Param("orgncode") String orgncode);

	List<Orderheader> findAllByOrdernum(@Param("objcode") String objcode, @Param("objitnum") BigDecimal objitnum);
	
	List<Orderheader> findAllByLinkedobjcode(@Param("linkedobjcode") String linkedobjcode);
	
	@Query("select count(o) from Orderheader o where o.objcode=(?1) and o.objtype =?2")
	Long findAllbySoassignment(@Param ("objcode") String objcode, @Param ("objtype") String objtype);

}
