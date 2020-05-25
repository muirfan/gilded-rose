package com.gildedrose.utils;

import java.util.ArrayList;
import java.util.Collection;

import com.gildedrose.model.Item;

public class TestUtils {
	public static Collection<Item> createMockItems() {
		Collection<Item> mockItems = new ArrayList<Item>();
		for (int count = 1; count <= 5; count ++) {
			Item item = new Item();
			item.setName("Item" + count);
			item.setDescription("Item " + count + " description");
			item.setPrice(10*count);
			item.setQuantity(5);
			mockItems.add(item);
		}
		return mockItems;
	}
}
