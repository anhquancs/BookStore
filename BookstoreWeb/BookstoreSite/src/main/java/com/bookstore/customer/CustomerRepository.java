package com.bookstore.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bookstore.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    @Query("SELECT c FROM Customer c WHERE c.email = ?1")
    public Customer findByEmail(String email); 

    @Query("SELECT c FROM Customer c WHERE c.verificationCode = ?1")
	public Customer findByVerificationCode(String code);

    @Query("UPDATE Customer c SET c.enabled = true, c.verificationCode = null WHERE c.id = ?1")
	@Modifying
	public void enable(Integer id);	
}