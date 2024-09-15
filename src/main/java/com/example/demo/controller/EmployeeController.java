package com.example.demo.controller;

import com.example.demo.dto.EmployeeDto;
import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class EmployeeController {
	private EmployeeService employeeService;
	
	@Autowired
	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	// Display list of employee
	@GetMapping("/")
	public String viewHomePage(Model model) {
		return findPaginatedWithSort(1, "id", "asc", model);
	}
	
	@GetMapping("/showEmployeeForm")
	public String showEmployeeForm(Model model) {
		EmployeeDto employee = new EmployeeDto();
		model.addAttribute("employee", employee);
		return "new-employee-form";
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		binder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	@PostMapping("/saveEmployee")
	public String saveEmployee(@Valid @ModelAttribute("employee") EmployeeDto employee,
	                           BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "new-employee-form";
		}
		employeeService.saveEmployee(employee);
		return "redirect:/showEmployeeForm";
	}
	
	@PostMapping("/updateEmployee")
	public String updateEmployeeForm(@ModelAttribute(name = "employee") Employee employee){
		employeeService.updateEmployee(employee);
		return "redirect:/";
	}
	
	@GetMapping("/showFormForUpdate/{id}")
	public String showFormForUpdate(@PathVariable long id, Model model) {
		Employee employee = employeeService.getEmployeeById(id);
		model.addAttribute("employee", employee);
		return "update-employee-form";
	}
	
	@GetMapping("/deleteEmployee/{id}")
	public String deleteEmployee(@PathVariable("id") long id) {
		employeeService.deleteEmployeeById(id);
		return "redirect:/";
	}
	
	@GetMapping("/detailEmployee/{id}")
	public String detailEmployee(@PathVariable(value = "id") long id, Model model) {
		Employee employee = employeeService.getEmployeeById(id);
		model.addAttribute("employee", employee);
		return "detail_employee";
	}
	
	@GetMapping("/page/{pageNo}")
	private String findPaginatedWithSort(@PathVariable(value = "pageNo") int pageNo,
	                                     @RequestParam("sortField") String sortField,
	                                     @RequestParam("sortDir") String sortDir,
	                                     Model model) {
		int pageSize = 5;
		
		Page<Employee> page = employeeService.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<Employee> listEmployees = page.getContent();
		
		// Đưa dữ liệu phân trang vào model
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		
		model.addAttribute("listEmployees", listEmployees);
		
		return "index";
	}
}
