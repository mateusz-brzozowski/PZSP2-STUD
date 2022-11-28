package com.example.android.ui;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.R;
import com.example.model.Concept;
import com.example.model.Paragraph;

public class ConceptRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	private static final int CONCEPT = 1;
	private static final int PARAGRAPH = 2;
	private static final int CONCEPT_EDITABLE = 3;
	private static final int PARAGRAPH_EDITABLE = 4;
	private static final int ADD_BUTTON = 5;
	private boolean editable = false;
	private Concept concept;

	public ConceptRecyclerViewAdapter() {
		this.concept = new Concept();
	}

	public ConceptRecyclerViewAdapter(Concept concept) {
		this.concept = concept;
	}

	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		if (viewType == CONCEPT) {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_concept, parent, false);
			return new ConceptViewHolder(view);
		} else if (viewType == PARAGRAPH) {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_paragraph, parent, false);
			return new ParagraphViewHolder(view);
		} else if (viewType == CONCEPT_EDITABLE) {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_concept_edit, parent, false);
			return new ConceptEditableViewHolder(view);
		} else if (viewType == PARAGRAPH_EDITABLE) {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_paragraph_edit, parent, false);
			return new ParagraphEditableViewHolder(view);
		} else if (viewType == ADD_BUTTON) {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_paragraph_new, parent, false);
			return new AddButtonViewHolder(view);
		}
		throw new UnsupportedOperationException("Unsupported type exception");
	}

	@SuppressLint("NotifyDataSetChanged")
	@Override
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
		if (holder instanceof ConceptViewHolder) {
			if (concept != null) {
				((ConceptViewHolder) holder).textConceptName.setText(concept.getKeyPhrase());
				((ConceptViewHolder) holder).textConceptContent.setText(concept.getSummary());
				((ConceptViewHolder) holder).buttonEditConcept.setOnClickListener((View v) -> changeDisplayMode());
			}
		} else if (holder instanceof ParagraphViewHolder) {
			Paragraph paragraph = concept.getParagraphs().get(position - 1);
			if (paragraph != null) {
				((ParagraphViewHolder) holder).textParagraphName.setText(paragraph.getHeader());
				((ParagraphViewHolder) holder).textParagraphContent.setText(paragraph.getDescription());
			}
		} else if (holder instanceof ConceptEditableViewHolder) {
			if (concept != null) {
				((ConceptEditableViewHolder) holder).editTextConceptName.setText(concept.getKeyPhrase());
				((ConceptEditableViewHolder) holder).editTextConceptContent.setText(concept.getSummary());
				((ConceptEditableViewHolder) holder).buttonConceptClose.setOnClickListener((View v) -> changeDisplayMode());
				((ConceptEditableViewHolder) holder).buttonConceptAccept.setOnClickListener((View v) -> {
					concept.setKeyPhrase(((ConceptEditableViewHolder) holder).editTextConceptName.getText().toString());
					concept.setSummary(((ConceptEditableViewHolder) holder).editTextConceptContent.getText().toString());
					changeDisplayMode();
				});
			}
		} else if (holder instanceof ParagraphEditableViewHolder) {
			Paragraph paragraph = concept.getParagraphs().get(position - 1);
			if (paragraph != null) {
				((ParagraphEditableViewHolder) holder).editTextParagraphName.setText(paragraph.getHeader());
				((ParagraphEditableViewHolder) holder).editTextParagraphContent.setText(paragraph.getDescription());
				((ParagraphEditableViewHolder) holder).editTextParagraphName.addTextChangedListener(new TextWatcher() {
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					}

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						if(s.length() != 0){
							paragraph.setHeader(s.toString());
						}
					}

					@Override
					public void afterTextChanged(Editable s) {
					}
				});
				((ParagraphEditableViewHolder) holder).editTextParagraphContent.addTextChangedListener(new TextWatcher() {
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					}

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						if(s.length() != 0){
							paragraph.setDescription(s.toString());
						}
					}

					@Override
					public void afterTextChanged(Editable s) {
					}
				});
				((ParagraphEditableViewHolder) holder).buttonParagraphDelete.setOnClickListener((View v) -> {
					concept.getParagraphs().remove(paragraph);
					notifyDataSetChanged();
				});
			}
		} else if (holder instanceof AddButtonViewHolder) {
			((AddButtonViewHolder) holder).buttonAddParagraph.setOnClickListener((View v) -> {
				concept.getParagraphs().add(new Paragraph());
				notifyDataSetChanged();
			});
		}
	}

	@Override
	public int getItemCount() {
		var button = editable ? 1 : 0;
		return 1 + concept.getParagraphs().size() + button;
	}

	@Override
	public int getItemViewType(int position) {
		if (editable) {
			if (position == 0) {
				return CONCEPT_EDITABLE;
			} else if (position == getItemCount() - 1) {
				return ADD_BUTTON;
			}
			return PARAGRAPH_EDITABLE;
		} else {
			if (position == 0) {
				return CONCEPT;
			}
			return PARAGRAPH;
		}
	}

	public void setConcept(Concept concept) {
		this.concept = concept;
	}

	public static class ConceptViewHolder extends RecyclerView.ViewHolder {
		public final TextView textConceptName;
		public final TextView textConceptContent;
		public final Button buttonEditConcept;

		public ConceptViewHolder(@NonNull View itemView) {
			super(itemView);

			this.textConceptName = itemView.findViewById(R.id.concept_name);
			this.textConceptContent = itemView.findViewById(R.id.concept_content);
			this.buttonEditConcept = itemView.findViewById(R.id.concept_edit_button);
		}
	}

	public static class ParagraphViewHolder extends RecyclerView.ViewHolder {
		public final TextView textParagraphName;
		public final TextView textParagraphContent;
		public final LinearLayout linearLayout;

		public ParagraphViewHolder(@NonNull View itemView) {
			super(itemView);

			this.textParagraphName = itemView.findViewById(R.id.paragraph_name);
			this.textParagraphContent = itemView.findViewById(R.id.paragraph_content);
			this.linearLayout = itemView.findViewById(R.id.paragraph_linear_layout);
		}
	}

	public static class ConceptEditableViewHolder extends RecyclerView.ViewHolder {
		public final EditText editTextConceptName;
		public final EditText editTextConceptContent;
		public final Button buttonConceptAccept;
		public final Button buttonConceptClose;

		public ConceptEditableViewHolder(@NonNull View itemView) {
			super(itemView);

			this.editTextConceptName = itemView.findViewById(R.id.concept_edit_name);
			this.editTextConceptContent = itemView.findViewById(R.id.concept_edit_content);
			this.buttonConceptAccept = itemView.findViewById(R.id.concept_edit_accept_button);
			this.buttonConceptClose = itemView.findViewById(R.id.concept_edit_close_button);
		}
	}

	public static class ParagraphEditableViewHolder extends RecyclerView.ViewHolder {
		public final EditText editTextParagraphName;
		public final EditText editTextParagraphContent;
		public final Button buttonParagraphDelete;

		public ParagraphEditableViewHolder(@NonNull View itemView) {
			super(itemView);

			this.editTextParagraphName = itemView.findViewById(R.id.paragraph_edit_name);
			this.editTextParagraphContent = itemView.findViewById(R.id.paragraph_edit_content);
			this.buttonParagraphDelete = itemView.findViewById(R.id.paragraph_edit_delete_button);
		}
	}

	public static class AddButtonViewHolder extends RecyclerView.ViewHolder {
		public final Button buttonAddParagraph;
		public AddButtonViewHolder(@NonNull View itemView) {
			super(itemView);

			this.buttonAddParagraph = itemView.findViewById(R.id.paragraph_new_button);
		}
	}

	@SuppressLint("NotifyDataSetChanged")
	public void changeDisplayMode() {
		editable = !editable;
		notifyDataSetChanged();
	}
}
