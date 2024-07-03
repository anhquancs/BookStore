package com.bookstore.admin.brand;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bookstore.entity.Brand;

public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer>{

    public Long countById(Integer id);

    public Brand findByName(String name);

    @Query("SELECT b FROM Brand b WHERE b.name LIKE %?1%")
    public Page<Brand> findAll(String keyword, Pageable pageable);

    

    public Brand save(Brand brand);

    public void deleteById(Integer id);

    public Optional<Brand> findById(int i);

    public List<Brand> findAll();
    
}
