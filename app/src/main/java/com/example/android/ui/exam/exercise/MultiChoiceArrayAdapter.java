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
import com.example.model.exam.MultipleChoice;
import com.example.model.exam.answer.ChoiceAnswer;
import java.util.Optional;

public class MultiChoiceArrayAdapter extends ArrayAdapter<ChoiceAnswer> {

	private final MultipleChoice exercise;
	private final LifecycleOwner lifecycleOwner;
	private final LiveData<State> stateLiveData;

	public MultiChoiceArrayAdapter(@NonNull Context context, MultipleChoice exercise, LifecycleOwner lifecycleOwner, LiveData<State> stateLiveData) {
		super(context, 0, exercise.getAnswers());
		this.exercise = exercise;
		this.lifecycleOwner = lifecycleOwner;
		this.stateLiveData = stateLiveData;
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		if (convertView == null) {
			var binding = ViewItemChoiceAnswerBinding.inflate(LayoutInflater.from(getContext()), parent, false);
			var checkbox = binding.checkboxAnswer;
			var answer = exercise.getAnswers().get(position);
			checkbox.setText(answer.getContent());
			checkbox.setChecked(Optional.ofNullable(answer.getChecked()).orElse(false));
			updateBasedOn(checkbox, position, stateLiveData.getValue());

			checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
				getItem(position).setChecked(isChecked);
				updateAllBasedOn(parent, stateLiveData.getValue());
			});

			stateLiveData.observe(lifecycleOwner, state -> updateBasedOn(checkbox, position, state));

			return binding.getRoot();
		} else {
			return convertView;
		}
	}

	private boolean alreadyFinished() {
		var alreadyFinished = exercise.getAnswers().stream().filter(answer -> answer.getChecked() != null).count();
		var correctCount = exercise.getAnswers().stream().filter(ChoiceAnswer::isCorrect).count();
		return alreadyFinished == correctCount;
	}

	private void updateAllBasedOn(ViewGroup parent, State state) {
		for (int i = 0; i < getCount(); i++) {
			var checkbox = (CheckBox) parent.getChildAt(i).findViewById(R.id.checkbox_answer);
			updateBasedOn(checkbox, i, state);
		}
	}

	private void updateBasedOn(CheckBox checkbox, int position, State state) {
		if (state == State.STUDY_ANSWERS || (state == State.STUDY && alreadyFinished())) {
			revealCheckbox(checkbox, position);
		}
	}

	private void revealCheckbox(CheckBox checkBox, int position) {
		var answer = getItem(position);

		if (!checkBox.isChecked() && answer.isCorrect()) {
			checkBox.setBackgroundResource(R.drawable.selectable_answer_yellow);
		} else if (checkBox.isChecked() && answer.isCorrect()) {
			checkBox.setBackgroundResource(R.drawable.selectable_answer_green);
		} else if (checkBox.isChecked() && !answer.isCorrect()) {
			checkBox.setBackgroundResource(R.drawable.selectable_answer_red);
		}

		checkBox.setClickable(false);
	}
}
