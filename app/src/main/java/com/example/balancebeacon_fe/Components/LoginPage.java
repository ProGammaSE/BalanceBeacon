package com.example.balancebeacon_fe.Components;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.balancebeacon_fe.Controllers.UserController;
import com.example.balancebeacon_fe.Models.Login;
import com.example.balancebeacon_fe.Models.UserResponse;
import com.example.balancebeacon_fe.R;
import com.example.balancebeacon_fe.Shared.RetrofitClient;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPage extends AppCompatActivity {

    // global variables
    ImageView loginLoginButton;
    EditText loginUsername;
    EditText loginPassword;
    TextView loginCreateNewAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loginLoginButton = findViewById(R.id.login_login_button);
        loginUsername = findViewById(R.id.login_email_field);
        loginPassword = findViewById(R.id.login_password_field);
        loginCreateNewAccount = findViewById(R.id.login_create_new_account);

        // button click
        loginLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Clicking on the Login button");
                clickOnTheLogin();
            }
        });

        // clicking on the "Create new account" text
        loginCreateNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginPage.this, RegisterPage.class);
                startActivity(intent);
            }
        });
    }

    /**
     * pop up window to show the result of requests.
     */
    private void showPopUpDialog(String popUpTitle, String popUpDescription, int status) {
        // declaring parameters
        ConstraintLayout alertPopUpSuccess = findViewById(R.id.alertPopUpSuccess);
        View view = LayoutInflater.from(LoginPage.this).inflate(R.layout.alert_pop_up_success, alertPopUpSuccess);
        Button popUpButton = view.findViewById(R.id.alert_success_done);
        TextView alertSuccessTitle = view.findViewById(R.id.alert_success_title);
        TextView alertSuccessDescription = view.findViewById(R.id.alert_success_description);


        // setting the given title and description
        alertSuccessTitle.setText(popUpTitle);
        alertSuccessDescription.setText(popUpDescription);

        // build the pop up dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginPage.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        // define the action of "Done" button in the pop up dialog
        popUpButton.findViewById(R.id.alert_success_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // below lines executes when clicking on the "Done" button in the pop up window
                // Navigates to the Landing page once clicked
                alertDialog.dismiss();

                if (status == 1) {
                    // the user is new to the system and does not have any assessments yet
                    Intent intent = new Intent(LoginPage.this, LandingPage.class);
                    startActivity(intent);
                }
                else {
                    // the user is old to the system and there are assessments completed
                    Intent intent = new Intent(LoginPage.this, MainPage.class);
                    startActivity(intent);
                }
            }
        });
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    public void clickOnTheLogin() {
        // checking whether the email field or the password fields are empty
        String userEmail = loginUsername.getText().toString();
        String userPassword = loginPassword.getText().toString();

        if (userEmail.isEmpty()) {
            Toast.makeText(LoginPage.this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else if (userPassword.isEmpty()) {
            Toast.makeText(LoginPage.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else {
            // creating a new object of the Login class and
            // assign the email & the password into it
            Login login = new Login();
            login.setUserEmail(userEmail);
            login.setUserPassword(userPassword);

            // create Retrofit object to send data to the backend
            try {
                UserController userController = RetrofitClient.getRetrofitInstance().create(UserController.class);
                Call<UserResponse> call = userController.userLogin(login);
                System.out.println("call: " + call.toString());

                call.enqueue(new Callback<UserResponse>() {

                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        if (response.body().getResponseStatus() == 200) {

                            // save logged in user details to the mobile cache
                            SharedPreferences sharedpreferences = getSharedPreferences("balanceBeacon",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();

                            editor.putInt("userId", response.body().getUsers().getUserId());
                            editor.putString("userName", response.body().getUsers().getUserName());
                            editor.apply();

                            Toast.makeText(LoginPage.this, response.body().getResponseDescription(), Toast.LENGTH_SHORT).show();
                            showPopUpDialog("Success",  sharedpreferences.getString("userName", "User") + " logged in successfully", response.body().getUsers().getUserStatus());

                        }
                        else {
                            Toast.makeText(LoginPage.this, response.body().getResponseDescription(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        System.out.println(t.getMessage());
                        System.out.println(t.getCause());
                        t.printStackTrace();
                        Toast.makeText(LoginPage.this, "Login failed!", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception ex) {
                Toast.makeText(LoginPage.this, "Cannot connect to the system!", Toast.LENGTH_SHORT).show();
                System.out.println(ex.getMessage());
                System.out.println(ex.getCause());
                ex.printStackTrace();
            }
        }
    }
}