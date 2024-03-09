package com.cop.serviceapi.service;

import java.util.List;

import com.cop.model.security.User;


public interface UserService {

    public void register(User user, String roleName);

    public List<User> findAll();

}
