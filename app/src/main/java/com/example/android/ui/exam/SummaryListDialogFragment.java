package com.example.android.ui.exam;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import com.example.android.R;
import com.example.android.databinding.FragmentSummaryBinding;
import com.example.model.exam.Exercise;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.text.MessageFormat;
import java.util.List;

public class SummaryListDialogFragment extends BottomSheetDialogFragment {

	private FragmentSummaryBinding binding;

	private ExercisesViewModel viewModel;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getActivity() != null) {
			viewModel = new ViewModelProvider(getActivity()).get(ExercisesViewModel.class);
		}
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		binding = FragmentSummaryBinding.inflate(inflater, container, false);
		return binding.getRoot();
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		var exercises = viewModel.getExercisesLiveData().getValue();
		assert exercises != null;

		var progressBar = binding.progressBarScore;
		var textScore = binding.textViewScore;

		var score = calculateScore(exercises);
		var maxScore = calculateMaxScore(exercises);

		int percentage;
		if (maxScore == 0) {
			percentage = 100;
		} else {
			percentage = 100 * score / maxScore;
		}

		textScore.setText(MessageFormat.format("{0}%", percentage));
		progressBar.setProgress(percentage);

		var listView = binding.listViewScore;

		if (getContext() != null) {
			var arrayAdapter = new SummaryArrayAdapter(getContext(), exercises);
			listView.setAdapter(arrayAdapter);
		}
	}

	@Override
	public int getTheme() {
		return R.style.Theme_Android_CustomBottomSheetDialog;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}

	private Integer calculateScore(List<Exercise> exercises) {
		return exercises.stream().map(Exercise::getScore).reduce(Integer::sum).orElse(0);
	}

	private Integer calculateMaxScore(List<Exercise> exercises) {
		return exercises.stream().map(Exercise::getPoints).reduce(Integer::sum).orElse(0);
	}
}