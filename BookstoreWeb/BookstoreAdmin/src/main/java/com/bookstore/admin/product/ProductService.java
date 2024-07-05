package com.bookstore.admin.product;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bookstore.admin.brand.BrandNotFoundException;
import com.bookstore.admin.brand.BrandRepository;

import com.bookstore.entity.Product;

@Service
public class ProductService {
    // public static final int BRANDS_PER_PAGE = 10;

    @Autowired
    private ProductRepository repo;

    public List<Product> listAll(){
        return (List<Product>) repo.findAll();
    }

    
}