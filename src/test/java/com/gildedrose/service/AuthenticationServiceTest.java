package com.gildedrose.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.gildedrose.exception.InvalidApiKeyException;

@SpringBootTest
public class AuthenticationServiceTest {

	@Autowired
	private AuthenticationService authenticationService;

	@Test
	public void validKeyShouldNotThrowException() throws Exception {
		authenticationService.validateApiKey(AuthenticationService.VALID_API_KEY);
	}

	@Test
	public void invalidKeyShouldThrowException() throws Exception {
		Assertions.assertThrows(InvalidApiKeyException.class, () -> authenticationService.validateApiKey("InvalidApiKey"));
	}
}
