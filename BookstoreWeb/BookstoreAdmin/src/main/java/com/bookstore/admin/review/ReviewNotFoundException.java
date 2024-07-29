package com.bookstore.admin.review;

public class ReviewNotFoundException extends Exception{

    public ReviewNotFoundException(String message) {
		super(message);
	}

    public ReviewNotFoundException() {
        
    }
}
