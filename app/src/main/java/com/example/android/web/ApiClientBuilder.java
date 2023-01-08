package com.example.android.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.concurrent.TimeUnit;
import lombok.NoArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@NoArgsConstructor
public class ApiClientBuilder {

	private String target = "http://10.0.2.2:8075/api/";

	public ApiClientBuilder target(String target) {
		this.target = target;
		return this;
	}

	public ApiClient build() {

		Retrofit retrofit = new Retrofit.Builder()
				.addConverterFactory(ScalarsConverterFactory.create())
				.addConverterFactory(ApiClientBuilder.gsonConverterFactory())
				.baseUrl(target)
				.client(ApiClientBuilder.client())
				.build();

		return retrofit.create(ApiClient.class);
	}

	private static OkHttpClient client() {
		HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
		loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

		// TODO: Increase timeout to 30s
		return new OkHttpClient.Builder()
				.addInterceptor(loggingInterceptor)
				.writeTimeout(1, TimeUnit.SECONDS)
				.readTimeout(1, TimeUnit.SECONDS)
				.connectTimeout(1, TimeUnit.SECONDS)
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
}
