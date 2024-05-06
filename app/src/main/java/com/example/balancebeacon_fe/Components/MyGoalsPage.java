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

import com.example.balancebeacon_fe.Controllers.GoalController;
import com.example.balancebeacon_fe.Controllers.UserTipsController;
import com.example.balancebeacon_fe.Models.GeneralResponse;
import com.example.balancebeacon_fe.Models.Goals;
import com.example.balancebeacon_fe.Models.UpdateUserTips;
import com.example.balancebeacon_fe.R;
import com.example.balancebeacon_fe.Shared.RetrofitClient;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyGoalsPage extends AppCompatActivity {

    LinearLayout activityMyGoalLayout;
    ImageView activityMyGoalButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_goals_page);
        Objects.requireNonNull(getSupportActionBar()).setTitle("My Goals");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activityMyGoalLayout = findViewById(R.id.activity_my_goal_layout);
        activityMyGoalButton = findViewById(R.id.activity_my_goal_button);

        loadMyGoals();

        // this function works when clicking on the "Continue" button
        activityMyGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyGoalsPage.this, MainPage.class);
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
            Intent intent = new Intent(MyGoalsPage.this, MainPage.class);
            startActivity(intent);
        } else if (itemId == R.id.menu_export) {
            Intent intent = new Intent(MyGoalsPage.this, ExportDataPage.class);
            startActivity(intent);
        } else if (itemId == R.id.menu_achievments) {
            Intent intent = new Intent(MyGoalsPage.this, AchiementsPage.class);
            startActivity(intent);
        } else if (itemId == R.id.menu_coaching_mentoring) {
            Intent intent = new Intent(MyGoalsPage.this, CoachesPage.class);
            startActivity(intent);
        } else if (itemId == R.id.menu_feedback) {
            Intent intent = new Intent(MyGoalsPage.this, FeedbackPage.class);
            startActivity(intent);
        } else if (itemId == R.id.menu_logout) {
            Intent intent = new Intent(MyGoalsPage.this, LoginPage.class);
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
        View view = LayoutInflater.from(MyGoalsPage.this).inflate(R.layout.alert_pop_up_success, alertPopUpSuccess);
        Button popUpButton = view.findViewById(R.id.alert_success_done);
        TextView alertSuccessTitle = view.findViewById(R.id.alert_success_title);
        TextView alertSuccessDescription = view.findViewById(R.id.alert_success_description);


        // setting the given title and description
        alertSuccessTitle.setText(popUpTitle);
        alertSuccessDescription.setText(popUpDescription);

        // build the pop up dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(MyGoalsPage.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        // define the action of "Done" button in the pop up dialog
        popUpButton.findViewById(R.id.alert_success_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // below lines executes when clicking on the "Done" button in the pop up window
                alertDialog.dismiss();
                loadMyGoals();

//                Intent intent = new Intent(MyGoalsPage.this, MyGoalsPage.class);
//                startActivity(intent);
            }
        });
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    // function to load all my tips boxes & details loaded from the backend (database)
    public void loadMyGoals() {
        activityMyGoalLayout.removeAllViews();
        try {
            // get current logged in user ID from the mobile cache and set it to the object
            SharedPreferences sharedpreferences = getSharedPreferences("balanceBeacon",MODE_PRIVATE);
            int userId = sharedpreferences.getInt("userId", 0);

            GoalController goalController = RetrofitClient.getRetrofitInstance().create(GoalController.class);
            Call<List<Goals>> call = goalController.getAllGoals(userId);

            call.enqueue(new Callback<List<Goals>>() {
                @Override
                public void onResponse(Call<List<Goals>> call, Response<List<Goals>> response) {
                    Toast.makeText(MyGoalsPage.this, "Goals loaded successfully", Toast.LENGTH_SHORT).show();

                    for (int i=0 ; i<response.body().size() ; i++) {
                        // defining objects in the layout
                        View myGoalView = getLayoutInflater().inflate(R.layout.my_goal_design, null);
                        TextView myGoalBoxTitle = myGoalView.findViewById(R.id.my_goal_box_title);
                        TextView myGoalBoxDescription = myGoalView.findViewById(R.id.my_goal_box_description);
                        TextView myGoalBoxDays = myGoalView.findViewById(R.id.my_goal_box_days);
                        TextView myGoalBoxStatus = myGoalView.findViewById(R.id.my_goal_box_status);

                        // assign values to the defined objects
                        String status = "";

                        if (response.body().get(i).getTipStatus() == 0) {
                            status = "Not Set";
                        }
                        if (response.body().get(i).getTipStatus() == 1) {
                            status = "Done";
                        }
                        if (response.body().get(i).getTipStatus() == 2) {
                            status = "Completed";
                        }

                        myGoalBoxTitle.setText(response.body().get(i).getAreaDescription());
                        myGoalBoxDescription.setText(response.body().get(i).getTipDescription());
                        myGoalBoxDays.setText(response.body().get(i).getGoalDays());
                        myGoalBoxStatus.setText(status);
                        int loop = i;

                        // if the goal is a active one then, Done click will available
                        // for completed and not set goals. click events will not be added
                        if (status.equals("Done")) {
                            myGoalBoxStatus.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // if user clicks on "Done" button then it will update the current goal in the database
                                    // creating a new "UpdateUserTips" object
                                    UpdateUserTips updateUserTips = new UpdateUserTips();
                                    updateUserTips.setUserTipId(response.body().get(loop).getUserTipId());
                                    updateUserTips.setAreaDescription(response.body().get(loop).getAreaDescription());
                                    updateUserTips.setTipDescription(response.body().get(loop).getTipDescription());
                                    updateUserTips.setTipStatus(2);
                                    updateUserTips.setGoalDays(response.body().get(loop).getGoalDays());

                                    UserTipsController userTipsController = RetrofitClient.getRetrofitInstance().create(UserTipsController.class);
                                    Call<GeneralResponse> call = userTipsController.updateUserTips(updateUserTips);

                                    call.enqueue(new Callback<GeneralResponse>() {
                                        @Override
                                        public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                                            if (response.body().getResponseCode() == 200) {
                                                Toast.makeText(MyGoalsPage.this, "Goal completed successfully", Toast.LENGTH_SHORT).show();

                                                // show pop up message
                                                showPopUpDialog("Wow! You are moving forward", "Your goal is successfully completed");
                                            }
                                            else {
                                                Toast.makeText(MyGoalsPage.this, response.body().getResponseDescription(), Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<GeneralResponse> call, Throwable t) {

                                        }
                                    });
                                }
                            });
                        }

                        activityMyGoalLayout.addView(myGoalView);
                    }
                }

                @Override
                public void onFailure(Call<List<Goals>> call, Throwable t) {

                }
            });
        } catch (Exception ex) {

        }
    }
}