package com.example.workoutplanner.activities.WorkoutSession;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.workoutplanner.database.WorkoutPlan.WorkoutPlan;

public class ExercisePagerAdapter extends FragmentStateAdapter {

    private WorkoutPlan workoutPlan;

    public ExercisePagerAdapter(@NonNull FragmentActivity fragmentActivity, WorkoutPlan workoutPlan) {
        super(fragmentActivity);
        this.workoutPlan = workoutPlan;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new ExerciseFragment(workoutPlan.getExercisePlans().get(position));
    }

    @Override
    public int getItemCount() {
        return workoutPlan.getExercisePlans().size();
    }
}
