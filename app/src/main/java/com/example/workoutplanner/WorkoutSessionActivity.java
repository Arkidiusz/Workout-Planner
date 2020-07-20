package com.example.workoutplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class WorkoutSessionActivity extends AppCompatActivity {
    private String[] exercises = new String[]{"Bench Press", "Plank"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_session);
        ViewPager2 viewpager2 = findViewById(R.id.vp_exercise_plans);
        viewpager2.setAdapter(new ExercisePagerAdapter(this));
        TabLayout tabLayout = findViewById(R.id.tl_exercise_plans);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewpager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:{
                        tab.setText("XD");
                        break;
                    }
                    default:{
                        tab.setText("AJE");
                        break;
                    }
                }
            }
        });
        tabLayoutMediator.attach();
    }
}