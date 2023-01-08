package com.example.android.ui.exam.exercise;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import com.example.android.R;
import com.example.android.databinding.ViewItemChoiceAnswerBinding;
import com.example.android.ui.exam.State;
import com.example.model.exam.Choice;
import java.util.Objects;

public class ChoiceArrayAdapter extends ArrayAdapter<String> {

	private final Choice choice;
	private final LifecycleOwner lifecycleOwner;
	private final LiveData<State> stateLiveData;

	public ChoiceArrayAdapter(@NonNull Context context, Choice choice, LifecycleOwner lifecycleOwner, LiveData<State> stateLiveData) {
		super(context, 0, choice.getPossibleAnswers());
		this.choice = choice;
		this.lifecycleOwner = lifecycleOwner;
		this.stateLiveData = stateLiveData;
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		if (convertView == null) {
			var binding = ViewItemChoiceAnswerBinding.inflate(LayoutInflater.from(getContext()), parent, false);

			var checkbox = binding.checkboxAnswer;
			var answer = choice.getPossibleAnswers().get(position);
			checkbox.setText(answer);
			checkbox.setChecked(Objects.equals(position, choice.getSelected()));
			updateBasedOn(checkbox, position, stateLiveData.getValue());

			checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
				uncheckAll(parent);
				checkbox.setChecked(isChecked);
				choice.setSelected(isChecked ? position : null);
				updateAllBasedOn(parent, stateLiveData.getValue());
			});

			stateLiveData.observe(lifecycleOwner, state -> updateBasedOn(checkbox, position, state));

			return binding.getRoot();
		} else {
			return convertView;
		}
	}

	private void uncheckAll(ViewGroup parent) {
		for (int i = 0; i < getCount(); i++) {
			var checkbox = (CheckBox) parent.getChildAt(i).findViewById(R.id.checkbox_answer);
			checkbox.setChecked(false);
		}
	}

	private void updateAllBasedOn(ViewGroup parent, State state) {
		for (int i = 0; i < getCount(); i++) {
			var checkbox = (CheckBox) parent.getChildAt(i).findViewById(R.id.checkbox_answer);
			updateBasedOn(checkbox, i, state);
		}
	}

	private void updateBasedOn(CheckBox checkbox, int position, State state) {
		if (state == State.STUDY_ANSWERS || (state == State.STUDY && choice.getSelected() != null)) {
			revealCheckbox(checkbox, position);
		}
	}

	private void revealCheckbox(CheckBox checkBox, int position) {
		var isCorrect = Objects.equals(choice.getCorrectAnswer(), getItem(position));

		if (!checkBox.isChecked() && isCorrect) {
			checkBox.setBackgroundResource(R.drawable.selectable_answer_yellow);
		} else if (checkBox.isChecked() && isCorrect) {
			checkBox.setBackgroundResource(R.drawable.selectable_answer_green);
		} else if (checkBox.isChecked() && !isCorrect) {
			checkBox.setBackgroundResource(R.drawable.selectable_answer_red);
		}

		checkBox.setClickable(false);
	}
}
