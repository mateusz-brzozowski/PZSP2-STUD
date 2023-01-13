package com.example.android.ui.tests;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android.R;
import com.example.android.ui.exam.ExamActivity;
import com.example.android.ui.exam.State;
import com.example.model.Section;
import java.util.ArrayList;
import java.util.List;

public class TestsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	private List<Section> tests;
	private final Context context;
	public static final String ARG_SECTION_ID = "section_id";

	public TestsRecyclerViewAdapter(Context context) {
		this.tests = new ArrayList<>();
		this.context = context;
	}

	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		var rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_tests, parent, false);
		return new TestsViewHolder(rootView);
	}

	@Override
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
		var section = tests.get(position);
		var viewHolder = (TestsViewHolder) holder;

		viewHolder.testName.setText(section.getName());


		viewHolder.examButton.setOnClickListener(v -> {
			var intent = new Intent(context, ExamActivity.class);
			intent.putExtra(ExamActivity.ARG_MODE, State.EXAM.getValue());
			intent.putExtra(ARG_SECTION_ID, section.getId());
			context.startActivity(intent);
		});

		viewHolder.studyButton.setOnClickListener(v -> {
			var intent = new Intent(context, ExamActivity.class);
			intent.putExtra(ExamActivity.ARG_MODE, State.STUDY.getValue());
			intent.putExtra(ARG_SECTION_ID, section.getId());
			context.startActivity(intent);
		});
	}

	@Override
	public int getItemCount() {
		return tests.size();
	}

	@SuppressLint("NotifyDataSetChanged")
	public void setTests(List<Section> tests) {
		this.tests.clear();
		this.tests = tests;
		notifyDataSetChanged();
	}

	static class TestsViewHolder extends RecyclerView.ViewHolder {
		TextView testName;
		TextView testDescription;
		Button examButton;
		Button studyButton;

		public TestsViewHolder(@NonNull View itemView) {
			super(itemView);
			this.testName = itemView.findViewById(R.id.test_name);
			this.testDescription = itemView.findViewById(R.id.test_description);
			this.examButton = itemView.findViewById(R.id.tests_exam_button);
			this.studyButton = itemView.findViewById(R.id.tests_study_button);
		}
	}
}
