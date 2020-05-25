package com.gildedrose.controller;

import java.util.Collection;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gildedrose.model.Item;
import com.gildedrose.service.ItemsService;

@RestController
public class ItemsController {

	private final ItemsService itemService;

	public ItemsController(ItemsService itemService) {
		this.itemService = itemService;
	}

	@GetMapping(value="/items", produces= MediaType.APPLICATION_JSON_VALUE)
	public Collection<Item> getItems() {
		return itemService.getItems();
	}
}
