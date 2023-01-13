package com.example.android.ui.concept;

import android.annotation.SuppressLint;
import android.text.method.LinkMovementMethod;
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
import com.example.android.util.TextChangedHandler;
import com.example.model.Concept;
import com.example.model.Paragraph;
import com.google.gson.Gson;
import java.util.function.Consumer;

public class ConceptRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	private boolean editable = false;
	private Concept concept;
	private Concept conceptEditable;

	private final Consumer<Concept> onSuccess;

	// TODO: This recycler view should be divided into two separate activities / but there is no time left :(
	public ConceptRecyclerViewAdapter(Consumer<Concept> onSuccess) {
		this.concept = new Concept();
		this.onSuccess = onSuccess;
	}

	private Concept copyConcept(Concept concept) {
		var gson = new Gson();
		var json = gson.toJson(concept);
		return gson.fromJson(json, Concept.class);
	}

	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		var elementType = ElementType.of(viewType);
		var view = LayoutInflater.from(parent.getContext()).inflate(elementType.getResource(), parent, false);
		return elementType.getConstructor().apply(view);
	}

	@SuppressLint("NotifyDataSetChanged")
	@Override
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
		if (holder instanceof ConceptViewHolder conceptViewHolder) {
			if (concept != null) {
				bindConceptViewHolder(conceptViewHolder);
			}
		} else if (holder instanceof ParagraphViewHolder paragraphViewHolder) {
			bindParagraphViewHolder(position, paragraphViewHolder);
		} else if (holder instanceof ConceptEditableViewHolder conceptEditableViewHolder) {
			if (conceptEditable != null) {
				bindConceptEditableViewHolder((ConceptEditableViewHolder) holder, conceptEditableViewHolder);
			}
		} else if (holder instanceof ParagraphEditableViewHolder paragraphEditableViewHolder) {
			bindParagraphEditableViewHolder(position, paragraphEditableViewHolder);
		} else if (holder instanceof AddButtonViewHolder addButtonViewHolder) {
			addButtonViewHolder.buttonAddParagraph.setOnClickListener((View v) -> {
				conceptEditable.getParagraphs().add(new Paragraph());
				notifyDataSetChanged();
			});
		}
	}

	@SuppressLint("NotifyDataSetChanged")
	private void bindParagraphEditableViewHolder(int position, ParagraphEditableViewHolder paragraphEditableViewHolder) {
		var paragraphEditable = conceptEditable.getParagraphs().get(position - 1);
		if (paragraphEditable != null) {
			paragraphEditableViewHolder.editTextParagraphName.setText(paragraphEditable.getHeader());
			paragraphEditableViewHolder.editTextParagraphContent.setText(paragraphEditable.getDescription());
			paragraphEditableViewHolder.editTextParagraphName.addTextChangedListener(
					TextChangedHandler.prepareTextWatcher(s -> {
						if (s.length() != 0) {
							paragraphEditable.setHeader(s);
						}
					})
			);
			paragraphEditableViewHolder.editTextParagraphContent.addTextChangedListener(
					TextChangedHandler.prepareTextWatcher(s -> {
						if (s.length() != 0) {
							paragraphEditable.setDescription(s);
						}
					})
			);
			paragraphEditableViewHolder.buttonParagraphDelete.setOnClickListener((View v) -> {
				conceptEditable.getParagraphs().remove(paragraphEditable);
				notifyDataSetChanged();
			});
		}
	}

	private void bindConceptEditableViewHolder(ConceptEditableViewHolder holder, ConceptEditableViewHolder conceptEditableViewHolder) {
		conceptEditableViewHolder.editTextConceptName.setText(conceptEditable.getKeyPhrase());
		conceptEditableViewHolder.editTextConceptContent.setText(conceptEditable.getSummary());
		conceptEditableViewHolder.buttonConceptClose.setOnClickListener((View v) -> {
			conceptEditable = copyConcept(concept);
			changeDisplayMode();
		});
		conceptEditableViewHolder.buttonConceptAccept.setOnClickListener((View v) -> {
			conceptEditable.setKeyPhrase(holder.editTextConceptName.getText().toString());
			conceptEditable.setSummary(holder.editTextConceptContent.getText().toString());

			concept = copyConcept(conceptEditable);
			onSuccess.accept(concept);
			changeDisplayMode();
		});
	}

	private void bindParagraphViewHolder(int position, ParagraphViewHolder paragraphViewHolder) {
		var paragraph = concept.getParagraphs().get(position - 1);
		if (paragraph != null) {
			paragraphViewHolder.textParagraphName.setText(paragraph.getHeader());
			paragraphViewHolder.textParagraphContent.setText(paragraph.getDescription());
		}
	}

	private void bindConceptViewHolder(ConceptViewHolder conceptViewHolder) {
		conceptViewHolder.textConceptName.setText(concept.getKeyPhrase());
		conceptViewHolder.textConceptContent.setText(concept.getSummary());
		conceptViewHolder.buttonEditConcept.setOnClickListener((View v) -> {
			conceptEditable = copyConcept(concept);
			changeDisplayMode();
		});
	}

	@Override
	public int getItemCount() {
		if (editable) {
			return 1 + conceptEditable.getParagraphs().size() + 1;
		} else {
			return 1 + concept.getParagraphs().size();
		}
	}

	@Override
	public int getItemViewType(int position) {
		if (editable) {
			if (position == 0) {
				return ElementType.CONCEPT_EDITABLE.getValue();
			} else if (position == getItemCount() - 1) {
				return ElementType.ADD_BUTTON.getValue();
			}
			return ElementType.PARAGRAPH_EDITABLE.getValue();
		} else {
			if (position == 0) {
				return ElementType.CONCEPT.getValue();
			}
			return ElementType.PARAGRAPH.getValue();
		}
	}

	@SuppressLint("NotifyDataSetChanged")
	public void setConcept(Concept concept) {
		this.concept = concept;
		notifyDataSetChanged();
	}

	public static class ConceptViewHolder extends RecyclerView.ViewHolder {
		public final TextView textConceptName;
		public final TextView textConceptContent;
		public final Button buttonEditConcept;

		public ConceptViewHolder(@NonNull View itemView) {
			super(itemView);

			this.textConceptName = itemView.findViewById(R.id.concept_name);
			this.textConceptContent = itemView.findViewById(R.id.concept_content);
			this.textConceptContent.setMovementMethod(LinkMovementMethod.getInstance());
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
			this.textParagraphContent.setMovementMethod(LinkMovementMethod.getInstance());
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
