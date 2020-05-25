package com.gildedrose.service;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gildedrose.model.Item;
import com.gildedrose.repository.ItemsRepository;

@Service
public class ItemsService {
	private final ItemsRepository itemsRepo;
	private final SurgeMonitorService surgeMonitorService;

	public ItemsService(ItemsRepository itemsRepo, SurgeMonitorService surgeMonitorService) {
		this.itemsRepo = itemsRepo;
		this.surgeMonitorService = surgeMonitorService;
	}

	public Collection<Item> getItems() {
		surgeMonitorService.recordView();
		Collection<Item> items = itemsRepo.getItems();
		if (surgeMonitorService.isSurge()) {
			// Bump up the prices
			items = items.stream().map(item -> {
				try {
					Item newItem = item.clone();
					newItem.setPrice(surgeMonitorService.getSurgeAdjustedPrice(newItem.getPrice()));
					return newItem;
				} catch (CloneNotSupportedException e) {
					return null;
				}
			}).collect(Collectors.toList());
		}
		return items;
	}
}
