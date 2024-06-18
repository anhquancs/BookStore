package com.bookstore.admin.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.bookstore.admin.security.BookStoreUserDetails;
import com.bookstore.admin.user.repository.UserRepository;
import com.bookstore.entity.User;

public class BookStoreUserDetailsService implements UserDetailsService{

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user =	userRepo.getUserByEmail(email);
		
		if(user != null) {
			return new BookStoreUserDetails(user);
		}
		throw new UsernameNotFoundException("Could not find user with email: " +email);
	}

}
