package com.cop.repository.transaction;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cop.model.security.User;



public interface UserRepository {

    public void register(User user, String roleName);

    public List<User> findAll();
    
    @Query(value = "select * from User b where  b.userId ilike %:keyword%", nativeQuery = true)
	 List<User> findByKeyword(@Param("keyword") String keyword);
    

	 @Modifying
	 @Transactional
	 void softdeleteUser(@Param("UserId")  Long userId, @Param("is_delete") String is_delete);

	public void save(User user);

}
