package com.example.balancebeacon_fe.Components;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.balancebeacon_fe.R;

public class LoadingPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_page);

        // hide top action bar
        getSupportActionBar().hide();

        // handle a 2 seconds delay and navigate to the next screen
        HandlerThread handlerThread = new HandlerThread("background-thread");
        handlerThread.start();
        final Handler handler = new Handler(handlerThread.getLooper());
        handler.postDelayed(new Runnable() {
            @Override public void run() {
                // call some methods here
                Intent intent = new Intent(LoadingPage.this, WelcomePage.class);
                startActivity(intent);

                // make sure to finish the thread to avoid leaking memory
                handlerThread.quitSafely();
            }
        }, 6000);
    }
}