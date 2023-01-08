package com.example.android.ui.menu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android.R;
import com.example.android.ui.section.SectionActivity;
import com.example.android.util.UiUtility;
import com.example.model.Section;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SectionRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	private List<Section> sections;
	private final Context context;

	public SectionRecyclerViewAdapter(Context context) {
		this.sections = new ArrayList<>();
		this.context = context;
	}

	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		var rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_section, parent, false);
		return new SectionViewHolder(rootView);
	}

	@Override
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
		var section = sections.get(position);
		var viewHolder = (SectionViewHolder) holder;

		Log.d("onBindViewHolder", "Refreshed");
		Log.d("TEST", section.toString());

		// TODO: fix the way state is stored xD
		viewHolder.sectionLinearLayout.setOnClickListener(v -> {
			section.setId(-section.getId());
			notifyItemChanged(position);
			Log.d("Test", "Item at position: " + position);
			Log.d("Test", "State: " + section.getId());
			Log.d("TEST", section.toString());
		});

		viewHolder.sectionIcon.setOnClickListener(v -> {
			var intent = new Intent(context, SectionActivity.class);
			intent.putExtra(SectionActivity.ARG_SECTION_ID, Math.abs(section.getId()));
			context.startActivity(intent);
		});

		viewHolder.sectionName.setText(section.getName());

		if (section.getId() == 1 && !section.getSubSections().isEmpty()) {
			viewHolder.subSections.setVisibility(View.VISIBLE);
			viewHolder.sectionLinearLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.secondary_active_background));
			var subSections = section.getSubSections()
					.stream()
					.map(Section::getName)
					.collect(Collectors.toList());

			ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, subSections);
			viewHolder.subSections.setAdapter(arrayAdapter);

			UiUtility.setListViewHeightBasedOnChildren(viewHolder.subSections);
		} else {
			viewHolder.sectionLinearLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.secondary_background));
			viewHolder.subSections.setAdapter(null);
			viewHolder.subSections.setVisibility(View.GONE);
		}
	}

	@Override
	public int getItemCount() {
		return sections.size();
	}

	@SuppressLint("NotifyDataSetChanged")
	public void setSections(List<Section> sections) {
		this.sections.clear();
		this.sections = sections;
		notifyDataSetChanged();
	}


	static class SectionViewHolder extends RecyclerView.ViewHolder {
		TextView sectionName;
		ImageView sectionIcon;
		ListView subSections;
		LinearLayout sectionLinearLayout;

		public SectionViewHolder(@NonNull View itemView) {
			super(itemView);
			this.sectionName = itemView.findViewById(R.id.section_name);
			this.subSections = itemView.findViewById(R.id.sub_section_list_view);
			this.sectionLinearLayout = itemView.findViewById(R.id.section_linear_layout);
			this.sectionIcon = itemView.findViewById(R.id.section_icon);
		}
	}
}

