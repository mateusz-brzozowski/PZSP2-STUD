package com.example.android.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.example.android.web.ApiClient;
import com.example.model.Concept;
import com.example.model.Paragraph;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConceptRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	private static final int CONCEPT = 1;
	private static final int PARAGRAPH = 2;
	private static final int CONCEPT_EDITABLE = 3;
	private static final int PARAGRAPH_EDITABLE = 4;
	private static final int ADD_BUTTON = 5;
	private boolean editable = false;
	private Concept concept;
	private Concept conceptEditable;
	private Context context;

	public ConceptRecyclerViewAdapter(Context context) {
		this.concept = new Concept();
		this.context = context;
	}

	public ConceptRecyclerViewAdapter(Concept concept) {
		this.concept = concept;
		this.conceptEditable = copyConcept(concept);
	}

	private Concept copyConcept(Concept concept) {
		var gson = new Gson();
		var json = gson.toJson(concept);
		return gson.fromJson(json, Concept.class);
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
				((ConceptViewHolder) holder).buttonEditConcept.setOnClickListener((View v) -> {
					conceptEditable = copyConcept(concept);
					changeDisplayMode();
				});
			}
		} else if (holder instanceof ParagraphViewHolder) {
			Paragraph paragraph = concept.getParagraphs().get(position - 1);
			if (paragraph != null) {
				((ParagraphViewHolder) holder).textParagraphName.setText(paragraph.getHeader());
				((ParagraphViewHolder) holder).textParagraphContent.setText(paragraph.getDescription());
			}
		} else if (holder instanceof ConceptEditableViewHolder) {
			if (conceptEditable != null) {
				((ConceptEditableViewHolder) holder).editTextConceptName.setText(conceptEditable.getKeyPhrase());
				((ConceptEditableViewHolder) holder).editTextConceptContent.setText(conceptEditable.getSummary());
				((ConceptEditableViewHolder) holder).buttonConceptClose.setOnClickListener((View v) -> {
					conceptEditable = copyConcept(concept);
					changeDisplayMode();
				});
				((ConceptEditableViewHolder) holder).buttonConceptAccept.setOnClickListener((View v) -> {
					conceptEditable.setKeyPhrase(((ConceptEditableViewHolder) holder).editTextConceptName.getText().toString());
					conceptEditable.setSummary(((ConceptEditableViewHolder) holder).editTextConceptContent.getText().toString());

					concept = copyConcept(conceptEditable);
					pushConcept();
					changeDisplayMode();
				});
			}
		} else if (holder instanceof ParagraphEditableViewHolder) {
			Paragraph paragraphEditable = conceptEditable.getParagraphs().get(position - 1);
			if (paragraphEditable != null) {
				((ParagraphEditableViewHolder) holder).editTextParagraphName.setText(paragraphEditable.getHeader());
				((ParagraphEditableViewHolder) holder).editTextParagraphContent.setText(paragraphEditable.getDescription());
				((ParagraphEditableViewHolder) holder).editTextParagraphName.addTextChangedListener(new TextWatcher() {
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					}

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						if (s.length() != 0) {
							paragraphEditable.setHeader(s.toString());
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
							paragraphEditable.setDescription(s.toString());
						}
					}

					@Override
					public void afterTextChanged(Editable s) {
					}
				});
				((ParagraphEditableViewHolder) holder).buttonParagraphDelete.setOnClickListener((View v) -> {
					conceptEditable.getParagraphs().remove(paragraphEditable);
					notifyDataSetChanged();
				});
			}
		} else if (holder instanceof AddButtonViewHolder) {
			((AddButtonViewHolder) holder).buttonAddParagraph.setOnClickListener((View v) -> {
				conceptEditable.getParagraphs().add(new Paragraph());
				notifyDataSetChanged();
			});
		}
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

	private void pushConcept() {
		var apiClient = ApiClient.getInstance();
		concept.setId(0);
		int i = 1;
		for (var paragraph : concept.getParagraphs()) {
			paragraph.setNumber(i);
			i = i + 1;
		}
		Call<Concept> call = apiClient.saveConcept(1, concept);
		call.enqueue(new Callback<>() {
			@SuppressLint("NotifyDataSetChanged")
			@Override
			public void onResponse(@NonNull Call<Concept> call, @NonNull Response<Concept> response) {
				if (response.isSuccessful() && response.body() != null) {
					concept = response.body();
					var settings = context.getSharedPreferences("Android", Activity.MODE_PRIVATE);
					SharedPreferences.Editor editor = settings.edit();
					editor.putInt("concept", concept.getId());
					editor.apply();
				} else {
					Log.e("Concept", "Response concept failure");
				}
			}

			@Override
			public void onFailure(@NonNull Call<Concept> call, @NonNull Throwable t) {
				Log.e("Concept", "Load concept failure");
			}
		});
	}
}
