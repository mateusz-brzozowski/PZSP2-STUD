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
import com.example.android.databinding.FragmentFillBlanksBinding;
import com.example.android.ui.exam.ExercisesViewModel;
import com.example.model.exam.FillBlanks;

public class FillBlanksFragment extends Fragment {

	private static final String ARG_EXERCISE = "exercise";

	private int position = 0;
	private FillBlanks exercise;
	private ExercisesViewModel viewModel;

	private FragmentFillBlanksBinding binding;

	private ListView paragraphList;

	public FillBlanksFragment() {
		// Required empty public constructor
	}

	public static FillBlanksFragment newInstance(int position) {
		var fragment = new FillBlanksFragment();
		var args = new Bundle();
		args.putInt(ARG_EXERCISE, position);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			position = getArguments().getInt(ARG_EXERCISE);
		}
		if (getActivity() != null) {
			viewModel = new ViewModelProvider(getActivity()).get(ExercisesViewModel.class);
			exercise = (FillBlanks) viewModel.getExerciseAtPosition(position);
		}
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		binding = FragmentFillBlanksBinding.inflate(inflater, container, false);
		return binding.getRoot();
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		paragraphList = binding.listViewParagraphs;

		if (getContext() != null) {
			var arrayAdapter = new FillBlanksArrayAdapter(getContext(), exercise, getViewLifecycleOwner(), viewModel.getStateLiveData());
			paragraphList.setAdapter(arrayAdapter);
		}
	}
}