package com.cop.repository.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.cop.model.database.Costcenter;

public interface CTRRepository extends JpaRepository<Costcenter, String> {

	String findOrgncode(@Param ("cctr") String cctr);
}
