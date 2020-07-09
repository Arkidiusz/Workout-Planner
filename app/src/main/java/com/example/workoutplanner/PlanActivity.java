package com.example.workoutplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.media.MediaRouter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class PlanActivity extends AppCompatActivity {

    private EditText workoutNameET;

    private ExercisesAdapter exercisesAdapter;
    private ArrayList<Exercise> exercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);



        workoutNameET = findViewById(R.id.editTextWorkoutName);

        // Button used to save created workout session
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

        // List of planned Exercises for a workout
        RecyclerView exercisesRV = findViewById(R.id.exercisesRV);
        exercises = new ArrayList<>();//testing sample
        exercises.add(new Exercise.TempoExercise("Bench Press", 5, 5, 4, 0, 1, 0));
        exercises.add(new Exercise.IsometricExercise("Plank",5, 60));
        exercisesAdapter = new ExercisesAdapter(this, exercises);
        exercisesRV.setAdapter(exercisesAdapter);
        exercisesRV.setLayoutManager(new LinearLayoutManager(this));
        exercisesRV.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        // Drag RV items to reorder
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
            @Override
            // Swap positions of the items moved
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                Collections.swap(exercises, fromPosition, toPosition);
                recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // do nothing;
            }
        });
        itemTouchHelper.attachToRecyclerView(exercisesRV);
    }

    public class ExercisesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private final Context context;
        private final ArrayList<Exercise> exercises;

        private static final int TEMPO_EXERCISE = 1;
        private static final int ISOMETRIC_EXERCISE = 2;

        public ExercisesAdapter(Context context, ArrayList<Exercise> exercises){
            Log.i("A", "startAdapter");
            this.context = context;
            this.exercises = exercises;
        }

        @Override
        public int getItemViewType(int position) {
            Log.i("A", "position");
            if(exercises.get(position) instanceof  Exercise.TempoExercise){
                return TEMPO_EXERCISE;
            }
            else{ // Isometric Exercise
                return ISOMETRIC_EXERCISE;
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Log.i("B", "Creating");
            LayoutInflater inflater = LayoutInflater.from(context);
            switch(viewType){
                case 1: {
                    Log.i("B", "Tempo");
                    View view = inflater.inflate(R.layout.tempo_exercise_row, parent, false);
                    return new TempoViewHolder(view);
                }
                case 2:{
                    View view = inflater.inflate(R.layout.isometric_exercise_row, parent, false);
                    return new IsometricViewHolder(view);
                }
            }
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Log.i("A", "Binding");
            switch (holder.getItemViewType()){
                case 1:{
                    Log.i("A", "Tempo");
                    Exercise.TempoExercise exercise = (Exercise.TempoExercise) exercises.get(position);
                    TempoViewHolder tempo_holder = (TempoViewHolder) holder;
                    tempo_holder.exerciseName.setText(exercise.getName());
                    Log.i("A", Integer.toString(exercise.getNoSets()));
                    tempo_holder.no_sets.setText(Integer.toString(exercise.getNoSets()));
                    tempo_holder.no_reps.setText(Integer.toString(exercise.getNoReps()));
                    StringBuilder sb = new StringBuilder();
                    sb.append(exercise.getEccentric()).append(exercise.getEccentricPause()).append(exercise.getConcentric()).append(exercise.getConcentricPause());
                    tempo_holder.tempo.setText(sb.toString());
                    break;
                }
                case 2:{
                    Exercise.IsometricExercise exercise = (Exercise.IsometricExercise) exercises.get(position);
                    IsometricViewHolder isometric_holder = (IsometricViewHolder) holder;
                    isometric_holder.exerciseName.setText(exercise.getName());
                    isometric_holder.no_sets.setText(Integer.toString(exercise.getNoSets()));
                    isometric_holder.time.setText(Integer.toString(exercise.getTime()));
                    break;
                }
            }
        }

        @Override
        public int getItemCount() {
            return exercises.size();
        }

        public class TempoViewHolder extends RecyclerView.ViewHolder{

            private TextView exerciseName;
            private EditText no_sets;
            private EditText no_reps;
            private EditText tempo;

            public TempoViewHolder(@NonNull View itemView) {
                super(itemView);
                exerciseName = itemView.findViewById(R.id.exercise_name);
                no_sets = itemView.findViewById(R.id.no_sets);
                no_reps = itemView.findViewById(R.id.no_reps);
                tempo = itemView.findViewById(R.id.tempo);
            }
        }

        public class IsometricViewHolder extends RecyclerView.ViewHolder{

            private TextView exerciseName;
            private EditText no_sets;
            private EditText time;

            public IsometricViewHolder(@NonNull View itemView) {
                super(itemView);
                exerciseName = itemView.findViewById(R.id.iso_exercise_name);
                no_sets = itemView.findViewById(R.id.iso_no_sets);
                time = itemView.findViewById(R.id.time);
            }
        }
    }
}