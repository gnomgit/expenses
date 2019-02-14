package com.home.expenses.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
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
	ProductController productController;
	
	@Autowired
	StoreController storeController;
	
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
			String geoHash = (String)jObject.get("gp");
			
			Store store = storeController.findByGp(geoHash);
			if (store == null)
				return (ResponseEntity<Product>) ResponseEntity.badRequest();
			
			return ResponseEntity.ok(productController.insert(name, paid, store, date, comments, presentation, currency, quantity));
			
		} 	catch (JSONException | ParseException e) {
			log.error(e.getStackTrace());
		}
		return (ResponseEntity<Product>) ResponseEntity.badRequest();		
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
