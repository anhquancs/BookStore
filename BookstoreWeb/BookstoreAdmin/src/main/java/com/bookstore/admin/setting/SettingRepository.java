package com.bookstore.admin.setting;

import org.springframework.data.repository.CrudRepository;

import com.bookstore.entity.Setting;

public interface SettingRepository extends CrudRepository<Setting, String>{
    
}
