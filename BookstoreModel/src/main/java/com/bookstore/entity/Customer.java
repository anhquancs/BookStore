package com.bookstore.entity;

import java.util.Date;


import jakarta.annotation.Resource;
import jakarta.persistence.*;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; 

    @Column(nullable = false, unique = true, columnDefinition = "nvarchar(150)")
    private String email; 

    @Column(nullable = false, length = 64)
    private String password;
    
    @Column(name = "first_name", nullable = false, columnDefinition = "nvarchar(50)")
    private String firstName;
    
    @Column(name = "last_name", nullable = false, columnDefinition = "nvarchar(50)")
    private String lastName; 

    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;
    
    @Column(name = "address_line", columnDefinition = "nvarchar(200)")
    private String addressLine;

    @Column(nullable = false, columnDefinition = "nvarchar(200)")
    private String ward; 

    @Column(nullable = false, columnDefinition = "nvarchar(2002)")
    private String district; 

    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    private boolean enabled; 

    @Column(name = "created_time")
    private Date createdTime; 

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    @Enumerated(EnumType.STRING)
    @Column(name = "authentication_type", length = 10)
    private  AuthenticationType authenticationType;

    @Column(name = "reset_password_token", length = 30)
    private String resetPasswordToken; 

    public Customer() {
    }

    public Customer(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    

    @Override
    public String toString() {
        return "Customer [id=" + id + ", email=" + email + ", password=" + password + ", firstName=" + firstName
                + ", lastName=" + lastName + ", phoneNumber=" + phoneNumber + ", addressLine=" + addressLine + ", ward="
                + ward + ", district=" + district + ", verificationCode=" + verificationCode + ", enabled=" + enabled
                + ", createdTime=" + createdTime + ", city=" + city + "]";
    }

    public String getFullName() {
		return lastName + " " +  firstName;
	}

    public AuthenticationType getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(AuthenticationType authenticationType) {
        this.authenticationType = authenticationType;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    
}
