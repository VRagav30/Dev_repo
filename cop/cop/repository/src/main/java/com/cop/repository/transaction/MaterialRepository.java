package com.cop.repository.transaction;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cop.model.database.Material;
import com.cop.model.database.Material;

@Repository
public interface MaterialRepository extends JpaRepository<Material, String> {

	@Query("select m from Material m where ((:matlcode is null) OR upper(m.matlcode) =upper(:matlcode)) and ((:plant is null) or upper(m.plant)= upper(:plant)) and  ((:valsub is null) or (m.valsub)=(:valsub))")

	List<Material> findAllByMatlcodeandPlantValsub(@Param("matlcode") String matlcode, @Param("plant") String plant,
			@Param("valsub") String valsub);

	// Custom query
	@Query(value = "select * from Material m where m.matlcode ilike %:keyword% or m.mattyp ilike %:keyword% ", nativeQuery = true)
	List<Material> findByKeyword(@Param("keyword") String keyword);

	@Override
	@Query(value = "select b from Material b where b.is_delete is null order by matpk")
	List<Material> findAll();

	@Modifying
	@Transactional
	void softdeleteMaterial(@Param("matpk") BigDecimal matpk, @Param("is_delete") String is_delete);

	List<Material> findAllByMatlcode(@Param("matlcode") String matlcode);
}
