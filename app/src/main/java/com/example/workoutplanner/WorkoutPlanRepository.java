package com.example.workoutplanner;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class WorkoutPlanRepository {
    private WorkoutPlanDao workoutPlanDao;
    private LiveData<List<WorkoutPlan>> allWorkoutPlans;

    WorkoutPlanRepository(Application application){
        WorkoutPlannerDatabase db = WorkoutPlannerDatabase.getDataBase(application);
        workoutPlanDao = db.workoutPlanDao();
        allWorkoutPlans = workoutPlanDao.getAllWorkoutPlans();
    }

    LiveData<List<WorkoutPlan>> getAllWorkoutPlans(){
        return allWorkoutPlans;
    }

    void insert(final WorkoutPlan workoutPlan){
        WorkoutPlannerDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                workoutPlanDao.insert(workoutPlan);
            }
        });
    }
}
