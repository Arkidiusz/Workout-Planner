package com.example.workout_planner.activities.plan;

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

import com.example.workout_planner.R;
import com.example.workout_planner.activities.add_exercise.AddExerciseActivity;
import com.example.workout_planner.database.ExercisePlan;
import com.example.workout_planner.database.workout_plan.WorkoutPlan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

/**
 * Activity for planning a workout session
 */
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
        mExercisesAdapter = new ExercisesAdapter(this);
        rvExercises.setAdapter(mExercisesAdapter);
        rvExercises.setLayoutManager(new LinearLayoutManager(this));
        rvExercises.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        // Drag rv items to reorder
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
                    return false;
                }
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) { }
        });
        itemTouchHelper.attachToRecyclerView(rvExercises);

        // Button used to save planned workout session
        Button btnSaveWorkout = findViewById(R.id.btn_save);
        btnSaveWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String workoutName = etWorkoutName.getText().toString();
                if (workoutName.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter the name.",
                            Toast.LENGTH_SHORT).show();
                } else if(!isWorkoutNameFree(workoutName)) {
                    Toast.makeText(getApplicationContext(), "This workout name is already used.",
                            Toast.LENGTH_SHORT).show();
                } else if(isPlanDataProvided(rvExercises)) {
                    for (int i = 0; i < rvExercises.getChildCount(); i++) {
                        RecyclerView.ViewHolder holder =
                                rvExercises.findViewHolderForAdapterPosition(i);
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
//                            Log.i("holder", isometricViewHolder.toString());
//                            Log.i("exercisePlan", exercisePlans.get(i).getExercise().toString());
//                            Log.i("i", Integer.toString(i));
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

    // Check if user has provided sufficient input to create a WorkoutPlan
    private boolean isPlanDataProvided(RecyclerView rvExercises){
        for (int i = 0; i < rvExercises.getChildCount(); i++) {
            RecyclerView.ViewHolder holder =
                    rvExercises.getChildViewHolder(rvExercises.getChildAt(i));
            if (holder instanceof ExercisesAdapter.TempoViewHolder) {
                ExercisesAdapter.TempoViewHolder tempoViewHolder =
                        (ExercisesAdapter.TempoViewHolder) holder;
                String noSets = tempoViewHolder.etNoSets.getText().toString();
                String noReps = tempoViewHolder.etNoReps.getText().toString();
                String tempo = tempoViewHolder.etTempo.getText().toString();
                if(noSets.isEmpty() || noReps.isEmpty() || tempo.isEmpty()){
                   Toast.makeText(PlanActivity.this, "Fill in all information.",
                           Toast.LENGTH_SHORT).show();
                   return false;
                }
                if(!noSets.matches("\\d+") || !noReps.matches("\\d+")
                        || !isTempoOfCorrectFormat(tempo)){
                    Toast.makeText(PlanActivity.this, "Incorrect data provided. Only use " +
                                    "integers, and tempo must consist of 4 digits",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else if (holder instanceof ExercisesAdapter.IsometricViewHolder) {
                ExercisesAdapter.IsometricViewHolder isometricViewHolder =
                        (ExercisesAdapter.IsometricViewHolder) holder;
                String noSets = isometricViewHolder.etNoSets.getText().toString();
                String time = isometricViewHolder.etTime.getText().toString();
                if(noSets.isEmpty() || time.isEmpty()){
                    Toast.makeText(PlanActivity.this, "Fill in all information.",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                if(!noSets.matches("\\d+") || !time.matches("\\d+")){
                    Toast.makeText(PlanActivity.this, "Incorrect data provided. Only use " +
                                    "integers.",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        return true;
    }

    // Check if string represents tempo (it is 4 digits long)
    private boolean isTempoOfCorrectFormat(String tempo){
        char[] tempoCharacters = tempo.toCharArray();
        if(tempoCharacters.length != 4) return false;
        for (char tempoCharacter : tempoCharacters) {
            if (!Character.isDigit(tempoCharacter)) return false;
        }
        return true;
    }

    private boolean isWorkoutNameFree(String workoutName){
        Intent intent = getIntent();
        ArrayList<String> workoutNames = (ArrayList<String>) intent.getSerializableExtra("workoutNames");
        if(workoutNames == null || workoutNames.isEmpty()) return true;
        for(String name : workoutNames){
            if(workoutName.equals(name)) return false;
        }
        return true;
    }

    public class ExercisesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TEMPO_EXERCISE = 1;
        private static final int ISOMETRIC_EXERCISE = 2;
        private final Context mContext;

        public ExercisesAdapter(Context mContext) {
            this.mContext = mContext;
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

        public class TempoViewHolder extends RecyclerView.ViewHolder {

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

        public class IsometricViewHolder extends RecyclerView.ViewHolder {

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