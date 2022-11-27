package com.example.android.web;

import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import lombok.Builder;

@Builder
public class ApiClientBuilder {

	private static final String TARGET_URL = "http://localhost:8075/api";

	public ApiClient build() {
		return Feign.builder()
				.client(new OkHttpClient())
				.encoder(new GsonEncoder())
				.decoder(new GsonDecoder())
				.target(ApiClient.class, TARGET_URL);
	}
}
