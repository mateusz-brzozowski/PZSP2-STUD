package com.example.android.ui.exam.edit;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.android.util.ExerciseMapperProvider;
import com.example.android.web.ApiClient;
import com.example.model.exam.Exercise;
import com.example.model.exam.ExerciseDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExerciseViewModel extends ViewModel {

	private final MutableLiveData<Boolean> sentLiveData = new MutableLiveData<>();

	public LiveData<Boolean> getSentLiveData() {
		return sentLiveData;
	}

	public void insertExercise(int sectionId, Exercise exercise) {
		var apiClient = ApiClient.getInstance();
		var call = apiClient.saveExercise(sectionId, ExerciseMapperProvider.EXERCISE_MAPPER.exerciseToDto(exercise));
		call.enqueue(new Callback<>() {
			@Override
			public void onResponse(@NonNull Call<ExerciseDto> call, @NonNull Response<ExerciseDto> response) {
				sentLiveData.setValue(true);
			}

			@Override
			public void onFailure(@NonNull Call<ExerciseDto> call, @NonNull Throwable t) {
				sentLiveData.setValue(true);

			}
		});
	}
}
