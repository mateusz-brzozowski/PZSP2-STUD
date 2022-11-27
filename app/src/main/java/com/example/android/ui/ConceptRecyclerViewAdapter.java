package com.example.android.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android.R;
import com.example.model.Concept;
import com.example.model.Paragraph;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConceptRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	private static final int CONCEPT = 1;
	private static final int PARAGRAPH = 2;
	private final Concept concept;


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
		}
		throw new UnsupportedOperationException("Unsupported type exception");
	}

	@Override
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
		if (holder instanceof ConceptViewHolder) {
			if (concept != null) {
				((ConceptViewHolder) holder).textConceptName.setText(concept.getKeyPhrase());
				((ConceptViewHolder) holder).textConceptContent.setText(concept.getSummary());
			}
		} else if (holder instanceof ParagraphViewHolder) {
			Paragraph paragraph = concept.getParagraphs().get(position - 1);
			if (paragraph != null) {
				((ParagraphViewHolder) holder).textParagraphName.setText(paragraph.getHeader());
				((ParagraphViewHolder) holder).textParagraphContent.setText(paragraph.getDescription());
			}
		}
	}

	@Override
	public int getItemCount() {
		return 1 + concept.getParagraphs().size();
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return CONCEPT;
		}
		return PARAGRAPH;
	}

	public static class ConceptViewHolder extends RecyclerView.ViewHolder {
		public final TextView textConceptName;
		public final TextView textConceptContent;

		public ConceptViewHolder(@NonNull View itemView) {
			super(itemView);

			this.textConceptName = itemView.findViewById(R.id.concept_name);
			this.textConceptContent = itemView.findViewById(R.id.concept_content);
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
}
