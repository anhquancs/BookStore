package com.bookstore.admin.setting;

import org.springframework.data.repository.CrudRepository;

import com.bookstore.entity.Setting;
import com.bookstore.entity.SettingCategory;

import java.util.List;


public interface SettingRepository extends CrudRepository<Setting, String>{
    public List<Setting> findByCategory(SettingCategory category);
    
}
