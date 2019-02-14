package com.home.expenses.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.home.expenses.models.Store;

@Repository
public interface StoreRepo extends ElasticsearchRepository<Store, UUID> {

	public List<Store> findByName (String name);
	
	public Store findFirstByName (String name);
	
	public List<Store> findByGp (String gp);
	
	public Store findFirstByGp (String gp);
	
}
