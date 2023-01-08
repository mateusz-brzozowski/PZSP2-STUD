package com.example.android.ui.section;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.android.web.ApiClient;
import com.example.model.Section;
import java.util.List;
import java.util.Optional;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubSectionsViewModel extends ViewModel {

	private final MutableLiveData<List<Section>> subSectionsLiveData = new MutableLiveData<>();

	public LiveData<List<Section>> getSubSectionsLiveData() {
		return subSectionsLiveData;
	}

	public void populateSubSections(int parentSectionId) {
		var apiClient = ApiClient.getInstance();
		var call = apiClient.getSectionsByParentId(parentSectionId);
		call.enqueue(new Callback<>() {
			@Override
			public void onResponse(@NonNull Call<List<Section>> call, @NonNull Response<List<Section>> response) {
				var result = Optional.ofNullable(response.body()).orElseGet(SubSectionsViewModel::getFallbackSections);
				subSectionsLiveData.setValue(result);
			}

			@Override
			public void onFailure(@NonNull Call<List<Section>> call, @NonNull Throwable t) {
				subSectionsLiveData.setValue(getFallbackSections());
			}
		});
	}

	private static List<Section> getFallbackSections() {
		var section0 = Section.builder()
				.id(1)
				.name("Usługi sieciowe")
				.build();

		var section1 = Section.builder()
				.id(1)
				.name("Serwery DNS")
				.build();

		var section2 = Section.builder()
				.id(1)
				.name("Protokoły TCP i UDP")
				.build();

		return List.of(section0, section1, section2);
	}
}
