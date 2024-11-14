
package com.example.fitnesstrackerapp;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class ActivityTrackingActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "fitness_tracker_channel"; // Channel ID
    private Spinner activitySpinner;
    private Button startTrackingButton, stopTrackingButton,btnMaps;
    private TextView trackingInfo;
    private EditText inputDuration;

    private CountDownTimer countDownTimer;
    private int steps = 0; // To store steps
    private double distance = 0.0; // To store distance
    private double caloriesBurned = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        createNotificationChannel(); // Create notification channel

        activitySpinner = findViewById(R.id.activity_spinner);
        startTrackingButton = findViewById(R.id.start_button);
        stopTrackingButton = findViewById(R.id.stop_button);
        trackingInfo = findViewById(R.id.tracking_info);
        inputDuration = findViewById(R.id.input_duration);
        btnMaps = findViewById(R.id.btnMaps);

        btnMaps.setOnClickListener(v -> startActivity(new Intent(ActivityTrackingActivity.this, MapsActivity.class)));




        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.activity_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activitySpinner.setAdapter(adapter);

        startTrackingButton.setOnClickListener(v -> startTracking());
        stopTrackingButton.setOnClickListener(v -> stopTracking());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Fitness Tracker Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }

    private void startTracking() {
        String durationString = inputDuration.getText().toString();

        if (durationString.isEmpty() || Integer.parseInt(durationString) <= 0) {
            Toast.makeText(this, "Please enter a valid duration", Toast.LENGTH_SHORT).show();
            return;
        }

        int duration = Integer.parseInt(durationString);

        countDownTimer = new CountDownTimer(duration * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                trackingInfo.setText("Time left: " + millisUntilFinished / 1000 + "s");
                steps += 10; // Example step increment
                distance += 0.01; // Example distance increment
                caloriesBurned += 0.5; // Example calories increment
            }

            @Override
            public void onFinish() {
                trackingInfo.setText("Tracking finished! \n Steps: " + steps + ", \nDistance: " + distance + ", \nCalories: " + caloriesBurned);
                sendNotification(); // Notify user of completion
            }
        }.start();
    }

    private void stopTracking() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        trackingInfo.setText("Tracking stopped. Steps: " + steps + ", Distance: " + distance + ", Calories: " + caloriesBurned);
        sendNotification(); // Notify user of stopping
    }

    private void sendNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(this, ActivityTrackingActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        String contentText = "Tracking finished! Steps: " + steps + ", Distance: " + distance + ", Calories: " + caloriesBurned;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification) // Replace with your app's notification icon
                .setContentTitle("Activity Tracker")
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        if (notificationManager != null) {
            notificationManager.notify(1, builder.build());
        }
    }
}