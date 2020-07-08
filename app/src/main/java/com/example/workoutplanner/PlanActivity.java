package com.example.workoutplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class PlanActivity extends AppCompatActivity {

    private EditText workoutNameET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        workoutNameET = findViewById(R.id.editTextWorkoutName);

        // Button used to finish creating of a workout
        Button saveExerciseButton = findViewById(R.id.saveButton);
        saveExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String workoutName = workoutNameET.getText().toString();
                if(!workoutName.isEmpty()){
                    ArrayList<Exercise> sampleExercises = new ArrayList<>();
                    sampleExercises.add(new Exercise("Bench Press", 5));
                    sampleExercises.add(new Exercise("Squat", 4));
                    Workout workout = new Workout(workoutNameET.getText().toString(), sampleExercises);
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("workout", workout);
                    setResult(AppCompatActivity.RESULT_OK, resultIntent);
                    Toast.makeText(getApplicationContext(),"Workout " + workoutName + " saved.",Toast.LENGTH_SHORT).show();
                    finish();
                }//@TODO Handle a case where a workout of same name exists
                else{
                    Toast.makeText(getApplicationContext(),"Please enter the name.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}