package com.cop.repository.transaction;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cop.model.database.Vendor;

public interface VendorRepository extends JpaRepository<Vendor, String> {
	
	//Custom query
		 @Query(value = "select * from Vendor v where  v.sname ilike %:keyword%", nativeQuery = true)
		 List<Vendor> findByKeyword(@Param("keyword") String keyword);
		 
		 @Override
			@Query(value="select b from Vendor b where b.is_delete is null")
			 List<Vendor> findAll();
			 
			 @Modifying
			 @Transactional
			 void softdeleteVendor(@Param("vendor") String vendor, @Param("is_delete") String is_delete);

}
