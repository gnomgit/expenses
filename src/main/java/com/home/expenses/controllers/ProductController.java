package com.home.expenses.controllers;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.home.expenses.models.Currency;
import com.home.expenses.models.Frequency;
import com.home.expenses.models.Meassure;
import com.home.expenses.models.Price;
import com.home.expenses.models.Product;
import com.home.expenses.models.Store;
import com.home.expenses.repositories.ProductRepo;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ProductController {

	@Autowired
	private ProductRepo productRepo;
	
	public Product insert ( String name, 
			Double paid, 
			Store store,
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
		p.setStore(store);
		p.setPaid(paid);
		p.setDate(date);
		p.setComments(comments);
		p.setMeassure(presentation);
		p.setQuantity(quantity);
		p.setFrequency(new Frequency());
		p.setPrice(new Price());
		p.setTicketName(name);
		
		productRepo.save(p);
		
		return p;
	}

	public List<Product> listAll () {
		return StreamSupport.stream(productRepo.findAll().spliterator(), false).collect(Collectors.toList());
	}
	
}
