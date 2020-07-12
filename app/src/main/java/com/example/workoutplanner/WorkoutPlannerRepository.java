package com.example.workoutplanner;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class WorkoutPlannerRepository {
    private ExerciseDao exerciseDao;
    private LiveData<List<Exercise>> exercises;

    WorkoutPlannerRepository(Application application){
        WorkoutPlannerDatabase db = WorkoutPlannerDatabase.getDataBase(application);
        exerciseDao = db.exerciseDao();
        exercises = exerciseDao.getAllExercises();
    }

    LiveData<List<Exercise>> getAllExercises(){
        return exercises;
    }

}
