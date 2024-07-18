package com.bookstore.customer;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bookstore.entity.City;
import com.bookstore.entity.Customer;
import com.bookstore.setting.CityRepository;

import jakarta.transaction.Transactional;
import net.bytebuddy.utility.RandomString;



@Service
@Transactional
public class CustomerService {

    @Autowired private CityRepository cityRepository;
    @Autowired private CustomerRepository customerRepository;
	@Autowired PasswordEncoder passwordEncoder;

    public List<City> listAllCity() {
        return cityRepository.findAllByOrderByNameAsc();
    }

    public boolean isEmailUnique(String email) {
		Customer customer = customerRepository.findByEmail(email);
		return customer == null;
	}

	public void registerCustomer(Customer customer) {

		encodePassword(customer);
		customer.setEnabled(false);
		customer.setCreatedTime(new Date());

		String randomCode = RandomString.make(64);
		customer.setVerificationCode(randomCode);

		customerRepository.save(customer);
	}

	void encodePassword(Customer customer) {
		String encodedPassword = passwordEncoder.encode(customer.getPassword());
		customer.setPassword(encodedPassword);
	}


	public boolean verify(String verificationCode) {
		Customer customer = customerRepository.findByVerificationCode(verificationCode);

		if (customer == null || customer.isEnabled()) {
			return false;
		} else {
			customerRepository.enable(customer.getId());
			return true;
		}
	}
}
