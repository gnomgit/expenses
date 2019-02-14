package com.home.expenses.models;

import java.util.List;
import java.util.UUID;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.GeoPointField;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Store {

	@Id
	private UUID id;
	private GeoPoint location;
	@GeoPointField
	private String gp;
	private String name;
	@ManyToOne
	@JoinColumn(name="store", nullable=true)
	List<Product> products;
	
	
	public Store () {
		this.id = UUID.randomUUID();
	}
	
	public Store (UUID id) {
		this.id = id;
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
