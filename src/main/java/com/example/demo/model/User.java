package com.example.demo.model;

import com.example.demo.dto.UserRegistrationDto;
import com.example.demo.dto.UserUpdateDto;
import jakarta.persistence.*;

import java.util.Collection;

@Entity
@Table(name = "user",
		uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	private String email;
	
	private String password;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = {
			CascadeType.ALL
	})
	@JoinTable(
			name ="users_roles",
			joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
	)
	private Collection<Role> roles;
	
	public User() {
	
	}
	
	public User(long id, String firstName, String lastName, String email, String password, Collection<Role> roles) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.roles = roles;
	}
	
	public User(String firstName, String lastName, String email, String password, Collection<Role> roles) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.roles = roles;
	}
	
	public User(long id, String firstName, String lastName, String email, String password) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Collection<Role> getRoles() {
		return roles;
	}
	
	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}
	
	public UserRegistrationDto toUserRegistrationDto() {
		UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
		userRegistrationDto.setId(id);
		userRegistrationDto.setFirstName(firstName);
		userRegistrationDto.setLastName(lastName);
		userRegistrationDto.setEmail(email);
		userRegistrationDto.setPassword(password);
		return userRegistrationDto;
	}
	
	public UserUpdateDto toUserUpdateDto(){
		UserUpdateDto userUpdateDto = new UserUpdateDto();
		userUpdateDto.setId(id);
		userUpdateDto.setFirstName(firstName);
		userUpdateDto.setLastName(lastName);
		userUpdateDto.setEmail(email);
		userUpdateDto.setPassword(password);
		return userUpdateDto;
	}
}
