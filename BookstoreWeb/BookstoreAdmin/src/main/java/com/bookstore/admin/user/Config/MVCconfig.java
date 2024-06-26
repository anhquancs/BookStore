package com.bookstore.admin.user.Config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MVCconfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String dirName = "user-photos";
		Path userPhotosDir = Paths.get(dirName);
		 
		 String userphotosPath = userPhotosDir.toFile().getAbsolutePath();
		 
		 registry.addResourceHandler("/" + dirName + "/**")
		 .addResourceLocations("file:/" + userphotosPath + "/");
		

		//CATEGORY============
		String categoryImagesDirName = "../category-images";
		Path categoryImagesDir = Paths.get(categoryImagesDirName);
		 
		 String categoryImagesPath = categoryImagesDir.toFile().getAbsolutePath();
		 
		 registry.addResourceHandler("/category-images/**")
		 .addResourceLocations("file:/" + categoryImagesPath + "/");
		
	}

}
