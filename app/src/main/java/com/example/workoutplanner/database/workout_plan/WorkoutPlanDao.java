package com.example.workoutplanner.database.workout_plan;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WorkoutPlanDao {
    @Insert
    void insert(WorkoutPlan workoutPlan);

    @Query("SELECT * from WorkoutPlan ORDER BY name ASC")
    LiveData<List<WorkoutPlan>> getAllWorkoutPlans();

    @Query("DELETE FROM WorkoutPlan")
    void deleteAll();
}
