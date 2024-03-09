package com.cop.repository.transaction;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cop.model.security.Role;

public interface RoleRepository {
	
	//Custom query
		 @Query(value = "select * from Role b where  b.name ilike %:keyword%", nativeQuery = true)
		 List<Role> findByKeyword(@Param("keyword") String keyword);
		
		@Query(value="select b from Role b where b.is_delete is null")
		 List<Role> findAll();
		 
		 @Modifying
		 @Transactional
		 void softdeleteRole(@Param("roleId") BigDecimal roleId, @Param("is_delete") String is_delete);
	}


