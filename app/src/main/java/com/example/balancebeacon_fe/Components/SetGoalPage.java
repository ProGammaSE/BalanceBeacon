package com.example.balancebeacon_fe.Components;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.balancebeacon_fe.R;

import java.util.Objects;

public class SetGoalPage extends AppCompatActivity {

    LinearLayout setGoalPageLayout;
    TextView buttonOneDay, buttonOneWeek, buttonTenDays, buttonOneMonth, buttonCustom;
    Button setGoalPageButton;
    Object selectedGoalButton = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_goal_page);
        setGoalPageLayout = findViewById(R.id.set_goal_page_layout);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Set Goal");
        setGoalPageButton = findViewById(R.id.set_goal_page_button);

        loadGoalWindow();

        // clicking on the "Set goal" button at the bottom of the Set Goal screen
        setGoalPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUpDialog("Success", "Goal added successfully");
            }
        });
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
        SharedPreferences sharedpreferences = getSharedPreferences("balanceBeacon",MODE_PRIVATE);
        tipGoalDescription.setText(sharedpreferences.getString("goalDescription", "null"));

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
                clickOnGoalButton(v);
            }
        });

        buttonOneWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnGoalButton(v);
            }
        });

        buttonTenDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnGoalButton(v);
            }
        });

        buttonOneMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnGoalButton(v);
            }
        });

        buttonCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnGoalButton(v);
            }
        });
        setGoalPageLayout.removeAllViews();
        setGoalPageLayout.addView(goalView);
    }

    public void clickOnGoalButton(View view) {
        buttonOneDay.setBackgroundResource(R.drawable.goal_design);
        buttonOneWeek.setBackgroundResource(R.drawable.goal_design);
        buttonTenDays.setBackgroundResource(R.drawable.goal_design);
        buttonOneMonth.setBackgroundResource(R.drawable.goal_design);
        buttonCustom.setBackgroundResource(R.drawable.goal_design);
        view.setBackgroundResource(R.drawable.goal_design_clicked);
        selectedGoalButton = view.getTag();
    }
}