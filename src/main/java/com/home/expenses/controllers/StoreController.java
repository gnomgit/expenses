package com.home.expenses.controllers;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.home.expenses.models.Store;
import com.home.expenses.repositories.StoreRepo;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class StoreController {

	@Autowired
	private StoreRepo storeRepo;

	public Store insert ( String storeName, 
							Double lat, 
							Double lon) {
		
		UUID id = UUID.randomUUID();
		
		Store s = null;
		do {
			id = UUID.randomUUID();
			storeRepo.findById(id);
		}	while (s != null);
		
		s = new Store(id);
		s.setName(storeName);
		s.setGp(lat, lon);
		
		storeRepo.save(s);
		
		return s;
	}

	public List<Store> listAll () {
		return StreamSupport.stream(storeRepo.findAll().spliterator(), false).collect(Collectors.toList());
	}
	
	public List<Store> listByGp (String gp) {
		return StreamSupport.stream(storeRepo.findByGp(gp).spliterator(), false).collect(Collectors.toList());
	}
	
	public List<Store> listByName (String name) {
		return StreamSupport.stream(storeRepo.findByName(name).spliterator(), false).collect(Collectors.toList());
	}
	
	public Store findByGp (String gp) {
		return storeRepo.findFirstByGp(gp);
	}
	
	public Store findByName (String name) {
		return storeRepo.findFirstByName(name);
	}
	
}
