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
@Document(indexName="statistic_index", type = "statistic")
@ToString
public class Statistic {

	@Id
	UUID id;
	String name;
	Time time;
	String spectrum;
	Double value;
	@DateTimeFormat
	private Date date;
	
	
	public Statistic (UUID id) {
		this.id = id;
		this.name = "";
		this.time = Time.WEEK;
		this.spectrum = "";
		this.value = 0.0;
		this.date = new Date();
	}
	
}
