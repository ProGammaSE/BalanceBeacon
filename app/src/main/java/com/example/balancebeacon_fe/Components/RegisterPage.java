package com.example.balancebeacon_fe.Components;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.balancebeacon_fe.Controllers.UserController;
import com.example.balancebeacon_fe.Models.UserResponse;
import com.example.balancebeacon_fe.Models.Users;
import com.example.balancebeacon_fe.R;
import com.example.balancebeacon_fe.Shared.RetrofitClient;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterPage extends AppCompatActivity {

    // global variables
    EditText registerNameField;
    EditText registerEmailField;
    EditText registerPhoneField;
    EditText registerAgeField;
    EditText registerPasswordField;
    EditText registerConfirmPasswordField;
    ImageView registerContinueButton;
    TextView registerAlreadyRegistered;
    Button maleButton, femaleButton;
    int genderValue = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        registerNameField = findViewById(R.id.register_name_field);
        registerEmailField = findViewById(R.id.register_email_field);
        registerPhoneField = findViewById(R.id.register_phone_field);
        registerAgeField = findViewById(R.id.register_age_field);
        registerPasswordField = findViewById(R.id.register_password_field);
        registerConfirmPasswordField = findViewById(R.id.register_confirm_password_field);
        registerContinueButton = findViewById(R.id.register_continue_button);
        registerAlreadyRegistered = findViewById(R.id.register_already_registered);
        maleButton = findViewById(R.id.gender_male);
        femaleButton = findViewById(R.id.gender_female);

        // clicking on the gender MALE button
        maleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                femaleButton.setTextColor(Color.parseColor("#FFFFFF"));
                maleButton.setTextColor(Color.parseColor("#FF8B00"));
                genderValue = 1;
            }
        });

        // clicking on the gender FEMALE button
        femaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maleButton.setTextColor(Color.parseColor("#FFFFFF"));
                femaleButton.setTextColor(Color.parseColor("#FF8B00"));
                genderValue = 2;
            }
        });

        // clicking on the continue button in the Register screen
        registerContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickOnTheRegister();
            }
        });

        // clicking on the "Already Registered" text
        registerAlreadyRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterPage.this, LoginPage.class);
                startActivity(intent);
            }
        });
    }

    private void showPopUpDialog(String popUpTitle, String popUpDescription) {
        // declaring parameters
        ConstraintLayout alertPopUpSuccess = findViewById(R.id.alertPopUpSuccess);
        View view = LayoutInflater.from(RegisterPage.this).inflate(R.layout.alert_pop_up_success, alertPopUpSuccess);
        Button popUpButton = view.findViewById(R.id.alert_success_done);
        TextView alertSuccessTitle = view.findViewById(R.id.alert_success_title);
        TextView alertSuccessDescription = view.findViewById(R.id.alert_success_description);


        // setting the given title and description
        alertSuccessTitle.setText(popUpTitle);
        alertSuccessDescription.setText(popUpDescription);

        // build the pop up dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterPage.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        // define the action of "Done" button in the pop up dialog
        popUpButton.findViewById(R.id.alert_success_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // below lines executes when clicking on the "Done" button in the pop up window
                // Navigates to the Login page once clicked
                alertDialog.dismiss();
                Intent intent = new Intent(RegisterPage.this, LoginPage.class);
                startActivity(intent);
            }
        });
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    private void clickOnTheRegister() {
        String registerName = registerNameField.getText().toString();
        String registerEmail = registerEmailField.getText().toString();
        String registerPhone = registerPhoneField.getText().toString();
        String registerAge = registerAgeField.getText().toString();
        String registerPassword = registerPasswordField.getText().toString();
        String confirmPassword = registerConfirmPasswordField.getText().toString();

        if (registerName.isEmpty()) {
            Toast.makeText(RegisterPage.this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else if (registerEmail.isEmpty()) {
            Toast.makeText(RegisterPage.this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else if (!registerEmail.contains("@")) {
            Toast.makeText(RegisterPage.this, "Please enter an valid email", Toast.LENGTH_SHORT).show();
        }
        else if (!registerEmail.contains(" ")) {
            Toast.makeText(RegisterPage.this, "Please enter an valid email", Toast.LENGTH_SHORT).show();
        }
        else if (registerPhone.isEmpty()) {
            Toast.makeText(RegisterPage.this, "Phone number cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else if (registerPhone.length() < 11) {
            Toast.makeText(RegisterPage.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
        }
        else if (genderValue == 0) {
            Toast.makeText(RegisterPage.this, "Please select your gender", Toast.LENGTH_SHORT).show();
        }
        else if (registerAge.isEmpty()) {
            Toast.makeText(RegisterPage.this, "Age cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else if (Integer.parseInt(registerAge) < 18 || Integer.parseInt(registerAge) > 40) {
            Toast.makeText(RegisterPage.this, "Age has to be between 18 & 40", Toast.LENGTH_SHORT).show();
        }
        else if (registerPassword.isEmpty()) {
            Toast.makeText(RegisterPage.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else if (registerPassword.contains(" ")) {
            Toast.makeText(RegisterPage.this, "Password cannot have spaces", Toast.LENGTH_SHORT).show();
        }
        else if (!registerPassword.matches(".*\\d.*")) {
            Toast.makeText(RegisterPage.this, "Password should have at least 1 number", Toast.LENGTH_SHORT).show();
        }
        else if (!registerPassword.matches(".*[A-Z].*")) {
            Toast.makeText(RegisterPage.this, "Password should have at least 1 capital letter", Toast.LENGTH_SHORT).show();
        }
        else if (registerPassword.length() < 5 || registerPassword.length() > 15) {
            Toast.makeText(RegisterPage.this, "The password must be 5 to 15 long", Toast.LENGTH_SHORT).show();
        }
        else if (confirmPassword.isEmpty()) {
            Toast.makeText(RegisterPage.this, "Please confirm the password", Toast.LENGTH_SHORT).show();
        }
        else if (!registerPassword.equals(confirmPassword)) {
            Toast.makeText(RegisterPage.this, "Passwords do not match", Toast.LENGTH_SHORT).show();

            // clean the values inside the password fields
            registerPasswordField.setText("");
            registerConfirmPasswordField.setText("");
        }
        else {
            Users user = new Users();
            user.setUserName(registerName);
            user.setUserEmail(registerEmail);
            user.setUserPhone(registerPhone);
            user.setUserGender(genderValue);
            user.setUserAge(Integer.parseInt(registerAge));
            user.setUserPassword(registerPassword);
            user.setUserStatus(1);

            UserController userController = RetrofitClient.getRetrofitInstance().create(UserController.class);
            Call<UserResponse> call = userController.registerUser(user);
            call.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    if (response.body().getResponseStatus() == 200) {
                        Toast.makeText(RegisterPage.this, response.body().getResponseDescription(), Toast.LENGTH_SHORT).show();
                        showPopUpDialog("Success", "BalanceBeacon user registration");

                    }
                    else {
                        Toast.makeText(RegisterPage.this, response.body().getResponseDescription(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                    Toast.makeText(RegisterPage.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}