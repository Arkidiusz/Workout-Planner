package com.example.workoutplanner;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.workoutplanner.database.ExerciseLog.ExerciseLog;
import com.example.workoutplanner.database.ExerciseLog.ExerciseLogDao;
import com.example.workoutplanner.database.WorkoutPlannerDatabase;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.workoutplanner", appContext.getPackageName());
    }

    @Test
    public void testExerciseLogsSaved(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        WorkoutPlannerDatabase workoutPlannerDatabase = WorkoutPlannerDatabase.getDataBase(appContext);
        ExerciseLogDao dao = workoutPlannerDatabase.exerciseLogDao();
        LiveData<List<ExerciseLog>> exerciseLogs = dao.getAllExerciseLogs();
        for(ExerciseLog exerciseLog : exerciseLogs.getValue()){
            Log.i("ExerciseLog", exerciseLog.toString());
        }
    }
}