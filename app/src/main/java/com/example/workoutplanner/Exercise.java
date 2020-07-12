package com.example.workoutplanner;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Exercise {
    @PrimaryKey
    @NonNull
    private String name;
    @NonNull
    private ExerciseType exerciseType;

    public Exercise(@NonNull String name, @NonNull ExerciseType exerciseType){
        this.name = name;
        this.exerciseType = exerciseType;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public ExerciseType getExerciseType() {
        return exerciseType;
    }

    public enum ExerciseType{
        ISOMETRIC,
        TEMPO
    }
}
