package com.example.workoutplanner.activities.Logs;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.workoutplanner.database.ExerciseLog.ExerciseLog;
import com.example.workoutplanner.database.ExerciseLog.ExerciseLogRepository;
import com.example.workoutplanner.database.WorkoutPlan.WorkoutPlan;
import com.example.workoutplanner.database.WorkoutPlan.WorkoutPlanRepository;

import java.util.List;

public class ExerciseLogsViewModel extends AndroidViewModel {
    private ExerciseLogRepository repository;

    public ExerciseLogsViewModel(Application application) {
        super(application);
        repository = new ExerciseLogRepository(application);
    }

    LiveData<List<ExerciseLog>> getAllExerciseLogs() {
        return repository.getAllExerciseLogs();
    }

    public void insert(ExerciseLog exerciseLog) {
        repository.insert(exerciseLog);
    }
}
