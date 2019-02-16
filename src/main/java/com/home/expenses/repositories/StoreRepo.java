package com.home.expenses.repositories;

import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.home.expenses.models.Store;

@Repository
public interface StoreRepo extends ElasticsearchRepository<Store, UUID> {

	public Iterable<Store> findByName (String name);
	
	public Store findFirstByName (String name);
	
	//public Iterable<Store> findByGp (String gp);
	
	//public Store findFirstByGp (String gp);
	@Transactional
	public Store removeById (UUID id);
	
}
