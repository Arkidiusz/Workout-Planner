package com.example.workout_planner.activities.add_exercise;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.workout_planner.database.exercise.Exercise;
import com.example.workout_planner.database.exercise.ExerciseRepository;

import java.util.List;

public class ExerciseViewModel extends AndroidViewModel {
    private ExerciseRepository repository;
    private LiveData<List<Exercise>> exercises;

    public ExerciseViewModel(@NonNull Application application) {
        super(application);
        repository = new ExerciseRepository(application);
        exercises = repository.getAllExercises();
    }

    LiveData<List<Exercise>> getAllExercises() {
        return exercises;
    }

    public void insert(Exercise exercise) {
        repository.insert(exercise);
    }
}
