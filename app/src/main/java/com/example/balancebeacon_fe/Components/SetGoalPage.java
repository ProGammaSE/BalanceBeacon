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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.balancebeacon_fe.Controllers.UserTipsController;
import com.example.balancebeacon_fe.Models.GeneralResponse;
import com.example.balancebeacon_fe.Models.UpdateUserTips;
import com.example.balancebeacon_fe.R;
import com.example.balancebeacon_fe.Shared.RetrofitClient;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetGoalPage extends AppCompatActivity {

    LinearLayout setGoalPageLayout;
    TextView buttonOneDay, buttonOneWeek, buttonTenDays, buttonOneMonth, buttonCustom;
    Button setGoalPageButton;
    Object selectedGoalButton = 0;
    String tipDescription_cache = "";
    String areaDescription_cache = "";
    int userId_cache = 0;
    int userTipId_cache = 0;
    String goalDays = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_goal_page);
        setGoalPageLayout = findViewById(R.id.set_goal_page_layout);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Set Goal");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setGoalPageButton = findViewById(R.id.set_goal_page_button);

        // load cache memory saved details
        // get current selected TIP description and id from the mobile cache and set it to the object
        SharedPreferences sharedpreferences = getSharedPreferences("balanceBeacon",MODE_PRIVATE);
        tipDescription_cache = sharedpreferences.getString("goalDescription", "null");
        areaDescription_cache = sharedpreferences.getString("goalTitle", "null");
        userTipId_cache = sharedpreferences.getInt("goalTipId", 0);
        userId_cache = sharedpreferences.getInt("userId", 0);

        loadGoalWindow();

        // clicking on the "Set goal" button at the bottom of the Set Goal screen
        setGoalPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userTipId_cache != 0 && !goalDays.isEmpty()) {
                    UpdateUserTips updateUserTips = new UpdateUserTips();
                    updateUserTips.setUserId(userId_cache);
                    updateUserTips.setUserTipId(userTipId_cache);
                    updateUserTips.setTipStatus(1);
                    updateUserTips.setTipDescription(tipDescription_cache);
                    updateUserTips.setAreaDescription(areaDescription_cache);
                    updateUserTips.setGoalDays(goalDays);

                    try {
                        UserTipsController userTipsController = RetrofitClient.getRetrofitInstance().create(UserTipsController.class);
                        Call<GeneralResponse> call = userTipsController.updateUserTips(updateUserTips);

                        call.enqueue(new Callback<GeneralResponse>() {
                            @Override
                            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                                if (response.body().getResponseCode() == 200) {
                                    Toast.makeText(SetGoalPage.this, "Goal added sucessfully", Toast.LENGTH_SHORT).show();
                                    showPopUpDialog("Success", response.body().getResponseDescription());
                                }
                                else {
                                    Toast.makeText(SetGoalPage.this, response.body().getResponseDescription(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<GeneralResponse> call, Throwable t) {

                            }
                        });

                    } catch (Exception ex) {
                        Toast.makeText(SetGoalPage.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(SetGoalPage.this, "Update details empty!", Toast.LENGTH_SHORT).show();
                }
                showPopUpDialog("Success", "Goal added successfully");
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
            Intent intent = new Intent(SetGoalPage.this, MainPage.class);
            startActivity(intent);
        } else if (itemId == R.id.menu_export) {
            Intent intent = new Intent(SetGoalPage.this, ExportDataPage.class);
            startActivity(intent);
        } else if (itemId == R.id.menu_my_goals) {
            Intent intent = new Intent(SetGoalPage.this, MyGoalsPage.class);
            startActivity(intent);
        } else if (itemId == R.id.menu_achievments) {
            Intent intent = new Intent(SetGoalPage.this, AchiementsPage.class);
            startActivity(intent);
        } else if (itemId == R.id.menu_coaching_mentoring) {
            Intent intent = new Intent(SetGoalPage.this, CoachesPage.class);
            startActivity(intent);
        } else if (itemId == R.id.menu_feedback) {
            Intent intent = new Intent(SetGoalPage.this, FeedbackPage.class);
            startActivity(intent);
        } else if (itemId == R.id.menu_logout) {
            Intent intent = new Intent(SetGoalPage.this, LoginPage.class);
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
        View view = LayoutInflater.from(SetGoalPage.this).inflate(R.layout.alert_pop_up_success, alertPopUpSuccess);
        Button popUpButton = view.findViewById(R.id.alert_success_done);
        TextView alertSuccessTitle = view.findViewById(R.id.alert_success_title);
        TextView alertSuccessDescription = view.findViewById(R.id.alert_success_description);


        // setting the given title and description
        alertSuccessTitle.setText(popUpTitle);
        alertSuccessDescription.setText(popUpDescription);

        // build the pop up dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(SetGoalPage.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        // define the action of "Done" button in the pop up dialog
        popUpButton.findViewById(R.id.alert_success_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // below lines executes when clicking on the "Done" button in the pop up window
                // Navigates to the Landing page once clicked
                alertDialog.dismiss();
                Intent intent = new Intent(SetGoalPage.this, MainPage.class);
                startActivity(intent);
            }
        });
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    public void loadGoalWindow() {
        View goalView = getLayoutInflater().inflate(R.layout.set_goal_window, null);
        TextView tipGoalDescription = goalView.findViewById(R.id.set_goal_tip_desc);

        // get current selected TIP description and id from the mobile cache and set it to the object
//        SharedPreferences sharedpreferences = getSharedPreferences("balanceBeacon",MODE_PRIVATE);
        tipGoalDescription.setText(tipDescription_cache);

        // define button on the set goal window
        buttonOneDay = goalView.findViewById(R.id.set_goal_box1);
        buttonOneWeek = goalView.findViewById(R.id.set_goal_box2);
        buttonTenDays = goalView.findViewById(R.id.set_goal_box3);
        buttonOneMonth = goalView.findViewById(R.id.set_goal_box4);
        buttonCustom = goalView.findViewById(R.id.set_goal_box5);

        // clicking on buttons
        buttonOneDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnGoalButton(v, "1 Day");
            }
        });

        buttonOneWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnGoalButton(v, "1 Week");
            }
        });

        buttonTenDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnGoalButton(v, "10 Days");
            }
        });

        buttonOneMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnGoalButton(v, "1 Month");
            }
        });

        buttonCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnGoalButton(v, "Custom");
            }
        });
        setGoalPageLayout.removeAllViews();
        setGoalPageLayout.addView(goalView);
    }

    public void clickOnGoalButton(View view, String days) {
        buttonOneDay.setBackgroundResource(R.drawable.goal_design);
        buttonOneWeek.setBackgroundResource(R.drawable.goal_design);
        buttonTenDays.setBackgroundResource(R.drawable.goal_design);
        buttonOneMonth.setBackgroundResource(R.drawable.goal_design);
        buttonCustom.setBackgroundResource(R.drawable.goal_design);
        view.setBackgroundResource(R.drawable.goal_design_clicked);
        selectedGoalButton = view.getTag();

        // set days once the user clicked on a red color goal button (ex: 1 Day, 1 Week, etc..)
        goalDays = days;
    }
}