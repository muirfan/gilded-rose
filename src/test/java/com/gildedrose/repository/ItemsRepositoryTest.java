package com.gildedrose.repository;

import java.util.Collection;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.gildedrose.model.Item;

@SpringBootTest
public class ItemsRepositoryTest {

	@Autowired
	private ItemsRepository itemsRepo;

	@Test
	public void getItemsShouldReturnAllItems() throws Exception {
		Collection<Item> retItems = itemsRepo.getItems();
		Assert.assertEquals(5, retItems.size());
	}
}
