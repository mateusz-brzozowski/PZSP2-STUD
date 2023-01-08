package com.example.android.util;

import android.text.Editable;
import android.text.TextWatcher;

public interface TextChangedHandler {

	void handle(String value);

	static TextWatcher prepareTextWatcher(TextChangedHandler handler) {
		return new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// Do nothing
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				handler.handle(s.toString());
			}

			@Override
			public void afterTextChanged(Editable s) {
				// Do nothing
			}
		};
	}
}