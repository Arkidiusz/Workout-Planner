package com.example.workoutplanner.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * Provides database access (Exercise only) for the rest of the app
 */
public class ExerciseRepository {
    private ExerciseDao exerciseDao;
    private LiveData<List<Exercise>> exercises;

    public ExerciseRepository(Application application) {
        WorkoutPlannerDatabase db = WorkoutPlannerDatabase.getDataBase(application);
        exerciseDao = db.exerciseDao();
        exercises = exerciseDao.getAllExercises();
    }

    public LiveData<List<Exercise>> getAllExercises() {
        return exercises;
    }

    public void insert(final Exercise exercise) {
        WorkoutPlannerDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                exerciseDao.insert(exercise);
            }
        });
    }
}