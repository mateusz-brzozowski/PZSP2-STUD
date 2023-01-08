package com.example.android.ui.tests;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.android.databinding.FragmentTestsBinding;

public class TestsFragment extends Fragment {

	private FragmentTestsBinding binding;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		TestsViewModel testsViewModel =
				new ViewModelProvider(this).get(TestsViewModel.class);

		binding = FragmentTestsBinding.inflate(inflater, container, false);
		View root = binding.getRoot();

		final TextView textView = binding.textTests;
		testsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
		return root;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}
}
