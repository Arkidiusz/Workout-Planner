package com.example.workoutplanner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

//TODO create a method for instantiating Exercises with default values
public class AddExerciseActivity extends AppCompatActivity {

    // Default values for new exercises
    private static final int DEFAULT_SETS = 5;
    private static final int DEFAULT_REPS = 5;
    private static final int DEFAULT_ECCENTRIC = 3;
    private static final int DEFAULT_ECCENTRIC_PAUSE = 0;
    private static final int DEFAULT_CONCENTRIC = 1;
    private static final int DEFAULT_CONCENTRIC_PAUSE = 0;
    private static final int DEFAULT_TIME = 60;

    private EditText etExerciseName;
    private Spinner spnExercises;
    private Spinner spnExerciseTypes;
    private ExerciseViewModel exerciseViewModel;
    private List<Exercise> exercises = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        etExerciseName = findViewById(R.id.et_exercise_name);

        // List of already existing exercises in the database
        spnExercises = findViewById(R.id.spn_existing_exercises);
//        final ArrayAdapter<CharSequence> exerciseAdapter = ArrayAdapter.createFromResource(this, R.array.sample_exercises, android.R.layout.simple_spinner_item);

//        exerciseAdapter.addAll(exercises);
////        exerciseAdapter.notifyDataSetChanged();
        final ArrayAdapter<Exercise> exerciseAdapter = new ArrayAdapter<Exercise>(this,
                android.R.layout.simple_spinner_item, exercises);
        exerciseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnExercises.setAdapter(exerciseAdapter);

        exerciseViewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);
        exerciseViewModel.getAllExercises().observe(this, new Observer<List<Exercise>>() {
            @Override
            public void onChanged(List<Exercise> exercises) {
                AddExerciseActivity.this.exercises.clear();
                AddExerciseActivity.this.exercises.addAll(exercises);
                exerciseAdapter.notifyDataSetChanged();
            }
        });

        // Spinner of types of exercises - tempo and isometric
        spnExerciseTypes = findViewById(R.id.spn_exercise_types);
        ArrayAdapter<CharSequence> exerciseTypeAdapter = ArrayAdapter.createFromResource(this, R.array.exercise_types, android.R.layout.simple_spinner_item);
        exerciseTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnExerciseTypes.setAdapter(exerciseTypeAdapter);

        Button btnAddExistingExercise = findViewById(R.id.btn_confirm_existing_exercise);
        btnAddExistingExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                ExercisePlan exercisePlan;
                //TODO similar things as below
                Log.i("ExercisePlan name", spnExercises.getSelectedItem().toString());
                switch (spnExercises.getSelectedItem().toString()) {
                    case "Deadlift": {
                        exercisePlan = new ExercisePlan.TempoExercisePlan("Deadlift", DEFAULT_SETS, DEFAULT_REPS, DEFAULT_ECCENTRIC, DEFAULT_ECCENTRIC_PAUSE, DEFAULT_CONCENTRIC, DEFAULT_CONCENTRIC_PAUSE);
                        break;
                    }
                    case "Plank": {
                        exercisePlan = new ExercisePlan.IsometricExercisePlan("Plank", DEFAULT_SETS, DEFAULT_TIME);
                        break;
                    }
                    default: { //@TODO ensure this never occurs
                        exercisePlan = new ExercisePlan(etExerciseName.getText().toString(), 5);
                        break;
                    }
                }
                resultIntent.putExtra("exercisePlan", exercisePlan);
                setResult(AppCompatActivity.RESULT_OK, resultIntent);
                Toast.makeText(getApplicationContext(), "ExercisePlan " + exercisePlan.getName() + " added.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        Button btnAddNewExercise = findViewById(R.id.btn_confirm_new_exercise);
        btnAddNewExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                ExercisePlan exercisePlan;
                switch (spnExerciseTypes.getSelectedItem().toString()) { //@TODO use enum instead
                    //@TODO handle the case where exerciseName is empty
                    case "Isometric": {
                        exercisePlan = new ExercisePlan.IsometricExercisePlan(etExerciseName.getText().toString(), DEFAULT_SETS, DEFAULT_TIME);
                        break;
                    }
                    default: {//tempo
                        exercisePlan = new ExercisePlan.TempoExercisePlan(etExerciseName.getText().toString(),
                                DEFAULT_SETS, DEFAULT_REPS, DEFAULT_ECCENTRIC,
                                DEFAULT_ECCENTRIC_PAUSE, DEFAULT_CONCENTRIC, DEFAULT_CONCENTRIC_PAUSE);
                        break;
                    }
                }
                resultIntent.putExtra("exercisePlan", exercisePlan);
                setResult(AppCompatActivity.RESULT_OK, resultIntent);
                Toast.makeText(getApplicationContext(), "ExercisePlan " + exercisePlan.getName() + " added.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}