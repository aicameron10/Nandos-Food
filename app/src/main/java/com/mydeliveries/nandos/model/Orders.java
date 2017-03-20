package com.mydeliveries.nandos.model;


public class Orders {
	private String id;
	private String order;
	private String status;
	private String name;
	private String surname;
	private String number;
	private String email;
	private String address;
	private String collection;
	private String delivery;
	private String preorder;
	private String orderdate;
	private String capture;
	private String reference;
	private String storedelivery;

	private String storecollect;
	private String storehours;
	private String nowactive;



	private String storename;
	private String storeapi;





	public Orders() {
	}

	public Orders(String id,String order,String status,String name,String surname,String number,String email,String address,String collection,String delivery,String preorder,String orderdate,String capture,String reference,String storedelivery,String storecollect,String storehours,String nowactive,String storename,String storeapi) {
		this.id = id;
		this.order = order;
		this.status = status;
		this.name = name;
		this.surname = surname;
		this.number = number;
		this.email = email;
		this.address = address;
		this.collection = collection;
		this.delivery = delivery;
		this.preorder = preorder;
		this.orderdate = orderdate;
		this.capture = capture;
		this.reference = reference;
		this.storedelivery = storedelivery;
		this.storecollect = storecollect;
		this.storehours = storehours;
		this.nowactive = nowactive;
		this.storename = storename;
		this.storeapi = storeapi;





	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public String getDelivery() {
		return delivery;
	}

	public void setDelivery(String delivery) {
		this.delivery = delivery;
	}

	public String getPreorder() {
		return preorder;
	}

	public void setPreorder(String preorder) {
		this.preorder = preorder;
	}

	public String getOrderdate() {
		return orderdate;
	}

	public void setOrderdate(String orderdate) {
		this.orderdate = orderdate;
	}

	public String getCapture() {
		return capture;
	}

	public void setCapture(String capture) {
		this.capture = capture;
	}

	public String getStorecollect() {
		return storecollect;
	}

	public void setStorecollect(String storecollect) {
		this.storecollect = storecollect;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getStoredelivery() {
		return storedelivery;
	}

	public void setStoredelivery(String storedelivery) {
		this.storedelivery = storedelivery;
	}

	public String getStorehours() {
		return storehours;
	}

	public void setStorehours(String storehours) {
		this.storehours = storehours;
	}

	public String getNowactive() {
		return nowactive;
	}

	public void setNowactive(String nowactive) {
		this.nowactive = nowactive;
	}

	public String getStoreapi() {
		return storeapi;
	}

	public void setStoreapi(String storeapi) {
		this.storeapi = storeapi;
	}

	public String getStorename() {
		return storename;
	}

	public void setStorename(String storename) {
		this.storename = storename;
	}

}
