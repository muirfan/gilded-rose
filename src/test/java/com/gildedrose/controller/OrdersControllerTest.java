package com.gildedrose.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gildedrose.exception.InvalidApiKeyException;
import com.gildedrose.exception.InvalidItemException;
import com.gildedrose.exception.ItemOutOfStockException;
import com.gildedrose.model.Order;
import com.gildedrose.service.AuthenticationService;
import com.gildedrose.service.OrdersService;

@SpringBootTest
@AutoConfigureMockMvc
public class OrdersControllerTest {

	private static final String ORDERS_ENDPOINT = "/orders";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OrdersService ordersService;

	@MockBean
	private AuthenticationService authenticationService;

	private String mockOrderJson;

	@BeforeEach
	public void setUp() throws JsonProcessingException {
		Order mockOrder = new Order();
		mockOrder.setItemName("Item1");
		mockOrder.setQuantity(3);

		mockOrderJson = new ObjectMapper().writeValueAsString(mockOrder);
	}
	
	@Test
	public void createOrderShouldSaveNewOrder() throws Exception {
		mockMvc.perform(
					post(ORDERS_ENDPOINT)
					.contentType(MediaType.APPLICATION_JSON)
					.content(mockOrderJson)
					.header("apikey", AuthenticationService.VALID_API_KEY)
				)
				.andExpect(status().isCreated())
				;
	}

	@Test
	public void createOrderWithoutApiKeyShouldReturnError() throws Exception {
		Mockito.doThrow(new InvalidApiKeyException()).when(authenticationService).validateApiKey(Mockito.isNull());
		
		mockMvc.perform(
					post(ORDERS_ENDPOINT)
					.contentType(MediaType.APPLICATION_JSON)
					.content(mockOrderJson)
				)
				.andExpect(status().isBadRequest())
				;
	}

	@Test
	public void createOrderWithInvalidApiKeyShouldReturnError() throws Exception {
		Mockito.doThrow(new InvalidApiKeyException()).when(authenticationService).validateApiKey(Mockito.anyString());
		
		mockMvc.perform(
					post(ORDERS_ENDPOINT)
					.contentType(MediaType.APPLICATION_JSON)
					.content(mockOrderJson)
					.header("apikey", "invalidkey")
				)
				.andExpect(status().isUnauthorized())
				;
	}

	@Test
	public void createOrderWithLargeQuantityShouldReturnError() throws Exception {
		Mockito.when(ordersService.createOrder(Mockito.any())).thenThrow(new ItemOutOfStockException());

		mockMvc.perform(
					post(ORDERS_ENDPOINT)
					.contentType(MediaType.APPLICATION_JSON)
					.content(mockOrderJson)
					.header("apikey", AuthenticationService.VALID_API_KEY)
				)
				.andExpect(status().isNotFound())
				;
	}

	@Test
	public void createOrderWithInvalidItemShouldReturnError() throws Exception {
		Mockito.when(ordersService.createOrder(Mockito.any())).thenThrow(new InvalidItemException());

		mockMvc.perform(
					post(ORDERS_ENDPOINT)
					.contentType(MediaType.APPLICATION_JSON)
					.content(mockOrderJson)
					.header("apikey", AuthenticationService.VALID_API_KEY)
				)
				.andExpect(status().isNotFound())
				;
	}
}
