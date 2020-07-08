package com.example.workoutplanner;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Workout day plan consisting of a set of exercises
 */
public class Workout implements Serializable {
    private String name;
    private ArrayList<Exercise> exercises;

    public Workout(String name, @NonNull ArrayList<Exercise> exercises) {
        this.name = name;
        this.exercises = exercises;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Exercise> getExercises() {
        return exercises;
    }
}