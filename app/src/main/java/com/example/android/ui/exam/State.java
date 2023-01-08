package com.example.android.ui.exam;

public enum State {
	EXAM(0),
	STUDY(1),
	STUDY_ANSWERS(2);

	private final int value;

	State(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
