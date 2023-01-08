package com.example.android.ui.concept;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android.R;
import java.util.EnumSet;
import java.util.function.Function;

public enum ElementType {
	CONCEPT(1, R.layout.view_concept, ConceptRecyclerViewAdapter.ConceptViewHolder::new),
	PARAGRAPH(2, R.layout.view_paragraph, ConceptRecyclerViewAdapter.ParagraphViewHolder::new),
	CONCEPT_EDITABLE(3, R.layout.view_concept_edit, ConceptRecyclerViewAdapter.ConceptEditableViewHolder::new),
	PARAGRAPH_EDITABLE(4, R.layout.view_paragraph_edit, ConceptRecyclerViewAdapter.ParagraphEditableViewHolder::new),
	ADD_BUTTON(5, R.layout.view_paragraph_new, ConceptRecyclerViewAdapter.AddButtonViewHolder::new),
	;

	ElementType(int value, int resource, Function<View, RecyclerView.ViewHolder> constructor) {
		this.value = value;
		this.resource = resource;
		this.constructor = constructor;
	}

	private final int value;
	private final int resource;
	private final Function<View, RecyclerView.ViewHolder> constructor;

	public int getValue() {
		return value;
	}

	public int getResource() {
		return resource;
	}

	public Function<View, RecyclerView.ViewHolder> getConstructor() {
		return constructor;
	}

	public static ElementType of(int value) {
		return EnumSet.allOf(ElementType.class)
				.stream()
				.filter(element -> element.getValue() == value)
				.findFirst()
				.orElse(null);
	}
}
