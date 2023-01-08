package com.example.android.ui.section;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.android.databinding.ActivitySectionBinding;
import com.example.android.ui.concept.ConceptActivity;
import com.example.android.ui.exam.ExamActivity;
import com.example.android.ui.exam.State;
import com.example.android.ui.section.edit.EditSectionActivity;
import com.example.android.util.UiUtility;
import com.example.model.Definition;
import com.example.model.Section;
import java.util.List;
import java.util.stream.Collectors;

public class SectionActivity extends AppCompatActivity {

	public static final String ARG_PARENT_SECTION_ID = "parent_section_id";
	public static final String ARG_SECTION_ID = "section_id";

	private ActivitySectionBinding binding;
	private TextView conceptTextView;
	private ListView conceptListView;
	private ListView subSectionsListView;
	private Button testButton;
	private Button studyButton;
	private Button editButton;

	private int sectionId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.binding = ActivitySectionBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		var previousIntent = getIntent();
		this.sectionId = previousIntent.getIntExtra(ARG_SECTION_ID, 0);

		this.conceptTextView = binding.sectionTextView;
		this.conceptListView = binding.conceptsView.conceptsListView;
		this.subSectionsListView = binding.subSectionsView.subSectionsListView;
		this.testButton = binding.sectionTestButton;
		this.studyButton = binding.sectionExamButton;
		this.editButton = binding.buttonEdit;

		setupViewModels();
		setupOnClickListeners();
	}

	private void setupViewModels() {
		var sectionViewModel = new ViewModelProvider(this).get(SectionViewModel.class);
		sectionViewModel.getSectionLiveData().observe(this, this::setSection);
		sectionViewModel.populateSection(sectionId);

		var conceptsViewModel = new ViewModelProvider(this).get(ConceptsViewModel.class);
		conceptsViewModel.getConceptsLiveData().observe(this, this::setConcepts);
		conceptsViewModel.populateConcepts(sectionId);

		var subSectionsViewModel = new ViewModelProvider(this).get(SubSectionsViewModel.class);
		subSectionsViewModel.getSubSectionsLiveData().observe(this, this::setSubSections);
		subSectionsViewModel.populateSubSections(sectionId);
	}

	private void setSection(Section section) {
		conceptTextView.setText(section.getName());
	}

	private void setConcepts(List<Definition> concepts) {
		var arrayAdapter = new ArrayAdapter<>(
				this,
				android.R.layout.simple_list_item_1,
				concepts.stream().map(Definition::getConcept).collect(Collectors.toList())
		);
		conceptListView.setAdapter(arrayAdapter);
		UiUtility.setListViewHeightBasedOnChildren(conceptListView);
		conceptListView.setOnItemClickListener((parent, view, position, id) -> {
			var intent = new Intent(this, ConceptActivity.class);
			intent.putExtra(SectionActivity.ARG_SECTION_ID, sectionId);
			intent.putExtra(ConceptActivity.ARG_CONCEPT_ID, concepts.get(position).getId());
			startActivity(intent);
		});
	}

	private void setSubSections(List<Section> sections) {
		var arrayAdapter = new ArrayAdapter<>(
				this,
				android.R.layout.simple_list_item_1,
				sections.stream().map(Section::getName).collect(Collectors.toList())
		);
		subSectionsListView.setAdapter(arrayAdapter);
		subSectionsListView.setOnItemClickListener((parent, view, position, id) -> {
			var intent = new Intent(this, SectionActivity.class);
			intent.putExtra(ARG_PARENT_SECTION_ID, sectionId);
			intent.putExtra(ARG_SECTION_ID, sections.get(position).getId());
			startActivity(intent);
		});
		UiUtility.setListViewHeightBasedOnChildren(subSectionsListView);
	}

	private void setupOnClickListeners() {
		testButton.setOnClickListener(v -> {
			var intent = new Intent(this, ExamActivity.class);
			intent.putExtra(ExamActivity.ARG_MODE, State.EXAM.getValue());
			intent.putExtra(ARG_SECTION_ID, sectionId);
			startActivity(intent);
		});

		studyButton.setOnClickListener(v -> {
			var intent = new Intent(this, ExamActivity.class);
			intent.putExtra(ExamActivity.ARG_MODE, State.STUDY.getValue());
			intent.putExtra(ARG_SECTION_ID, sectionId);
			startActivity(intent);
		});

		editButton.setOnClickListener(v -> {
			var intent = new Intent(this, EditSectionActivity.class);
			intent.putExtra(ARG_SECTION_ID, sectionId);
			startActivity(intent);
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.binding = null;
	}
}
