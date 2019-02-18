package com.home.expenses.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.home.expenses.models.Product;
import com.home.expenses.models.Statistic;
import com.home.expenses.models.Store;
import com.home.expenses.models.Time;
import com.home.expenses.repositories.ProductRepo;
import com.home.expenses.repositories.StatisticRepo;
import com.home.expenses.repositories.StoreRepo;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class StatisticController {

	@Autowired
	private StatisticRepo statisticRepo;
	
	@Autowired
	private StoreRepo storeRepo;
	
	@Autowired
	private ProductRepo productRepo;

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
	
	@Scheduled(fixedRate = 10000)
    public void reportCurrentTime() {
        log.info("The time is now {}", new Date());
        
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        String start = "2019-02-14T00:00-00:00";
        String end = "2019-02-15T00:00-00:00";
        
        try {
			Date startDate = dateformat.parse(start);
			Date endDate = dateformat.parse(end);
			
			//List<Product> prods = productRepo.findAllByDateLessThanAndDateGreaterThanEqual( OffsetDateTime.parse(end), OffsetDateTime.parse(start));
			List<Product> prods = productRepo.findByDateBeforeAndDateAfter( endDate.getTime(), startDate.getTime());
			//List<Product> prods = productRepo.findByDateBefore(endDate.getTime());
			
			prods.forEach(p -> log.info("{} {}", p.getDate(), p.getName()));
			
		} 	catch (ParseException e) {
			log.error(e.getMessage());
		}
        
        
    }
	
}
