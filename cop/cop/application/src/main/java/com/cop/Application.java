package com.cop;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
//@ComponentScan(basePackages =  {"com.cop.utilities","com.cop.serviceimpl","com.cop.application", "com.cop.serviceimpl.service"})
@ComponentScan(basePackages = { "com.cop" })
@EnableJpaRepositories({ "com.cop.repository" })
@EntityScan({ "com.cop.model" })
public class Application {

	@Autowired
	static PasswordEncoder passwordEncoder;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args); 
		//System.out.println(Application.passwordEncoder.encode("reshu"));
	}

}