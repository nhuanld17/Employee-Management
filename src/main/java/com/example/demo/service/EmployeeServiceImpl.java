package com.example.demo.service;

import com.example.demo.dto.EmployeeDto;
import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService{
	private EmployeeRepository employeeRepository;
	private EntityManager entityManager;
	
	@Autowired
	public EmployeeServiceImpl(EmployeeRepository employeeRepository, EntityManager entityManager) {
		this.employeeRepository = employeeRepository;
		this.entityManager = entityManager;
	}
	
	@Override
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}
	
	@Override
	public Employee getEmployeeById(long id) {
		return employeeRepository.getReferenceById(id);
	}
	
	@Override
	public List<Employee> findByFirstName(String firstName) {
		TypedQuery<Employee> query = entityManager.createNamedQuery(
				"select e from Employee e where e.firstName = :firstName", Employee.class);
		query.setParameter("firstName", firstName);
		return query.getResultList();
	}
	
	@Override
	@Transactional
	public void saveEmployee(EmployeeDto employee) {
		employeeRepository.saveAndFlush(
				new Employee(employee.getFirstName(),
						employee.getLastName(),
						employee.getEmail())
		);
	}
	
	@Override
	@Transactional
	public void updateEmployee(EmployeeDto employeeDto) {
		employeeRepository.saveAndFlush(employeeDto.toEmployee());
	}
	
	@Override
	@Transactional
	public void deleteEmployeeById(long id) {
		employeeRepository.deleteById(id);
	}
	
	@Override
	public Page<Employee> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		return employeeRepository.findAll(pageable);
	}
	
	@Override
	public Page<Employee> searchEmployeeByEmailWithPaginationAndSort(String email, int pageNo, int pageSize, String sortField, String sortDirection) {
		// Xác định thứ tự sắp xếp tăng dần hoặc giảm dần
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
				Sort.by(sortField).ascending() : Sort.by(sortField).descending();
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		return employeeRepository.findByEmailContainingIgnoreCase(email, pageable);
	}
}
