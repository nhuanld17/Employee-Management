package com.example.demo.service;

import com.example.demo.dto.EmployeeDto;
import com.example.demo.model.Employee;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeeService {
	public List<Employee> getAllEmployees();
	public List<Employee> findByFirstName(String firstName);
	public void saveEmployee(EmployeeDto employeeDto);
	public void updateEmployee(Employee employee);
	public Employee getEmployeeById(long id);
	public void deleteEmployeeById(long id);
	Page<Employee> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
}
