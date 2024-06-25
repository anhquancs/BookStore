package com.bookstore.admin.category;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.bookstore.entity.Category;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer>{

	Category save(Category category);

	
}
