package com.home.expenses.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Frequency {

	private Double quantity;
	private Time time;
	private Integer timeValue;
	
}
