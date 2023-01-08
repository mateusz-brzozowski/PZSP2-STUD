package com.example.android.util;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;
import com.example.android.R;
import java.util.Locale;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SettingsUtility {

	@SuppressLint("ApplySharedPref")
	public static void setTheme(Context context, String theme) {
		var sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		sharedPreferences.edit().putString(context.getString(R.string.settings_theme_key), theme).commit();
		updateTheme(context);
	}

	public static void updateTheme(Context context) {
		var sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		var theme = sharedPreferences.getString(context.getString(R.string.settings_theme_key), context.getString(R.string.system_key));
		if (theme.equals(context.getString(R.string.light_key))) {
			AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
		} else if (theme.equals(context.getString(R.string.dark_key))) {
			AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
		} else {
			AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
		}
	}

	@SuppressLint("ApplySharedPref")
	public static void setLanguage(Context context, String language) {
		var sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		sharedPreferences.edit().putString(context.getString(R.string.language_key), language).commit();
		updateLanguage(context);
	}

	public static void updateLanguage(Context context) {
		var sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		var language = sharedPreferences.getString(context.getString(R.string.language_key), "pl");
		var locale = new Locale(language);
		Locale.setDefault(locale);

		var resources = context.getResources();
		var displayMetrics = resources.getDisplayMetrics();
		var config = resources.getConfiguration();
		config.setLocale(locale);
		config.setLayoutDirection(locale);
		resources.updateConfiguration(config, displayMetrics);
	}
}
