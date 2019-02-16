package com.home.expenses.controllers;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.home.expenses.models.Store;
import com.home.expenses.repositories.ProductRepo;
import com.home.expenses.repositories.StoreRepo;

@Service
public class StoreController {

	@Autowired
	private StoreRepo storeRepo;
	
	@Autowired
	private ProductRepo productRepo;

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
		
		GeoDistanceQueryBuilder g = new GeoDistanceQueryBuilder("gp").point(new GeoPoint(gp)).distance(1, DistanceUnit.METERS);
		
		return StreamSupport.stream(storeRepo.search(g).spliterator(), false).collect(Collectors.toList());
		
		//return StreamSupport.stream(storeRepo.findB(gp).spliterator(), false).collect(Collectors.toList());
	}
	
	public List<Store> listByName (String name) {
		return StreamSupport.stream(storeRepo.findByName(name).spliterator(), false).collect(Collectors.toList());
	}
	
	public Store findByGp (String gp) {
		GeoDistanceQueryBuilder g = new GeoDistanceQueryBuilder("gp").point(new GeoPoint(gp)).distance(1, DistanceUnit.METERS);
		List<Store> results = StreamSupport.stream(storeRepo.search(g).spliterator(), false).collect(Collectors.toList());
		if (results !=  null && !results.isEmpty())
			return results.get(0);
		return null;
	}
	
	public Store findByName (String name) {
		return storeRepo.findFirstByName(name);
	}

	public Store update(Store store) {
		return storeRepo.save(store);
	}
	
	public Store remove (UUID id) {
		Store deleted = storeRepo.removeById(id);
		if (deleted != null) {
			deleted.getProducts().forEach(p -> productRepo.removeById(id));
		}
		return null;
	}
	
}
