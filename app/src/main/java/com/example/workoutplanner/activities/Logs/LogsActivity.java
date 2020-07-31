package com.example.workoutplanner.activities.Logs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workoutplanner.R;
import com.example.workoutplanner.database.Exercise.Exercise;
import com.example.workoutplanner.database.ExerciseLog.ExerciseLog;
import com.example.workoutplanner.database.ExerciseLog.ExerciseLogDao;
import com.example.workoutplanner.database.WorkoutPlan.WorkoutPlan;
import com.example.workoutplanner.database.WorkoutPlannerDatabase;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LogsActivity extends AppCompatActivity {

    private List<ExerciseLog> exerciseLogs;
    private List<String> exercises;
    private List<LocalDate> dates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        Thread threadExerciseNames = new Thread(new Runnable() {
            @Override
            public void run() {
                WorkoutPlannerDatabase db = WorkoutPlannerDatabase.getDataBase(getApplication());
                ExerciseLogDao exerciseLogDao = db.exerciseLogDao();
                exercises = exerciseLogDao.getAllExerciseNames();
            }
        });
        threadExerciseNames .start();
        while(exercises == null){
            try {
                //TODO let user know that he needs to wait, i.e. loading screen
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Log.e("Exception", Arrays.toString(e.getStackTrace())); //TODO handle appropriately
            }
        }

        Spinner spinner = findViewById(R.id.spn_exercise_logs);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, exercises);
        spinner.setAdapter(adapter);


        final String selectedExercise = spinner.getSelectedItem().toString();
        Thread threadExerciseLogs = new Thread(new Runnable() {
            @Override
            public void run() {
                WorkoutPlannerDatabase db = WorkoutPlannerDatabase.getDataBase(getApplication());
                ExerciseLogDao exerciseLogDao = db.exerciseLogDao();
                exerciseLogs = exerciseLogDao.getExerciseLogsOfName(selectedExercise);
                dates = exerciseLogDao.getDatesOfExercise(selectedExercise);
            }
        });
        threadExerciseLogs.start();
        while(exerciseLogs == null || dates == null){
            try {
                Log.i("thread", "sleep");
                //TODO let user know that he needs to wait, i.e. loading screen
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Log.e("Exception", Arrays.toString(e.getStackTrace())); //TODO handle appropriately
            }
        }
        final RecyclerView recyclerView = findViewById(R.id.rv_exercise_logs);
        final ExerciseLogsAdapter exerciseLogsAdapter = new ExerciseLogsAdapter(this, dates, exerciseLogs);
        dates = null;
        exerciseLogs = null;
        recyclerView.setAdapter(exerciseLogsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                final String selectedExercise = adapterView.getItemAtPosition(pos).toString();
                Thread threadExerciseLogs = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        WorkoutPlannerDatabase db = WorkoutPlannerDatabase.getDataBase(getApplication());
                        ExerciseLogDao exerciseLogDao = db.exerciseLogDao();
                        exerciseLogs = exerciseLogDao.getExerciseLogsOfName(selectedExercise);
                        dates = exerciseLogDao.getDatesOfExercise(selectedExercise);
                    }
                });
                threadExerciseLogs.start();
                while(exerciseLogs == null || dates == null){
                    try {
                        Log.i("thread", "sleep");
                        //TODO let user know that he needs to wait, i.e. loading screen
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Log.e("Exception", Arrays.toString(e.getStackTrace())); //TODO handle appropriately
                    }
                }
                exerciseLogsAdapter.setExerciseLogs(dates, exerciseLogs);
                dates = null;
                exerciseLogs = null;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }

    private static class ExerciseLogsAdapter extends RecyclerView.Adapter<ExerciseLogsAdapter.ExerciseLogsViewHolder>  {

        private Context context;
        private List<LocalDate> dates;
        private List<ExerciseLog> exerciseLogs;

        public ExerciseLogsAdapter(Context context, List<LocalDate> dates, List<ExerciseLog> exerciseLogs){
            this.context = context;
            this.dates = dates;
            this.exerciseLogs = exerciseLogs;
        }

        @NonNull
        @Override
        public ExerciseLogsAdapter.ExerciseLogsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.item_exercise_log, parent, false);
            return new ExerciseLogsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ExerciseLogsAdapter.ExerciseLogsViewHolder holder, int position) {
            if(position < dates.size()){
                LocalDate date = dates.get(position);
                holder.tvDate.setText(date.toString());
                ArrayList<ExerciseLog> exerciseLogs = new ArrayList<>();

                for(ExerciseLog exerciseLog : this.exerciseLogs){
                    if(exerciseLog.getDate().isEqual(date)){
                        exerciseLogs.add(exerciseLog);
                    }
                }
                String exerciseSets = "";
                StringBuilder stringBuilder = new StringBuilder();
                for(int i = 0; i < exerciseLogs.size(); i++){
                    ExerciseLog exerciseLog = exerciseLogs.get(i);
                    stringBuilder.append("Set ").append(i + 1).append(": ").append(exerciseLog.getWeight()).append("kg x ");
                    if(exerciseLog.getReps() == -1){
                        stringBuilder.append(exerciseLog.getTime()).append("s\n");
                    }
                    else{
                        stringBuilder.append(exerciseLog.getReps()).append("\n");
                    }
                }
                holder.tvSets.setText(stringBuilder.toString());
            }
        }

        @Override
        public int getItemCount() {
            return dates.size();
        }

        void setExerciseLogs(List<LocalDate> dates, List<ExerciseLog> exerciseLogs) {
            this.dates = dates;
            this.exerciseLogs = exerciseLogs;
            notifyDataSetChanged();
        }

        public static class ExerciseLogsViewHolder extends RecyclerView.ViewHolder{
            private TextView tvDate;
            private TextView tvSets;

            public ExerciseLogsViewHolder(@NonNull View itemView) {
                super(itemView);
                tvDate = itemView.findViewById(R.id.tv_date);
                tvSets = itemView.findViewById(R.id.tv_sets);
            }
        }
    }
}