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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.balancebeacon_fe.Controllers.AssessAreaController;
import com.example.balancebeacon_fe.Models.GeneralResponse;
import com.example.balancebeacon_fe.Models.UserAssessResponse;
import com.example.balancebeacon_fe.R;
import com.example.balancebeacon_fe.Shared.RetrofitClient;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssessmentPage extends AppCompatActivity {

    LinearLayout assessmentLayout;
    ImageView assessmentContinueButton;
    UserAssessResponse userAssessResponse = new UserAssessResponse();
    int questionLoopCount = 0;
    int areaLoopCount = 0;
    View currentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_page);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Assessments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        assessmentLayout = findViewById(R.id.assessment_layout);
        loadAssessmentAreas();
    }

    private void showPopUpDialog(String popUpTitle, String popUpDescription) {
        // declaring parameters
        ConstraintLayout alertPopUpSuccess = findViewById(R.id.alertPopUpSuccess);
        View view = LayoutInflater.from(AssessmentPage.this).inflate(R.layout.alert_pop_up_success, alertPopUpSuccess);
        Button popUpButton = view.findViewById(R.id.alert_success_done);
        TextView alertSuccessTitle = view.findViewById(R.id.alert_success_title);
        TextView alertSuccessDescription = view.findViewById(R.id.alert_success_description);

        // setting the given title and description
        alertSuccessTitle.setText(popUpTitle);
        alertSuccessDescription.setText(popUpDescription);

        // build the pop up dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(AssessmentPage.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        // define the action of "Done" button in the pop up dialog
        popUpButton.findViewById(R.id.alert_success_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // below lines executes when clicking on the "Done" button in the pop up window
                // Navigates to the Landing page once clicked
                alertDialog.dismiss();
                Intent intent = new Intent(AssessmentPage.this, MainPage.class);
                startActivity(intent);
            }
        });
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    public void loadQuestions() {
        if (userAssessResponse != null) {
            if (questionLoopCount != 0) {
                View view = getLayoutInflater().inflate(R.layout.assess_question_set, null);
                currentView = view;
                TextView questionTitle = view.findViewById(R.id.question_title);
                questionTitle.setText(userAssessResponse.getAssessmentPayloads().get(areaLoopCount).getAreaDescription());

                // define questions
                TextView question01 = view.findViewById(R.id.question_01);
                TextView question02 = view.findViewById(R.id.question_02);

                question01.setText("How do you feel about your " + userAssessResponse.getAssessmentPayloads().get(areaLoopCount).getAreaDescription() + " right now?");
                question02.setText("How do you feel about your " + userAssessResponse.getAssessmentPayloads().get(areaLoopCount).getAreaDescription() +" in the future?");

                // define percentage numbers in the question window
                TextView currentPercentage = view.findViewById(R.id.question_percentage_01);
                TextView futurePercentage = view.findViewById(R.id.question_percentage_02);

                // define the seekbars (slider bars) in the question window
                SeekBar currentBar = view.findViewById(R.id.question_seekbar_01);
                SeekBar futureBar = view.findViewById(R.id.question_seekbar_02);

                // define the "Done" button in the question window
                Button questionDone = view.findViewById(R.id.question_done);
                questionDone.setEnabled(false);

                int seekBarSetAreaCount = areaLoopCount;
                // below functions work when changing the CURRENT seekbar slider
                currentBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        // set the percentage value
                        currentPercentage.setText(progress + "/10");

                        // adding current value to the update object
                        userAssessResponse.getAssessmentPayloads().get(seekBarSetAreaCount).setAreaCurrent(progress);

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                // below functions work when changing the FUTURE seekbar slider
                futureBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        // set the percentage value
                        futurePercentage.setText(progress + "/10");

                        // adding future value to the update object
                        userAssessResponse.getAssessmentPayloads().get(seekBarSetAreaCount).setAreaFuture(progress);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        questionDone.setEnabled(false);
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // enable the "Done" button only if future value is set
                        if (seekBar.getProgress() != 0) {
                            questionDone.setEnabled(true);
                        }
                    }
                });

                // define the done button in the question window
                Button doneButton = view.findViewById(R.id.question_done);
                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickingOnDoneButton();
                    }
                });
                assessmentLayout.addView(view);
                questionLoopCount--;
                areaLoopCount++;
            }
            else {
                Toast.makeText(AssessmentPage.this, "Question round completed", Toast.LENGTH_SHORT).show();
                updateUserAreas();
            }
        }
    }

    public void clickingOnDoneButton() {
        assessmentLayout.removeAllViews();
        loadQuestions();
    }

    /**
     * function load all the areas of the logged in user
     */
    public void loadAssessmentAreas() {
        AssessAreaController assessAreaController = RetrofitClient.getRetrofitInstance().create(AssessAreaController.class);

        // get current logged user ID
        // save logged in user details to the mobile cache
        SharedPreferences sharedpreferences = getSharedPreferences("balanceBeacon",MODE_PRIVATE);
        int userId = sharedpreferences.getInt("userId", 0);

        Call<UserAssessResponse> call = assessAreaController.getAllUserAreas(userId);

        call.enqueue(new Callback<UserAssessResponse>() {
            @Override
            public void onResponse(Call<UserAssessResponse> call, Response<UserAssessResponse> response) {
                Toast.makeText(AssessmentPage.this, "User areas loaded", Toast.LENGTH_SHORT).show();
                userAssessResponse = response.body();
                assert response.body() != null;
                questionLoopCount = response.body().getAssessmentPayloads().size();

                // calling the loadQuestions function to display questions one by one
                loadQuestions();
            }

            @Override
            public void onFailure(Call<UserAssessResponse> call, Throwable t) {

            }
        });
    }

    public void updateUserAreas() {
        AssessAreaController assessAreaController = RetrofitClient.getRetrofitInstance().create(AssessAreaController.class);
        Call<GeneralResponse> call = assessAreaController.updateUserAreas(userAssessResponse);

        call.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                Toast.makeText(AssessmentPage.this, response.body().getResponseDescription(), Toast.LENGTH_SHORT).show();
                showPopUpDialog("Success", "Areas updated successfully");
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                Toast.makeText(AssessmentPage.this, "Area update failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}