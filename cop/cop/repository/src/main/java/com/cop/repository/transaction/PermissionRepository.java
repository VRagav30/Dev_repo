package com.cop.repository.transaction;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cop.model.security.Permission;

public interface PermissionRepository {
	
	//Custom query
	 @Query(value = "select * from Permission b where  b.name ilike %:keyword%", nativeQuery = true)
	 List<Permission> findByKeyword(@Param("keyword") String keyword);
	
	@Query(value="select b from Permission b where b.is_delete is null")
	 List<Permission> findAll();
	 
	 @Modifying
	 @Transactional
	 void softdeletePermission(@Param("permissionId") BigDecimal permissionId, @Param("is_delete") String is_delete);

}
