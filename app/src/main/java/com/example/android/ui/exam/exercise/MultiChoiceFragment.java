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
import com.example.android.databinding.FragmentMultiChoiceBinding;
import com.example.android.ui.exam.ExercisesViewModel;
import com.example.model.exam.MultipleChoice;

public class MultiChoiceFragment extends Fragment {

	private static final String ARG_EXERCISE = "exercise";

	private int position = 0;
	private MultipleChoice choice;
	private ExercisesViewModel viewModel;

	private FragmentMultiChoiceBinding binding;

	private ListView answerList;

	public MultiChoiceFragment() {
		// Required empty public constructor
	}

	public static MultiChoiceFragment newInstance(int position) {
		var fragment = new MultiChoiceFragment();
		var args = new Bundle();
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
			choice = (MultipleChoice) viewModel.getExerciseAtPosition(position);
		}
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		binding = FragmentMultiChoiceBinding.inflate(inflater, container, false);
		return binding.getRoot();
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		answerList = binding.listViewChoice;

		if (getContext() != null) {
			var arrayAdapter = new MultiChoiceArrayAdapter(getContext(), choice, getViewLifecycleOwner(), viewModel.getStateLiveData());
			answerList.setAdapter(arrayAdapter);
		}
	}
}