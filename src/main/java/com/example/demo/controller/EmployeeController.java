package com.example.demo.controller;

import com.example.demo.dto.EmployeeDto;
import com.example.demo.dto.UserUpdateDto;
import com.example.demo.model.CustomUserDetails;
import com.example.demo.model.Employee;
import com.example.demo.model.User;
import com.example.demo.service.EmployeeService;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.IOException;
import java.util.List;

@Controller
public class EmployeeController {
	private final EmployeeService employeeService;
	private final UserService userService;
	
	@Autowired
	public EmployeeController(EmployeeService employeeService, UserService userService) {
		this.employeeService = employeeService;
		this.userService = userService;
		
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
	public String updateEmployeeForm(@Valid @ModelAttribute(name = "employee") EmployeeDto employee,
	                                 BindingResult bindingResult){
		if (bindingResult.hasErrors()) {
			return "update-employee-form";
		}
		employeeService.updateEmployee(employee);
		return "redirect:/";
	}
	
	@GetMapping("/showFormForUpdate/{id}")
	public String showFormForUpdate(@PathVariable long id, Model model) {
		Employee employee = employeeService.getEmployeeById(id);
		EmployeeDto employeeDto = employee.toEmployeeDto();
		model.addAttribute("employee", employeeDto);
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
	
	@GetMapping("/showProfile")
	public String showProfile(Model model) throws IOException {
		
		// Lấy thông tin người dùng đã đăng nhập
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails userDetails = (CustomUserDetails)authentication.getPrincipal();
		
		UserUpdateDto userUpdateDto = userService.findById(userDetails.getId()).toUserUpdateDto();
		System.out.println("Id: " + userUpdateDto.getId());
		System.out.println("First name: " + userUpdateDto.getFirstName());
		System.out.println("Last name: " + userUpdateDto.getLastName());
		System.out.println("Email: " + userUpdateDto.getEmail());
		System.out.println("Image: " + userUpdateDto.getProfilePictureFile().getOriginalFilename());
		System.out.println("==========================================");
		
		if (userUpdateDto.getProfilePictureFile() != null){
			String base64Encoded = Base64.encodeBase64String(userUpdateDto.getProfilePictureFile().getBytes());
			model.addAttribute("profilePicture", base64Encoded);
		}
		// Thêm thông tin vào model để hiển thị trong view
		model.addAttribute("user", userUpdateDto);
		
		return "profile";
	}
	
	@PostMapping("/updateUser")
	public String updateUser(@Valid @ModelAttribute("user") UserUpdateDto userUpdateDto,
	                         BindingResult bindingResult) throws IOException {
		if (bindingResult.hasErrors()) {
			System.out.println("Error: " + bindingResult.getAllErrors());
			return "profile";
		}
		
		User user = userService.findById(userUpdateDto.getId());
		userUpdateDto.setPassword(user.getPassword());
		
		// Handling avatar files
		MultipartFile profilePicture = userUpdateDto.getProfilePictureFile();
		System.out.println(userUpdateDto.getProfilePictureFile().isEmpty());
		if (profilePicture != null && !profilePicture.isEmpty()) {
			try {
				user.setProfilePicture(profilePicture.getBytes());
			} catch (IOException e){
				e.printStackTrace();
			}
		}
		userService.update(userUpdateDto);
		return "redirect:/showProfile";
	}
	
	@GetMapping("/search")
	public String searchEmpByEmail(
			@RequestParam("search-by-email") String email,
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "size", defaultValue = "5") int pageSize,
			@RequestParam(value = "sortField", defaultValue = "id") String sortField,
			@RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection,
			Model model
	){
		// Gọi service để tìm kiếm, phân trang và sắp xếp
		Page<Employee> employeePage = employeeService.searchEmployeeByEmailWithPaginationAndSort(email, pageNo, pageSize, sortField, sortDirection);
		model.addAttribute("employees", employeePage.getContent());
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDirection", sortDirection);
		model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
		model.addAttribute("totalPages", employeePage.getTotalPages());
		model.addAttribute("email", email);
		
		return "search-results-list";
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
