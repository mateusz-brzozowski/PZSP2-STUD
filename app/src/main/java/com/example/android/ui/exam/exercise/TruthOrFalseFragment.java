package com.example.android.ui.exam.exercise;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.android.R;
import com.example.android.databinding.FragmentTruthOrFalseBinding;
import com.example.android.ui.exam.ExercisesViewModel;
import com.example.android.ui.exam.State;
import com.example.model.exam.TruthOrFalse;

public class TruthOrFalseFragment extends Fragment {

	private static final String ARG_EXERCISE = "exercise";

	private int position = 0;
	private TruthOrFalse exercise;
	private ExercisesViewModel viewModel;

	private FragmentTruthOrFalseBinding binding;

	private RadioButton truthButton;
	private RadioButton falseButton;

	public TruthOrFalseFragment() {
		// Required empty public constructor
	}

	public static TruthOrFalseFragment newInstance(int position) {
		TruthOrFalseFragment fragment = new TruthOrFalseFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_EXERCISE, position);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			position = getArguments().getInt(ARG_EXERCISE, 0);
		}
		if (getActivity() != null) {
			viewModel = new ViewModelProvider(getActivity()).get(ExercisesViewModel.class);
			exercise = (TruthOrFalse) viewModel.getExerciseAtPosition(position);
		}
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		binding = FragmentTruthOrFalseBinding.inflate(inflater, container, false);
		return binding.getRoot();
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		truthButton = binding.truthButton;
		falseButton = binding.falseButton;

		var checked = exercise.getChosen();

		if (checked != null) {
			if (checked) {
				truthButton.setChecked(true);
			} else {
				falseButton.setChecked(true);
			}
		}
		updateBasedOn(viewModel.getState());

		viewModel.getStateLiveData().observe(getViewLifecycleOwner(), this::updateBasedOn);

		truthButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
			exercise.setChosen(true);
			updateBasedOn(viewModel.getState());
		});
		falseButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
			exercise.setChosen(false);
			updateBasedOn(viewModel.getState());
		});
	}

	private void updateBasedOn(State state) {
		if (state == State.STUDY_ANSWERS || (state == State.STUDY && (truthButton.isChecked() || falseButton.isChecked()))) {
			revealAnswer();
		}
	}

	private void revealAnswer() {
		if (truthButton.isChecked() && exercise.isCorrect()) {
			truthButton.setBackgroundResource(R.drawable.selectable_answer_green);
		} else if (falseButton.isChecked() && !exercise.isCorrect()) {
			falseButton.setBackgroundResource(R.drawable.selectable_answer_green);
		} else if (!truthButton.isChecked() && !falseButton.isChecked()) {
			if (exercise.isCorrect()) {
				truthButton.setBackgroundResource(R.drawable.selectable_answer_yellow);
			} else {
				falseButton.setBackgroundResource(R.drawable.selectable_answer_yellow);
			}
		} else {
			if (exercise.isCorrect()) {
				truthButton.setBackgroundResource(R.drawable.selectable_answer_yellow);
				falseButton.setBackgroundResource(R.drawable.selectable_answer_red);
			} else {
				truthButton.setBackgroundResource(R.drawable.selectable_answer_red);
				falseButton.setBackgroundResource(R.drawable.selectable_answer_yellow);
			}
		}

		truthButton.setClickable(false);
		falseButton.setClickable(false);
	}
}