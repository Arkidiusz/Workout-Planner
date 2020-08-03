package com.example.workoutplanner.database.exercise_log;

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

    @Query("SELECT * from ExerciseLog WHERE (exerciseName = :exerciseName) ORDER BY date ASC")
    List<ExerciseLog> getExerciseLogsOfName(String exerciseName);

    @Query("SELECT DISTINCT date from ExerciseLog ORDER BY date DESC")
    List<LocalDate> getAllDates();

    @Query("SELECT DISTINCT date from ExerciseLog WHERE exerciseName = :exerciseName ORDER BY " +
            "date DESC")
    List<LocalDate> getDatesOfExercise(String exerciseName);

    @Query("SELECT DISTINCT exerciseName from ExerciseLog ORDER BY exerciseName ASC")
    List<String> getAllExerciseNames();

    @Query("DELETE FROM ExerciseLog")
    void deleteAll();
}
