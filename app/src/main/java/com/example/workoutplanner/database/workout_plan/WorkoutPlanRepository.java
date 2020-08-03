package com.example.workoutplanner.database.workout_plan;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.workoutplanner.database.WorkoutPlannerDatabase;

import java.util.List;

public class WorkoutPlanRepository {
    private WorkoutPlanDao workoutPlanDao;
    private LiveData<List<WorkoutPlan>> allWorkoutPlans;

    public WorkoutPlanRepository(Application application) {
        WorkoutPlannerDatabase db = WorkoutPlannerDatabase.getDataBase(application);
        workoutPlanDao = db.workoutPlanDao();
        allWorkoutPlans = workoutPlanDao.getAllWorkoutPlans();
    }

    public LiveData<List<WorkoutPlan>> getAllWorkoutPlans() {
        return allWorkoutPlans;
    }

    public void insert(final WorkoutPlan workoutPlan) {
        WorkoutPlannerDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                workoutPlanDao.insert(workoutPlan);
            }
        });
    }
}
