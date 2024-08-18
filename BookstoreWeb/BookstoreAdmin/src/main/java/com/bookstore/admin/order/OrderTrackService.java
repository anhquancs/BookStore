package com.bookstore.admin.order;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.admin.product.ProductService;
import com.bookstore.entity.order.Order;
import com.bookstore.entity.order.OrderDetail;
import com.bookstore.entity.order.OrderStatus;
import com.bookstore.entity.order.OrderTrack;
import com.bookstore.entity.product.Product;

@Service
public class OrderTrackService {

    @Autowired
    private OrderTrackRepository orderTrackRepository;

    @Autowired
    private ProductService productService;

    public void updateOrderTrackStatus(int orderTrackId, OrderStatus newStatus) {
        OrderTrack orderTrack = orderTrackRepository.findById(orderTrackId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy OrderTrack"));

        OrderStatus oldStatus = orderTrack.getStatus();
        orderTrack.setStatus(newStatus);
        orderTrackRepository.save(orderTrack);

        if (newStatus == OrderStatus.RETURNED && oldStatus != OrderStatus.RETURNED) {
            restoreProductQuantities(orderTrack.getOrder());
        }
    }

    private void restoreProductQuantities(Order order) {
        Set<OrderDetail> orderDetails = order.getOrderDetails();
        for (OrderDetail orderDetail : orderDetails) {
            Product product = orderDetail.getProduct();
            int returnedQuantity = orderDetail.getQuantity();
            product.setQuantity(product.getQuantity() + returnedQuantity);
            if (!product.isInStock()) {
                product.setInStock(true);
            }
            productService.save(product);
        }
    }
}
