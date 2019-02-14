package com.home.expenses.controllers;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.home.expenses.models.Currency;
import com.home.expenses.models.Meassure;
import com.home.expenses.models.Product;
import com.home.expenses.repositories.ProductRepo;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ProductController {

	@Autowired
	private ProductRepo productRepo;

	public Product insert(String storeName, 
						String name, 
						Double paid, 
						Double lat, 
						Double lon, 
						Date date, 
						String comments, 
						Meassure presentation, 
						Currency currency, 
						Double quantity) {
		
		UUID id = UUID.randomUUID();
		
		Product p = null;
		do {
			id = UUID.randomUUID();
			productRepo.findById(id);
		}	while (p != null);
		
		p = new Product();
		p.setId(id);
		p.setName(name);
		p.setStoreName(storeName);
		p.setPaid(paid);
		p.setGp(lat, lon);
		p.setDate(date);
		p.setComments(comments);
		p.setMeassure(presentation);
		
		productRepo.save(p);
		
		return p;
	}

	public List<Product> listAll() {
		
	}
	
}
