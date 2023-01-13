package com.example.android.ui.tests;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.android.databinding.FragmentTestsBinding;

public class TestsFragment extends Fragment {

	private FragmentTestsBinding binding;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		TestsViewModel testsViewModel =
				new ViewModelProvider(this).get(TestsViewModel.class);

		binding = FragmentTestsBinding.inflate(inflater, container, false);
		View root = binding.getRoot();

		var progressBar = binding.progressBarTests;
		var recyclerView = binding.testsRecyclerView;
		var recyclerViewAdapter = new TestsRecyclerViewAdapter(this.getContext());

		recyclerView.setHasFixedSize(true);

		var manager = new LinearLayoutManager(this.getContext());
		recyclerView.setLayoutManager(manager);

		recyclerView.setAdapter(recyclerViewAdapter);

		testsViewModel.getTestsLiveData().observe(getViewLifecycleOwner(), tests -> {
			progressBar.setVisibility(View.GONE);
			recyclerViewAdapter.setTests(tests);
		});
		return root;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}
}
