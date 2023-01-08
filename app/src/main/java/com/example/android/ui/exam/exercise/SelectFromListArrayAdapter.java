package com.example.android.ui.exam.exercise;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import com.example.android.R;
import com.example.android.databinding.ViewItemDropdownBinding;
import com.example.android.ui.exam.State;
import com.example.model.exam.SelectFromList;
import com.example.model.exam.answer.ListAnswer;
import java.util.ArrayList;
import java.util.Optional;

public class SelectFromListArrayAdapter extends ArrayAdapter<ListAnswer> {

	private final SelectFromList exercise;
	private final LifecycleOwner lifecycleOwner;
	private final LiveData<State> stateLiveData;


	public SelectFromListArrayAdapter(
			@NonNull Context context,
			SelectFromList exercise,
			LifecycleOwner lifecycleOwner,
			LiveData<State> stateLiveData
	) {
		super(context, 0, exercise.getAnswers());
		this.exercise = exercise;
		this.lifecycleOwner = lifecycleOwner;
		this.stateLiveData = stateLiveData;
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		if (convertView != null) {
			return convertView;
		}

		@SuppressLint("ViewHolder")
		var binding = ViewItemDropdownBinding.inflate(LayoutInflater.from(getContext()), parent, false);
		var spinner = binding.spinnerAnswers;
		var textStart = binding.textViewStartParagraph;
		var textEnd = binding.textViewEndParagraph;

		var task = getItem(position);

		textStart.setText(task.getStart());
		textEnd.setText(task.getEnd());

		var possibleAnswers = new ArrayList<String>();
		possibleAnswers.add("");
		possibleAnswers.addAll(task.getPossibleAnswers());

		var adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, possibleAnswers);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		var selected = task.getSelected();
		if (selected != null) {
			spinner.setSelection(selected + 1);
		}

		updateBasedOn(spinner, position, stateLiveData.getValue());
		stateLiveData.observe(lifecycleOwner, state -> updateBasedOn(spinner, position, state));

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> p, View view, int position, long id) {
				if (position == 0) {
					task.setSelected(null);
				} else if (spinner.isEnabled()) {
					task.setSelected(position - 1);
					updateAllBasedOn(parent, stateLiveData.getValue());
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				task.setSelected(null);
			}
		});

		return binding.getRoot();
	}

	private boolean alreadyFinished() {
		var alreadyFinished = exercise.getAnswers().stream().filter(answer -> answer.getSelected() != null).count();
		return alreadyFinished == getCount();
	}

	private void updateAllBasedOn(ViewGroup parent, State state) {
		for (int i = 0; i < getCount(); i++) {
			var child = parent.getChildAt(i);
			var spinner = (Spinner) child.findViewById(R.id.spinner_answers);
			updateBasedOn(spinner, i, state);
		}
	}

	private void updateBasedOn(Spinner spinner, int position, State state) {
		if (state == State.STUDY_ANSWERS || (state == State.STUDY && alreadyFinished())) {
			revealAnswer(spinner, position);
		}
	}

	private void revealAnswer(Spinner spinner, int position) {
		var task = getItem(position);

		var selected = Optional.ofNullable(task.getSelected()).map(i -> task.getPossibleAnswers().get(i)).orElse(null);

		if (selected == null) {
			spinner.setBackgroundResource(R.drawable.selectable_answer_yellow);
		} else if (task.getCorrectAnswer().equalsIgnoreCase(selected)) {
			spinner.setBackgroundResource(R.drawable.selectable_answer_green);
		} else {
			spinner.setBackgroundResource(R.drawable.selectable_answer_red);
		}

		var correctPosition = task.getPossibleAnswers().indexOf(task.getCorrectAnswer());

		spinner.setEnabled(false);
		spinner.setSelection(correctPosition + 1);
	}
}
