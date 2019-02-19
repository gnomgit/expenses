package com.home.expenses.repositories;

import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.home.expenses.models.Statistic;
import com.home.expenses.models.Store;

@Repository
public interface StatisticRepo extends ElasticsearchRepository<Statistic, UUID> {

	Page<Statistic> findByName (String name, Pageable pageable);
	
	Statistic findFirstByName (String name);
	
	boolean existsByName(String name);
	
	@Transactional
	Statistic removeById (UUID id);
	
}
