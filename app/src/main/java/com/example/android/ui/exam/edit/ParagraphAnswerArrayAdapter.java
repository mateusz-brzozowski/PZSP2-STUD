package com.example.android.ui.exam.edit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.android.data.model.ParagraphAnswer;
import com.example.android.databinding.ViewParagraphAnswerBinding;
import com.example.android.util.TextChangedHandler;
import java.util.List;

public class ParagraphAnswerArrayAdapter extends ArrayAdapter<ParagraphAnswer> {

	private final List<ParagraphAnswer> paragraphAnswers;

	public ParagraphAnswerArrayAdapter(@NonNull Context context, List<ParagraphAnswer> paragraphAnswers) {
		super(context, 0, paragraphAnswers);
		this.paragraphAnswers = paragraphAnswers;
	}

	public List<ParagraphAnswer> getParagraphAnswers() {
		return paragraphAnswers;
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		if (convertView != null) {
			return convertView;
		}
		@SuppressLint("ViewHolder")
		var binding = ViewParagraphAnswerBinding.inflate(LayoutInflater.from(getContext()), parent, false);

		var answer = getItem(position);
		binding.editTextStart.setText(answer.getStart());
		binding.editTextEnd.setText(answer.getEnd());
		binding.editTextCorrectAnswer.setText(answer.getAnswer());
		binding.editTextOtherAnswers.setText(answer.getPossibleAnswers());

		binding.editTextStart.addTextChangedListener(TextChangedHandler.prepareTextWatcher(value -> getItem(position).setStart(value)));
		binding.editTextEnd.addTextChangedListener(TextChangedHandler.prepareTextWatcher(value -> getItem(position).setEnd(value)));
		binding.editTextCorrectAnswer.addTextChangedListener(TextChangedHandler.prepareTextWatcher(value -> getItem(position).setAnswer(value)));
		binding.editTextOtherAnswers.addTextChangedListener(
				TextChangedHandler.prepareTextWatcher(value -> getItem(position).setPossibleAnswers(value))
		);

		return binding.getRoot();
	}
}
