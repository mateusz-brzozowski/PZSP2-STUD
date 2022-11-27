package com.example.android.web;

import com.example.model.Concept;
import com.example.model.Definition;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiClient {

	static ApiClient getInstance() {
		return ApiClientBuilder.getInstance();
	}

	@GET("ping/{name}")
	Call<String> testConnection(@Path(value = "name") String name);

	@GET("concept/definition/{id}")
	Call<Definition> getDefinitionById(@Path(value = "id") int id);

	@GET("concept/{id}")
	Call<Concept> getConceptById(@Path(value = "id") int id);

	@GET("concept/{sectionId}")
	Call<Concept> saveConcept(@Path(value = "sectionId") int sectionId, Concept concept);
}
