package com.example.workout_planner.activities.workout_session;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workout_planner.R;
import com.example.workout_planner.database.ExercisePlan;
import com.example.workout_planner.database.exercise.Exercise;
import com.example.workout_planner.database.exercise_log.ExerciseLog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.threeten.bp.LocalDate;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Displays sets of ont of  exercises from a workoutSession
 */
public class ExerciseFragment extends Fragment {

    private ExercisePlan exercisePlan;

    private RecyclerView rvSets;

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

        // Set label texts according to ExercisePlan data
        TextView tvExerciseLabel = view.findViewById(R.id.tv_exercise_label);
        TextView tvRepsOrTime = view.findViewById(R.id.tv_reps_or_time);
        switch (exercisePlan.getExercise().getExerciseType()) {
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

        //RecyclerView
        rvSets = view.findViewById(R.id.rv_sets);
        final SetsAdapter setsAdapter = new SetsAdapter(view.getContext(), exercisePlan.getNoSets());
        rvSets.setAdapter(setsAdapter);
        rvSets.setLayoutManager(new LinearLayoutManager(view.getContext()));
        // Buttons for adding and removing sets from recyclerview
        FloatingActionButton fabAddSet = view.findViewById(R.id.fab_add_set);
        fabAddSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setsAdapter.addSet();
            }
        });
        FloatingActionButton fabRemoveSet = view.findViewById(R.id.fab_delete_set);
        fabRemoveSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setsAdapter.removeSet();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exercise, container, false);
    }

    /**
     * @return a list of ExerciseLogs generated from user input
     */
    public ArrayList<ExerciseLog> getExerciseLogs() {
        ArrayList<ExerciseLog> exerciseLogs = new ArrayList<>();
        SetsAdapter setsAdapter = (SetsAdapter) rvSets.getAdapter();
        for(SetsAdapter.SetsViewHolder setsViewHolder : setsAdapter.getAllViewHolders()){
            String weight = setsViewHolder.etWeight.getText().toString();
            String repsOrTime = setsViewHolder.etRepsOrTime.getText().toString();
            if(!(weight.isEmpty() || repsOrTime.isEmpty())){
                if (exercisePlan.getExercise().getExerciseType() == Exercise.ExerciseType.TEMPO) {
                    exerciseLogs.add(new ExerciseLog(LocalDate.now(),
                            exercisePlan.getExercise().getName(),
                            Double.parseDouble(weight), -1,
                            Integer.parseInt(repsOrTime)));
                } else {
                    exerciseLogs.add(new ExerciseLog(LocalDate.now(),
                            exercisePlan.getExercise().getName(),
                            Double.parseDouble(weight),
                            Integer.parseInt(repsOrTime), -1));
                }
            }
        }
        return exerciseLogs;
    }

    public class SetsAdapter extends RecyclerView.Adapter<ExerciseFragment.SetsAdapter.SetsViewHolder> {

        private Context context;
        private List<Integer> sets;
        private ArrayList<SetsViewHolder> viewHolders = new ArrayList<>();

        public SetsAdapter(Context context, int noSets) {
            this.context = context;
            List<Integer> sets = new ArrayList<>();
            for (int i = 1; i <= noSets; i++) {
                sets.add(i);
            }
            this.sets = sets;
        }


        @NonNull
        @Override
        public SetsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.item_set, parent, false);
            SetsViewHolder setsViewHolder = new SetsViewHolder(view);
            viewHolders.add(setsViewHolder);
            return setsViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull SetsViewHolder holder, int position) {
            holder.tvSetNumber.setText(MessageFormat.format("{0}.", position + 1));
        }

        public ArrayList<SetsViewHolder> getAllViewHolders(){
            return viewHolders;
        }

        public void addSet() {
            sets.add(sets.size() + 1);
            notifyItemInserted(sets.size());
        }

        public void removeSet() {
            if (sets.size() > 0) {
                sets.remove(sets.size() - 1);
                notifyItemRemoved(sets.size());
            }
        }

        @Override
        public int getItemCount() {
            return sets.size();
        }

        public class SetsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private EditText etWeight;
            private EditText etRepsOrTime;
            private TextView tvSetNumber;

            public SetsViewHolder(@NonNull View itemView) {
                super(itemView);
                etWeight = itemView.findViewById(R.id.et_weight);
                etRepsOrTime = itemView.findViewById(R.id.et_reps_or_time);
                tvSetNumber = itemView.findViewById(R.id.tv_set_number);
            }

            @Override
            public void onClick(View view) { }
        }
    }
}