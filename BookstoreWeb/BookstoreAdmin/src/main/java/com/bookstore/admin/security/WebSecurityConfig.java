package com.bookstore.admin.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	//extends WebSecurityConfigurationAdapter

	@Bean
    public PasswordEncoder PasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

	@Bean
	SecurityFilterChain configureHttpSecurity(HttpSecurity http) throws Exception {
		
		http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
		
		return http.build();
	}

	// @Override
	// protected void configure(HttpSecurity http) throws Exception{
	// 	http.authorizeRequests()
	// 		.anyRequest().authenticated()
	// 		.and()
	// 		.formLogin()
	// 			.loginPage("/login")
	// 			.usernameParameter("email")
	// 			.permitAll();
	// }

	// @Override
	// public void configure(WebSecurity web) throws Exception{
	// 	web.ignoring().antMatchers("/images/++", "/js/++", "webjars/++");
	// }

}