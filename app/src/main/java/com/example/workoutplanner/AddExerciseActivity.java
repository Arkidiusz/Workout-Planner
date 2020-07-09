package com.example.workoutplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

//TODO create a method for instantiating Exercises with default values
public class AddExerciseActivity extends AppCompatActivity {

    private EditText exerciseName;
    private Spinner exerciseList;
    private Spinner exerciseTypes;
    private Button addExistingExercise;
    private Button addNewExercise;

    // Default values for new exercises
    private static final int DEFAULT_SETS = 5;
    private static final int DEFAULT_REPS = 5;
    private static final int DEFAULT_ECCENTRIC = 3;
    private static final int DEFAULT_ECCENTRIC_PAUSE = 0;
    private static final int DEFAULT_CONCENTRIC = 1;
    private static final int DEFAULT_CONCENTRIC_PAUSE = 0;
    private static final int DEFAULT_TIME = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        exerciseName = findViewById(R.id.new_exercise_name);

        // List of already existing exercises
        exerciseList = findViewById(R.id.existing_exercises_spinner);
        ArrayAdapter<CharSequence> exerciseAdapter = ArrayAdapter.createFromResource(this, R.array.sample_exercises, android.R.layout.simple_spinner_item);
        exerciseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exerciseList.setAdapter(exerciseAdapter);


        // Spinner of types of exercises - tempo and isometric
        exerciseTypes = findViewById(R.id.exercise_types_spinner);
        ArrayAdapter<CharSequence> exerciseTypeAdapter = ArrayAdapter.createFromResource(this, R.array.exercise_types, android.R.layout.simple_spinner_item);
        exerciseTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exerciseTypes.setAdapter(exerciseTypeAdapter);

        addExistingExercise = findViewById(R.id.confirm_exercise2);
        addExistingExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                Exercise exercise;
                //TODO similar things as below
                Log.i("Exercise name", exerciseList.getSelectedItem().toString());
                switch(exerciseList.getSelectedItem().toString()){
                    case "Deadlift":{
                        exercise = new Exercise.TempoExercise("Deadlift",DEFAULT_SETS,DEFAULT_REPS,DEFAULT_ECCENTRIC,DEFAULT_ECCENTRIC_PAUSE,DEFAULT_CONCENTRIC,DEFAULT_CONCENTRIC_PAUSE);
                        break;
                    }
                    case "Plank":{
                        exercise = new Exercise.IsometricExercise("Plank", DEFAULT_SETS, DEFAULT_TIME);
                        break;
                    }
                    default:{ //@TODO ensure this never occurs
                        exercise = new Exercise(exerciseName.getText().toString(), 5);
                        break;
                    }
                }
                resultIntent.putExtra("exercise", exercise);
                setResult(AppCompatActivity.RESULT_OK, resultIntent);
                Toast.makeText(getApplicationContext(), "Exercise " + exercise.getName() + " added.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        addNewExercise = findViewById(R.id.confirm_exercise);
        addNewExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                Exercise exercise;
                switch (exerciseTypes.getSelectedItem().toString()){ //@TODO use enum instead
                    //@TODO handle the case where exerciseName is empty
                    case "Isometric":{
                        exercise = new Exercise.IsometricExercise(exerciseName.getText().toString(),DEFAULT_SETS,DEFAULT_TIME);
                        break;
                    }
                    default:{//tempo
                        exercise = new Exercise.TempoExercise(exerciseName.getText().toString(), DEFAULT_SETS, DEFAULT_REPS, DEFAULT_ECCENTRIC,DEFAULT_ECCENTRIC_PAUSE,DEFAULT_CONCENTRIC,DEFAULT_CONCENTRIC_PAUSE);
                        break;
                    }
                }
                resultIntent.putExtra("exercise", exercise);
                setResult(AppCompatActivity.RESULT_OK, resultIntent);
                Toast.makeText(getApplicationContext(), "Exercise " + exercise.getName() + " added.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}