package com.example.workoutplanner.activities.chart;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.workoutplanner.R;
import com.example.workoutplanner.activities.logs.LogsActivity;
import com.example.workoutplanner.database.exercise_log.ExerciseLog;
import com.example.workoutplanner.database.exercise_log.ExerciseLogDao;
import com.example.workoutplanner.database.WorkoutPlannerDatabase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChartActivity extends AppCompatActivity {
    private List<LocalDate> dates;
    private List<ExerciseLog> exerciseLogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        Intent intent = getIntent();
        final String exerciseName = intent.getStringExtra("exerciseName");

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                WorkoutPlannerDatabase db = WorkoutPlannerDatabase.getDataBase(getApplication());
                ExerciseLogDao dao = db.exerciseLogDao();
                dates = dao.getDatesOfExercise(exerciseName);
                exerciseLogs = dao.getExerciseLogsOfName(exerciseName);
            }
        });
        thread.start();
        while (dates == null || exerciseLogs == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Collections.reverse(dates);

        ArrayList<Entry> dataValues = new ArrayList<Entry>();
        for (LocalDate date : dates) {
            double current1RM = 0;
            for (ExerciseLog exerciseLog : exerciseLogs) {
                if (exerciseLog.getDate().isEqual(date)) {
                    double _1RM = LogsActivity.estimate1RM(exerciseLog.getWeight(),
                            exerciseLog.getReps());
                    if (_1RM > current1RM) current1RM = _1RM;
                }
            }
            dataValues.add(new Entry(date.toEpochDay(), (float) current1RM));
        }

        LineChart lineChart = findViewById(R.id.line_chart);
        LineDataSet lindDataSet = new LineDataSet(dataValues, "Bench Press 1RM\"");
        lindDataSet.setLineWidth(3f);
        lindDataSet.setCircleRadius(6f);
        lindDataSet.setCircleHoleRadius(3f);
        lindDataSet.setCircleColor(ContextCompat.getColor(getApplicationContext(),
                R.color.primaryColor));

        lindDataSet.setColor(ContextCompat.getColor(getApplicationContext(), R.color.primaryColor));
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lindDataSet);
        LineData data = new LineData(dataSets);
        lineChart.setScaleEnabled(true);
        lineChart.setData(data);
        lineChart.getXAxis().setValueFormatter(new DateFormatter());
        lineChart.animateXY(1000, 1000);
        lindDataSet.setValueTextSize(12f);
        lineChart.getDescription().setEnabled(false);

        XAxis xAxis = lineChart.getXAxis();
//        xAxis.setTypeface(tfLight);
        xAxis.setTextSize(12f);
//        xAxis.setDrawAxisLine(true);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(4);
        lineChart.setExtraTopOffset(4f);

        lineChart.getAxisLeft().setValueFormatter(new KgFormatter());
        lineChart.getAxisLeft().setTextSize(12f);
        lineChart.getAxisRight().setTextSize(12f);
        lineChart.getAxisRight().setValueFormatter(new KgFormatter());

        lineChart.invalidate();
    }

    public static class DateFormatter extends ValueFormatter {

        @Override
        public String getFormattedValue(float value) {
            return LocalDate.ofEpochDay((long) value).toString();
        }
    }

    public static class KgFormatter extends ValueFormatter{

        @Override
        public String getFormattedValue(float value) {
            return value + "kg";
        }
    }
}