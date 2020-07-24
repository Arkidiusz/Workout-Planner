package com.example.workoutplanner.activities.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutplanner.R;
import com.example.workoutplanner.activities.Plan.PlanActivity;
import com.example.workoutplanner.activities.WorkoutSession.WorkoutSessionActivity;
import com.example.workoutplanner.database.ExercisePlan;
import com.example.workoutplanner.database.WorkoutPlan.WorkoutPlan;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Display a list of workoutPlans that can be accessed and added.
 */
public class MainActivity extends AppCompatActivity {

    public static final int WORKOUT_REQUEST = 1;
    private List<WorkoutPlan> workoutPlans = new ArrayList<>();
    private WorkoutsAdapter mWorkoutsAdapter;
    private WorkoutPlanViewModel workoutPlanViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rvWorkouts = findViewById(R.id.rv_workouts);
        mWorkoutsAdapter = new WorkoutsAdapter(this, workoutPlans);
        rvWorkouts.setAdapter(mWorkoutsAdapter);
        rvWorkouts.setLayoutManager(new LinearLayoutManager(this));

        workoutPlanViewModel = new ViewModelProvider(this).get(WorkoutPlanViewModel.class);
        workoutPlanViewModel.getAllWorkoutPlans().observe(this, new Observer<List<WorkoutPlan>>() {
            @Override
            public void onChanged(List<WorkoutPlan> workoutPlans) {
                mWorkoutsAdapter.setWorkoutList(workoutPlans);
            }
        });

        //Setup FAB for adding workoutPlans
        FloatingActionButton fabAddWorkout = findViewById(R.id.btn_add_workout);
        fabAddWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PlanActivity.class);
                startActivityForResult(intent, WORKOUT_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == WORKOUT_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    workoutPlanViewModel.insert((WorkoutPlan) data.getSerializableExtra("workoutPlan"));
                }
            }
        }
    }

    public class WorkoutsAdapter extends RecyclerView.Adapter<WorkoutsAdapter.WorkoutsViewHolder> {

        private final Context mContext;
        private List<WorkoutPlan> workoutPlans;

        public WorkoutsAdapter(Context mContext, List<WorkoutPlan> workoutPlans) {
            this.mContext = mContext;
            this.workoutPlans = workoutPlans;
        }

        @NonNull
        @Override
        public WorkoutsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.item_workout, parent, false);
            return new WorkoutsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull WorkoutsViewHolder holder, int position) {
            WorkoutPlan workoutPlan = workoutPlans.get(position);

            holder.tvWorkoutName.setText(workoutPlan.getName());

            // Build a String of all exercises of a WorkoutPlan to be displayed
            StringBuilder exercises = new StringBuilder();
            boolean firstExercise = true;
            for (ExercisePlan exercisePlan : workoutPlan.getExercisePlans()) {
                if (!firstExercise) {
                    exercises.append(", ");
                }
                exercises.append(exercisePlan.getExercise().getName());
                firstExercise = false;
            }
            exercises.lastIndexOf(exercises.toString());
            holder.tvExercises.setText(exercises);
        }

        void setWorkoutList(List<WorkoutPlan> workoutPlans) {
            this.workoutPlans = workoutPlans;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            if (workoutPlans != null)
                return workoutPlans.size();
            else return 0;
        }

        public class WorkoutsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView tvWorkoutName;
            private TextView tvExercises;
            private CardView cv;

            public WorkoutsViewHolder(@NonNull View itemView) {
                super(itemView);
                tvWorkoutName = itemView.findViewById(R.id.tv_workout_name);
                tvExercises = itemView.findViewById(R.id.tv_exercises);
                cv = itemView.findViewById(R.id.cv_item_workout);
                cv.setOnClickListener(this);
            }

            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, WorkoutSessionActivity.class);
                intent.putExtra("workoutPlan", (Serializable) workoutPlans.get(getAdapterPosition()));
                startActivity(intent);
            }
        }
    }
}
