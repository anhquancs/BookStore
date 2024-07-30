package com.bookstore.admin.review;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.bookstore.admin.paging.SearchRepository;
import com.bookstore.entity.Review;

public interface ReviewRepository extends SearchRepository<Review, Integer>{

    @Query("SELECT r FROM Review r WHERE r.headline LIKE %?1% OR " 
            + "r.comment LIKE %?1% OR r.product.name LIKE %?1% OR "
            + "CONCAT(r.customer.firstName, ' ', r.customer.lastName) LIKE %?1%")

    public Page<Review> findAll(String keyword, Pageable pageable);

    public List<Review> findAll();

    public Optional<Review> findById(Integer id);

    public Review save(Review reviewInDB);

    public boolean existsById(Integer id);

    public void deleteById(Integer id);

}
