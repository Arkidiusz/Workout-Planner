package com.example.workoutplanner.database.workout_plan;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import com.example.workoutplanner.RuntimeTypeAdapterFactory;
import com.example.workoutplanner.database.ExercisePlan;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * WorkoutPlan day plan consisting of a set of exercisePlans
 */
@Entity
public class WorkoutPlan implements Serializable {
    @PrimaryKey
    @NonNull
    private String name;

    @NonNull
    private ArrayList<ExercisePlan> exercisePlans;

    public WorkoutPlan(@NonNull String name, @NonNull ArrayList<ExercisePlan> exercisePlans) {
        this.name = name;
        this.exercisePlans = exercisePlans;
    }

    @TypeConverter
    public static String fromExercisePlans(ArrayList<ExercisePlan> exercisePlans) {
        Gson gson = new Gson();
        Log.i("JSON", gson.toJson(exercisePlans));
        return gson.toJson(exercisePlans);
    }

    @TypeConverter
    public static ArrayList<ExercisePlan> stringToExercisePlans(String value) {
        Type exercisePlansType = new TypeToken<ArrayList<ExercisePlan>>() {
        }.getType();
        RuntimeTypeAdapterFactory<ExercisePlan> adapter =
                RuntimeTypeAdapterFactory.of(ExercisePlan.class, "type")
                        .registerSubtype(ExercisePlan.TempoExercisePlan.class,
                                ExercisePlan.TempoExercisePlan.class.getName())
                        .registerSubtype(ExercisePlan.IsometricExercisePlan.class,
                                ExercisePlan.IsometricExercisePlan.class.getName());
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(adapter).create();
        return gson.fromJson(value, exercisePlansType);
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public ArrayList<ExercisePlan> getExercisePlans() {
        return exercisePlans;
    }
}