package com.home.expenses.models;

import java.util.Date;
import java.util.UUID;

import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.search.DocValueFormat.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Document(indexName="expenses_index")
public class Product {

	@Id
	private UUID id;
	private GeoPoint location;
	@GeoPointField
	private String gp;
	private String name;
	private Frequency frequency;
	private Price price;
	private double paid;
	private String ticketName;
	private String storeName;
	@DateTimeFormat
	private Date date;
	private String comments;
	private Meassure meassure;
	
	
	public Product () {
		this.id = UUID.randomUUID();
		this.gp = new GeoPoint(0, 0).geohash();
		this.location = new GeoPoint(this.gp);
		this.frequency = new Frequency(0.0, Time.WEEK, 1);
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
