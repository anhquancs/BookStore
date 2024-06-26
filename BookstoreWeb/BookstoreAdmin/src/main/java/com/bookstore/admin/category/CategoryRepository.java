package com.bookstore.admin.category;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.bookstore.entity.Category;

public interface CategoryRepository extends PagingAndSortingRepository <Category, Integer> {

    Category save(Category category);

    void saveAll(List<Category> of);

    Category findById(int i);

    Iterable<Category> findAll();
  
}
