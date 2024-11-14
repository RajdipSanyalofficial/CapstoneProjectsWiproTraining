package com.example.fitnesstrackerapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ActivityDao {
    @Insert
    void insert(Activity activity);

    @Query("SELECT * FROM activities ORDER BY date DESC")
    List<Activity> getAllActivities();
}

