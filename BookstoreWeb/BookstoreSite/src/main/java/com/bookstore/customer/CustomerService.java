package com.bookstore.customer;

import java.util.Date;
import java.util.List;

import com.bookstore.entity.AuthenticationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

	@Autowired
	private CityRepository cityRepository;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	PasswordEncoder passwordEncoder;

	public List<City> listAllCity() {
		return cityRepository.findAllByOrderByNameAsc();
	}

	public boolean isEmailUnique(String email) {
		Customer customer = customerRepository.findByEmail(email);

		return customer == null;
	}

	public void registerCustomer(Customer customer) {
		try {
			String pass = customer.getPassword();
			System.out.println(pass);

			encodePassword(customer);

			checkPassword(customer, pass);
			
			customer.setEnabled(false);
			customer.setCreatedTime(new Date());
			customer.setAuthenticationType(AuthenticationType.BOOKSTORE);

			String randomCode = RandomString.make(64);
			customer.setVerificationCode(randomCode);

			customerRepository.save(customer);
		} catch (Exception e) {
			// Log lỗi
			e.printStackTrace();
		}
	}

	public Customer getCustomerByEmail(String email) {
		return customerRepository.findByEmail(email);
	}
 
	public boolean checkPassword(Customer customer, String rawPassword) {
		// So sánh mật khẩu nhập vào với mật khẩu đã mã hóa
		return new BCryptPasswordEncoder().matches(rawPassword, customer.getPassword());
	}

	private void encodePassword(Customer customer) {
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

	public void updateAuthenticationType(Customer customer, AuthenticationType type) {
		if (!customer.getAuthenticationType().equals(type)) {
			customerRepository.updateAuthenticationType(customer.getId(), type);
		}
	}

    public void addNewCustomerUponOAuthLogin(String name, String email) {
        Customer customer = new Customer(); 
        customer.setEmail(email);

		setName(name, customer);

        customer.setFirstName(name);
        customer.setEnabled(true);
        customer.setCreatedTime(new Date());
        customer.setAuthenticationType(AuthenticationType.GOOGLE);
        customer.setPassword("");
        customer.setAddressLine("");
        customer.setPhoneNumber("");
        customer.setCity(null);;
        customer.setDistrict("");
        customer.setWard("");

		customerRepository.save(customer);
    }

	private void setName(String name, Customer customer) {
		String[] nameArray = name.split(" ");
		if (nameArray.length < 2) {
			customer.setFirstName(name);
			customer.setLastName("");
		} else {
			String firstName = nameArray[0];
			customer.setFirstName(firstName);

			String lastName = name.replaceFirst(firstName, "");
			customer.setLastName(lastName);
		}
	}
}
