package com.home.expenses.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.GeoPointField;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Document(indexName="stores_index", type = "store")
public class Store {

	@Id
	private UUID id;
	private GeoPoint location;
	@GeoPointField
	private String gp;
	private String name;
	List<UUID> products;
	
	
	public Store () {
		this.products = new ArrayList<UUID>();
		this.id = UUID.randomUUID();
	}
	
	public Store (UUID id) {
		this.products = new ArrayList<UUID>();
		this.id = id;
	}
	
	public void addProduct (Product product) {
		if (!this.products.stream().filter(p -> p.equals(product.getId())).findAny().isPresent())
			this.products.add(product.getId());
	}
	
	public void setGp (double lat, double lon) {
		this.gp = new GeoPoint(lat, lon).geohash();
		this.location = new GeoPoint(this.gp);
	}
	
	public void setLocation (double lat, double lon) {
		this.gp = new GeoPoint(lat, lon).geohash();
		this.location = new GeoPoint(this.gp);
	}
	
}
