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
import org.springframework.data.util.Pair;
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
		int day = 2;
		
		try {
		
			SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
			Date endDate = dateformat.parse(dateformat.format(new Date()));
	        //endDate = dateformat.parse(end);
			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.add(Calendar.DAY_OF_MONTH, -7);
			Date startDate = dateformat.parse(dateformat.format(cal.getTime()));
			
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
			
			if (statistic.getValue() != 0)
				statistic = statisticRepo.save(statistic);
			log.info("{} {}", statistic, it);
			
		} 	catch (ParseException e) {
			log.error(e.getMessage());
		}
        
        
    }
	
	@Scheduled(fixedRate = 86400000)
    public void monthTotalReport() {
		log.info("Month Total Report attepmt: {}", new Date());
		String start = "2019-02-14";
        String end = "2019-01-22";
		int day = 1;
		
		try {
		
			SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
			Date endDate = dateformat.parse(dateformat.format(new Date()));
	        //endDate = dateformat.parse(end);
			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.add(Calendar.MONTH, -1);
			Date startDate = dateformat.parse(dateformat.format(cal.getTime()));
			//startDate = dateformat.parse(start);
			
			if (cal.get(Calendar.DAY_OF_MONTH) != day) return;
			
			List<Product> plist = new LinkedList<Product>();
			int i = 0;
			Page<Product> prods = productRepo.findByDateBeforeAndDateAfter( endDate.getTime(), startDate.getTime(), PageRequest.of(i, 10));
			while (prods.getNumberOfElements() > 0) {
				prods.forEach(p -> plist.add(p));
				prods = productRepo.findByDateBeforeAndDateAfter( endDate.getTime(), startDate.getTime(), PageRequest.of(++i, 10));
			}
			plist.forEach(p -> log.info("{} {}", p.getDate(), p.getName()));
			
			Statistic statistic = new Statistic();
			statistic.setName("Y" + cal.get(Calendar.YEAR) + "M" + cal.get(Calendar.MONTH));
			statistic.setSpectrum("TOTAL");
			statistic.setDate(endDate);
			statistic.setTime(Time.MONTH);
			
			Statistic tmp = statisticRepo.findFirstByName(statistic.getName());
			Iterable<Statistic> it = statisticRepo.findAll();
			if (tmp != null) return;
			
			Double total = plist.stream().mapToDouble(Product::getPaid).sum();
			List<UUID>ids = plist.stream().map(Product::getId).collect(Collectors.toList());	
			statistic.setImplied(ids);
			statistic.setValue(total);
			
			if (statistic.getValue() != 0)
				statistic = statisticRepo.save(statistic);
			log.info("{} {}", statistic, it);
			
		} 	catch (ParseException e) {
			log.error(e.getMessage());
		}
        
        
    }
	
    public Statistic manualTotalReport(String end, Time time) {
		log.info("Week Total Report attepmt: {}", new Date());
		
		try {
		
			SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
			Date endDate = dateformat.parse(end);
			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			Pair<Integer, Integer> substraction = getSubstractionValue (time);
			cal.add(substraction.getFirst(), substraction.getSecond());
			Date startDate = dateformat.parse(dateformat.format(cal.getTime()));
			//startDate = dateformat.parse(start);
			
			List<Product> plist = new LinkedList<Product>();
			int i = 0;
			Page<Product> prods = productRepo.findByDateBeforeAndDateAfter( endDate.getTime(), startDate.getTime(), PageRequest.of(i, 10));
			while (prods.getNumberOfElements() > 0) {
				prods.forEach(p -> plist.add(p));
				prods = productRepo.findByDateBeforeAndDateAfter( endDate.getTime(), startDate.getTime(), PageRequest.of(++i, 10));
			}
			plist.forEach(p -> log.info("{} {}", p.getDate(), p.getName()));
			
			Statistic statistic = new Statistic();
			statistic.setName("Y" + cal.get(Calendar.YEAR) + getNamePart(time, cal));
			statistic.setSpectrum("TOTAL");
			statistic.setDate(endDate);
			statistic.setTime(time);
			
			Statistic tmp = statisticRepo.findFirstByName(statistic.getName());
			Iterable<Statistic> it = statisticRepo.findAll();
			if (tmp != null) return tmp;
			
			Double total = plist.stream().mapToDouble(Product::getPaid).sum();
			List<UUID>ids = plist.stream().map(Product::getId).collect(Collectors.toList());	
			statistic.setImplied(ids);
			statistic.setValue(total);
			
			if (statistic.getValue() != 0)
				statistic = statisticRepo.save(statistic);
			log.info("{} {}", statistic, it);
			
			return statistic;
			
		} 	catch (ParseException e) {
			log.error(e.getMessage());
		}
        
        return new Statistic();
    }
    
    public Statistic manualTotalReportForced (String end, Time time) {
		log.info("Week Total Report attepmt: {}", new Date());
		
		try {
		
			SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
			Date endDate = dateformat.parse(end);
			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			Pair<Integer, Integer> substraction = getSubstractionValue (time);
			cal.add(substraction.getFirst(), substraction.getSecond());
			Date startDate = dateformat.parse(dateformat.format(cal.getTime()));
			//startDate = dateformat.parse(start);
			
			List<Product> plist = new LinkedList<Product>();
			int i = 0;
			Page<Product> prods = productRepo.findByDateBeforeAndDateAfter( endDate.getTime(), startDate.getTime(), PageRequest.of(i, 10));
			while (prods.getNumberOfElements() > 0) {
				prods.forEach(p -> plist.add(p));
				prods = productRepo.findByDateBeforeAndDateAfter( endDate.getTime(), startDate.getTime(), PageRequest.of(++i, 10));
			}
			plist.forEach(p -> log.info("{} {}", p.getDate(), p.getName()));
			
			Statistic statistic = new Statistic();
			statistic.setName("Y" + cal.get(Calendar.YEAR) + getNamePart(time, cal));
			statistic.setSpectrum("TOTAL");
			statistic.setDate(endDate);
			statistic.setTime(time);
			
			Statistic tmp = statisticRepo.findFirstByName(statistic.getName());
			Iterable<Statistic> it = statisticRepo.findAll();
			if (tmp != null) {
				statisticRepo.removeById(tmp.getId());
			}
			
			Double total = plist.stream().mapToDouble(Product::getPaid).sum();
			List<UUID>ids = plist.stream().map(Product::getId).collect(Collectors.toList());	
			statistic.setImplied(ids);
			statistic.setValue(total);
			
			if (statistic.getValue() != 0)
				statistic = statisticRepo.save(statistic);
			log.info("{} {}", statistic, it);
			
			return statistic;
			
		} 	catch (ParseException e) {
			log.error(e.getMessage());
		}
        
        return new Statistic();
    }

	private Pair<Integer, Integer> getSubstractionValue(Time time) {
		if (time == Time.MONTH) {
			return Pair.of(Calendar.MONTH, -1);
		}
		if (time == Time.WEEK) {
			return Pair.of(Calendar.DAY_OF_MONTH, -7);
		}
		if (time == Time.YEAR) {
			return Pair.of(Calendar.YEAR, -1);
		}
		return Pair.of(Calendar.DAY_OF_YEAR, 0);
	}
	
	private String getNamePart (Time time, Calendar cal) {
		if (time == Time.WEEK) {
			return "" + time.name().charAt(0) + cal.get(Calendar.WEEK_OF_YEAR);
		}
		if (time == Time.DAY) {
			return "" + time.name().charAt(0) + cal.get(Calendar.DAY_OF_YEAR);
		}
		if (time == Time.MONTH) {
			return "" + time.name().charAt(0) + cal.get(Calendar.MONTH);
		}
		return "";
	}
	
}
