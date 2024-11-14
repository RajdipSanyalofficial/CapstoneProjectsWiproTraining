package com.example.fitnesstrackerapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "activities")
public class Activity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String activityType;
    public int steps;
    public float distance; // in kilometers
    public int calories;
    public String date; // Store date as String for simplicity

    // Constructor, getters and setters
}