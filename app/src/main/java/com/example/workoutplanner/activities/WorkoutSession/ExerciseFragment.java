package com.example.workoutplanner.activities.WorkoutSession;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.workoutplanner.R;
import com.example.workoutplanner.database.Exercise.Exercise;
import com.example.workoutplanner.database.ExercisePlan;

import java.text.MessageFormat;

public class ExerciseFragment extends Fragment {

    private ExercisePlan exercisePlan;

    public ExerciseFragment(ExercisePlan exercisePlan) {
        this.exercisePlan = exercisePlan;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Log.i("ExercisePlan:",
//                String.valueOf(exercisePlan instanceof ExercisePlan.TempoExercisePlan || exercisePlan instanceof ExercisePlan.IsometricExercisePlan));
        TextView tvExerciseLabel = view.findViewById(R.id.tv_exercise_label);
        TextView tvRepsOrTime = view.findViewById(R.id.tv_reps_or_time);
        switch(exercisePlan.getExercise().getExerciseType()){
            case ISOMETRIC:
                ExercisePlan.IsometricExercisePlan isometricExercisePlan =
                        (ExercisePlan.IsometricExercisePlan) exercisePlan;
                tvExerciseLabel.setText(MessageFormat.format("Target: {0} x {1}s",
                        isometricExercisePlan.getNoSets(), isometricExercisePlan.getTime()));
                tvRepsOrTime.setText(R.string.all_time);
                break;
            case TEMPO:

                ExercisePlan.TempoExercisePlan tempoExercisePlan =
                        (ExercisePlan.TempoExercisePlan) exercisePlan;
                tvExerciseLabel.setText(MessageFormat.format("Target: {0} x {1} at {2}",
                        tempoExercisePlan.getNoSets(), tempoExercisePlan.getNoReps(),
                        tempoExercisePlan.getTempo()));
                tvRepsOrTime.setText(R.string.all_reps);
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exercise, container, false);
    }
}