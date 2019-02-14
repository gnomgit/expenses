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
import com.home.expenses.models.Currency;
import com.home.expenses.models.Meassure;
import com.home.expenses.models.Product;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/expenses")
@Log4j2
@ComponentScan
public class ProductServices {

	@Autowired
	ProductController productController;
	
	@PostMapping("/insert")
	ResponseEntity<Product> insert (@RequestBody String prod) {
		JSONObject jObject;
		try {
			jObject = new JSONObject(prod);
			String name = (String)jObject.get("name");
			String store = (String)jObject.get("store");
			Double paid = ((Number)jObject.get("paid")).doubleValue();
			Double lat = ((Number)jObject.get("lat")).doubleValue();
			Double lon = ((Number)jObject.get("lon")).doubleValue();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
			Date date = formatter.parse((String)jObject.getString("date"));
			String comments = (String)jObject.get("comments");
			Meassure presentation = Meassure.valueOf((String)jObject.get("presentation"));
			Currency currency = Currency.valueOf((String)jObject.get("currency"));
			Double quantity = ((Number)jObject.get("quantity")).doubleValue();
			
			return ResponseEntity.ok(productController.insert(store, 
															name, 
															paid, 
															lat, 
															lon, 
															date, 
															comments, 
															presentation, 
															currency,
															quantity));
		} 	catch (JSONException | ParseException e) {
			log.error(e.getStackTrace());
		}
		return (ResponseEntity<Product>) ResponseEntity.badRequest();		
	}
	
	@GetMapping("/list")
	ResponseEntity<List<Product>> list () {
		productController.listAll();
	}
	
}
