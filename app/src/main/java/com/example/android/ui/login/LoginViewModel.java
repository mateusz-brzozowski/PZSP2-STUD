package com.example.android.ui.login;

import android.util.Patterns;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.android.R;
import com.example.android.data.Result;
import com.example.android.data.model.Authorization;
import com.example.android.util.TokenHandler;
import com.example.android.web.ApiClient;
import com.example.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {

	private final MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
	private final MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();

	LiveData<LoginFormState> getLoginFormState() {
		return loginFormState;
	}

	LiveData<LoginResult> getLoginResult() {
		return loginResult;
	}

	public void login(String username, String password) {
		var apiClient = ApiClient.getInstance();
		var call = apiClient.login(User.builder().login(username).password(password).build());
		call.enqueue(new Callback<>() {
			@Override
			public void onResponse(@NonNull Call<Authorization> call, @NonNull Response<Authorization> response) {
				var data = response.body();
				if (data != null) {
					var handler = TokenHandler.getInstance();
					handler.setUser(username);
					handler.setPassword(password);
					loginResult.setValue(new LoginResult(new LoggedInUserView(data.getToken())));
				} else {
					loginResult.setValue(new LoginResult(R.string.login_failed));
				}
			}

			@Override
			public void onFailure(@NonNull Call<Authorization> call, @NonNull Throwable t) {
				loginResult.setValue(new LoginResult(R.string.login_failed));
			}
		});
	}

	public void loginDataChanged(String username, String password) {
		if (!isUserNameValid(username)) {
			loginFormState.setValue(new LoginFormState(R.string.invalid_email, null));
		} else if (!isPasswordValid(password)) {
			loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
		} else {
			loginFormState.setValue(new LoginFormState(true));
		}
	}

	// A placeholder username validation check
	private boolean isUserNameValid(String username) {
		if (username == null) {
			return false;
		}
		if (username.contains("@")) {
			return Patterns.EMAIL_ADDRESS.matcher(username).matches();
		} else {
			return !username.trim().isEmpty();
		}
	}

	// A placeholder password validation check
	private boolean isPasswordValid(String password) {
		return password != null && password.trim().length() > 7;
	}
}
