package com.example.android.ui.registration;

import android.os.Bundle;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.example.android.databinding.ActivityRegistrationBinding;

public class RegistrationActivity extends AppCompatActivity {

	private ActivityRegistrationBinding binding;

	private EditText usernameEditText;
	private EditText emailEditText;
	private EditText passwordEditText;
	private EditText repeatPasswordEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		usernameEditText = binding.registrationUsername;
		emailEditText = binding.registrationEmail;
		passwordEditText = binding.registrationPasswordEditText;
		repeatPasswordEditText = binding.registrationPasswordEditText2;
	}
}