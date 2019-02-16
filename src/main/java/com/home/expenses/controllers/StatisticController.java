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

import com.home.expenses.models.Statistic;
import com.home.expenses.models.Store;
import com.home.expenses.models.Time;
import com.home.expenses.repositories.StatisticRepo;

@Service
public class StatisticController {

	@Autowired
	private StatisticRepo statisticRepo;

	public Statistic insert ( String name, 
							Time time,
							String spectrum, 
							Double value) {
		
		UUID id = UUID.randomUUID();
		
		Statistic s = null;
		do {
			id = UUID.randomUUID();
			statisticRepo.findById(id);
		}	while (s != null);
		
		s = new Statistic(id);
		s.setName(name);
		s.setTime(time);
		s.setSpectrum(spectrum);
		s.setValue(value);
		
		statisticRepo.save(s);
		
		return s;
	}

	public List<Statistic> listAll () {
		return StreamSupport.stream(statisticRepo.findAll().spliterator(), false).collect(Collectors.toList());
	}
	
	public List<Statistic> listByName (String name) {
		return StreamSupport.stream(statisticRepo.findByName(name).spliterator(), false).collect(Collectors.toList());
	}
	
	public Statistic findByName (String name) {
		return statisticRepo.findFirstByName(name);
	}

	public Statistic update(Statistic statistic) {
		return statisticRepo.save(statistic);
	}
	
	public Statistic remove (UUID id) {
		return statisticRepo.removeById(id);
	}
	
}
