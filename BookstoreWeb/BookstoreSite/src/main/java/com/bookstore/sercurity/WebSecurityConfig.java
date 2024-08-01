package com.bookstore.sercurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.bookstore.sercurity.oauth.CustomerOAuth2UserService;
import com.bookstore.sercurity.oauth.OAuth2LoginSuccessHandler;

@Configuration
public class WebSecurityConfig {

	@Autowired
	private CustomerOAuth2UserService auth2UserService;
	@Autowired
	private OAuth2LoginSuccessHandler auth2LoginSuccessHandler;
	@Autowired
	private DatabaseLoginSuccessHandler databaseLoginSuccessHandler;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain configureHttpSecurity(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(auth -> auth
				.requestMatchers("/account_details", "/update_account_details", "/cart", "/address_book/**",
						"/order/**", "/checkout", "/place_order", "/reviews/**", "/write_review/**", "/post_review")
				.authenticated()
				.anyRequest().permitAll())
				.formLogin(form -> form
						.loginPage("/login")
						.usernameParameter("email")
						.successHandler(databaseLoginSuccessHandler)
						.passwordParameter("password")
						.permitAll())
				.logout(LogoutConfigurer::permitAll)
				.oauth2Login(o2 -> o2
						.loginPage("/login")
						.userInfoEndpoint(u -> u.userService(auth2UserService))
						.successHandler(auth2LoginSuccessHandler))
				.rememberMe(rem -> rem
						.key("AbcDefghijklmnopqrs_1234567890")
						.tokenValiditySeconds(7 * 24 * 60 * 60))
				.sessionManagement(s -> s
						.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));

		return http.build();
	}

	@Bean
	WebSecurityCustomizer configureWebSecurity() throws Exception {
		return (web) -> web.ignoring().requestMatchers("/images/**", "/js/**", "/webjars/**");
	}

	@Bean
	UserDetailsService userDetailsService() {
		return new CustomerUserDetailService();
	}

	@Bean
	DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

		authenticationProvider.setUserDetailsService(userDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());

		return authenticationProvider;
	}
}