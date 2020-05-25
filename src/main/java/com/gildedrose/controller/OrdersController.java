package com.gildedrose.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.gildedrose.model.Order;
import com.gildedrose.service.AuthenticationService;
import com.gildedrose.service.OrdersService;

@RestController
public class OrdersController {

	private final AuthenticationService authenticationService;
	private final OrdersService ordersService;

	public OrdersController(AuthenticationService authenticationService, OrdersService ordersService) {
		this.authenticationService = authenticationService;
		this.ordersService = ordersService;
	}

	@PostMapping("/orders")
	public ResponseEntity<Order> createOrder(@RequestBody Order order, @RequestHeader("apikey") String apiKey) {
		authenticationService.validateApiKey(apiKey);
		// Future enhancement - provide location uri of new order, this requires "GET /orders/{id}" be created 
		return ResponseEntity.created(null).body(ordersService.createOrder(order));
	}
}
