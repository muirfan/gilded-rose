package com.gildedrose.service;

import java.util.Collection;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gildedrose.model.Item;
import com.gildedrose.repository.ItemsRepository;
import com.gildedrose.utils.TestUtils;

@SpringBootTest
public class ItemsServiceTest {

	@MockBean
	private ItemsRepository itemsRepo;
	
	@MockBean
	private SurgeMonitorService surgeMonitorService;

	private ItemsService itemsService;

	private Collection<Item> mockItems;

	@BeforeEach
	public void setUp() throws JsonProcessingException {
		itemsService = new ItemsService(itemsRepo, surgeMonitorService);
		mockItems = TestUtils.createMockItems();
	}

	@Test
	public void getItemsShouldReturnAllItems() throws Exception {
		Mockito.doReturn(mockItems).when(itemsRepo).getItems();
		Mockito.doAnswer(new Answer<Double>() {
			@Override
		    public Double answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				return (Double) args[0];
		    }
		}).when(surgeMonitorService).getSurgeAdjustedPrice(Mockito.anyDouble());

		Collection<Item> retItems = itemsService.getItems();
		
		Assert.assertEquals(mockItems.size(), retItems.size());
	}
}
