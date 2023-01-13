package com.example.android.ui.tests;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.android.web.ApiClient;
import com.example.model.Section;

import java.util.List;
import java.util.Optional;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestsViewModel extends ViewModel {

	private final MutableLiveData<List<Section>> testsLiveData = new MutableLiveData<>();

	public TestsViewModel() {
		populateTests();
	}

	public MutableLiveData<List<Section>> getTestsLiveData() {
		return testsLiveData;
	}

	public void populateTests() {
		var apiClient = ApiClient.getInstance();
		var call = apiClient.getRootSections();
		call.enqueue(new Callback<>() {
			@Override
			public void onResponse(@NonNull Call<List<Section>> call, @NonNull Response<List<Section>> response) {
				var result = Optional.ofNullable(response.body()).orElse(getFallbackTests());
				testsLiveData.setValue(result);
			}

			@Override
			public void onFailure(@NonNull Call<List<Section>> call, @NonNull Throwable t) {
				testsLiveData.setValue(getFallbackTests());
			}
		});
	}

	private List<Section> getFallbackTests() {
		var section0 = Section.builder()
				.id(1)
				.name("Symulacja egzaminu")
				.build();

		var section1 = Section.builder()
				.id(1)
				.name("Sieci komputerowe")
				.build();

		var section2 = Section.builder()
				.id(1)
				.name("Metody Numeryczne")
				.build();

		return List.of(section0, section1, section2);
	}
}
