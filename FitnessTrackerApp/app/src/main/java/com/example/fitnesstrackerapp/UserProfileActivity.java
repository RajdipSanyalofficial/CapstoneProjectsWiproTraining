package com.example.fitnesstrackerapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Delete;



import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UserProfileActivity extends AppCompatActivity {
    private EditText nameEditText, ageEditText, weightEditText, heightEditText;
    private Button saveButton, deleteButton;
    private DatabaseHelper databaseHelper;
    private TextView bmiTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        nameEditText = findViewById(R.id.nameEditText);
        ageEditText = findViewById(R.id.ageEditText);
        weightEditText = findViewById(R.id.weightEditText);
        heightEditText = findViewById(R.id.heightEditText);
        saveButton = findViewById(R.id.saveButton);
        bmiTextView = findViewById(R.id.bmiTextView);
        deleteButton = findViewById(R.id.deleteButton);

        databaseHelper = new DatabaseHelper(this);

        loadUserProfile();

        deleteButton.setOnClickListener(v -> showDeleteConfirmationDialog());

        saveButton.setOnClickListener(v -> saveUserProfile());
    }

    private void loadUserProfile() {
        UserProfile userProfile = databaseHelper.getUserProfile();
        if (userProfile != null) {
            nameEditText.setText(userProfile.name);
            ageEditText.setText(String.valueOf(userProfile.age));
            weightEditText.setText(String.valueOf(userProfile.weight));
            heightEditText.setText(String.valueOf(userProfile.height));
            calculateAndDisplayBMI(userProfile.weight, userProfile.height);
        }
    }

    private void saveUserProfile() {
        String name = nameEditText.getText().toString();
        String ageString = ageEditText.getText().toString();
        String weightString = weightEditText.getText().toString();
        String heightString = heightEditText.getText().toString();

        if (name.isEmpty() || ageString.isEmpty() || weightString.isEmpty() || heightString.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        int age = Integer.parseInt(ageString);
        float weight = Float.parseFloat(weightString);
        float height = Float.parseFloat(heightString);

        UserProfile userProfile = new UserProfile();
        userProfile.name = name;
        userProfile.age = age;
        userProfile.weight = weight;
        userProfile.height = height;

        databaseHelper.insertUserProfile(userProfile);
        Toast.makeText(this, "Profile saved!", Toast.LENGTH_SHORT).show();
        calculateAndDisplayBMI(weight, height);
        clearFields();
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Profile")
                .setMessage("Are you sure you want to delete?")
                .setPositiveButton("Yes", (dialog, which) -> deleteUserProfile())
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteUserProfile() {
        // Assuming you have a method in DatabaseHelper to delete the profile
        if (databaseHelper.deleteUserProfile()) {
            Toast.makeText(this, "Profile deleted", Toast.LENGTH_SHORT).show();
            clearFields();
            bmiTextView.setText("BMI: N/A");
            finish();
        } else {
            Toast.makeText(this, "Failed to delete profile", Toast.LENGTH_SHORT).show();
        }
    }

    private void calculateAndDisplayBMI(float weight, float height) {
        float bmi = weight / (height * height);
        bmiTextView.setText(String.format("BMI: %.2f", bmi));
    }

    private void clearFields() {
        nameEditText.setText("");
        ageEditText.setText("");
        weightEditText.setText("");
        heightEditText.setText("");
    }
}