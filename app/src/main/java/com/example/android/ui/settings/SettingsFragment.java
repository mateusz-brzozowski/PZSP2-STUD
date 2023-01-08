package com.example.android.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import com.example.android.R;
import com.example.android.ui.login.LoginActivity;
import com.example.android.ui.registration.RegistrationActivity;
import com.example.android.util.SettingsUtility;

public class SettingsFragment extends PreferenceFragmentCompat {

	private SettingsViewModel viewModel;

	@Override
	public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
		setPreferencesFromResource(R.xml.root_preferences, rootKey);
		assert getActivity() != null;
		viewModel = new ViewModelProvider(getActivity()).get(SettingsViewModel.class);

		setupEmail();
		setupLogout();
		setupChangePassword();
		setupLanguage();
		setupTheme();
	}

	private void setupEmail() {
		var emailPreference = (EditTextPreference) findPreference(getString(R.string.email_key));
		assert emailPreference != null;
		emailPreference.setOnPreferenceChangeListener((preference, newValue) -> {
			viewModel.setEmail((String) newValue, getContext());
			return true;
		});
		if (getActivity() != null) {
			viewModel.getEmailLiveData().observe(getActivity(), emailPreference::setText);
		}
		viewModel.loadEmail(getContext());
	}

	private void setupLogout() {
		var logoutPreference = (Preference) findPreference(getString(R.string.logout_key));
		assert logoutPreference != null;
		logoutPreference.setOnPreferenceClickListener(pref -> {
			var intent = new Intent(getContext(), LoginActivity.class);
			startActivity(intent);
			if (getActivity() != null) {
				getActivity().finish();
			}
			return true;
		});
	}

	private void setupChangePassword() {
		var passwordPreference = (Preference) findPreference(getString(R.string.password_key));
		assert passwordPreference != null;
		passwordPreference.setOnPreferenceClickListener(pref -> {
			var intent = new Intent(getContext(), RegistrationActivity.class);
			startActivity(intent);
			return true;
		});
	}

	private void setupLanguage() {
		var languagePreference = (ListPreference) findPreference(getString(R.string.language_key));
		assert languagePreference != null;
		languagePreference.setOnPreferenceChangeListener((preference, newValue) -> {
			SettingsUtility.setLanguage(this.getContext(), (String) newValue);
			var activity = getActivity();
			if (activity == null) {
				return false;
			}
			var intent = activity.getIntent();
			activity.finish();
			startActivity(intent);
			return true;
		});
	}

	private void setupTheme() {
		var themePreference = (ListPreference) findPreference(getString(R.string.settings_theme_key));
		assert themePreference != null;
		themePreference.setOnPreferenceChangeListener((preference, newValue) -> {
			SettingsUtility.setTheme(this.getContext(), (String) newValue);
			return true;
		});
	}
}
