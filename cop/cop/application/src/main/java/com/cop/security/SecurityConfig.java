package com.cop.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	public void configure(WebSecurity web) throws Exception {
		// Spring Security ignores URLs of static resources
		web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// configure login
		http.formLogin().loginPage("/login").usernameParameter("account").passwordParameter("password")
				.defaultSuccessUrl("/home").permitAll();
		// configure logout
		http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login")
				.deleteCookies("JSESSIONID").invalidateHttpSession(true);
		// configure URL authorization
		http.authorizeRequests().mvcMatchers("/home").hasAnyAuthority("admin", "orgAdmin","SuperAdmin","Prod","MasterData","Finance","Costing")
								.and()
			.authorizeRequests().mvcMatchers("/admin").hasAnyAuthority("admin","orgAdmin","SuperAdmin","Prod","MasterData","Finance","Costing");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
