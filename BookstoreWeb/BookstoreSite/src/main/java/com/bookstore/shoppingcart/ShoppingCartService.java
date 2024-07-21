package com.bookstore.shoppingcart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.entity.CartItem;
import com.bookstore.entity.Customer;
import com.bookstore.entity.Product;

@Service
public class ShoppingCartService {

    @Autowired private CartItemRepository cartItemRepository; 

    public Integer addProduct(Integer productId, Integer quantity, Customer customer) throws ShoppingCartException {
        Integer updateQuantity = quantity; 
        Product product = new Product(productId); 

        CartItem cartItem = cartItemRepository.findByCustomerAndProduct(customer, product); 

        if (cartItem != null) {
            updateQuantity = cartItem.getQuantity() + quantity; 
            if (updateQuantity > 5) {
                throw new ShoppingCartException("Không thể thêm hơn " + quantity + " sản phảm");
            }
        } else {
            cartItem = new CartItem(); 
            cartItem.setCustomer(customer);
            cartItem.setProduct(product);
        }

        cartItem.setQuantity(updateQuantity);
        cartItemRepository.save(cartItem);
        return updateQuantity;
    }
}
