package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class UserUpdateDto {
	private long id;
	
	@NotBlank(message = "Thông tin bắt buộc")
	@Size(min = 3, message = "Độ dài tối thiểu là 1")
	private String firstName;
	
	@NotBlank(message = "Thông tin bắt buộc")
	@Size(min = 3, message = "Độ dài tối thiểu là 1")
	private String lastName;
	
	@NotBlank(message = "Thông tin bắt buộc")
	@Email(message = "Email không hợp lệ")
	private String email;
	
	@Size(min = 8, message = "Độ dài ít nhất 8 kí tự")
	@Pattern(regexp = "^(?=.*\\d)(?=.*[\\W_]).{8,}$",
			message = "Mật khẩu phải chứa ít nhất 1 chữ số và 1 kí tự đặc biệt")
	private String password;
	
	private MultipartFile profilePictureFile;
	
	public UserUpdateDto() {
	
	}
	
	public UserUpdateDto(long id, String firstName, String lastName, String email, String password) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
	}
	
	public UserUpdateDto(String firstName, String lastName, String email, String password) {
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
	
	public MultipartFile getProfilePictureFile() {
		return profilePictureFile;
	}
	
	public void setProfilePictureFile(MultipartFile profilePictureFile) {
		this.profilePictureFile = profilePictureFile;
	}
}
