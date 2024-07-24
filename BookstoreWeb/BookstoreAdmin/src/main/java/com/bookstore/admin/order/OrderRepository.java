package com.bookstore.admin.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bookstore.admin.paging.SearchRepository;
import com.bookstore.entity.order.Order;

public interface OrderRepository extends CrudRepository<Order, Integer>, PagingAndSortingRepository<Order, Integer>, SearchRepository<Order, Integer>  {
    @Query("SELECT o FROM Order o WHERE o.firstName LIKE %?1% OR"
            + " o.lastName LIKE %?1% OR o.phoneNumber LIKE %?1% OR"
            + " o.addressLine LIKE %?1% OR"
            + " o.ward LIKE %?1% OR"
            + " o.district LIKE %?1% OR o.city LIKE %?1% OR"
            + " o.paymentMethod LIKE %?1% OR o.status LIKE %?1% OR"
            + " o.customer.firstName LIKE %?1% OR"
            + " o.customer.lastName LIKE %?1%")
    public Page<Order> findAll(String keyword, Pageable pageable);

    public Long countById(Integer id);

}
