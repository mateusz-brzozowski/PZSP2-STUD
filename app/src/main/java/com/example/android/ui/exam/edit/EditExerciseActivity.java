package com.example.android.ui.exam.edit;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.android.R;
import com.example.android.data.model.ParagraphAnswer;
import com.example.android.data.model.PossibleAnswer;
import com.example.android.databinding.ActivityEditExerciseBinding;
import com.example.android.ui.section.SectionActivity;
import com.example.android.util.UiUtility;
import com.example.model.exam.Choice;
import com.example.model.exam.Exercise;
import com.example.model.exam.FillBlanks;
import com.example.model.exam.FlashCard;
import com.example.model.exam.MultipleChoice;
import com.example.model.exam.MultipleTruthOrFalse;
import com.example.model.exam.SelectFromList;
import com.example.model.exam.TruthOrFalse;
import com.example.model.exam.answer.BlankAnswer;
import com.example.model.exam.answer.ChoiceAnswer;
import com.example.model.exam.answer.ListAnswer;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EditExerciseActivity extends AppCompatActivity {

	private final Map<InputType, Runnable> spinnerHandler = Map.of(
			InputType.SIMPLE, this::initFlashCard,
			InputType.TRUTH_OR_FALSE, this::initTruthOrFalse,
			InputType.CHOICE, this::initChoiceAnswer,
			InputType.PARAGRAPH, this::initParagraphAnswer
	);

	private final Map<Class<? extends Exercise>, Function<String, Exercise>> exerciseMappers = Map.of(
			FlashCard.class, this::toFlashCard,
			FillBlanks.class, this::toFillBlanks,
			SelectFromList.class, this::toSelectFromList,
			Choice.class, this::toChoice,
			MultipleChoice.class, this::toMultipleChoice,
			MultipleTruthOrFalse.class, this::toMultipleTruthOrFalse,
			TruthOrFalse.class, this::toTruthOrFalse
	);

	private Map<String, Class<? extends Exercise>> exercises;

	private ExerciseViewModel viewModel;

	private Spinner spinnerExerciseType;
	private EditText editTextQuestion;

	private Button buttonAdd;
	private Button buttonDone;

	private EditText editTextFlashCard;
	private CheckBox checkBoxTruthOrFalse;
	private ListView listViewAnswers;

	private ChoiceAnswerArrayAdapter choiceAdapter;
	private ParagraphAnswerArrayAdapter paragraphAdapter;

	private int sectionId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		var binding = ActivityEditExerciseBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		this.spinnerExerciseType = binding.spinnerPossibleExercises;
		this.editTextQuestion = binding.editTextQuestion;
		this.buttonAdd = binding.buttonAdd;
		this.buttonDone = binding.buttonDone;
		this.editTextFlashCard = binding.editTextFlashCard;
		this.checkBoxTruthOrFalse = binding.checkboxTruthOrFalse;
		this.listViewAnswers = binding.listViewAnswers;

		sectionId = getIntent().getIntExtra(SectionActivity.ARG_SECTION_ID, 0);
		setupExercises();
		setupViewModel();
		setupSpinner();
		setupDoneButton();
	}

	private void setupExercises() {
		exercises = Map.of(
				getString(R.string.flash_card), FlashCard.class,
				getString(R.string.fill_blanks), FillBlanks.class,
				getString(R.string.select_from_list), SelectFromList.class,
				getString(R.string.single_choice), Choice.class,
				getString(R.string.multiple_choice), MultipleChoice.class,
				getString(R.string.truth_or_false), TruthOrFalse.class,
				getString(R.string.multiple_truth_or_false), MultipleTruthOrFalse.class
		);
	}

	private void setupViewModel() {
		this.viewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);
		viewModel.getSentLiveData().observe(this, sent -> finish());
	}

	private void setupSpinner() {
		var exerciseNames = new ArrayList<>(exercises.keySet());
		var adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, exerciseNames);
		spinnerExerciseType.setAdapter(adapter);
		spinnerExerciseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				var chosen = exercises.get(exerciseNames.get(position));
				EnumSet.allOf(InputType.class)
						.stream()
						.filter(inputType -> inputType.getClasses().contains(chosen))
						.findFirst()
						.map(spinnerHandler::get)
						.ifPresent(Runnable::run);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// Do nothing
			}
		});
	}

	private void initFlashCard() {
		editTextFlashCard.setVisibility(View.VISIBLE);
		checkBoxTruthOrFalse.setVisibility(View.GONE);
		listViewAnswers.setVisibility(View.GONE);
		buttonAdd.setVisibility(View.GONE);
	}

	private void initTruthOrFalse() {
		editTextFlashCard.setVisibility(View.GONE);
		checkBoxTruthOrFalse.setVisibility(View.VISIBLE);
		listViewAnswers.setVisibility(View.GONE);
		buttonAdd.setVisibility(View.GONE);
	}

	private void initChoiceAnswer() {
		editTextFlashCard.setVisibility(View.GONE);
		checkBoxTruthOrFalse.setVisibility(View.GONE);
		listViewAnswers.setVisibility(View.VISIBLE);
		buttonAdd.setVisibility(View.VISIBLE);

		var answers = new ArrayList<PossibleAnswer>();
		answers.add(new PossibleAnswer());
		choiceAdapter = new ChoiceAnswerArrayAdapter(this, answers);
		listViewAnswers.setAdapter(choiceAdapter);

		buttonAdd.setOnClickListener(v -> {
			choiceAdapter.add(new PossibleAnswer());
			UiUtility.setListViewHeightBasedOnChildren(listViewAnswers);
		});
	}

	private void initParagraphAnswer() {
		editTextFlashCard.setVisibility(View.GONE);
		checkBoxTruthOrFalse.setVisibility(View.GONE);
		listViewAnswers.setVisibility(View.VISIBLE);
		buttonAdd.setVisibility(View.VISIBLE);

		var answers = new ArrayList<ParagraphAnswer>();
		answers.add(new ParagraphAnswer());
		paragraphAdapter = new ParagraphAnswerArrayAdapter(this, answers);
		listViewAnswers.setAdapter(paragraphAdapter);

		buttonAdd.setOnClickListener(v -> {
			paragraphAdapter.add(new ParagraphAnswer());
			UiUtility.setListViewHeightBasedOnChildren(listViewAnswers);
		});
	}

	private void setupDoneButton() {
		buttonDone.setOnClickListener(v -> {
			var chosen = exercises.get((String) spinnerExerciseType.getSelectedItem());
			var question = editTextQuestion.getText().toString();
			var mapper = exerciseMappers.get(chosen);
			if (mapper != null) {
				var exercise = mapper.apply(question);
				viewModel.insertExercise(sectionId, exercise);
			}
		});
	}

	private FlashCard toFlashCard(String question) {
		var answer = editTextFlashCard.getText().toString();
		return FlashCard.builder().question(question).answer(answer).build();
	}

	private FillBlanks toFillBlanks(String question) {
		var answers = paragraphAdapter.getParagraphAnswers()
				.stream()
				.map(paragraph ->
						BlankAnswer.builder()
								.start(paragraph.getStart())
								.answer(paragraph.getAnswer())
								.end(paragraph.getEnd())
								.build()
				)
				.collect(Collectors.toList());
		return FillBlanks.builder().question(question).answers(answers).build();
	}

	private SelectFromList toSelectFromList(String question) {
		var answers = paragraphAdapter.getParagraphAnswers()
				.stream()
				.map(paragraph ->
						ListAnswer.builder()
								.start(paragraph.getStart())
								.correctAnswer(paragraph.getAnswer())
								.end(paragraph.getEnd())
								.possibleAnswers(List.of(paragraph.getPossibleAnswers().split(",")))
								.build()
				)
				.collect(Collectors.toList());
		return SelectFromList.builder().question(question).answers(answers).build();
	}

	private Choice toChoice(String question) {
		var answers = choiceAdapter.getPossibleAnswers()
				.stream()
				.map(choice ->
						ChoiceAnswer.builder()
								.correct(choice.isCorrect())
								.content(choice.getAnswer())
								.build()
				)
				.collect(Collectors.toList());
		var possibleAnswers = answers.stream().map(ChoiceAnswer::getContent).collect(Collectors.toList());
		var correct = answers.stream().filter(ChoiceAnswer::isCorrect).findFirst().map(ChoiceAnswer::getContent).orElse(null);
		return Choice.builder().question(question).possibleAnswers(possibleAnswers).correctAnswer(correct).build();
	}

	private MultipleChoice toMultipleChoice(String question) {
		var answers = choiceAdapter.getPossibleAnswers()
				.stream()
				.map(choice ->
						ChoiceAnswer.builder()
								.correct(choice.isCorrect())
								.content(choice.getAnswer())
								.build()
				)
				.collect(Collectors.toList());
		return MultipleChoice.builder().question(question).answers(answers).build();
	}

	private MultipleTruthOrFalse toMultipleTruthOrFalse(String question) {
		var answers = choiceAdapter.getPossibleAnswers()
				.stream()
				.map(choice ->
						ChoiceAnswer.builder()
								.correct(choice.isCorrect())
								.content(choice.getAnswer())
								.build()
				)
				.collect(Collectors.toList());
		return MultipleTruthOrFalse.builder().question(question).tasks(answers).build();
	}

	private TruthOrFalse toTruthOrFalse(String question) {
		return TruthOrFalse.builder().question(question).correct(checkBoxTruthOrFalse.isChecked()).build();
	}
}