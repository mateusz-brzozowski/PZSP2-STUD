package com.example.android;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.android.web.ApiClient;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TextView text = findViewById(R.id.hello_api);

		var client = ApiClient.getInstance();
		text.setText(client.getDefinitionById(2).toString());
	}
}
