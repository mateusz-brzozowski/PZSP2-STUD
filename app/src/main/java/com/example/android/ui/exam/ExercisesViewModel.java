package com.example.android.ui.exam;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.android.util.ExerciseMapperProvider;
import com.example.android.web.ApiClient;
import com.example.model.exam.Choice;
import com.example.model.exam.Exercise;
import com.example.model.exam.ExerciseDto;
import com.example.model.exam.FillBlanks;
import com.example.model.exam.FlashCard;
import com.example.model.exam.MultipleChoice;
import com.example.model.exam.MultipleTruthOrFalse;
import com.example.model.exam.SelectFromList;
import com.example.model.exam.TruthOrFalse;
import com.example.model.exam.answer.BlankAnswer;
import com.example.model.exam.answer.ChoiceAnswer;
import com.example.model.exam.answer.ListAnswer;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExercisesViewModel extends ViewModel {

	private final MutableLiveData<Integer> currentExerciseNumberLiveData = new MutableLiveData<>();
	private final MutableLiveData<List<Exercise>> exercisesLiveData = new MutableLiveData<>();
	private final MutableLiveData<State> stateLiveData = new MutableLiveData<>();

	public ExercisesViewModel() {
		currentExerciseNumberLiveData.setValue(0);
	}

	public LiveData<List<Exercise>> getExercisesLiveData() {
		return exercisesLiveData;
	}

	public int getExercisesSize() {
		return Optional.ofNullable(exercisesLiveData.getValue()).map(List::size).orElse(0);
	}

	public LiveData<Integer> getCurrentExerciseNumberLiveData() {
		return currentExerciseNumberLiveData;
	}

	public LiveData<State> getStateLiveData() {
		return stateLiveData;
	}

	public State getState() {
		return stateLiveData.getValue();
	}

	public void setState(State state) {
		stateLiveData.setValue(state);
	}

	public Exercise getExerciseAtPosition(int position) {
		return Optional.ofNullable(exercisesLiveData.getValue())
				.map(exercises -> exercises.get(position))
				.orElse(null);
	}

	public int getCurrentExerciseNumber() {
		return Optional.ofNullable(currentExerciseNumberLiveData.getValue())
				.orElse(0);
	}

	public void nextExercise() {
		int value = getCurrentExerciseNumber() + 1;
		if (value < getExercisesSize()) {
			currentExerciseNumberLiveData.setValue(value);
		} else {
			stateLiveData.setValue(State.STUDY_ANSWERS);
		}
	}

	public void previousExercise() {
		int value = getCurrentExerciseNumber() - 1;
		if (value >= 0) {
			currentExerciseNumberLiveData.setValue(value);
		}
	}

	public void setExerciseNumber(int position) {
		currentExerciseNumberLiveData.setValue(position);
	}

	public void populateExercises(int sectionId) {
		var apiClient = ApiClient.getInstance();
		var call = apiClient.getExercisesBySectionId(sectionId);
		call.enqueue(new Callback<>() {
			@Override
			public void onResponse(@NonNull Call<List<ExerciseDto>> call, @NonNull Response<List<ExerciseDto>> response) {
				if (response.body() == null) {
					exercisesLiveData.setValue(getFallbackExercises());
				} else {
					var result = response.body().stream()
							.map(ExerciseMapperProvider.EXERCISE_MAPPER::dtoToExercise)
							.collect(Collectors.toList());
					exercisesLiveData.setValue(result);
				}
			}

			@Override
			public void onFailure(@NonNull Call<List<ExerciseDto>> call, @NonNull Throwable t) {
				exercisesLiveData.setValue(getFallbackExercises());

			}
		});
	}

	private static List<Exercise> getFallbackExercises() {
		return List.of(
				Choice.builder()
						.question("Czemu służy podział na poziomy agregacji (aggregation levels) w adresie IP v.6?")
						.correctAnswer("zmniejszeniu liczby bitów analizowanych przez routery")
						.possibleAnswers(List.of(
								"uproszczeniu działania odwrswitchotnego DNS",
								"zmniejszeniu liczby bitów analizowanych przez routery",
								"odzwierciedleniu hierarchii DNS",
								"agregacji protokołów warstwy czwartej"
						))
						.build(),
				MultipleChoice.builder()
						.question("Jakie rodzaje serwerów występują w systemie DNS?")
						.answers(List.of(
								ChoiceAnswer.builder().content("secondary master").correct(false).build(),
								ChoiceAnswer.builder().content("secondary").correct(true).build(),
								ChoiceAnswer.builder().content("primary").correct(true).build(),
								ChoiceAnswer.builder().content("caching only").correct(true).build(),
								ChoiceAnswer.builder().content("primary slave").correct(false).build(),
								ChoiceAnswer.builder().content("duplicating").correct(false).build()
						))
						.build(),
				TruthOrFalse.builder()
						.question("W protokole TCP buforowanie oraz potiwerdzanie transmisji następuje w czwartej warstwie modelu ISO/OSI.")
						.correct(true)
						.build(),
				MultipleTruthOrFalse.builder()
						.question("Jakie  rekordów mogą występować w systemie DNS?")
						.tasks(List.of(
								ChoiceAnswer.builder().content("CNAME").correct(true).build(),
								ChoiceAnswer.builder().content("IN").correct(false).build(),
								ChoiceAnswer.builder().content("PTR").correct(true).build(),
								ChoiceAnswer.builder().content("HINFO").correct(true).build(),
								ChoiceAnswer.builder().content("WKS").correct(true).build(),
								ChoiceAnswer.builder().content("SOA").correct(true).build()
						))
						.build(),
				FlashCard.builder()
						.question("Protokół PPTP jest kombinacją protokołów:")
						.answer("PPP i GRE")
						.build(),
				FillBlanks.builder()
						.question("Uzupełnij luki:")
						.answers(List.of(
								BlankAnswer.builder().start("Parametr \"expire\" rekordu SOA jest wyrażony w:").answer("sekundach").end("").build(),
								BlankAnswer.builder().start(", a parametr \"refresh\" wyrażony w:").answer("sekundach").end("").build()
						))
						.build(),
				SelectFromList.builder()
						.question("Wybierz z listy dostępnych wartości:")
						.answers(List.of(
								ListAnswer.builder().start("Początkowe dwa człony zapytania do odwrotnego DNSa w przypadku IP v.6 odpowiadają: ")
										.possibleAnswers(List.of(
												"pierwszym dwu bajtom adresu IP",
												"ostatnim dwu cyfrom heksadecymalnym adresu IP",
												"pierwszym dwu cyfrom heksadecymalnym adresu IP",
												"pierwszym dwu dwubajtowym słowom adresu IP"
										))
										.correctAnswer("ostatnim dwu cyfrom heksadecymalnym adresu IP").end("").build()
						))
						.build()
		);
	}
}
