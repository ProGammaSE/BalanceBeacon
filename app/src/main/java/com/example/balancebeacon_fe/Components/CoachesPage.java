package com.example.balancebeacon_fe.Components;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.balancebeacon_fe.Controllers.CoachController;
import com.example.balancebeacon_fe.Controllers.GoalController;
import com.example.balancebeacon_fe.Models.Coaches;
import com.example.balancebeacon_fe.Models.Goals;
import com.example.balancebeacon_fe.R;
import com.example.balancebeacon_fe.Shared.RetrofitClient;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoachesPage extends AppCompatActivity {

    LinearLayout activityCoachLayout;
    ImageView activityCoachButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coaches_page);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Coaching & Mentoring");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activityCoachLayout = findViewById(R.id.activity_coach_layout);
        activityCoachButton = findViewById(R.id.activity_coach_button);

        loadCoaches();

        activityCoachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CoachesPage.this, MainPage.class);
                startActivity(intent);
            }
        });
    }

    // this function works when clicking on the Menu icon
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    // when clicks on the button in the menu, this function will routes to the relevant page
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == R.id.menu_main_page) {
            Intent intent = new Intent(CoachesPage.this, MainPage.class);
            startActivity(intent);
        } else if (itemId == R.id.menu_assessments) {
            Intent intent = new Intent(CoachesPage.this, AssessmentPage.class);
            startActivity(intent);
        } else if (itemId == R.id.menu_my_goals) {
            Intent intent = new Intent(CoachesPage.this, MyGoalsPage.class);
            startActivity(intent);
        } else if (itemId == R.id.menu_achievments) {
            Intent intent = new Intent(CoachesPage.this, AchiementsPage.class);
            startActivity(intent);
        } else if (itemId == R.id.menu_coaching_mentoring) {
            Intent intent = new Intent(CoachesPage.this, CoachesPage.class);
            startActivity(intent);
        } else if (itemId == R.id.menu_feedback) {
            Intent intent = new Intent(CoachesPage.this, FeedbackPage.class);
            startActivity(intent);
        } else if (itemId == R.id.menu_logout) {
            Intent intent = new Intent(CoachesPage.this, LoginPage.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    // function to load available coaches in the database
    public void loadCoaches() {
        activityCoachLayout.removeAllViews();

        try {
            CoachController coachController = RetrofitClient.getRetrofitInstance().create(CoachController.class);
            Call<List<Coaches>> call = coachController.getAllCoaches();

            call.enqueue(new Callback<List<Coaches>>() {
                @Override
                public void onResponse(Call<List<Coaches>> call, Response<List<Coaches>> response) {
                    Toast.makeText(CoachesPage.this, "Coaches loaded successfully", Toast.LENGTH_SHORT).show();

                    for (int i=0 ; i<response.body().size() ; i++) {
                        View view = LayoutInflater.from(CoachesPage.this).inflate(R.layout.coaches_design, null);
                        TextView coachName = view.findViewById(R.id.coach_name);
                        TextView coachSpeciality = view.findViewById(R.id.coach_speciality);
                        TextView coachGender = view.findViewById(R.id.coach_gender);
                        TextView coachPhone = view.findViewById(R.id.coach_call);

                        coachName.setText(response.body().get(i).getCoachName());
                        coachSpeciality.setText(response.body().get(i).getCoachSpeciality());
                        coachGender.setText(response.body().get(i).getCoachGender());

                        int loop = i;
                        // setting to initiate a call when clicking on the "Call" button
                        coachPhone.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:" + response.body().get(loop).getPhoneNumber()));
                                startActivity(intent);
                            }
                        });
                        activityCoachLayout.addView(view);
                    }
                }

                @Override
                public void onFailure(Call<List<Coaches>> call, Throwable t) {
                    Toast.makeText(CoachesPage.this, "Coaches loading failed!", Toast.LENGTH_SHORT).show();

                }
            });
        } catch (Exception ex) {

        }
    }
}