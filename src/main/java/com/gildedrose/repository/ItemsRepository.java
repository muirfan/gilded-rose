package com.gildedrose.repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

import com.gildedrose.model.Item;

@Repository
public class ItemsRepository {

	// This list mimics in-memory repository
	private Map<String, Item> itemsMap;

	@PostConstruct
	public void init() {
		// Initialize with a static sample data
		itemsMap = new HashMap<String, Item>();
		for (int count = 1; count <= 5; count ++) {
			Item item = new Item();
			item.setName("Item" + count);
			item.setDescription("Item " + count + " description");
			item.setPrice(10*count);
			item.setQuantity(50);
			itemsMap.put(item.getName(), item);
		}
	}

	public Collection<Item> getItems() {
		return itemsMap.values();
	}

	public Item getItem(String name) {
		return itemsMap.get(name);
	}
}
