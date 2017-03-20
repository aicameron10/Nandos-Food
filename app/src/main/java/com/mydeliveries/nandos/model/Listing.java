package com.mydeliveries.nandos.model;


public class Listing {
	private String id;
	private String name;
	private String address;
	private String number;
	private String lat;



	private String lon;
	private String email;
	private String api_key;
	private String hours;
	private String lastactive;

	private Integer delivery, nowactive, collect, loadshedding, breakfast, coffee, drivethru, fullservice, halaal,kosher,licensed;



	private Double distance;


	private Integer count;


	public Listing() {
	}

	public Listing(String id,String name,String address,String number,String lat,String lon,String email,String api_key,String hours,String lastactive,Integer delivery,Integer nowactive,Integer collect,Integer loadshedding,Integer breakfast,Integer coffee,Integer drivethru,Integer fullservice,Integer halaal,Integer kosher,Integer licensed,Double distance) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.number = number;
		this.email = email;
		this.lat = lat;
		this.lon = lon;
		this.api_key = api_key;
		this.hours = hours;
		this.lastactive = lastactive;
		this.delivery = delivery;
		this.nowactive = nowactive;
		this.collect = collect;
		this.loadshedding = loadshedding;
		this.breakfast = breakfast;
		this.coffee = coffee;
		this.drivethru = drivethru;
		this.fullservice = fullservice;
		this.halaal = halaal;
		this.kosher = kosher;
		this.licensed = licensed;
		this.distance = distance;




		
	
		
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getApi_key() {
		return api_key;
	}

	public void setApi_key(String api_key) {
		this.api_key = api_key;
	}

	public String getHours() {
		return hours;
	}

	public void setHours(String hours) {
		this.hours = hours;
	}

	public String getLastactive() {
		return lastactive;
	}

	public void setLastactive(String lastactive) {
		this.lastactive = lastactive;
	}

	public Integer getDelivery() {
		return delivery;
	}

	public void setDelivery(Integer delivery) {
		this.delivery = delivery;
	}

	public Integer getNowactive() {
		return nowactive;
	}

	public void setNowactive(Integer nowactive) {
		this.nowactive = nowactive;
	}

	public Integer getCollect() {
		return collect;
	}

	public void setCollect(Integer collect) {
		this.collect = collect;
	}

	public Integer getLoadshedding() {
		return loadshedding;
	}

	public void setLoadshedding(Integer loadshedding) {
		this.loadshedding = loadshedding;
	}

	public Integer getBreakfast() {
		return breakfast;
	}

	public void setBreakfast(Integer breakfast) {
		this.breakfast = breakfast;
	}

	public Integer getCoffee() {
		return coffee;
	}

	public void setCoffee(Integer coffee) {
		this.coffee = coffee;
	}

	public Integer getDrivethru() {
		return drivethru;
	}

	public void setDrivethru(Integer drivethru) {
		this.drivethru = drivethru;
	}

	public Integer getFullservice() {
		return fullservice;
	}

	public void setFullservice(Integer fullservice) {
		this.fullservice = fullservice;
	}

	public Integer getHalaal() {
		return halaal;
	}

	public void setHalaal(Integer halaal) {
		this.halaal = halaal;
	}

	public Integer getKosher() {
		return kosher;
	}

	public void setKosher(Integer kosher) {
		this.kosher = kosher;
	}

	public Integer getLicensed() {
		return licensed;
	}

	public void setLicensed(Integer licensed) {
		this.licensed = licensed;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}


}
