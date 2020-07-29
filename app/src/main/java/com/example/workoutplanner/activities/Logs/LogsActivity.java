package com.example.workoutplanner.activities.Logs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.workoutplanner.R;
import com.example.workoutplanner.activities.Main.WorkoutPlanViewModel;
import com.example.workoutplanner.database.ExerciseLog.ExerciseLog;
import com.example.workoutplanner.database.ExerciseLog.ExerciseLogRepository;

import java.util.List;

public class LogsActivity extends AppCompatActivity {

    private ExerciseLogsViewModel exerciseLogsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        exerciseLogsViewModel = new ViewModelProvider(this).get(ExerciseLogsViewModel.class);
        LiveData<List<ExerciseLog>> exerciseLogs = exerciseLogsViewModel.getAllExerciseLogs();
        TextView tv = findViewById(R.id.textView);
        tv.setText(exerciseLogs.getValue().toString());
    }
}