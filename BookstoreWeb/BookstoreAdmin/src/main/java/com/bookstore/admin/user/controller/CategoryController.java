package com.bookstore.admin.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.bookstore.admin.user.service.CategoryService;
import com.bookstore.entity.Category;

import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class CategoryController {
	@Autowired
	private CategoryService service;
	
	@GetMapping("/categories")
	public String listAll(Model model) {
		List<Category> listCategories = service.listAll();
		model.addAttribute("listCategories", listCategories);

		return "categories/categories";
	}
	
	
}
