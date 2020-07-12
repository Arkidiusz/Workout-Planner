package com.example.workoutplanner;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

@Database(entities = {Exercise.class}, version = 1, exportSchema = false)
public abstract class WorkoutPlannerDatabase extends RoomDatabase {
    public abstract ExerciseDao exerciseDao();
    private static WorkoutPlannerDatabase INSTANCE;

    public static WorkoutPlannerDatabase getDataBase(final Context context){
        if(INSTANCE == null){
            synchronized (WorkoutPlannerDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WorkoutPlannerDatabase.class, "workout_planner_database").fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}
