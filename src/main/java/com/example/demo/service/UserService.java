package com.example.demo.service;

import com.example.demo.dto.UserRegistrationDto;
import com.example.demo.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService extends UserDetailsService {
	public User save(UserRegistrationDto userRegistrationDto);
	public User findByEmail(String email);
}
