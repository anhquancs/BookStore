package com.bookstore.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.entity.order.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}