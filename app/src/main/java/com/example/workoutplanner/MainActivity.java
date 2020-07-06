package com.example.workoutplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

/**
 * Display a list of workouts that can be added, modified and deleted by the user.
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView workoutsRecyclerView;
    private String[] workouts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup RecyclerView of Workouts
        workouts = getResources().getStringArray(R.array.sample_workouts);
        workoutsRecyclerView = findViewById(R.id.workoutsRecyclerView);
        WorkoutsAdapter workoutsAdapter = new WorkoutsAdapter(this, workouts);
        workoutsRecyclerView.setAdapter(workoutsAdapter);
        workoutsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public class WorkoutsAdapter extends RecyclerView.Adapter<WorkoutsAdapter.WorkoutsViewHolder>{

        private final Context context;
        private final String[] workoutDays;

        public WorkoutsAdapter(Context context, String[] workoutDays){
            this.context = context;
            this.workoutDays = workoutDays;
        }

        @NonNull
        @Override
        public WorkoutsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.wd_row, parent, false);
            return new WorkoutsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull WorkoutsViewHolder holder, int position) {
            holder.workoutName.setText(workoutDays[position]);
        }

        @Override
        public int getItemCount() {
            return workoutDays.length;
        }

        public class WorkoutsViewHolder extends RecyclerView.ViewHolder{

            TextView workoutName;

            public WorkoutsViewHolder(@NonNull View itemView) {
                super(itemView);
                workoutName = itemView.findViewById(R.id.workout_name);
            }
        }
    }
}