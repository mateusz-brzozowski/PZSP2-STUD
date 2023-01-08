package com.example.android.ui.exam.exercise;

import androidx.fragment.app.Fragment;

public interface FragmentSupplier {

	Fragment newInstance(int position);
}
