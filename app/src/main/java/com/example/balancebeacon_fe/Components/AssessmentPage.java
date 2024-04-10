package com.example.balancebeacon_fe.Components;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.balancebeacon_fe.Controllers.AssessAreaController;
import com.example.balancebeacon_fe.Models.UserAssessResponse;
import com.example.balancebeacon_fe.R;
import com.example.balancebeacon_fe.Shared.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssessmentPage extends AppCompatActivity {

    LinearLayout assessmentLayout;
    ImageView assessmentContinueButton;

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

                try {
                    for (int i=0 ; i<response.body().getAssessmentPayloads().size() ; i++) {
                        View view = getLayoutInflater().inflate(R.layout.assess_progress_set, null);

                        // get area description text and set the description coming from the backend
                        TextView progressSetDesc = view.findViewById(R.id.progress_set_desc);
                        progressSetDesc.setText(response.body().getAssessmentPayloads().get(i).getAreaDescription());

                        // get the progress percentage text
                        TextView percentageText = view.findViewById(R.id.progress_set_percentage);

                        // get the progress bar
                        SeekBar progressBar = view.findViewById(R.id.progress_set_bar);
                        int finalI = i;
                        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                percentageText.setText("" + progress + "%");
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }
                        });

                        assessmentLayout.addView(view);
                    }
                } catch (Exception ex) {
                    System.out.println(ex.getCause());
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<UserAssessResponse> call, Throwable t) {

            }
        });



    }
}