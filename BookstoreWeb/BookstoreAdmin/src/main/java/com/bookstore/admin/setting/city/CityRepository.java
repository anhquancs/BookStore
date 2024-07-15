package com.bookstore.admin.setting.city;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bookstore.entity.City;

public interface CityRepository extends CrudRepository<City, Integer>{
    
    public List<City> findAllByOrderByNameAsc();
    
}
