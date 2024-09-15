package com.example.demo.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
// Lưu trữ thông tin của người đăng nhập
public class CustomUserDetails implements UserDetails {
	private User user;
	
	public CustomUserDetails(User user) {
		this.user = user;
	}
	
	public CustomUserDetails() {
	
	}
	
	public Long getId(){
		return user.getId();
	}
	
	public String getFirstName() {
		return user.getFirstName();
	}
	
	public String getLastName() {
		return user.getLastName();
	}
	
	public String getEmail() {
		return user.getEmail();
	}
	
	@Override
	public String getUsername() {
		return user.getEmail(); // Sử dụng email làm tên đăng nhập
	}
	
	@Override
	public String getPassword() {
		return user.getPassword();
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList();
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return UserDetails.super.isAccountNonExpired();
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return UserDetails.super.isAccountNonLocked();
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return UserDetails.super.isCredentialsNonExpired();
	}
	
	@Override
	public boolean isEnabled() {
		return UserDetails.super.isEnabled();
	}
	
	public User getUser(){
		return user;
	}
}
