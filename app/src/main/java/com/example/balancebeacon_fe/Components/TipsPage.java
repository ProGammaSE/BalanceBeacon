package com.example.balancebeacon_fe.Components;

import static com.example.balancebeacon_fe.Models.AreaIdDefined.getAreaName;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.balancebeacon_fe.Controllers.UserTipsController;
import com.example.balancebeacon_fe.Models.UserTips;
import com.example.balancebeacon_fe.R;
import com.example.balancebeacon_fe.Shared.RetrofitClient;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TipsPage extends AppCompatActivity {

    LinearLayout tipPageLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips_page);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Tips");
        tipPageLayout = findViewById(R.id.tip_page_layout);
        loadTips();
    }

    // load area title and tips
    public void loadTips() {
        System.out.println("Line 46 : functions starts");
        // get current logged in user ID from the mobile cache and set it to the object
        SharedPreferences sharedpreferences = getSharedPreferences("balanceBeacon",MODE_PRIVATE);
        int userId = sharedpreferences.getInt("userId", 0);
        int assessmentId = sharedpreferences.getInt("assessmentId", 0);
        int status = 0;

        UserTipsController userTipsController = RetrofitClient.getRetrofitInstance().create(UserTipsController.class);
        Call<List<UserTips>> call = userTipsController.getAllUserTips(userId, assessmentId, status);

        call.enqueue(new Callback<List<UserTips>>() {
            @Override
            public void onResponse(Call<List<UserTips>> call, Response<List<UserTips>> response) {
                if (response.body().size() > 0) {
                    Toast.makeText(TipsPage.this, "Tips loaded successfully", Toast.LENGTH_SHORT).show();
                    System.out.println("Response body size: " + response.body().size());

                    for (int j=0 ; j<response.body().size() ; j++) {
                        String tipDescription = response.body().get(j).getTipDescription();
                        int tipId = response.body().get(j).getUserTipId();

                        View tipBoxView = getLayoutInflater().inflate(R.layout.tip_box, null);

                        // tip box title
                        TextView tipBoxTitle = tipBoxView.findViewById(R.id.tip_box_title);

                        // tip box description
                        TextView tipBoxDescription = tipBoxView.findViewById(R.id.tip_box_description);

                        tipBoxTitle.setText(getAreaName(response.body().get(j).getAreaId()));
                        tipBoxDescription.setText(tipDescription);
                        tipBoxView.setId(response.body().get(j).getUserTipId());

                        // add the clickable event
                        // the below code parts work when click on Tip descriptions
                        tipBoxDescription.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // set assessment id in the cache memory
                                SharedPreferences sharedpreferences = getSharedPreferences("balanceBeacon",MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString("goalDescription",tipDescription);
                                editor.putInt("goalTipId", tipId);
                                editor.apply();

                                // navigate to the Set Goal Page
                                Intent intent = new Intent(TipsPage.this, SetGoalPage.class);
                                startActivity(intent);
                            }
                        });

                        tipPageLayout.addView(tipBoxView);
                    }

                    // create Area title views
//                    for (int i=0 ; i<8 ; i++) {
//                        View tipTitleView = getLayoutInflater().inflate(R.layout.tip_title, null);
//                        TextView tipTitleText = tipTitleView.findViewById(R.id.tip_title_text);
//                        tipTitleText.setText(getAreaName(response.body().get(i)));
//                        tipPageLayout.addView(tipTitleView);
//
//                        for (int j=0 ; j<4 ; j++) {
//                            View tipBoxView = getLayoutInflater().inflate(R.layout.tip_box, null);
//                            TextView tipBoxDescription = tipBoxView.findViewById(R.id.tip_box_description);
//                            tipBoxDescription.setText("Explore different philosophical or spiritual texts to broaden perspectives " + j);
//                            tipPageLayout.addView(tipBoxView);
//                        }
//                    }
                }
                else {
                    Toast.makeText(TipsPage.this, "Tips loading failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserTips>> call, Throwable t) {
                Toast.makeText(TipsPage.this, "Tips loading failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}