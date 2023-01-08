package com.example.android.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.example.android.R;
import com.example.android.databinding.ActivityMainMenuBinding;
import com.example.android.util.SettingsUtility;

public class MainMenuActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SettingsUtility.updateLanguage(this);

		var binding = ActivityMainMenuBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		var navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main_menu);
		NavigationUI.setupWithNavController(binding.navView, navController);
	}

}
