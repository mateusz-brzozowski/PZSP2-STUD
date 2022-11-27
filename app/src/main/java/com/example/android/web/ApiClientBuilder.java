package com.example.android.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.concurrent.TimeUnit;
import lombok.Builder;
import lombok.experimental.UtilityClass;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@Builder
@UtilityClass
public class ApiClientBuilder {

	private static final String TARGET_URL = "http://10.0.2.2:8075/api/";
	private static ApiClient apiClient;

	public static ApiClient getInstance() {
		if (apiClient == null) {
			apiClient = build();
		}
		return apiClient;
	}

	private static OkHttpClient client() {
		HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
		loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

		return new OkHttpClient.Builder()
				.addInterceptor(loggingInterceptor)
				.writeTimeout(30, TimeUnit.SECONDS)
				.readTimeout(30, TimeUnit.SECONDS)
				.connectTimeout(30, TimeUnit.SECONDS)
				.build();
	}

	private static Converter.Factory gsonConverterFactory() {
		Gson gson = new GsonBuilder()
				.setDateFormat("yyyy-MM-dd")
				.serializeNulls()
				.setLenient()
				.create();

		return GsonConverterFactory.create(gson);
	}

	private static ApiClient build() {

		Retrofit retrofit = new Retrofit.Builder()
				.addConverterFactory(ScalarsConverterFactory.create())
				.addConverterFactory(ApiClientBuilder.gsonConverterFactory())
				.baseUrl(TARGET_URL)
				.client(ApiClientBuilder.client())
				.build();

		return retrofit.create(ApiClient.class);
	}
}
