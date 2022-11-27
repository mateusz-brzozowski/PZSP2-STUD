package com.example.android.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android.R;
import com.example.model.Concept;
import com.example.model.Paragraph;
import java.util.List;

public class ConceptActivity extends AppCompatActivity {
	private RecyclerView recyclerView;
	private ConceptRecyclerViewAdapter conceptRecyclerViewAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_concept);
		initViews();
	}

	private void initViews() {
		recyclerView = findViewById(R.id.concept_recycler_view);
		setUpRecyclerView();
	}

	private void setUpRecyclerView() {
		recyclerView.setHasFixedSize(true);
		recyclerView.setVisibility(View.GONE);

		RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(manager);

		Concept concept = Concept.builder().summary("Lorem\nLorem\nSIEMA").keyPhrase("Concept")
				.paragraphs(List.of(Paragraph.builder().header("Paragraph").description("SIEMASKOSDADASD").build())).build();
		conceptRecyclerViewAdapter = new ConceptRecyclerViewAdapter(concept);

		recyclerView.setAdapter(conceptRecyclerViewAdapter);
		recyclerView.setVisibility(View.VISIBLE);
	}
}
