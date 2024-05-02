package com.example.balancebeacon_fe.Components;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.balancebeacon_fe.Controllers.FeedbackController;
import com.example.balancebeacon_fe.Models.Feedback;
import com.example.balancebeacon_fe.Models.GeneralResponse;
import com.example.balancebeacon_fe.R;
import com.example.balancebeacon_fe.Shared.RetrofitClient;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackPage extends AppCompatActivity {

    RatingBar ratingBar;
    Button ratingBarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_page);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Feedback");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ratingBar = findViewById(R.id.ratingBar);
        ratingBarButton = findViewById(R.id.ratingBarButton);

        ratingBarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takingUserFeedback();
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
            Intent intent = new Intent(FeedbackPage.this, MainPage.class);
            startActivity(intent);
        } else if (itemId == R.id.menu_assessments) {
            Intent intent = new Intent(FeedbackPage.this, AssessmentPage.class);
            startActivity(intent);
        } else if (itemId == R.id.menu_my_goals) {
            Intent intent = new Intent(FeedbackPage.this, MyGoalsPage.class);
            startActivity(intent);
        } else if (itemId == R.id.menu_achievments) {
            Intent intent = new Intent(FeedbackPage.this, AchiementsPage.class);
            startActivity(intent);
        } else if (itemId == R.id.menu_coaching_mentoring) {
            Intent intent = new Intent(FeedbackPage.this, CoachesPage.class);
            startActivity(intent);
        } else if (itemId == R.id.menu_feedback) {
            Intent intent = new Intent(FeedbackPage.this, FeedbackPage.class);
            startActivity(intent);
        } else if (itemId == R.id.menu_logout) {
            Intent intent = new Intent(FeedbackPage.this, LoginPage.class);
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
        View view = LayoutInflater.from(FeedbackPage.this).inflate(R.layout.alert_pop_up_success, alertPopUpSuccess);
        Button popUpButton = view.findViewById(R.id.alert_success_done);
        TextView alertSuccessTitle = view.findViewById(R.id.alert_success_title);
        TextView alertSuccessDescription = view.findViewById(R.id.alert_success_description);


        // setting the given title and description
        alertSuccessTitle.setText(popUpTitle);
        alertSuccessDescription.setText(popUpDescription);

        // build the pop up dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(FeedbackPage.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        // define the action of "Done" button in the pop up dialog
        popUpButton.findViewById(R.id.alert_success_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // below lines executes when clicking on the "Done" button in the pop up window
                alertDialog.dismiss();

                Intent intent = new Intent(FeedbackPage.this, MainPage.class);
                startActivity(intent);
            }
        });
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    // function to get the value of user rating and send it to the backend
    // then the backend will store it in the database
    public void takingUserFeedback() {
        float ratings = ratingBar.getRating();
        Feedback feedback = new Feedback();

        // get current logged in user ID from the mobile cache and set it to the object
        SharedPreferences sharedpreferences = getSharedPreferences("balanceBeacon",MODE_PRIVATE);
        int userId = sharedpreferences.getInt("userId", 0);

        // assign details to the object
        feedback.setUserId(userId);
        feedback.setFeedbackRate(ratings);

        FeedbackController feedbackController = RetrofitClient.getRetrofitInstance().create(FeedbackController.class);
        Call<GeneralResponse> call = feedbackController.addFeedback(feedback);

        call.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (response.body().getResponseCode() == 200) {
                    Toast.makeText(FeedbackPage.this, response.body().getResponseDescription(), Toast.LENGTH_SHORT).show();

                    if (ratings == 0.0) {
                        showPopUpDialog("Thank you!", "Sorry to hear that you don't have any feedback. But, Thank you for your time");
                    }
                    else {
                        showPopUpDialog("Thank you!", "Your " + ratings + " rate is very valuable to us");
                    }
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                Toast.makeText(FeedbackPage.this, "Something went wrong!", Toast.LENGTH_SHORT).show();

            }
        });
    }
}