package com.bookstore.shoppingcart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.bookstore.Utility;
import com.bookstore.customer.CustomerService;
import com.bookstore.entity.CartItem;
import com.bookstore.entity.Customer;
import com.bookstore.exception.CustomerNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService cartService;
    @Autowired
    private CustomerService customerService;

    @GetMapping("/cart")
    public String viewCart(Model model, HttpServletRequest request) {
        Customer customer = getAuthenticatedCustomer(request); 
        List<CartItem> cartItems = cartService.listByCartItems(customer); 

        float estimatedTotal = 0.0F; 

        for (CartItem cartItem : cartItems) {
            estimatedTotal  += cartItem.getSubtotal();    
        }   

        model.addAttribute("cartItems", cartItems); 
        model.addAttribute("estimatedTotal", estimatedTotal); 
        
        return "cart/shopping_cart";
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request){
        String email = Utility.getEmailOfAuthenticatedCustomer(request);
        return customerService.getCustomerByEmail(email);
    }
}
