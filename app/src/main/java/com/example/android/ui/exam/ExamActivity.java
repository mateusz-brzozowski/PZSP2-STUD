package com.example.android.ui.exam;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.example.android.databinding.ActivityExamBinding;
import com.example.android.ui.section.SectionActivity;
import com.example.model.exam.Exercise;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExamActivity extends AppCompatActivity {

	public static final String ARG_MODE = "mode";
	private static final int EXAM_TIME = 60_000;

	private int sectionId;
	private State mode;

	private ProgressBar timeProgressBar;
	private TextView time;
	private TextView examProgressTextView;
	private ProgressBar examProgressBar;
	private TextView backTextView;
	private TextView nextTextView;
	private ViewPager2 viewPager;

	private CountDownTimer timer;

	private ExercisesViewModel viewModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		var binding = ActivityExamBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		this.timeProgressBar = binding.examTimeProgressBar;
		this.time = binding.examTimeTextView;
		this.examProgressTextView = binding.examQuestionTextView;
		this.examProgressBar = binding.examProgressBar;
		this.backTextView = binding.examBackTextView;
		this.nextTextView = binding.examNextTextView;
		this.viewPager = binding.examViewPager;

		setStateBasedOnIntent();
		setupViewPager();
		registerViewModel();
		setTimer();
	}

	private void setStateBasedOnIntent() {
		var intent = getIntent();
		mode = intent.getIntExtra(ARG_MODE, 0) == State.EXAM.getValue() ? State.EXAM : State.STUDY;
		sectionId = intent.getIntExtra(SectionActivity.ARG_SECTION_ID, 0);
	}

	private void setupViewPager() {
		viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
			@Override
			public void onPageSelected(int position) {
				viewModel.setExerciseNumber(position);
			}
		});

		this.viewPager.setPageTransformer(new ZoomOutPageTransformer());
	}

	private void registerViewModel() {
		this.viewModel = new ViewModelProvider(this).get(ExercisesViewModel.class);

		viewModel.getExercisesLiveData().observe(this, this::setExercises);
		viewModel.getCurrentExerciseNumberLiveData().observe(this, this::setCurrentQuestion);

		viewModel.populateExercises(sectionId);
		viewModel.setState(mode);

		backTextView.setOnClickListener(v -> viewModel.previousExercise());
		nextTextView.setOnClickListener(v -> viewModel.nextExercise());

		viewModel.getStateLiveData().observe(this, state -> {
			if (state == State.STUDY_ANSWERS) {
				timer.cancel();
				new SummaryListDialogFragment().show(getSupportFragmentManager(), "dialog");
			}
		});
	}

	@SuppressLint("DefaultLocale")
	private void setCurrentQuestion(int currentQuestion) {
		examProgressTextView.setText(String.format("%d/%d", currentQuestion + 1, viewModel.getExercisesSize()));
		viewPager.setCurrentItem(currentQuestion);
	}

	@SuppressLint("DefaultLocale")
	private void setExercises(List<Exercise> exercises) {
		examProgressTextView.setText(String.format("%d/%d", viewModel.getCurrentExerciseNumber() + 1, exercises.size()));
		FragmentStateAdapter pagerAdapter = new ExerciseSlidePagerAdapter(this, exercises);
		viewPager.setAdapter(pagerAdapter);
		examProgressBar.setVisibility(View.GONE);
	}

	private void setTimer() {
		timer = new CountDownTimer(EXAM_TIME, 1000) {
			@SuppressLint("SimpleDateFormat")
			public void onTick(long millisUntilFinished) {
				time.setText(new SimpleDateFormat("mm:ss").format(new Date(millisUntilFinished)));
				timeProgressBar.setProgress((int) (millisUntilFinished * 100 / EXAM_TIME), false);
			}

			@Override
			public void onFinish() {
				if (viewModel.getState() == State.EXAM) {
					viewModel.setState(State.STUDY_ANSWERS);
				}
			}
		};
		timer.start();
	}
}