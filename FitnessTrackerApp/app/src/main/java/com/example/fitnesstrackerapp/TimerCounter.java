package com.example.fitnesstrackerapp;



import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;



import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TimerCounter extends AppCompatActivity {

    private TextView timerTextView;
    private Button startButton, stopButton;
    private Handler handler;
    private long startTime;
    private boolean isRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_counter); // Ensure you have this layout created

        timerTextView = findViewById(R.id.timerTextView);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);
        handler = new Handler();

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
            }
        });
    }

    private void startTimer() {
        if (!isRunning) {
            startTime = System.currentTimeMillis();
            isRunning = true;
            handler.postDelayed(updateTimerRunnable, 0);
        }
    }

    private void stopTimer() {
        if (isRunning) {
            isRunning = false;
            handler.removeCallbacks(updateTimerRunnable);
            showStopConfirmationDialog();
        }
    }

    private Runnable updateTimerRunnable = new Runnable() {
        @Override
        public void run() {
            if (isRunning) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                long seconds = (elapsedTime / 1000) % 60;
                long minutes = (elapsedTime / (1000 * 60)) % 60;
                long hours = (elapsedTime / (1000 * 60 * 60)) % 24;

                timerTextView.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));

                handler.postDelayed(this, 1000);
            }
        }
    };

    private void showStopConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Timer Stopped")
                .setMessage("Your timer has been stopped. Total time: " + timerTextView.getText())
                .setPositiveButton("OK", null)
                .show();
    }
}