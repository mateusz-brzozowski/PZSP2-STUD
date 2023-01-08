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
import com.example.android.databinding.FragmentMultiTruthOrFalseBinding;
import com.example.android.ui.exam.ExercisesViewModel;
import com.example.model.exam.MultipleTruthOrFalse;

public class MultiTruthOrFalseFragment extends Fragment {

	private static final String ARG_EXERCISE = "exercise";

	private int position = 0;
	private MultipleTruthOrFalse exercise;
	private ExercisesViewModel viewModel;

	private FragmentMultiTruthOrFalseBinding binding;

	private ListView tasksList;


	public MultiTruthOrFalseFragment() {
		// Required empty public constructor
	}

	public static MultiTruthOrFalseFragment newInstance(int position) {
		MultiTruthOrFalseFragment fragment = new MultiTruthOrFalseFragment();
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
			exercise = (MultipleTruthOrFalse) viewModel.getExerciseAtPosition(position);
		}
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		binding = FragmentMultiTruthOrFalseBinding.inflate(inflater, container, false);
		return binding.getRoot();
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		tasksList = binding.listViewTruthOrFalse;

		if (getContext() != null) {
			var arrayAdapter = new MultiTruthOrFalseArrayAdapter(getContext(), exercise, getViewLifecycleOwner(), viewModel.getStateLiveData());
			tasksList.setAdapter(arrayAdapter);
		}
	}
}