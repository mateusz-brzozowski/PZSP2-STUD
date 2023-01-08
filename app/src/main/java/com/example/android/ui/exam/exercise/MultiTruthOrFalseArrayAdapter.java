package com.example.android.ui.exam.exercise;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import com.example.android.R;
import com.example.android.databinding.ViewItemTruthOrFalseBinding;
import com.example.android.ui.exam.State;
import com.example.model.exam.MultipleTruthOrFalse;
import com.example.model.exam.answer.ChoiceAnswer;

public class MultiTruthOrFalseArrayAdapter extends ArrayAdapter<ChoiceAnswer> {

	private final MultipleTruthOrFalse exercise;
	private final LifecycleOwner lifecycleOwner;
	private final LiveData<State> stateLiveData;

	public MultiTruthOrFalseArrayAdapter(
			@NonNull Context context,
			MultipleTruthOrFalse exercise,
			LifecycleOwner lifecycleOwner,
			LiveData<State> stateLiveData
	) {
		super(context, 0, exercise.getTasks());
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
		var binding = ViewItemTruthOrFalseBinding.inflate(LayoutInflater.from(getContext()), parent, false);
		var truthButton = binding.truthButton;
		var falseButton = binding.falseButton;
		var radioGroup = binding.radioGroupTruthOrFalse;
		var textView = binding.textViewQuestion;

		var task = getItem(position);
		var checked = task.getChecked();

		if (checked != null) {
			if (checked) {
				truthButton.setChecked(true);
			} else {
				falseButton.setChecked(true);
			}
		}

		textView.setText(task.getContent());

		radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
			task.setChecked(checkedId == R.id.truth_button);
			updateAllBasedOn(parent, stateLiveData.getValue());
		});

		updateBasedOn(truthButton, falseButton, position, stateLiveData.getValue());
		stateLiveData.observe(lifecycleOwner, state -> updateBasedOn(truthButton, falseButton, position, state));

		return binding.getRoot();
	}

	private boolean alreadyFinished() {
		var alreadyFinished = exercise.getTasks().stream().filter(answer -> answer.getChecked() != null).count();
		return alreadyFinished == getCount();
	}

	private void updateAllBasedOn(ViewGroup parent, State state) {
		for (int i = 0; i < getCount(); i++) {
			var child = parent.getChildAt(i);
			var truthButton = (RadioButton) child.findViewById(R.id.truth_button);
			var falseButton = (RadioButton) child.findViewById(R.id.false_button);
			updateBasedOn(truthButton, falseButton, i, state);
		}
	}

	private void updateBasedOn(RadioButton truthButton, RadioButton falseButton, int position, State state) {
		if (state == State.STUDY_ANSWERS || (state == State.STUDY && alreadyFinished())) {
			revealAnswer(truthButton, falseButton, position);
		}
	}

	private void revealAnswer(RadioButton truthButton, RadioButton falseButton, int position) {
		var task = getItem(position);

		if (truthButton.isChecked() && task.isCorrect()) {
			truthButton.setBackgroundResource(R.drawable.selectable_answer_green);
		} else if (falseButton.isChecked() && !task.isCorrect()) {
			falseButton.setBackgroundResource(R.drawable.selectable_answer_green);
		} else if (!truthButton.isChecked() && !falseButton.isChecked()) {
			if (task.isCorrect()) {
				truthButton.setBackgroundResource(R.drawable.selectable_answer_yellow);
			} else {
				falseButton.setBackgroundResource(R.drawable.selectable_answer_yellow);
			}
		} else {
			if (task.isCorrect()) {
				truthButton.setBackgroundResource(R.drawable.selectable_answer_yellow);
				falseButton.setBackgroundResource(R.drawable.selectable_answer_red);
			} else {
				truthButton.setBackgroundResource(R.drawable.selectable_answer_red);
				falseButton.setBackgroundResource(R.drawable.selectable_answer_yellow);
			}
		}

		truthButton.setClickable(false);
		falseButton.setClickable(false);
	}
}
