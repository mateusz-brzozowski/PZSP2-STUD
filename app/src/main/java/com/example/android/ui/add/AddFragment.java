package com.example.android.ui.add;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.android.databinding.ActivityEditSectionBinding;
import com.example.android.ui.MainMenuActivity;
import com.example.android.ui.concept.ConceptActivity;
import com.example.android.ui.exam.edit.EditExerciseActivity;
import com.example.android.ui.section.SectionActivity;
import com.example.android.ui.section.SectionViewModel;
import com.example.android.ui.section.edit.EditSectionActivity;

public class AddFragment extends Fragment {

	private ActivityEditSectionBinding binding;

	private EditText editTextSection;

	private Button buttonDone;
	private Button buttonCancel;

	private Button buttonAddConcept;
	private Button buttonAddSubSection;
	private Button buttonAddExercise;

	private final int parentSectionId = 0;
	private int sectionId = 0;

	private SectionViewModel viewModel;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		binding = ActivityEditSectionBinding.inflate(inflater, container, false);

		return binding.getRoot();
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		viewModel = new ViewModelProvider(this).get(SectionViewModel.class);
		viewModel.populateNullSection();

		this.editTextSection = binding.editTextSection;
		this.buttonDone = binding.buttonDone;
		this.buttonCancel = binding.buttonCancel;
		this.buttonAddConcept = binding.buttonAddConcept;
		this.buttonAddSubSection = binding.buttonAddSubSection;
		this.buttonAddExercise = binding.buttonAddExercise;

		this.buttonCancel.setVisibility(View.GONE);

		setupView();
		setupListeners();
	}

	private void setupView() {
		viewModel.getSectionLiveData().observe(getViewLifecycleOwner(), section -> {
			sectionId = section.getId();
			editTextSection.setText(section.getName());
		});
	}

	private void setupListeners() {
		buttonDone.setOnClickListener(v -> {
			var section = viewModel.getSection();
			section.setName(editTextSection.getText().toString());
			viewModel.saveSection(parentSectionId, section);
		});

		buttonCancel.setOnClickListener(v -> {
			Intent intent = new Intent(getContext(), MainMenuActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			viewModel.deleteSection(sectionId);
			startActivity(intent);
		});

		buttonAddConcept.setOnClickListener(v -> {
			var intent = new Intent(getContext(), ConceptActivity.class);
			intent.putExtra(SectionActivity.ARG_SECTION_ID, sectionId);
			// TODO: open in edit view
			startActivity(intent);
		});

		buttonAddSubSection.setOnClickListener(v -> {
			var intent = new Intent(getContext(), EditSectionActivity.class);
			intent.putExtra(SectionActivity.ARG_PARENT_SECTION_ID, sectionId);
			intent.putExtra(SectionActivity.ARG_SECTION_ID, 0);
			startActivity(intent);
		});

		buttonAddExercise.setOnClickListener(v -> {
			var intent = new Intent(getContext(), EditExerciseActivity.class);
			intent.putExtra(SectionActivity.ARG_SECTION_ID, sectionId);
			startActivity(intent);
		});
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}
}
