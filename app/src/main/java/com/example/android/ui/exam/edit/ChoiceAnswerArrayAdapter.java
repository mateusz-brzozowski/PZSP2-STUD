package com.example.android.ui.exam.edit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.android.data.model.PossibleAnswer;
import com.example.android.databinding.ViewCheckboxAnswerBinding;
import com.example.android.util.TextChangedHandler;
import java.util.List;

public class ChoiceAnswerArrayAdapter extends ArrayAdapter<PossibleAnswer> {

	private final List<PossibleAnswer> possibleAnswers;

	public ChoiceAnswerArrayAdapter(@NonNull Context context, List<PossibleAnswer> answers) {
		super(context, 0, answers);
		this.possibleAnswers = answers;
	}

	public List<PossibleAnswer> getPossibleAnswers() {
		return possibleAnswers;
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		if (convertView != null) {
			return convertView;
		}
		@SuppressLint("ViewHolder")
		var binding = ViewCheckboxAnswerBinding.inflate(LayoutInflater.from(getContext()), parent, false);

		binding.editTextAnswer.addTextChangedListener(TextChangedHandler.prepareTextWatcher(value -> getItem(position).setAnswer(value)));
		binding.checkboxCorrect.setOnCheckedChangeListener((buttonView, isChecked) -> getItem(position).setCorrect(isChecked));

		return binding.getRoot();
	}
}
