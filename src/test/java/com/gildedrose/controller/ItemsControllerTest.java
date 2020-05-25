package com.gildedrose.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collection;
import java.util.Iterator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gildedrose.model.Item;
import com.gildedrose.service.ItemsService;
import com.gildedrose.utils.TestUtils;

@SpringBootTest
@AutoConfigureMockMvc
public class ItemsControllerTest {

	private static final String ITEMS_ENDPOINT = "/items";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ItemsService itemsService;

	private Collection<Item> mockItems;

	@BeforeEach
	public void setUp() throws JsonProcessingException {
		mockItems = TestUtils.createMockItems();
	}

	@Test
	public void getItemsShouldReturnAllItems() throws Exception {
		when(itemsService.getItems()).thenReturn(mockItems);
		Iterator<Item> mockItemIterator = mockItems.iterator();

		int itemIndex = 0;
		mockMvc.perform(get(ITEMS_ENDPOINT))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", hasSize(mockItems.size())))
				.andExpect(jsonPath("$[" + itemIndex++ + "].name", is(mockItemIterator.next().getName())))
				.andExpect(jsonPath("$[" + itemIndex++ + "].name", is(mockItemIterator.next().getName())))
				.andExpect(jsonPath("$[" + itemIndex++ + "].name", is(mockItemIterator.next().getName())))
				.andExpect(jsonPath("$[" + itemIndex++ + "].name", is(mockItemIterator.next().getName())))
				.andExpect(jsonPath("$[" + itemIndex++ + "].name", is(mockItemIterator.next().getName())))
				;
	}
}
