package com.home.expenses.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

	@Autowired
	public StoreController storeController;
	
	@Autowired
	public ProductController productController;
	
	
	
}
