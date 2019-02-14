package com.home.expenses.services;

import java.util.List;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.home.expenses.controllers.ProductController;
import com.home.expenses.controllers.StoreController;
import com.home.expenses.models.Product;
import com.home.expenses.models.Store;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/store")
@Log4j2
@ComponentScan
public class StoreServices {

	@Autowired
	ProductController productController;
	
	@Autowired
	StoreController storeController;
	
	@PostMapping("/insert")
	ResponseEntity<Store> insert (@RequestBody String store) {
		JSONObject jObject;
		try {
			jObject = new JSONObject(store);
			String name = (String)jObject.get("name");
			Double lat = ((Number)jObject.get("lat")).doubleValue();
			Double lon = ((Number)jObject.get("lon")).doubleValue();
			String geoHash = new GeoPoint(lat, lon).geohash();
			
			Store newStore = storeController.findByGp(geoHash);
			if (store == null)
				return ResponseEntity.ok(storeController.insert(name, lat, lon));
			
			return (ResponseEntity<Store>) ResponseEntity.badRequest();
			
		} 	catch (JSONException e) {
			log.error(e.getStackTrace());
		}
		return (ResponseEntity<Store>) ResponseEntity.badRequest();		
	}
	
	@GetMapping("/list")
	ResponseEntity<List<Product>> list () {
		List<Product> result = productController.listAll();
		if (result != null) {
			return ResponseEntity.ok(result);
		}
		return (ResponseEntity<List<Product>>) ResponseEntity.badRequest();
	}
	
}
