package com.example.android.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.R;
import com.example.android.web.ApiClient;
import com.example.model.Concept;
import com.example.model.Paragraph;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

		conceptRecyclerViewAdapter = new ConceptRecyclerViewAdapter();
		getConcept();

		recyclerView.setAdapter(conceptRecyclerViewAdapter);
		recyclerView.setVisibility(View.VISIBLE);
	}

	private void getConcept() {
		ApiClient apiClient = ApiClient.getInstance();
		Call<Concept> call = apiClient.getConceptById(1);
		call.enqueue(new Callback<>() {
			@SuppressLint("NotifyDataSetChanged")
			@Override
			public void onResponse(@NonNull Call<Concept> call, @NonNull Response<Concept> response) {
				if (response.isSuccessful() && response.body() != null) {
					Concept concept = response.body();
					conceptRecyclerViewAdapter.setConcept(concept);
					runOnUiThread(() -> conceptRecyclerViewAdapter.notifyDataSetChanged());
				} else {
					Log.e("Concept", "Response concept failure");
				}
			}

			@Override
			public void onFailure(@NonNull Call<Concept> call, @NonNull Throwable t) {
				Log.e("Concept", "Load concept failure");
			}
		});
	}
}
