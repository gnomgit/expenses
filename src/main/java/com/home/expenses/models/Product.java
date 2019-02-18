package com.home.expenses.models;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@Document(indexName="expenses_index", type="product")
@ToString
public class Product {

	@Id
	private UUID id;
	private UUID store;
	private String name;
	private Frequency frequency;
	private Price price;
	private Double paid;
	private Double quantity;
	private String ticketName;
	private String storeName;
	@DateTimeFormat
	private Date date;
	private String comments;
	private Meassure meassure;
	
	
	public Product () {
		this.id = UUID.randomUUID();
		this.frequency = new Frequency(0.0, Time.WEEK, 1);
	}
	
	public Product (UUID id) {
		this.id = id;
		this.frequency = new Frequency(0.0, Time.WEEK, 1);
	}
	
}
