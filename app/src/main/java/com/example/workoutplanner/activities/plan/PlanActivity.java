package com.example.workoutplanner.activities.plan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutplanner.R;
import com.example.workoutplanner.activities.add_exercise.AddExerciseActivity;
import com.example.workoutplanner.database.ExercisePlan;
import com.example.workoutplanner.database.workout_plan.WorkoutPlan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class PlanActivity extends AppCompatActivity {

    public static final int EXERCISE_REQUEST = 2;
    private EditText etWorkoutName;
    private ExercisesAdapter mExercisesAdapter;
    private ArrayList<ExercisePlan> exercisePlans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        etWorkoutName = findViewById(R.id.et_workout_name);

        Button btnAddExercise = findViewById(R.id.btn_add_exercise);
        btnAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlanActivity.this,
                        AddExerciseActivity.class);
                startActivityForResult(intent, EXERCISE_REQUEST);
            }
        });

        // List of planned Exercises for a workout
        final RecyclerView rvExercises = findViewById(R.id.rv_exercises);
        exercisePlans = new ArrayList<>();//testing sample
//        exercisePlans.add(new ExercisePlan.TempoExercisePlan("Bench Press", 5, 5, 4, 0, 1, 0));
//        exercisePlans.add(new ExercisePlan.IsometricExercisePlan("Plank", 5, 60));
        mExercisesAdapter = new ExercisesAdapter(this, exercisePlans);
        rvExercises.setAdapter(mExercisesAdapter);
        rvExercises.setLayoutManager(new LinearLayoutManager(this));
        rvExercises.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        // Drag RV items to reorder
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START
                        | ItemTouchHelper.END, 0) {
            @Override
            // Swap positions of the items moved
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                Collections.swap(exercisePlans, fromPosition, toPosition);
                if (recyclerView.getAdapter() == null) {
                    throw new NullPointerException("Unexpected null adapter");
                } else {
                    recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
                }
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // do nothing;
            }
        });
        itemTouchHelper.attachToRecyclerView(rvExercises);

        // Button used to save created workout session
        Button btnSaveWorkout = findViewById(R.id.btn_save);
        btnSaveWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String workoutName = etWorkoutName.getText().toString();
                if (workoutName.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter the name.",
                            Toast.LENGTH_SHORT).show();
                } else if(!workoutNameFree(workoutName)) {
                    Toast.makeText(getApplicationContext(), "This workout name is already used.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < rvExercises.getChildCount(); i++) {
                        RecyclerView.ViewHolder holder =
                                rvExercises.getChildViewHolder(rvExercises.getChildAt(i));
                        if (holder instanceof ExercisesAdapter.TempoViewHolder) {
                            ExercisesAdapter.TempoViewHolder tempoViewHolder =
                                    (ExercisesAdapter.TempoViewHolder) holder;
                            ExercisePlan.TempoExercisePlan exercisePlan =
                                    (ExercisePlan.TempoExercisePlan) exercisePlans.get(i);
                            exercisePlan.setNoSets(Integer.parseInt(tempoViewHolder
                                    .etNoSets.getText().toString()));
                            exercisePlan.setNoReps(Integer.parseInt(tempoViewHolder
                                    .etNoReps.getText().toString()));
                            exercisePlan.parseAndSetTempo(tempoViewHolder
                                    .etTempo.getText().toString());

                        } else if (holder instanceof ExercisesAdapter.IsometricViewHolder) {
                            ExercisesAdapter.IsometricViewHolder isometricViewHolder =
                                    (ExercisesAdapter.IsometricViewHolder) holder;
                            ExercisePlan.IsometricExercisePlan exercisePlan =
                                    (ExercisePlan.IsometricExercisePlan) exercisePlans.get(i);
                            exercisePlan.setNoSets(Integer.parseInt(isometricViewHolder
                                    .etNoSets.getText().toString()));
                            exercisePlan.setTime(Integer.parseInt(isometricViewHolder.etTime.getText().toString()));
                        }
                    }
                    WorkoutPlan workoutPlan = new WorkoutPlan(etWorkoutName.getText().toString(),
                            exercisePlans);
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("workoutPlan", workoutPlan);
                    setResult(AppCompatActivity.RESULT_OK, resultIntent);
                    Toast.makeText(getApplicationContext(), "WorkoutPlan " + workoutName
                            + " saved.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EXERCISE_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    exercisePlans.add((ExercisePlan) data.getSerializableExtra("exercisePlan"));
                    mExercisesAdapter.notifyItemInserted(exercisePlans.size() - 1);
                }
            }
        }
    }

    private boolean workoutNameFree(String workoutName){
        Intent intent = getIntent();
        Log.i("XD", "called");
        ArrayList<String> workoutNames = (ArrayList<String>) intent.getSerializableExtra("workoutNames");
        for(String name : workoutNames){
            Log.i("XD", name);
            if(workoutName.equals(name)) return true;
        }
        return false;
    }

    public static class ExercisesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TEMPO_EXERCISE = 1;
        private static final int ISOMETRIC_EXERCISE = 2;
        private final Context mContext;
        private final ArrayList<ExercisePlan> exercisePlans;

        public ExercisesAdapter(Context mContext, ArrayList<ExercisePlan> exercisePlans) {
            this.mContext = mContext;
            this.exercisePlans = exercisePlans;
        }

        @Override
        public int getItemViewType(int position) {
            if (exercisePlans.get(position) instanceof ExercisePlan.TempoExercisePlan) {
                return TEMPO_EXERCISE;
            } else {
                return ISOMETRIC_EXERCISE;
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            switch (viewType) {
                case 1: {
                    View view = inflater.inflate(R.layout.item_tempo_exercise, parent, false);
                    return new TempoViewHolder(view);
                }
                case 2: {
                    View view = inflater.inflate(R.layout.item_isometric_exercise, parent, false);
                    return new IsometricViewHolder(view);
                }
                default: {
                    throw new IllegalStateException("Unexpected viewType " + viewType);
                }
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            switch (holder.getItemViewType()) {
                case 1: {
                    final ExercisePlan.TempoExercisePlan exercise
                            = (ExercisePlan.TempoExercisePlan) exercisePlans.get(position);
                    TempoViewHolder tempo_holder = (TempoViewHolder) holder;
                    tempo_holder.tvExerciseName.setText(exercise.getExercise().getName());
                    tempo_holder.etNoSets.setText(String.format(Locale.getDefault(), "%d",
                            exercise.getNoSets()));
                    tempo_holder.etNoReps.setText(String.format(Locale.getDefault(), "%d",
                            exercise.getNoReps()));
                    String sb = exercise.getTempo();
                    tempo_holder.etTempo.setText(sb);
                    tempo_holder.btnRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int position = exercisePlans.indexOf(exercise);
                            exercisePlans.remove(exercise);
                            notifyItemRemoved(position);
                        }
                    });
                    break;
                }
                case 2: {
                    final ExercisePlan.IsometricExercisePlan exercise
                            = (ExercisePlan.IsometricExercisePlan) exercisePlans.get(position);
                    IsometricViewHolder isometric_holder = (IsometricViewHolder) holder;
                    Log.i("XD", exercise.getExercise().toString());
                    isometric_holder.tvExerciseName.setText(exercise.getExercise().getName());
                    isometric_holder.etNoSets.setText(String.format(Locale.getDefault(), "%d",
                            exercise.getNoSets()));
                    isometric_holder.etTime.setText(String.format(Locale.getDefault(), "%d",
                            exercise.getTime()));
                    isometric_holder.btnRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int position = exercisePlans.indexOf(exercise);
                            exercisePlans.remove(exercise);
                            notifyItemRemoved(position);
                        }
                    });
                    break;
                }
            }
        }

        @Override
        public int getItemCount() {
            return exercisePlans.size();
        }

        public static class TempoViewHolder extends RecyclerView.ViewHolder {

            private TextView tvExerciseName;
            private EditText etNoSets;
            private EditText etNoReps;
            private EditText etTempo;
            private ImageButton btnRemove;

            public TempoViewHolder(@NonNull View itemView) {
                super(itemView);
                tvExerciseName = itemView.findViewById(R.id.tv_exercise_name);
                etNoSets = itemView.findViewById(R.id.et_no_sets);
                etNoReps = itemView.findViewById(R.id.et_no_reps);
                etTempo = itemView.findViewById(R.id.et_tempo);
                btnRemove = itemView.findViewById(R.id.btn_remove_exercise);
            }
        }

        public static class IsometricViewHolder extends RecyclerView.ViewHolder {

            private TextView tvExerciseName;
            private EditText etNoSets;
            private EditText etTime;
            private ImageButton btnRemove;

            public IsometricViewHolder(@NonNull View itemView) {
                super(itemView);
                tvExerciseName = itemView.findViewById(R.id.tv_iso_exercise_name);
                etNoSets = itemView.findViewById(R.id.et_iso_no_sets);
                etTime = itemView.findViewById(R.id.et_iso_time);
                btnRemove = itemView.findViewById(R.id.btn_remove_iso_exercise);
            }
        }
    }
}