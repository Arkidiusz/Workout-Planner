package com.example.workout_planner.activities.logs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workout_planner.R;
import com.example.workout_planner.activities.chart.ChartActivity;
import com.example.workout_planner.database.exercise_log.ExerciseLog;
import com.example.workout_planner.database.exercise_log.ExerciseLogDao;
import com.example.workout_planner.database.WorkoutPlannerDatabase;

import org.threeten.bp.LocalDate;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class LogsActivity extends AppCompatActivity {

    private List<ExerciseLog> exerciseLogs;
    private List<String> exercises;
    private List<LocalDate> dates;
    private Spinner spinner;

    /**
     * Use Epley formula to estimate one-repetition maximum
     *
     * @param weight - weight lifted
     * @param reps   - number of times lifted
     * @return an estimate of 1RM
     */
    public static double estimate1RM(double weight, int reps) {
        return weight * (1 + (double) reps / 30);
    }

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
        threadExerciseNames.start();
        while (exercises == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                finish();
            }
        }

        spinner = findViewById(R.id.spn_exercise_logs);
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
        while (exerciseLogs == null || dates == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                finish();
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
                while (exerciseLogs == null || dates == null) {
                    try {
                        Log.i("thread", "sleep");
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        finish();
                    }
                }
                exerciseLogsAdapter.setExerciseLogs(dates, exerciseLogs);
                dates = null;
                exerciseLogs = null;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        Button btnChart = findViewById(R.id.btn_chart);
        btnChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogsActivity.this, ChartActivity.class);
                intent.putExtra("exerciseName", spinner.getSelectedItem().toString());
                startActivity(intent);
            }
        });
    }

    private static class ExerciseLogsAdapter extends RecyclerView.Adapter<ExerciseLogsAdapter.ExerciseLogsViewHolder> {

        private Context context;
        private List<LocalDate> dates;
        private List<ExerciseLog> exerciseLogs;

        public ExerciseLogsAdapter(Context context, List<LocalDate> dates, List<ExerciseLog> exerciseLogs) {
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

        @SuppressLint("DefaultLocale")
        @Override
        public void onBindViewHolder(@NonNull ExerciseLogsAdapter.ExerciseLogsViewHolder holder, int position) {
            if (position < dates.size()) {
                LocalDate date = dates.get(position);
                holder.tvDate.setText(date.toString());
                ArrayList<ExerciseLog> exerciseLogs = new ArrayList<>();

                for (ExerciseLog exerciseLog : this.exerciseLogs) {
                    if (exerciseLog.getDate().isEqual(date)) {
                        exerciseLogs.add(exerciseLog);
                    }
                }
                double current1RM = 0;
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < exerciseLogs.size(); i++) {
                    ExerciseLog exerciseLog = exerciseLogs.get(i);
                    stringBuilder.append("Set ").append(i + 1).append(": ").append(exerciseLog.getWeight()).append("kg x ");
                    if (exerciseLog.getReps() == -1) {
                        stringBuilder.append(exerciseLog.getTime()).append("s\n");
                    } else {
                        stringBuilder.append(exerciseLog.getReps()).append("\n");
                        // Additionally, estimate 1RM
                        double _1RM = LogsActivity.estimate1RM(exerciseLog.getWeight(),
                                exerciseLog.getReps());
                        if (_1RM > current1RM) {
                            current1RM = _1RM;
                        }
                    }
                }
                holder.tvSets.setText(stringBuilder.toString());
                if (current1RM > 0) {
                    holder.tv1RM.setText(MessageFormat.format("1RM: {0}kg", String.format("%.2f",
                            current1RM)));
                } else {
                    holder.tv1RM.setText("");
                }
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

        public static class ExerciseLogsViewHolder extends RecyclerView.ViewHolder {
            private TextView tvDate;
            private TextView tvSets;
            private TextView tv1RM;

            public ExerciseLogsViewHolder(@NonNull View itemView) {
                super(itemView);
                tvDate = itemView.findViewById(R.id.tv_date);
                tvSets = itemView.findViewById(R.id.tv_sets);
                tv1RM = itemView.findViewById(R.id.tv_1RM);
            }
        }
    }
}