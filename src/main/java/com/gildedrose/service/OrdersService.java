package com.gildedrose.service;

import org.springframework.stereotype.Service;

import com.gildedrose.exception.InvalidItemException;
import com.gildedrose.exception.ItemOutOfStockException;
import com.gildedrose.model.Item;
import com.gildedrose.model.Order;
import com.gildedrose.repository.ItemsRepository;
import com.gildedrose.repository.OrdersRepository;

@Service
public class OrdersService {
	private final ItemsRepository itemsRepo;
	private final OrdersRepository ordersRepo;
	private final SurgeMonitorService surgeMonitorService;

	public OrdersService(final ItemsRepository itemsRepo, final OrdersRepository ordersRepo, final SurgeMonitorService surgeMonitorService) {
		this.itemsRepo = itemsRepo;
		this.ordersRepo = ordersRepo;
		this.surgeMonitorService = surgeMonitorService;
	}
	
	public Order createOrder(Order order) {
		Item item = itemsRepo.getItem(order.getItemName());
		if (null == item) {
			throw new InvalidItemException();
		}

		if (item.getQuantity() < order.getQuantity()) {
			throw new ItemOutOfStockException();
		}

		double basePrice = item.getPrice();
		order.setPrice(surgeMonitorService.isSurge() ? surgeMonitorService.getSurgeAdjustedPrice(basePrice) : basePrice);

		synchronized(OrdersService.class) {
			// Check again inside sync block
			if (item.getQuantity() < order.getQuantity()) {
				throw new ItemOutOfStockException();
			}

			ordersRepo.createOrder(order);
			item.setQuantity(item.getQuantity() - order.getQuantity());
		}

		return order;
	}
}
