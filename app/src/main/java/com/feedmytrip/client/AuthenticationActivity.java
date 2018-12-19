package com.feedmytrip.client;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.client.results.SignInResult;

import static com.amazonaws.mobile.client.UserState.SIGNED_IN;

public class AuthenticationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        final ProgressBar login_progress = findViewById(R.id.loginProgressBar);
        login_progress.setVisibility(View.GONE);

        final Button login_btn = findViewById(R.id.loginButton);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_progress.setVisibility(View.VISIBLE);

                String username = ((EditText)findViewById(R.id.usernameEditText)).getText().toString();
                String password = ((EditText)findViewById(R.id.passwordEditText)).getText().toString();

                AWSMobileClient.getInstance().signIn(username, password, null, new Callback<SignInResult>() {
                    @Override
                    public void onResult(final SignInResult signInResult) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("AWS_AUTH_LOGIN", "Sign-in callback state: " + signInResult.getSignInState());
                                switch (signInResult.getSignInState()) {
                                    case DONE:
                                        Intent intent = new Intent(AuthenticationActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        break;
                                    default:
                                        makeToast("Unsupported sign-in confirmation: " + signInResult.getSignInState());
                                        break;
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("AWS_AUTH_LOGIN", "Sign-in error", e);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                makeToast(getApplication().getResources().getString(R.string.login_failed));
                                login_progress.setVisibility(View.GONE);
                            }
                        });
                    }
                });

            }
        });

        AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {
            @Override
            public void onResult(UserStateDetails userStateDetails) {
                Log.i("AWS_AUTH_INIT", "onResult: " + userStateDetails.getUserState());
                if (userStateDetails.getUserState() == SIGNED_IN) {
                    Intent intent = new Intent(AuthenticationActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e("AWS_AUTH_INIT", "Initialization error.", e);
            }
        });
    }

    public void makeToast(String text) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(context, text, duration).show();
    }
}
