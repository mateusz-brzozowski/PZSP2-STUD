package com.example.android.ui.exam.exercise;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.android.R;
import com.example.android.databinding.FragmentFlashCardBinding;
import com.example.android.ui.exam.ExercisesViewModel;
import com.example.android.ui.exam.State;
import com.example.model.exam.FlashCard;

public class FlashCardFragment extends Fragment {

	private static final String ARG_EXERCISE = "exercise";

	private int position = 0;
	private FlashCard exercise;
	private ExercisesViewModel viewModel;

	private FragmentFlashCardBinding binding;

	private TextView textViewAnswer;
	private Button buttonHideReveal;

	public FlashCardFragment() {
		// Required empty public constructor
	}

	public static FlashCardFragment newInstance(int position) {
		FlashCardFragment fragment = new FlashCardFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_EXERCISE, position);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			position = getArguments().getInt(ARG_EXERCISE, 0);
		}
		if (getActivity() != null) {
			viewModel = new ViewModelProvider(getActivity()).get(ExercisesViewModel.class);
			exercise = (FlashCard) viewModel.getExerciseAtPosition(position);
		}
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		binding = FragmentFlashCardBinding.inflate(inflater, container, false);
		return binding.getRoot();
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		textViewAnswer = binding.textViewAnswer;
		buttonHideReveal = binding.buttonReveal;

		buttonHideReveal.setOnClickListener(v -> {
			exercise.setShown(!exercise.isShown());
			updateView();
		});

		viewModel.getStateLiveData().observe(getViewLifecycleOwner(), this::updateBasedOn);

		updateView();
	}


	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}

	private void updateView() {
		if (exercise.isShown()) {
			textViewAnswer.setText(exercise.getAnswer());
			buttonHideReveal.setText(R.string.hide);
		} else {
			textViewAnswer.setText(R.string.hidden_text);
			buttonHideReveal.setText(R.string.reveal);
		}
	}

	private void updateBasedOn(State state) {
		if (state == State.STUDY_ANSWERS) {
			revealAnswer();
		}
	}

	private void revealAnswer() {
		exercise.setShown(true);
		updateView();
		buttonHideReveal.setClickable(false);
	}
}