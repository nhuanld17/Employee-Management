package com.example.demo.service;

import com.example.demo.dto.UserRegistrationDto;
import com.example.demo.dto.UserUpdateDto;
import com.example.demo.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;


public interface UserService extends UserDetailsService {
	public User save(UserRegistrationDto userRegistrationDto);
	public User findByEmail(String email);
	public User findById(long id);
	public void update(UserUpdateDto userUpdateDto) throws IOException;
}
