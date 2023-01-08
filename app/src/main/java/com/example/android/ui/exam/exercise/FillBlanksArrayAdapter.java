package com.example.android.ui.exam.exercise;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import com.example.android.R;
import com.example.android.databinding.ViewItemFillBlankBinding;
import com.example.android.ui.exam.State;
import com.example.model.exam.FillBlanks;
import com.example.model.exam.answer.BlankAnswer;
import java.util.Optional;

public class FillBlanksArrayAdapter extends ArrayAdapter<BlankAnswer> {

	private FillBlanks exercise;
	private final LifecycleOwner lifecycleOwner;
	private final LiveData<State> stateLiveData;

	public FillBlanksArrayAdapter(@NonNull Context context, FillBlanks exercise, LifecycleOwner lifecycleOwner, LiveData<State> stateLiveData) {
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
		var binding = ViewItemFillBlankBinding.inflate(LayoutInflater.from(getContext()), parent, false);
		var editText = binding.editTextAnswer;
		var textStart = binding.textViewStartParagraph;
		var textEnd = binding.textViewEndParagraph;

		var answer = getItem(position);

		textStart.setText(answer.getStart());
		textEnd.setText(answer.getEnd());
		editText.setText(answer.getEntered());

		editText.setOnFocusChangeListener((v, hasFocus) -> {
			if (!hasFocus) {
				var text = editText.getText().toString();
				answer.setEntered(text);
				updateAllBasedOn(parent, stateLiveData.getValue());
			}
		});

		stateLiveData.observe(lifecycleOwner, state -> updateBasedOn(editText, position, state));
		updateBasedOn(editText, position, stateLiveData.getValue());

		return binding.getRoot();
	}

	private boolean alreadyFinished() {
		var alreadyFinished = exercise.getAnswers().stream().filter(answer -> answer.getEntered() != null).count();
		return alreadyFinished == getCount();
	}

	private void updateAllBasedOn(ViewGroup parent, State state) {
		for (int i = 0; i < getCount(); i++) {
			var child = parent.getChildAt(i);
			var editText = (EditText) child.findViewById(R.id.edit_text_answer);
			updateBasedOn(editText, i, state);
		}
	}

	private void updateBasedOn(EditText editText, int position, State state) {
		if (state == State.STUDY_ANSWERS || (state == State.STUDY && alreadyFinished())) {
			revealAnswer(editText, position);
		}
	}

	private void revealAnswer(EditText editText, int position) {
		var answer = getItem(position);
		var entered = Optional.ofNullable(answer.getEntered()).map(str -> str.strip()).orElse(null);

		if (answer.getAnswer().equalsIgnoreCase(entered)) {
			editText.setBackgroundResource(R.drawable.selectable_answer_green);
		} else {
			editText.setBackgroundResource(R.drawable.selectable_answer_red);
			if (entered == null) {
				editText.setText(answer.getAnswer());
			} else {
				editText.setText(String.format("%s / %s", entered, answer.getAnswer()));
			}
		}

		editText.setClickable(false);
		editText.setFocusable(false);
	}
}
