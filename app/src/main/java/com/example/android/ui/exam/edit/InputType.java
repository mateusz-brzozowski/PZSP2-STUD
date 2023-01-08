package com.example.android.ui.exam.edit;

import com.example.model.exam.Choice;
import com.example.model.exam.Exercise;
import com.example.model.exam.FillBlanks;
import com.example.model.exam.FlashCard;
import com.example.model.exam.MultipleChoice;
import com.example.model.exam.MultipleTruthOrFalse;
import com.example.model.exam.SelectFromList;
import com.example.model.exam.TruthOrFalse;
import java.util.List;

public enum InputType {

	SIMPLE(List.of(FlashCard.class)),
	TRUTH_OR_FALSE(List.of(TruthOrFalse.class)),
	CHOICE(List.of(Choice.class, MultipleChoice.class, MultipleTruthOrFalse.class)),
	PARAGRAPH(List.of(FillBlanks.class, SelectFromList.class));

	private final List<Class<? extends Exercise>> classes;

	InputType(List<Class<? extends Exercise>> classes) {
		this.classes = classes;
	}

	public List<Class<? extends Exercise>> getClasses() {
		return classes;
	}
}
