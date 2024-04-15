package com.example.balancebeacon_fe.Components;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.balancebeacon_fe.Controllers.AssessAreaController;
import com.example.balancebeacon_fe.Models.GeneralResponse;
import com.example.balancebeacon_fe.Models.UserAssessResponse;
import com.example.balancebeacon_fe.R;
import com.example.balancebeacon_fe.Shared.RetrofitClient;

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

        assessmentLayout = findViewById(R.id.assessment_layout);
        assessmentContinueButton = findViewById(R.id.assessment_continue_button);
        loadAssessmentAreas();

        // clicking on the continue button
        assessmentContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AssessmentPage.this, "Assessment updated", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void loadQuestions() {
        if (userAssessResponse != null) {
            if (questionLoopCount != 0) {
                View view = getLayoutInflater().inflate(R.layout.assess_question_set, null);
                currentView = view;
                TextView questionTitle = view.findViewById(R.id.question_title);
                questionTitle.setText(userAssessResponse.getAssessmentPayloads().get(areaLoopCount).getAreaDescription());

                // define percentage numbers in the question qindow
                TextView currentPercentage = view.findViewById(R.id.question_percentage_01);
                TextView futurePercentage = view.findViewById(R.id.question_percentage_02);

                // define the seekbars (slider bars) in the question window
                SeekBar currentBar = view.findViewById(R.id.question_seekbar_01);
                SeekBar futureBar = view.findViewById(R.id.question_seekbar_02);

                int seekBarSetAreaCount = areaLoopCount;
                // below functions work when changing the CURRENT seekbar slider
                currentBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
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
                        futurePercentage.setText(progress + "/10");

                        // adding future value to the update object
                        userAssessResponse.getAssessmentPayloads().get(seekBarSetAreaCount).setAreaFuture(progress);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

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
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                Toast.makeText(AssessmentPage.this, "Area update failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}