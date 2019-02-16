package com.home.expenses.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
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
import com.home.expenses.models.Currency;
import com.home.expenses.models.Meassure;
import com.home.expenses.models.Product;
import com.home.expenses.models.Store;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/expenses")
@Log4j2
@ComponentScan
public class ProductServices {

	@Autowired
	Config cfg;
	
	@PostMapping("/insert")
	ResponseEntity<Product> insert (@RequestBody String prod) {
		JSONObject jObject;
		try {
			jObject = new JSONObject(prod);
			String name = (String)jObject.get("name");
			Double paid = ((Number)jObject.get("paid")).doubleValue();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
			Date date = formatter.parse((String)jObject.getString("date"));
			String comments = (String)jObject.get("comments");
			Meassure presentation = Meassure.valueOf((String)jObject.get("presentation"));
			Currency currency = Currency.valueOf((String)jObject.get("currency"));
			Double quantity = ((Number)jObject.get("quantity")).doubleValue();
			String geoHash = (String)jObject.get("store");
			
			Store store = cfg.storeController.findByGp(geoHash);
			if (store == null)
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			
			Product p = cfg.productController.insert(name, paid, store, date, comments, presentation, currency, quantity);
			if (p != null) {
				store.addProduct(p);
				cfg.storeController.update (store);
			}
			
			return ResponseEntity.ok(p);
			
		} 	catch (JSONException | ParseException e) {
			log.error(e.getMessage());
		}
		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);		
	}
	
	@GetMapping("/list")
	ResponseEntity<List<Product>> list () {
		List<Product> result = cfg.productController.listAll();
		if (result != null) {
			return ResponseEntity.ok(result);
		}
		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/remove")
	ResponseEntity <Product> remove (@PathVariable String id) {
		Product result = cfg.productController.remove(UUID.fromString(id));
		if (result != null) {
			return ResponseEntity.ok(result);
		}
		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	}
	
}
