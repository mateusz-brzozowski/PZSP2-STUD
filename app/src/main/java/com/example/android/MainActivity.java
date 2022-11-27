package com.example.android;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.android.web.ApiClient;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TextView textView = findViewById(R.id.hello_api);

		var client = ApiClient.getInstance();

		new Thread(
				() -> {
					String text = "";
					try {
						text = client
								.testConnection("Bartłomiej Krawczyk")
								.execute()
								.body();
					} catch (IOException e) {
						e.printStackTrace();
					}

					String finalText = text;
					runOnUiThread(() -> textView.setText(finalText));
				}
		).start();
	}
}
