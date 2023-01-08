package com.example.android.ui.section;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.android.web.ApiClient;
import com.example.model.Section;
import java.util.Optional;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SectionViewModel extends ViewModel {

	private final MutableLiveData<Section> sectionLiveData = new MutableLiveData<>();

	public LiveData<Section> getSectionLiveData() {
		return sectionLiveData;
	}

	public Section getSection() {
		return sectionLiveData.getValue();
	}

	public void populateNullSection() {
		sectionLiveData.setValue(new Section());
	}

	public void populateSection(int sectionId) {
		var apiClient = ApiClient.getInstance();
		var call = apiClient.getSectionById(sectionId);
		call.enqueue(new Callback<>() {
			@Override
			public void onResponse(@NonNull Call<Section> call, @NonNull Response<Section> response) {
				var result = Optional.ofNullable(response.body()).orElseGet(SectionViewModel::getFallbackSection);
				sectionLiveData.setValue(result);
			}

			@Override
			public void onFailure(@NonNull Call<Section> call, @NonNull Throwable t) {
				sectionLiveData.setValue(getFallbackSection());
			}
		});
	}

	public void saveSection(int parentSectionId, Section section) {
		var apiClient = ApiClient.getInstance();
		var call = apiClient.saveSection(parentSectionId != 0 ? parentSectionId : null, section);
		call.enqueue(new Callback<>() {
			@Override
			public void onResponse(@NonNull Call<Section> call, @NonNull Response<Section> response) {
				var result = Optional.ofNullable(response.body()).orElse(section);
				sectionLiveData.setValue(result);
			}

			@Override
			public void onFailure(@NonNull Call<Section> call, @NonNull Throwable t) {
				sectionLiveData.setValue(section);
			}
		});
	}

	public void deleteSection(int sectionId) {
		var apiClient = ApiClient.getInstance();
		var call = apiClient.deleteSection(sectionId);
		call.enqueue(new Callback<>() {
			@Override
			public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
				// Do nothing
			}

			@Override
			public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
				// Do nothing
			}
		});
	}

	private static Section getFallbackSection() {
		return Section.builder()
				.id(1)
				.name("Sieci Komputerowe")
				.build();
	}
}
