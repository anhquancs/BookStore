package com.bookstore.admin.category;



import org.springframework.data.repository.CrudRepository;

import com.bookstore.entity.Category;

public interface CategoryRepository extends CrudRepository<Category, Integer> {

}
