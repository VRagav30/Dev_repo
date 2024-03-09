package com.cop.repository.transaction;

import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.cop.model.security.User;
import com.cop.model.security.Role;
import com.cop.model.security.User;


@Repository
public class UserRepositoryImpl implements UserRepository {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void register(User user, String roleName) {
        Role role = entityManager.createQuery("SELECT r FROM Roles r WHERE r.name = :name", Role.class)
                .setParameter("name", roleName)
                .getSingleResult();
        user.setRoles(new HashSet<Role>() {
			private static final long serialVersionUID = 1L;
		{
            add(role);
        }});
        entityManager.merge(user);
    }

    public List<User> findAll() {
        List<User> users = entityManager
                .createQuery("SELECT u FROM User u LEFT JOIN FETCH u.roles r", User.class)
                .getResultList();
        return users;
    }

	@Override
	public void softdeleteUser(Long userId, String is_delete) {
		// TODO Auto-generated method
		
		entityManager.createQuery("update User  set is_delete=:is_delete where userId=:userId", User.class);
		
	}

	
	public void save(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<User> findByKeyword(String keyword) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
