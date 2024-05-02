package com.example.balancebeacon_fe.Components;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.balancebeacon_fe.Controllers.AchievementController;
import com.example.balancebeacon_fe.Models.Achievements;
import com.example.balancebeacon_fe.R;
import com.example.balancebeacon_fe.Shared.RetrofitClient;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AchiementsPage extends AppCompatActivity {

    TextView achievementAwardsCount;
    ImageView rowOneColumnOne, rowOneColumnTwo, rowTwoColumnOne, rowTwoColumnTwo, rowThreeColumnOne, rowThreeColumnTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achiements_page);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Achievements");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        achievementAwardsCount = findViewById(R.id.achievements_awards_count);
        rowOneColumnOne = findViewById(R.id.rowOneColumnOne);
        rowOneColumnTwo = findViewById(R.id.rowOneColumnTwo);
        rowTwoColumnOne = findViewById(R.id.rowTwoColumnOne);
        rowTwoColumnTwo = findViewById(R.id.rowTwoColumnTwo);
        rowThreeColumnOne = findViewById(R.id.rowThreeColumnOne);
        rowThreeColumnTwo = findViewById(R.id.rowThreeColumnTwo);

        loadAwards();
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
            Intent intent = new Intent(AchiementsPage.this, MainPage.class);
            startActivity(intent);
        } else if (itemId == R.id.menu_assessments) {
            Intent intent = new Intent(AchiementsPage.this, AssessmentPage.class);
            startActivity(intent);
        } else if (itemId == R.id.menu_my_goals) {
            Intent intent = new Intent(AchiementsPage.this, MyGoalsPage.class);
            startActivity(intent);
        } else if (itemId == R.id.menu_achievments) {
            Intent intent = new Intent(AchiementsPage.this, AchiementsPage.class);
            startActivity(intent);
        } else if (itemId == R.id.menu_coaching_mentoring) {
            Intent intent = new Intent(AchiementsPage.this, CoachesPage.class);
            startActivity(intent);
        } else if (itemId == R.id.menu_feedback) {
            Intent intent = new Intent(AchiementsPage.this, FeedbackPage.class);
            startActivity(intent);
        } else if (itemId == R.id.menu_logout) {
            Intent intent = new Intent(AchiementsPage.this, LoginPage.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * pop up window to show the result of requests.
     */
    private void showPopUpDialog(String popUpTitle, String popUpDescription) {
        // declaring parameters
        ConstraintLayout alertPopUpSuccess = findViewById(R.id.alertPopUpSuccess);
        View view = LayoutInflater.from(AchiementsPage.this).inflate(R.layout.alert_pop_up_success, alertPopUpSuccess);
        Button popUpButton = view.findViewById(R.id.alert_success_done);
        TextView alertSuccessTitle = view.findViewById(R.id.alert_success_title);
        TextView alertSuccessDescription = view.findViewById(R.id.alert_success_description);


        // setting the given title and description
        alertSuccessTitle.setText(popUpTitle);
        alertSuccessDescription.setText(popUpDescription);

        // build the pop up dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(AchiementsPage.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        // define the action of "Done" button in the pop up dialog
        popUpButton.findViewById(R.id.alert_success_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // below lines executes when clicking on the "Done" button in the pop up window
                // Navigates to the Landing page once clicked
                alertDialog.dismiss();

            }
        });
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    // function to get new achievement details from the backend
    public void loadAwards() {
        // get current logged in user ID from the mobile cache and set it to the object
        SharedPreferences sharedpreferences = getSharedPreferences("balanceBeacon",MODE_PRIVATE);
        int userId = sharedpreferences.getInt("userId", 0);

        AchievementController achievementController = RetrofitClient.getRetrofitInstance().create(AchievementController.class);
        Call<Achievements> call = achievementController.getAchievements(userId);

        call.enqueue(new Callback<Achievements>() {
            @Override
            public void onResponse(Call<Achievements> call, Response<Achievements> response) {
                int totalAchievementCount = 0;

                if (response.body() != null) {
                    Toast.makeText(AchiementsPage.this, "Achievements loaded successfully", Toast.LENGTH_SHORT).show();

                    // checking which achievements are collected
                    if (response.body().getAssessmentCount() > 0) {
                        totalAchievementCount++;
                        rowOneColumnOne.setBackgroundResource(R.drawable.award_01_yes);
                    }
                    if (response.body().getGoalCount() > 0) {
                        totalAchievementCount++;
                        rowOneColumnTwo.setBackgroundResource(R.drawable.award_02_yes);
                    }
                    if (response.body().getGoalCompletionCount() > 0) {
                        totalAchievementCount++;
                        rowTwoColumnOne.setBackgroundResource(R.drawable.award_03_yes);
                    }
                    if (totalAchievementCount > 0) {
                        achievementAwardsCount.setText("" + totalAchievementCount + "/6");
                        showPopUpDialog("Hooray!", "You have won " + totalAchievementCount + " rewards");
                    }
                    else {
                        achievementAwardsCount.setText("0/6");
                        showPopUpDialog("You tried!", "Better luck next time");
                    }
                }
                else {
                    Toast.makeText(AchiementsPage.this, "No achievements", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Achievements> call, Throwable t) {
                Toast.makeText(AchiementsPage.this, "Achievement not loaded!", Toast.LENGTH_SHORT).show();

            }
        });
    }
}