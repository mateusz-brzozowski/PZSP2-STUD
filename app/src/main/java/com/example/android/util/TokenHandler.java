package com.example.android.util;

import android.content.Context;
import android.util.Log;
import androidx.preference.PreferenceManager;
import com.example.android.R;
import com.example.android.web.ApiClient;
import com.example.model.User;
import java.io.IOException;

public class TokenHandler {

	private static TokenHandler handler;
	private final Context context;
	private String token;
	private String user;
	private String password;

	public TokenHandler(Context context) {
		this.context = context.getApplicationContext();
		var sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		this.token = sharedPreferences.getString(context.getString(R.string.token_key), null);
		this.user = sharedPreferences.getString(context.getString(R.string.user_key), null);
		this.password = sharedPreferences.getString(context.getString(R.string.password_key), null);
	}

	public static void initialize(Context context) {
		handler = new TokenHandler(context);
	}

	public static TokenHandler getInstance() {
		return handler;
	}

	public void setToken(String token) {
		this.token = token;
		var sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		sharedPreferences.edit().putString(context.getString(R.string.token_key), token).apply();
	}

	public String getToken() {
		return token;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void regenerateToken() {
		var apiClient = ApiClient.getInstance();
		var call = apiClient.login(User.builder().login(user).password(password).build());
		try {
			var response = call.execute();
			var data = response.body();
			if (data != null) {
				setToken(data.getToken());
			}
		} catch (IOException e) {
			Log.e("TOKEN", e.getLocalizedMessage());
		}
	}
}
