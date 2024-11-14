
package com.example.fitnesstrackerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button userProfileButton, trackActivityButton, timerCounterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        trackActivityButton = findViewById(R.id.btn_track_activity);
        userProfileButton = findViewById(R.id.btn_user_profile);
        //oprnTimerCounter = findViewById(R.id.timerCounterButton);

        timerCounterButton = findViewById(R.id.timerCounterButton);


        //userProfileButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, UserProfileActivity.class)));
        trackActivityButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ActivityTrackingActivity.class)));
        userProfileButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, UserProfileActivity.class)));
        //open.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, TimerCounter.class)));
        //trackActivityButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ActivityTrackingActivity.class)));

        timerCounterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, TimerCounter.class);
                startActivity(intent);
            }
        });
    }
}

