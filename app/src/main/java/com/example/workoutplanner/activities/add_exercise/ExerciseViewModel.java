package com.example.workoutplanner.activities.add_exercise;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.workoutplanner.database.exercise.Exercise;
import com.example.workoutplanner.database.exercise.ExerciseRepository;

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
