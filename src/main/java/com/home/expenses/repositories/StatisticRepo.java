package com.home.expenses.repositories;

import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.home.expenses.models.Statistic;
import com.home.expenses.models.Store;

@Repository
public interface StatisticRepo extends ElasticsearchRepository<Statistic, UUID> {

	public Iterable<Statistic> findByName (String name);
	
	public Statistic findFirstByName (String name);
	
	@Transactional
	public Statistic removeById (UUID id);
	
}
