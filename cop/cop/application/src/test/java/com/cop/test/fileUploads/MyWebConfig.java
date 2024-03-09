package com.cop.test.fileUploads;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.cop.controllers.FileUploadController;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = { "com.cop.utilities", "com.cop.serviceimpl", "com.cop.application",
		"com.cop.serviceimpl.service" })
@EnableJpaRepositories({ "com.cop.repository" })
@EntityScan({ "com.cop.model" })
public class MyWebConfig {

	@Bean
	public FileUploadController fileUploadController() {
		return new FileUploadController();
	}
}