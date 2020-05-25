package com.gildedrose.service;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class SurgeMonitorServiceTest {
	@Autowired
	private SurgeMonitorService surgeMonitorService;

	@Value("${surge.monitor.views.threshold}")
	private long surgeViewThreshold;

	@Test
	public void isSurgeShouldNotBeFalseWhenViewsLessThanThreshold() throws Exception {
		surgeMonitorService.recordView();
		Assert.assertFalse(surgeMonitorService.isSurge());
	}

	@Test
	public void isSurgeShouldBeTrueWhenViewsAboveThreshold() throws Exception {
		for(int count = 1; count <= surgeViewThreshold + 1; count ++) {
			surgeMonitorService.recordView();
		}
		Assert.assertTrue(surgeMonitorService.isSurge());
	}
}
