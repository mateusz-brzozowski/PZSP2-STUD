package com.example.android.ui.settings;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.preference.PreferenceManager;
import com.example.android.R;

public class SettingsViewModel extends ViewModel {

	private final MutableLiveData<String> emailLiveData = new MutableLiveData<>();

	public LiveData<String> getEmailLiveData() {
		return emailLiveData;
	}

	public void setEmail(String email, Context context) {
		emailLiveData.setValue(email);
		var sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		sharedPreferences.edit().putString(context.getString(R.string.email_key), email).commit();
		// TODO: update email on server

	}

	public void loadEmail(Context context) {
		var sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		var email = sharedPreferences.getString(context.getString(R.string.email_key), "admin@gmail.com");
		emailLiveData.setValue(email);
	}
}
