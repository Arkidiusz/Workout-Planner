package com.example.workoutplanner.activities.WorkoutSession;

import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.workoutplanner.database.Exercise.Exercise;
import com.example.workoutplanner.database.WorkoutPlan.WorkoutPlan;

public class ExercisePagerAdapter extends FragmentStateAdapter {
    private SparseArray<ExerciseFragment> registeredFragments = new SparseArray<ExerciseFragment>();

    private WorkoutPlan workoutPlan;

    public ExercisePagerAdapter(@NonNull FragmentActivity fragmentActivity, WorkoutPlan workoutPlan) {
        super(fragmentActivity);
        this.workoutPlan = workoutPlan;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        ExerciseFragment exerciseFragment =
                new ExerciseFragment(workoutPlan.getExercisePlans().get(position));
        registeredFragments.put(position, exerciseFragment);
        return exerciseFragment;
    }

    @Override
    public int getItemCount() {
        return workoutPlan.getExercisePlans().size();
    }

    public SparseArray<ExerciseFragment> getRegisteredFragments(){
        return registeredFragments;
    }
}
