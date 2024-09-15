package com.example.demo.controller;

import com.example.demo.dto.UserRegistrationDto;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

	private UserService userService;
	
	@Autowired
	public UserRegistrationController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping
	public String showRegistrationForm(Model model) {
		UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
		model.addAttribute("user", userRegistrationDto);
		return "registration-form";
	}
	
	/* Handle empty string */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		binder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	@PostMapping
	public String processRegistrationForm(@Valid @ModelAttribute("user") UserRegistrationDto register,
	                                      BindingResult bindingResult,
	                                      Model model,
	                                      HttpSession session) {
		// Form validation
		if (bindingResult.hasErrors()) {
			return "registration-form";
		}
		
		// Kiểm tra user đã tồn tại hay chưa
		User user = userService.findByEmail(register.getEmail());
		if (user != null) {
			model.addAttribute("user", new UserRegistrationDto());
			model.addAttribute("user_error", "Tài khoản đã tồn tại");
			return "registration-form";
		}
		
		// Nếu chưa tồn tại thì lưu vào csdl
		user = userService.save(register);
		session.setAttribute("user", user);
		// Trả về trang đăng nhập với các trường input đã được điền sẵn nhờ vào session
		return "login-form";
	}
}
