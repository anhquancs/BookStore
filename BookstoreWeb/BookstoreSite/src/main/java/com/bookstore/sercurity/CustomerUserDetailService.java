package com.bookstore.sercurity;

import com.bookstore.customer.CustomerRepository;
import com.bookstore.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomerUserDetailService implements UserDetailsService {
    @Autowired private CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(email);
        if (customer == null)
            throw new UsernameNotFoundException("No email: " + email);
        return new CustomerUserDetails(customer);
    }
}
