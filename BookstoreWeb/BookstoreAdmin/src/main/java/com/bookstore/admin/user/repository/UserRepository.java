package com.bookstore.admin.user.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.bookstore.entity.User;

public interface UserRepository  extends CrudRepository<User, Integer>{
    @Query("Select u from User u Where u.email = :email")
	public User getUserByEmail(@Param("email") String email);
    
}
