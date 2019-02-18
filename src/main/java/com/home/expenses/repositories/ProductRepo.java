package com.home.expenses.repositories;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.home.expenses.models.Product;

@Repository
public interface ProductRepo extends ElasticsearchRepository<Product, UUID> {

	@Transactional
	Product removeById (UUID id);
	
	//List<Product> findAllByDateLessThanAndDateGreaterThanEqual(Date endDate, Date startDate);
	
	List<Product> findByDateBeforeAndDateAfter(Long endDate, Long startDate);
	
	List<Product> findByDateBefore(Long date);
	
}
