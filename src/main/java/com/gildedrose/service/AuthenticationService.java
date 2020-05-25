package com.gildedrose.service;

import org.springframework.stereotype.Service;

import com.gildedrose.exception.InvalidApiKeyException;

@Service
public class AuthenticationService {
	public static final String VALID_API_KEY = "XYZ";

	public void validateApiKey(String apiKey) {
		if (!VALID_API_KEY.equals(apiKey)) {
			throw new InvalidApiKeyException();
		}
	}
}
