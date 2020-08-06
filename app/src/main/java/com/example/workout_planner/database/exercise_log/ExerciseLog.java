package com.example.workout_planner.database.exercise_log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import org.threeten.bp.LocalDate;

/**
 * A log of a single exercise set
 */
@Entity
public class ExerciseLog {
    @PrimaryKey(autoGenerate = true)
    private int logID;
    @NonNull
    private LocalDate date;
    @NonNull
    private String exerciseName;
    private double weight;
    private int time;
    private int reps;

    public ExerciseLog(@NonNull LocalDate date, @NonNull String exerciseName,
                       double weight, int time, int reps) {
        this.date = date;
        this.exerciseName = exerciseName;
        this.weight = weight;
        this.time = time;
        this.reps = reps;
    }

    @TypeConverter
    public static String fromLocalDate(LocalDate date) {
        return date.toString();
    }

    @TypeConverter
    public static LocalDate stringToLocalDate(String string) {
        return LocalDate.parse(string);
    }

    @NonNull
    public LocalDate getDate() {
        return date;
    }

    @NonNull
    public String getExerciseName() {
        return exerciseName;
    }

    public double getWeight() {
        return weight;
    }

    public int getTime() {
        return time;
    }

    public int getReps() {
        return reps;
    }

    public int getLogID() {
        return logID;
    }

    public void setLogID(int logID) {
        this.logID = logID;
    }
}
