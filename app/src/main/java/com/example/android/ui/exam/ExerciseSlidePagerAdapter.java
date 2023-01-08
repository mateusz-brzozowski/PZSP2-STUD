package com.example.android.ui.exam;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.android.ui.exam.exercise.ExerciseFragment;
import com.example.model.exam.Exercise;
import java.util.List;

public class ExerciseSlidePagerAdapter extends FragmentStateAdapter {

	private final List<Exercise> exercises;

	public ExerciseSlidePagerAdapter(@NonNull FragmentActivity fragmentActivity, List<Exercise> exercises) {
		super(fragmentActivity);
		this.exercises = exercises;
	}

	@NonNull
	@Override
	public Fragment createFragment(int position) {
		return ExerciseFragment.newInstance(position);
	}

	@Override
	public int getItemCount() {
		return exercises.size();
	}
}
