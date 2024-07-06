package com.bookstore.admin.product;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bookstore.entity.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer>{

    public Long countById(Integer id);

    public Product findByName(String name);

    @Query("UPDATE Product p SET p.enabled = ?2 WHERE p.id = ?1")
    @Modifying
    public void updateEnabledStatus(Integer id, boolean enabled);




    public Product save(Product product);

    public Iterable<Product> findAll();

    public Optional<Product> findById(Integer id);

    public void deleteById(Integer id);

}