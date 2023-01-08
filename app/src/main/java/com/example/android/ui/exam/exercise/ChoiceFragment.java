package com.example.android.ui.exam.exercise;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.android.databinding.FragmentChoiceBinding;
import com.example.android.ui.exam.ExercisesViewModel;
import com.example.model.exam.Choice;

public class ChoiceFragment extends Fragment {

	private static final String ARG_EXERCISE = "exercise";
	private int position = 0;
	private Choice choice;
	private ExercisesViewModel viewModel;

	private FragmentChoiceBinding binding;

	private ListView answerList;

	public ChoiceFragment() {
		// Required empty public constructor
	}

	public static ChoiceFragment newInstance(int position) {
		var args = new Bundle();
		var fragment = new ChoiceFragment();
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
			choice = (Choice) viewModel.getExerciseAtPosition(position);
		}
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		binding = FragmentChoiceBinding.inflate(inflater, container, false);
		return binding.getRoot();
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		answerList = binding.listViewChoice;

		if (getContext() != null) {
			var arrayAdapter = new ChoiceArrayAdapter(getContext(), choice, getViewLifecycleOwner(), viewModel.getStateLiveData());
			answerList.setAdapter(arrayAdapter);
		}
	}
}