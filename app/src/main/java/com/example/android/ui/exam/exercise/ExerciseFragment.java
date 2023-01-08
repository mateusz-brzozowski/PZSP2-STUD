package com.example.android.ui.exam.exercise;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.android.R;
import com.example.android.databinding.FragmentExerciseBinding;
import com.example.android.ui.exam.ExercisesViewModel;
import com.example.model.exam.Choice;
import com.example.model.exam.Exercise;
import com.example.model.exam.FillBlanks;
import com.example.model.exam.FlashCard;
import com.example.model.exam.MultipleChoice;
import com.example.model.exam.MultipleTruthOrFalse;
import com.example.model.exam.SelectFromList;
import com.example.model.exam.TruthOrFalse;
import java.util.Map;

public class ExerciseFragment extends Fragment {

	private static final String ARG_EXERCISE = "exercise";

	private static final Map<Class<? extends Exercise>, FragmentSupplier> AVAILABLE_FRAGMENTS = Map.of(
			Choice.class, ChoiceFragment::newInstance,
			MultipleChoice.class, MultiChoiceFragment::newInstance,
			TruthOrFalse.class, TruthOrFalseFragment::newInstance,
			MultipleTruthOrFalse.class, MultiTruthOrFalseFragment::newInstance,
			FlashCard.class, FlashCardFragment::newInstance,
			FillBlanks.class, FillBlanksFragment::newInstance,
			SelectFromList.class, SelectFromListFragment::newInstance
	);

	private int position = 0;
	private Exercise exercise;
	private ExercisesViewModel viewModel;

	private FragmentExerciseBinding binding;

	private FrameLayout imageFrameLayout;
	private ProgressBar imageProgressBar;
	private ImageView imageView;

	private Fragment fragment;

	public ExerciseFragment() {
		// Required empty public constructor
	}

	public static ExerciseFragment newInstance(int position) {
		var fragment = new ExerciseFragment();
		var args = new Bundle();
		args.putInt(ARG_EXERCISE, position);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			position = getArguments().getInt(ARG_EXERCISE, 0);
		}
		if (getActivity() != null) {
			viewModel = new ViewModelProvider(getActivity()).get(ExercisesViewModel.class);
		}
		exercise = viewModel.getExerciseAtPosition(position);
		var supplier = AVAILABLE_FRAGMENTS.get(exercise.getClass());
		if (supplier != null) {
			fragment = supplier.newInstance(position);
		}
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		binding = FragmentExerciseBinding.inflate(inflater, container, false);
		return binding.getRoot();
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		TextView questionTextView = binding.textViewQuestionCard;
		imageFrameLayout = binding.frameLayoutQuestionCard;
		imageView = binding.imageViewQuestionCard;
		imageProgressBar = binding.progressBarQuestionCard;

		questionTextView.setText(exercise.getQuestion());
		setupImage();

		getChildFragmentManager().beginTransaction()
				.replace(R.id.fragment_container_exercise, fragment)
				.commit();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}

	private void setupImage() {
		var image = exercise.getContent();
		if (image != null) {
			// TODO: Load images
			Log.i("IMG", "Loading image");
			imageView.setImageDrawable(AppCompatResources.getDrawable(getContext(), R.drawable.ic_settings));
		} else {
			imageFrameLayout.setVisibility(View.GONE);
			imageProgressBar.setVisibility(View.GONE);
		}
	}
}