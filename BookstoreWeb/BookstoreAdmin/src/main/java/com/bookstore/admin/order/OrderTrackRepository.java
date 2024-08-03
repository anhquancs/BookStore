package com.bookstore.admin.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.entity.order.OrderTrack;

public interface OrderTrackRepository extends JpaRepository<OrderTrack, Integer> {
}