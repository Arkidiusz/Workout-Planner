package com.example.workoutplanner.activities.add_exercise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.workoutplanner.R;
import com.example.workoutplanner.database.exercise.Exercise;
import com.example.workoutplanner.database.ExercisePlan;

import java.util.ArrayList;
import java.util.List;

/**
 * Add new ExercisePlan to a WorkoutPlan, either from existing Exercising or newly created
 */
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
        final ArrayAdapter<Exercise> exerciseAdapter = new ArrayAdapter<>(this,
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

        // Button to add exercises from a list of already existing ones
        Button btnAddExistingExercise = findViewById(R.id.btn_confirm_existing_exercise);
        btnAddExistingExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                Exercise exercise = (Exercise) spnExercises.getSelectedItem();
                ExercisePlan exercisePlan;
                switch (exercise.getExerciseType()) {
                    case ISOMETRIC: {
                        exercisePlan = new ExercisePlan.IsometricExercisePlan(exercise,
                                DEFAULT_SETS, DEFAULT_TIME);
                        break;
                    }
                    case TEMPO: {
                        exercisePlan = new ExercisePlan
                                .TempoExercisePlan((Exercise) spnExercises.getSelectedItem(),
                                DEFAULT_SETS, DEFAULT_REPS, DEFAULT_ECCENTRIC,
                                DEFAULT_ECCENTRIC_PAUSE, DEFAULT_CONCENTRIC,
                                DEFAULT_CONCENTRIC_PAUSE);
                        break;
                    }
                    default:
                        throw new IllegalStateException("Unexpected exerciseType enum.");
                }

                resultIntent.putExtra("exercisePlan", exercisePlan);
                setResult(AppCompatActivity.RESULT_OK, resultIntent);
                Toast.makeText(getApplicationContext(),
                        "ExercisePlan " + exercisePlan.getExercise().getName() + " " +
                                "added.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Spinner of types of exercises - tempo and isometric
        spnExerciseTypes = findViewById(R.id.spn_exercise_types);
        ArrayAdapter<Exercise.ExerciseType> exerciseTypeAdapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item, Exercise.ExerciseType.values());
        exerciseTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnExerciseTypes.setAdapter(exerciseTypeAdapter);

        // Button for creating new Exercise and adding it to a workout
        Button btnAddNewExercise = findViewById(R.id.btn_confirm_new_exercise);
        btnAddNewExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                String exerciseName = etExerciseName.getText().toString();
                if (exerciseName.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Specify Exercise name",
                            Toast.LENGTH_SHORT).show();

                } else if (!isExerciseNameFree(exerciseName)) {
                    Toast.makeText(getApplicationContext(), "Exercise name " + exerciseName +
                            " is already used.", Toast.LENGTH_SHORT).show();
                } else {
                    Exercise.ExerciseType exerciseType =
                            (Exercise.ExerciseType) spnExerciseTypes.getSelectedItem();
                    Exercise exercise = new Exercise(exerciseName, exerciseType);
                    ExercisePlan exercisePlan;
                    switch (exerciseType) {
                        case ISOMETRIC:
                            exerciseViewModel.insert(exercise);
                            exercisePlan = new ExercisePlan.IsometricExercisePlan(exercise,
                                    DEFAULT_SETS, DEFAULT_TIME);
                            break;
                        case TEMPO:
                            exerciseViewModel.insert(exercise);
                            exercisePlan = new ExercisePlan.TempoExercisePlan(exercise,
                                    DEFAULT_SETS, DEFAULT_REPS, DEFAULT_ECCENTRIC,
                                    DEFAULT_ECCENTRIC_PAUSE, DEFAULT_CONCENTRIC,
                                    DEFAULT_CONCENTRIC_PAUSE);
                            break;
                        default:
                            throw new IllegalStateException("Unexpected exerciseType enum");
                    }
                    resultIntent.putExtra("exercisePlan", exercisePlan);
                    setResult(AppCompatActivity.RESULT_OK, resultIntent);
                    Toast.makeText(getApplicationContext(),
                            exercisePlan.getExercise().getName() + " added to workout plan.",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    // Check if given name is not already used by another Exercise
    private boolean isExerciseNameFree(String string) {
        List<Exercise> exercises = exerciseViewModel.getAllExercises().getValue();
        if (exercises == null || exercises.isEmpty()) {
            return true;
        } else {
            for (Exercise exercise : exercises) {
                if (exercise.getName().equals(string))
                    return false;
            }
        }
        return true;
    }
}