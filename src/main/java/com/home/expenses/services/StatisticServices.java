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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.home.expenses.controllers.Config;
import com.home.expenses.models.Statistic;
import com.home.expenses.models.Store;
import com.home.expenses.repositories.StoreRepo;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/statistic")
@Log4j2
@ComponentScan
public class StatisticServices {
	
	@Autowired
	Config cfg;
	
	
	@GetMapping("/list")
	ResponseEntity<List<Statistic>> list () {
		List<Statistic> result = cfg.statisticController.listAll();
		if (result != null) {
			return ResponseEntity.ok(result);
		}
		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping(value="/findbyname/{name}")
	ResponseEntity<Statistic> findByName (@PathVariable String name) {
		Statistic result = cfg.statisticController.findByName(name);
		if (result != null) {
			return ResponseEntity.ok(result);
		}
		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/remove")
	ResponseEntity <Statistic> remove (@PathVariable String id) {
		Statistic result = cfg.statisticController.remove(UUID.fromString(id));
		if (result != null) {
			return ResponseEntity.ok(result);
		}
		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	}
	
}
