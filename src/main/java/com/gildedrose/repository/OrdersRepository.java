package com.gildedrose.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

import com.gildedrose.model.Order;

@Repository
public class OrdersRepository {

	// This list mimics in-memory repository
	private List<Order> orders;

	@PostConstruct
	public void init() {
		orders = Collections.synchronizedList(new ArrayList<Order>());
	}
	
	public void createOrder(Order order) {
		orders.add(order);
	}

}
