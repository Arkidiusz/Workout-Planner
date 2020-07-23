package com.example.workoutplanner.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.workoutplanner.database.WorkoutPlan.WorkoutPlan;
import com.example.workoutplanner.database.WorkoutPlan.WorkoutPlanDao;
import com.example.workoutplanner.database.Exercise.Exercise;
import com.example.workoutplanner.database.Exercise.ExerciseDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Exercise.class, WorkoutPlan.class}, version = 2, exportSchema = false)
@TypeConverters({Exercise.class, WorkoutPlan.class})
public abstract class WorkoutPlannerDatabase extends RoomDatabase {
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static WorkoutPlannerDatabase INSTANCE;
    private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            databaseWriteExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    ExerciseDao exerciseDao = INSTANCE.exerciseDao();
                    WorkoutPlanDao workoutPlanDao = INSTANCE.workoutPlanDao();
                }
            });
        }
    };

    public static WorkoutPlannerDatabase getDataBase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WorkoutPlannerDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WorkoutPlannerDatabase.class, "workout_planner_database").fallbackToDestructiveMigration().addCallback(roomDatabaseCallback).build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract ExerciseDao exerciseDao();

    public abstract WorkoutPlanDao workoutPlanDao();
}
