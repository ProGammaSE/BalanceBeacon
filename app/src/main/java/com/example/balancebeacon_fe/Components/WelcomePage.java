package com.example.balancebeacon_fe.Components;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.balancebeacon_fe.R;

public class WelcomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        getSupportActionBar().setTitle("Welcome");

        ImageView welcomeLoginButton;
        ImageView welcomeRegisterButton;

        welcomeLoginButton = findViewById(R.id.welcome_login_button);
        welcomeRegisterButton = findViewById(R.id.welcome_register_button);


        /**
         * this function works when clicking on the Login button in the Welcome page
         * when clicking it navigates to the Login page
         */
        welcomeLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomePage.this, LoginPage.class);
                startActivity(intent);
            }
        });

        /**
         * this function works when clicking on the Register button in the Welcome page
         * when clicking it navigates to the Register page
         */
        welcomeRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomePage.this, RegisterPage.class);
                startActivity(intent);
            }
        });
    }
}