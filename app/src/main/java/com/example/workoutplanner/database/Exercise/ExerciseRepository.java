package com.example.workoutplanner.database.Exercise;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.workoutplanner.database.WorkoutPlannerDatabase;

import java.util.List;

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