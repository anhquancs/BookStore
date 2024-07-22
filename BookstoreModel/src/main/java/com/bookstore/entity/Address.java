package com.bookstore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; 

    @Column(name = "first_name", nullable = false, columnDefinition = "nvarchar(45)")
    private String firstName; 

    @Column(name = "last_name", nullable = false, columnDefinition = "nvarchar(45)")
    private String lastName; 

    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber; 

    @Column(name = "address_line", nullable = false, columnDefinition = "nvarchar(100)")
    private String addressLine; 

    @Column(nullable = false, columnDefinition = "nvarchar(45)")
    private String ward; 

    @Column(nullable = false, columnDefinition = "nvarchar(45)")
    private String district; 

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city; 

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer; 

    @Column(name = "default_address")
    private boolean defaultForShipping;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public boolean isDefaultForShipping() {
        return defaultForShipping;
    }

    public void setDefaultForShipping(boolean defaultForShipping) {
        this.defaultForShipping = defaultForShipping;
    }

    @Override
    public String toString() {
        String address = lastName;

        if (lastName != null && !lastName.isEmpty()) address += " " + firstName;
        else address = firstName;

        if (addressLine != null && !addressLine.isEmpty()) address += ", " + addressLine; 
        
        if (!ward.isEmpty()) address += ", " + ward; 

        if (district != null && !district.isEmpty()) address += ", " + district + ", "; 
        
        address += city.getName(); 

        if (!phoneNumber.isEmpty()) address += ". Số điện thoại: " + phoneNumber; 

        return address;
    } 

    
}
