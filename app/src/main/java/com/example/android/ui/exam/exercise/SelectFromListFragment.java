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
import com.example.android.databinding.FragmentSelectFromListBinding;
import com.example.android.ui.exam.ExercisesViewModel;
import com.example.model.exam.SelectFromList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectFromListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectFromListFragment extends Fragment {

	private static final String ARG_EXERCISE = "exercise";

	private int position = 0;
	private SelectFromList exercise;
	private ExercisesViewModel viewModel;

	private FragmentSelectFromListBinding binding;

	private ListView tasksList;

	public SelectFromListFragment() {
		// Required empty public constructor
	}

	public static SelectFromListFragment newInstance(int position) {
		var fragment = new SelectFromListFragment();
		var args = new Bundle();
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
			exercise = (SelectFromList) viewModel.getExerciseAtPosition(position);
		}
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		binding = FragmentSelectFromListBinding.inflate(inflater, container, false);
		return binding.getRoot();
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		tasksList = binding.listViewDropdown;

		if (getContext() != null) {
			var arrayAdapter = new SelectFromListArrayAdapter(getContext(), exercise, getViewLifecycleOwner(), viewModel.getStateLiveData());
			tasksList.setAdapter(arrayAdapter);
		}
	}
}