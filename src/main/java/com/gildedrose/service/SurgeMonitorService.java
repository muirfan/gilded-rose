package com.gildedrose.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SurgeMonitorService {
	private Map<Long, Long> slotCounterMap;
	private static final long SECONDS_IN_SLOT = 60; // Currently slot size it set to 1 minute
	private static final long SECONDS_IN_HOUR = 60 * 60;

	@Value("${surge.monitor.views.threshold}")
	private long surgeViewThreshold;

	@Value("${surge.monitor.interval}")
	private long monitorInterval;

	@Value("${surge.price.percentage}")
	private long surgePricePercentage;

	@PostConstruct
	public void init() {
		slotCounterMap = new ConcurrentHashMap<Long, Long>();
	}
	
	public void recordView() {
		long timestamp = Instant.now().getEpochSecond();
		// Rounding to slot size
		long slotTimestamp = timestamp - (timestamp % SECONDS_IN_SLOT);
		// Increment slot request counter
		slotCounterMap.put(slotTimestamp, slotCounterMap.getOrDefault(slotTimestamp, 0L) + 1);
	}

	public boolean isSurge() {
		// Clean-up older (than one hour) slots
		long lastHourTimestamp = Instant.now().getEpochSecond() - SECONDS_IN_HOUR;
		slotCounterMap.entrySet().removeIf(entry -> entry.getKey() < lastHourTimestamp);

		long hourlyRequestRate = slotCounterMap.values().stream().reduce(0L, Long::sum);

		return hourlyRequestRate > surgeViewThreshold;
	}

	public double getSurgeAdjustedPrice(final double price) {
		double newPrice = price * (1 + (double) surgePricePercentage / 100);
	    BigDecimal bigDecimal = BigDecimal.valueOf(newPrice);
	    bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
	    return bigDecimal.doubleValue();
	}
}
