package com.example.android.ui.section;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.android.web.ApiClient;
import com.example.model.Definition;
import java.util.List;
import java.util.Optional;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConceptsViewModel extends ViewModel {

	private final MutableLiveData<List<Definition>> conceptsLiveData = new MutableLiveData<>();

	public LiveData<List<Definition>> getConceptsLiveData() {
		return conceptsLiveData;
	}

	public void populateConcepts(int sectionId) {
		var apiClient = ApiClient.getInstance();
		var call = apiClient.getConceptsBySectionId(sectionId);
		call.enqueue(new Callback<>() {
			@Override
			public void onResponse(@NonNull Call<List<Definition>> call, @NonNull Response<List<Definition>> response) {
				var result = Optional.ofNullable(response.body()).orElseGet(ConceptsViewModel::getFallbackConcepts);
				conceptsLiveData.setValue(result);
			}

			@Override
			public void onFailure(@NonNull Call<List<Definition>> call, @NonNull Throwable t) {
				conceptsLiveData.setValue(getFallbackConcepts());
			}
		});
	}

	private static List<Definition> getFallbackConcepts() {
		var concept1 = Definition.builder()
				.concept("Sieć")
				.build();
		var concept2 = Definition.builder()
				.concept("Intersieć")
				.build();
		var concept3 = Definition.builder()
				.concept("Internet")
				.build();
		var concept4 = Definition.builder()
				.concept("Stos ISO/OSI")
				.build();
		return List.of(concept1, concept2, concept3, concept4);
	}
}
