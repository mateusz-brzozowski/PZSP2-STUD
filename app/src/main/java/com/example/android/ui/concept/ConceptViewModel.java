package com.example.android.ui.concept;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.android.web.ApiClient;
import com.example.model.Concept;
import com.example.model.Paragraph;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConceptViewModel extends ViewModel {

	private final MutableLiveData<Concept> conceptLiveData = new MutableLiveData<>();

	public LiveData<Concept> getConceptLiveData() {
		return conceptLiveData;
	}

	public void publishConcept(int sectionId, Concept concept) {
		int i = 1;
		for (var paragraph : concept.getParagraphs()) {
			paragraph.setNumber(i);
			i = i + 1;
		}
		var apiClient = ApiClient.getInstance();
		var call = apiClient.saveConcept(sectionId, concept);
		call.enqueue(new Callback<>() {
			@Override
			public void onResponse(@NonNull Call<Concept> call, @NonNull Response<Concept> response) {
				// Do nothing for now
			}

			@Override
			public void onFailure(@NonNull Call<Concept> call, @NonNull Throwable t) {
				// Do nothing for now
			}
		});
	}

	public void populateConcept(int conceptId) {
		var apiClient = ApiClient.getInstance();
		var call = apiClient.getConceptById(conceptId);
		call.enqueue(new Callback<>() {
			@Override
			public void onResponse(@NonNull Call<Concept> call, @NonNull Response<Concept> response) {
				var result = Optional.ofNullable(response.body()).orElseGet(ConceptViewModel::getFallbackConcept);
				result.getParagraphs().sort(Comparator.comparingInt(Paragraph::getSequentialNumber));
				conceptLiveData.setValue(result);
			}

			@Override
			public void onFailure(@NonNull Call<Concept> call, @NonNull Throwable t) {
				conceptLiveData.setValue(getFallbackConcept());
			}
		});
	}

	private static Concept getFallbackConcept() {
		return Concept.builder()
				.keyPhrase("Sieć")
				.summary(
						"zbiór komputerów i innych urządzeń połączonych z sobą kanałami komunikacyjnymi oraz oprogramowanie wykorzystywane w tej sieci. Umożliwia ona wzajemne przekazywanie informacji oraz udostępnianie zasobów własnych między podłączonymi do niej urządzeniami, zwanymi punktami sieci.")
				.paragraphs(List.of(
						Paragraph.builder()
								.header("Przeznaczenie")
								.description(
										"Głównym przeznaczeniem sieci komputerowej – ideą, dla której została stworzona – jest ułatwienie komunikacji między ludźmi (będącymi faktycznymi użytkownikami sieci). Sieć umożliwia łatwy i szybki dostęp do publikowanych danych, jak również otwiera techniczną możliwość tworzenia i korzystania ze wspólnych zasobów informacji i zasobów danych. W sensie prawnym, i w pewnym przybliżeniu, użytkownicy sieci komputerowej są również jej beneficjentami.")
								.build(),
						Paragraph.builder()
								.header("Cechy użytkowe")
								.description(
										"ułatwienie komunikacji między ludźmi. Korzystając z sieci, ludzie mogą komunikować się szybko i łatwo przy wykorzystaniu odpowiednich programów komputerowych i oferowanych w danej sieci usług sieciowych. W odniesieniu do sieci Internet należy wyróżnić dwa rodzaje programów i skojarzone z nimi usługi")
								.build()
				))
				.build();
	}
}
