package com.feedmytrip.client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.amazonaws.mobile.client.AWSMobileClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button login_btn = findViewById(R.id.btnSignOut);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AWSMobileClient.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, AuthenticationActivity.class);
                startActivity(intent);
            }
        });
    }
}
