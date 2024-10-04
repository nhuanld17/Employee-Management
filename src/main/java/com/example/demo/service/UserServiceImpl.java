package com.example.demo.service;

import com.example.demo.dto.UserRegistrationDto;
import com.example.demo.dto.UserUpdateDto;
import com.example.demo.model.CustomUserDetails;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{
	private UserRepository userRepository;
	private BCryptPasswordEncoder passwordEncoder;
	private EntityManager entityManager;
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, EntityManager entityManager) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.entityManager = entityManager;
	}
	
	@Override
	public User save(UserRegistrationDto userRegistrationDto) {
		User user = new User(userRegistrationDto.getFirstName(),
				userRegistrationDto.getLastName(), userRegistrationDto.getEmail(),
				passwordEncoder.encode(userRegistrationDto.getPassword()), Arrays.asList(new Role("ROLE_USER")));
		return userRepository.saveAndFlush(user);
	}
	
	@Override
	public User findByEmail(String email) {
		TypedQuery<User> query = entityManager.createQuery(
				"select u from User u where u.email=:email", User.class);
		query.setParameter("email", email);
		
		// Sử dụng getResultList() để tránh NoResultException
		List<User> users = query.getResultList();
		
		// Kiểm tra nếu danh sách kết quả rỗng, trả về null
		if (users.isEmpty()) {
			return null; // Trả về null nếu không có người dùng nào với email này
		} else {
			return users.get(0); // Trả về người dùng đầu tiên (nếu email là duy nhất)
		}
	}
	
	@Override
	public User findById(long id) {
		return userRepository.getReferenceById(id);
	}
	
	@Override
	@Transactional
	public void update(UserUpdateDto userUpdateDto) throws IOException {
		Role role = new Role("ROLE_USER");
		userRepository.save(new User(userUpdateDto.getId(),
				userUpdateDto.getFirstName(),
				userUpdateDto.getLastName(),
				userUpdateDto.getEmail(),
				userUpdateDto.getPassword(),
				Arrays.asList(role),
				userUpdateDto.getProfilePictureFile().getBytes()));
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid email or password");
		}
		return new CustomUserDetails(user);
	}
}
