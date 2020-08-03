package com.example.workoutplanner.activities.main;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.workoutplanner.database.workout_plan.WorkoutPlan;
import com.example.workoutplanner.database.workout_plan.WorkoutPlanRepository;

import java.util.List;

public class WorkoutPlanViewModel extends AndroidViewModel {
    private WorkoutPlanRepository repository;
    private LiveData<List<WorkoutPlan>> allWorkoutPlans;

    public WorkoutPlanViewModel(Application application) {
        super(application);
        repository = new WorkoutPlanRepository(application);
        allWorkoutPlans = repository.getAllWorkoutPlans();
    }

    LiveData<List<WorkoutPlan>> getAllWorkoutPlans() {
        return allWorkoutPlans;
    }

    public void insert(WorkoutPlan workoutPlan) {
        repository.insert(workoutPlan);
    }
}
