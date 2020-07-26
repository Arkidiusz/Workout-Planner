package com.example.workoutplanner.database.ExerciseLog;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import org.threeten.bp.LocalDate;
import java.util.List;

@Dao
public interface ExerciseLogDao {
    @Insert
    void insert(ExerciseLog exerciseLog);

    @Query("SELECT * from ExerciseLog ORDER BY date ASC")
    List<ExerciseLog> getAllExerciseLogs();

    /**
     * @param date of WorkoutSession
     * @return a list of all ExerciseLogs at a given date (entire workout session)
     */
    @Query("SELECT * from ExerciseLog WHERE (date = :date) ORDER BY date ASC")
    List<ExerciseLog> getExerciseLogsAtDate(LocalDate date);

    /**
     * @param exerciseName uniquely identifies an exercise
     * @return a list of ExerciseLogs of given exerciseName
     */
    @Query("SELECT * from ExerciseLog WHERE (exerciseName = :exerciseName) ORDER BY date ASC")
    List<ExerciseLog> getExerciseLogsOfName(String exerciseName);
}
