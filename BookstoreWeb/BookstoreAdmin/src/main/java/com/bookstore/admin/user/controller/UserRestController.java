package com.bookstore.admin.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.admin.user.UserService;

@RestController
public class UserRestController {
	@Autowired
	private UserService service;
	
	@PostMapping("/users/check_email")
	public String checkDuplicatEmail(Integer id, String email) {
		return service.isEmailUnique(id, email) ? "OK" : "Duplicated";
	}
}
