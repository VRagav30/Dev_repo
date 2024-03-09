package com.cop.repository.transaction;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cop.model.database.Customer;

public interface CustomerRepository extends JpaRepository<Customer, String> {
	//Custom query
		 @Query(value = "select * from Customer c where  c.customer ilike %:keyword%", nativeQuery = true)
		 List<Customer> findByKeyword(@Param("keyword") String keyword);
		 
		 @Modifying
		 @Transactional
		 void softdeleteCustomer(@Param("customer") String customer, @Param("is_delete") String is_delete);
		 
		 @Override
			@Query(value="select c from Customer c where c.is_delete is null")
			 List<Customer> findAll();
			 

}
