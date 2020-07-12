package com.example.workoutplanner;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * WorkoutPlan day plan consisting of a set of exercisePlans
 */
public class WorkoutPlan implements Serializable {
    private String name;

    @NonNull
    private ArrayList<ExercisePlan> exercisePlans;

    public WorkoutPlan(String name, @NonNull ArrayList<ExercisePlan> exercisePlans) {
        this.name = name;
        this.exercisePlans = exercisePlans;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    public ArrayList<ExercisePlan> getExercisePlans() {
        return exercisePlans;
    }
}