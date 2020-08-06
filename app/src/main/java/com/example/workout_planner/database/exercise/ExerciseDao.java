package com.example.workout_planner.database.exercise;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ExerciseDao {
    @Insert
    void insert(Exercise exercise);

    @Query("SELECT * from Exercise ORDER BY name ASC")
    LiveData<List<Exercise>> getAllExercises();

    @Query("SELECT * from Exercise ORDER BY name ASC")
    List<Exercise> getAllExercisesAsList();

    @Query("DELETE FROM Exercise")
    void deleteAll();
}
