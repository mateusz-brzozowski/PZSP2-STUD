package com.example.android.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository.
 */
public class Authorization {

	private final String token;

	public Authorization(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}
}
