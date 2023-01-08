package com.example.android.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.android.databinding.ActivityLoginBinding;
import com.example.android.ui.MainMenuActivity;
import com.example.android.util.SettingsUtility;

public class LoginActivity extends AppCompatActivity {

	private LoginViewModel loginViewModel;
	private ActivityLoginBinding binding;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SettingsUtility.updateLanguage(this);

		binding = ActivityLoginBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
				.get(LoginViewModel.class);

		final EditText usernameEditText = binding.loginEmail;
		final EditText passwordEditText = binding.loginPasswordEditText;
		final Button loginButton = binding.loginButton;
		final ProgressBar loadingProgressBar = binding.loginLoading;

		loginViewModel.getLoginFormState().observe(this, loginFormState -> {
			if (loginFormState == null) {
				return;
			}
			loginButton.setEnabled(loginFormState.isDataValid());
			if (loginFormState.getUsernameError() != null) {
				usernameEditText.setError(getString(loginFormState.getUsernameError()));
			}
			if (loginFormState.getPasswordError() != null) {
				passwordEditText.setError(getString(loginFormState.getPasswordError()));
			}
		});

		loginViewModel.getLoginResult().observe(this, loginResult -> {
			if (loginResult == null) {
				return;
			}
			loadingProgressBar.setVisibility(View.GONE);
			if (loginResult.getError() != null) {
				showLoginFailed(loginResult.getError());
			}
			if (loginResult.getSuccess() != null) {
				updateUiWithUser(loginResult.getSuccess());
			}
			setResult(Activity.RESULT_OK);

			//Complete and destroy login activity once successful
			finish();
		});

		TextWatcher afterTextChangedListener = new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// ignore
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// ignore
			}

			@Override
			public void afterTextChanged(Editable s) {
				loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
						passwordEditText.getText().toString());
			}
		};
		usernameEditText.addTextChangedListener(afterTextChangedListener);
		passwordEditText.addTextChangedListener(afterTextChangedListener);
		passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
			if (actionId == EditorInfo.IME_ACTION_DONE) {
				loginViewModel.login(usernameEditText.getText().toString(),
						passwordEditText.getText().toString());
			}
			return false;
		});

		loginButton.setOnClickListener(v -> {
			loadingProgressBar.setVisibility(View.VISIBLE);
			loginViewModel.login(usernameEditText.getText().toString(),
					passwordEditText.getText().toString());
		});
	}

	private void updateUiWithUser(LoggedInUserView model) {
		var intent = new Intent(this, MainMenuActivity.class);
		startActivity(intent);
	}

	private void showLoginFailed(@StringRes Integer errorString) {
		Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
	}
}
