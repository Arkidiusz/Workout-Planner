package com.example.workoutplanner.database.ExerciseLog;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Locale;

/**
 * A log of a single exercise set
 */
@Entity
public class ExerciseLog {
    @PrimaryKey
    @NonNull
    private LocalDate date;
    @NonNull
    private String exerciseName;
    private double weight;
    private int time;
    private int reps;

    public ExerciseLog(@NonNull LocalDate date, @NonNull String exerciseName,
                       double weight, int time, int reps){
        this.date = date;
        this.exerciseName = exerciseName;
        this.weight = weight;
        this.time = time;
        this.reps = reps;
    }

    @NonNull
    public LocalDate getDate(){
        return date;
    }

    @NonNull
    public String getExerciseName(){
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

    @TypeConverter
    public static String fromLocalDate(LocalDate date){
        return date.toString();
    }

    @TypeConverter
    public static LocalDate stringToLocalDate(String string){
        return LocalDate.parse(string);
    }
}
