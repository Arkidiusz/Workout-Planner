package com.example.workoutplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
                if(!workoutName.isEmpty()){
                    Workout workout = new Workout(workoutNameET.getText().toString(), null);
                    Toast.makeText(getApplicationContext(),"Workout " + workoutName + " saved.",Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("workout", workout);
                    setResult(AppCompatActivity.RESULT_OK, resultIntent);
                }
                finish();
            }
        });
    }
}