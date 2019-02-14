package com.home.expenses.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Price {
	
	private double quantity;
	private Currency currency;
	private Meassure meassure;
	
}
