package com.example.demo.dto;

import com.example.demo.model.Employee;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EmployeeDto {
	private long id;
	
	@NotBlank(message = "Thông tin bắt buộc")
	@Size(min = 1, message = "Độ dài tối thiểu là 1 kí tự")
	private String firstName;
	
	@NotBlank(message = "Thông tin bắt buộc")
	@Size(min = 1, message = "Độ dài tối thiểu là 1 kí tự")
	private String lastName;
	
	@NotBlank(message = "Thông tin bắt buộc")
	@Email(message = "Email không hợp lệ")
	private String email;
	
	public EmployeeDto(String firstName, String lastName, String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}
	
	public EmployeeDto() {
	}
	
	public EmployeeDto(long id, String firstName, String lastName, String email) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
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
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public Employee toEmployee(){
		return new Employee(id, firstName, lastName, email);
	}
}
