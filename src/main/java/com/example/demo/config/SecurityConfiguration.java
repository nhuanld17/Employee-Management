package com.example.demo.config;

import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	private final UserService userService;
	private final BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	public SecurityConfiguration(UserService userService, BCryptPasswordEncoder passwordEncoder) {
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(request -> request
				.requestMatchers("/").hasAnyRole("USER", "ADMIN")
				.requestMatchers("/registration**").permitAll()
				.requestMatchers("/js/**").permitAll()
				.requestMatchers("/css/**").permitAll()
				.requestMatchers("/img/**").permitAll()
				.anyRequest().authenticated()
		).formLogin(form -> form
				.loginPage("/showLoginPage")
				.loginProcessingUrl("/authenticateTheUser")
				.permitAll()
		).logout(
				logout -> logout
						.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
						.permitAll()
		);
		return http.build();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
				.userDetailsService(userService)
				.passwordEncoder(passwordEncoder);
	}
}
