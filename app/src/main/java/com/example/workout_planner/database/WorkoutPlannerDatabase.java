package com.example.workout_planner.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.workout_planner.database.exercise.Exercise;
import com.example.workout_planner.database.exercise.ExerciseDao;
import com.example.workout_planner.database.exercise_log.ExerciseLog;
import com.example.workout_planner.database.exercise_log.ExerciseLogDao;
import com.example.workout_planner.database.workout_plan.WorkoutPlan;
import com.example.workout_planner.database.workout_plan.WorkoutPlanDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Exercise.class, WorkoutPlan.class, ExerciseLog.class}, version = 9,
        exportSchema = false)
@TypeConverters({Exercise.class, WorkoutPlan.class, ExerciseLog.class})
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
                    ExerciseLogDao exerciseLogDao = INSTANCE.exerciseLogDao();
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

    public abstract ExerciseLogDao exerciseLogDao();
}
