package com.example.android.ui.exam;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.android.R;
import com.example.android.databinding.ViewItemSummaryBinding;
import com.example.model.exam.Exercise;
import java.text.MessageFormat;
import java.util.List;

public class SummaryArrayAdapter extends ArrayAdapter<Exercise> {


	public SummaryArrayAdapter(@NonNull Context context, List<Exercise> exercises) {
		super(context, 0, exercises);
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		if (convertView != null) {
			return convertView;
		}

		@SuppressLint("ViewHolder")
		var binding = ViewItemSummaryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
		var textTask = binding.textViewTask;
		var textScore = binding.textViewScore;

		var exercise = getItem(position);

		textTask.setText(MessageFormat.format(getContext().getString(R.string.summary_task), position + 1));
		textScore.setText(MessageFormat.format("{0} / {1}", exercise.getScore(), exercise.getPoints()));

		return binding.getRoot();
	}
}
