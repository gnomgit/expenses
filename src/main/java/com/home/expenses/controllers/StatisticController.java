package com.home.expenses.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.home.expenses.models.Product;
import com.home.expenses.models.Statistic;
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
		Page<Statistic> stats = statisticRepo.findByName(name, PageRequest.of(0, 1));
		log.info("{}", stats);
		return StreamSupport.stream(statisticRepo.findByName(name, PageRequest.of(0, 1)).spliterator(), false).collect(Collectors.toList());
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
	
	public Statistic find (UUID id) {
		return statisticRepo.findById(id).get();
	}
	
	@Scheduled(fixedRate = 86400000)
    public void weekTotalReport() {
		log.info("Week Total Report attepmt: {}", new Date());
		String start = "2019-02-14";
        String end = "2019-01-22";
		int day = 4;
		
		try {
		
			SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
			Date endDate = dateformat.parse(dateformat.format(new Date()));
	        //endDate = dateformat.parse(end);
			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.add(Calendar.DAY_OF_MONTH, -7);
			Date startDate = dateformat.parse(dateformat.format(cal.getTime()));
			//startDate = dateformat.parse(start);
			
			cal.setTime(endDate);
			
			if (cal.get(Calendar.DAY_OF_WEEK) != day) return;
			
			List<Product> plist = new LinkedList<Product>();
			int i = 0;
			Page<Product> prods = productRepo.findByDateBeforeAndDateAfter( endDate.getTime(), startDate.getTime(), PageRequest.of(i, 10));
			while (prods.getNumberOfElements() > 0) {
				prods.forEach(p -> plist.add(p));
				prods = productRepo.findByDateBeforeAndDateAfter( endDate.getTime(), startDate.getTime(), PageRequest.of(++i, 10));
			}
			plist.forEach(p -> log.info("{} {}", p.getDate(), p.getName()));
			
			Statistic statistic = new Statistic();
			statistic.setName("Y" + cal.get(Calendar.YEAR) + "W" + cal.get(Calendar.WEEK_OF_YEAR));
			statistic.setSpectrum("TOTAL");
			statistic.setDate(endDate);
			statistic.setTime(Time.WEEK);
			
			Statistic tmp = statisticRepo.findFirstByName(statistic.getName());
			Iterable<Statistic> it = statisticRepo.findAll();
			if (tmp != null) return;
			
			Double total = plist.stream().mapToDouble(Product::getPaid).sum();
			List<UUID>ids = plist.stream().map(Product::getId).collect(Collectors.toList());	
			statistic.setImplied(ids);
			statistic.setValue(total);
			
			statistic = statisticRepo.save(statistic);
			log.info("{} {}", statistic, it);
			
		} 	catch (ParseException e) {
			log.error(e.getMessage());
		}
        
        
    }
	
}
