package com.example.workoutplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PlanActivity extends AppCompatActivity {

    private EditText workoutNameET;
    private Button saveExerciseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        workoutNameET = findViewById(R.id.editTextWorkoutName);
        saveExerciseButton = findViewById(R.id.saveButton);
        saveExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String workoutName = workoutNameET.getText().toString();
                Toast.makeText(getApplicationContext(),"Workout " + workoutName + " saved.",Toast.LENGTH_SHORT).show();
            }
        });
    }
}