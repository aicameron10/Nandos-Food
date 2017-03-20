package com.mydeliveries.nandos.model;

public class Price {

private static Double price;




	public Price() {
	}

	public Price(Double price) {
		
		Price.price = price;

	}


	public static Double getPrice() {
		return price;
	}

	public static void setPrice(Double price) {
		Price.price = price;
	}



}
