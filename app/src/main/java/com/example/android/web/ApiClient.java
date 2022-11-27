package com.example.android.web;

import com.example.model.Concept;
import com.example.model.Definition;
import feign.Param;
import feign.RequestLine;

public interface ApiClient {

	static ApiClient getInstance() {
		return ApiClient.builder()
				.build();
	}

	static ApiClientBuilder builder() {
		return new ApiClientBuilder();
	}

	@RequestLine("GET concept/definition/{id}")
	Definition getDefinitionById(@Param(value = "id") int id);

	@RequestLine("GET concept/{id}")
	Concept getConceptById(@Param(value = "id") int id);

	@RequestLine("GET concept/{sectionId}")
	Concept saveConcept(@Param(value = "sectionId") int sectionId, Concept concept);
}
