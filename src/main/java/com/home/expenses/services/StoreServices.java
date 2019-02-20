package com.home.expenses.services;

import java.util.List;
import java.util.UUID;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.home.expenses.controllers.Config;
import com.home.expenses.models.Store;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/store")
@Log4j2
@ComponentScan
public class StoreServices {
	
	@Autowired
	Config cfg;

	@PostMapping("/insert")
	ResponseEntity<Store> insert (@RequestBody String store) {
		JSONObject jObject;
		try {
			jObject = new JSONObject(store);
			String name = (String)jObject.get("name");
			Double lat = ((Number)jObject.get("lat")).doubleValue();
			Double lon = ((Number)jObject.get("lon")).doubleValue();
			String geoHash = new GeoPoint(lat, lon).geohash();
			
			Store newStore = cfg.storeController.findByGp(geoHash);
			if (newStore == null)
				return ResponseEntity.ok(cfg.storeController.insert(name, lat, lon));
			
			return new ResponseEntity<>(newStore, HttpStatus.FOUND);
			
		} 	catch (JSONException e) {
			log.error(e.getStackTrace());
		}
		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/list")
	ResponseEntity<List<Store>> list () {
		List<Store> result = cfg.storeController.listAll();
		if (result != null) {
			return ResponseEntity.ok(result);
		}
		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/remove")
	ResponseEntity <Store> remove (@PathVariable String id) {
		Store result = cfg.storeController.remove(UUID.fromString(id));
		if (result != null) {
			return ResponseEntity.ok(result);
		}
		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	}
	
}
