package com.example.workoutplanner.database.ExerciseLog;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.workoutplanner.database.Exercise.Exercise;
import com.example.workoutplanner.database.WorkoutPlannerDatabase;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import java.util.List;

public class ExerciseLogRepository {
    private ExerciseLogDao exerciseLogDao;

    public ExerciseLogRepository(Application application){
        WorkoutPlannerDatabase db = WorkoutPlannerDatabase.getDataBase(application);
        exerciseLogDao = db.exerciseLogDao();
    }

    public List<ExerciseLog> getAllExerciseLogs() {
        return exerciseLogDao.getAllExerciseLogs();
    }
    public List<ExerciseLog> getExerciseLogsAtDate(LocalDate date) {
        return exerciseLogDao.getExerciseLogsAtDate(date);
    }
    public List<ExerciseLog> getExerciseLogsOfName(String exerciseName) {
        return exerciseLogDao.getExerciseLogsOfName(exerciseName);
    }

    //@TODO create a method for inserting a list of exercises
    public void insert(final ExerciseLog exerciseLog) {
        WorkoutPlannerDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                exerciseLogDao.insert(exerciseLog);
            }
        });
    }
}
