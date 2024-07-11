package com.bookstore.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bookstore.entity.Product;

@Service
@Transactional
public class ProductService {
    public static final int PRODUCTS_PER_PAGE = 10;

    @Autowired private ProductRepository repo;

    public Page<Product> listByCategory(int pageNum, Integer categoryId){
        String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
        Pageable pageable = PageRequest.of(pageNum -1, PRODUCTS_PER_PAGE);

        return repo.listByCategory(categoryId, categoryIdMatch, pageable);
    }
    

}