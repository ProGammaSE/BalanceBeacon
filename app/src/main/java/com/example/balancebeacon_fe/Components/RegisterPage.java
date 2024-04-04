package com.example.balancebeacon_fe.Components;

import android.app.AlertDialog;
import android.content.Intent;
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
    ImageView registerBackButton;
    TextView registerAlreadyRegistered;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_page);

        registerNameField = findViewById(R.id.register_name_field);
        registerEmailField = findViewById(R.id.register_email_field);
        registerPhoneField = findViewById(R.id.register_phone_field);
        registerAgeField = findViewById(R.id.register_age_field);
        registerPasswordField = findViewById(R.id.register_password_field);
        registerConfirmPasswordField = findViewById(R.id.register_confirm_password_field);
        registerContinueButton = findViewById(R.id.register_continue_button);
        registerBackButton = findViewById(R.id.register_back_button);
        registerAlreadyRegistered = findViewById(R.id.register_already_registered);

        // clicking on the continue button in the Register screen
        registerContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickOnTheRegister();
            }
        });

        // clicking on the back button
        registerBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterPage.this, WelcomePage.class);
                startActivity(intent);
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
            Toast.makeText(RegisterPage.this, "Name cannot be empty", Toast.LENGTH_LONG).show();
        }
        else if (registerEmail.isEmpty()) {
            Toast.makeText(RegisterPage.this, "Email cannot be empty", Toast.LENGTH_LONG).show();
        }
        else if (registerPhone.isEmpty()) {
            Toast.makeText(RegisterPage.this, "Phone number cannot be empty", Toast.LENGTH_LONG).show();
        }
        else if (registerAge.isEmpty()) {
            Toast.makeText(RegisterPage.this, "Age cannot be empty", Toast.LENGTH_LONG).show();
        }
        else if (registerPassword.isEmpty()) {
            Toast.makeText(RegisterPage.this, "Password cannot be empty", Toast.LENGTH_LONG).show();
        }
        else if (confirmPassword.isEmpty()) {
            Toast.makeText(RegisterPage.this, "Please confirm the password", Toast.LENGTH_LONG).show();
        }
        else if (!registerPassword.equals(confirmPassword)) {
            Toast.makeText(RegisterPage.this, "Passwords do not match", Toast.LENGTH_LONG).show();

            // clean the values inside the password fields
            registerPasswordField.setText("");
            registerConfirmPasswordField.setText("");
        }
        else {
            Users user = new Users();
            user.setUserName(registerName);
            user.setUserEmail(registerEmail);
            user.setUserPhone(registerPhone);
            user.setUserGender(1);
            user.setUserAge(Integer.parseInt(registerAge));
            user.setUserPassword(registerPassword);
            user.setUserStatus(true);

            UserController userController = RetrofitClient.getRetrofitInstance().create(UserController.class);
            Call<UserResponse> call = userController.registerUser(user);
            call.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    if (response.body().getResponseStatus() == 200) {
                        Toast.makeText(RegisterPage.this, response.body().getResponseDescription(), Toast.LENGTH_LONG).show();
                        showPopUpDialog("Success", "BalanceBeacon user registration");

                    }
                    else {
                        Toast.makeText(RegisterPage.this, response.body().getResponseDescription(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                    Toast.makeText(RegisterPage.this, "Registration failed", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}