package com.mydeliveries.nandos.model;

public class ExtraItem {

private static String item1,item2;



	public ExtraItem() {
	}



	public ExtraItem(String item1, String item2) {
		
		ExtraItem.item1 = item1;
		ExtraItem.item2 = item2;
	
		
	}

	public static String getItem1() {
		return item1;
	}

	public static void setItem1(String item1) {
		ExtraItem.item1 = item1;
	}

	public static String getItem2() {
		return item2;
	}

	public static void setItem2(String item2) {
		ExtraItem.item2 = item2;
	}




}
