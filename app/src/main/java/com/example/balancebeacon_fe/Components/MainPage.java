package com.example.balancebeacon_fe.Components;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.balancebeacon_fe.Controllers.AssessAreaController;
import com.example.balancebeacon_fe.Models.UserAssessResponse;
import com.example.balancebeacon_fe.R;
import com.example.balancebeacon_fe.Shared.RetrofitClient;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPage extends AppCompatActivity {

    // global variables
    PieChart pieChart;
    RadarChart radarChart;
    TableLayout tableLayout;
    TextView pieChartMiddleText;

    Button mainPageContinueButton;
    ImageView shareIcon;
    UserAssessResponse userAssessResponse = new UserAssessResponse();
//    View rootView = getWindow().getDecorView().findViewById(R.id.main_table_layout);
//    Bitmap bitmap = MainPage.getBitmapFromView(rootView);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Wheel of Life");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pieChart = findViewById(R.id.main_piechart);
//        radarChart = findViewById(R.id.main_radar_chart);
        tableLayout = findViewById(R.id.main_table_layout);
        mainPageContinueButton = findViewById(R.id.main_page_continue_button);
        pieChartMiddleText = findViewById(R.id.pie_chart_middle_text);
        shareIcon = findViewById(R.id.share_icon);

        getAllUserAreas();

        // this function works when clicking on the continue button
        mainPageContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPage.this, TipsPage.class);
                startActivity(intent);
            }
        });

        // clicking on the Pir chart to do a new assessment
        pieChartMiddleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPage.this, LandingPage.class);
                startActivity(intent);
            }
        });

        // clicking on the share icon
        shareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPage.this, ExportDataPage.class);
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
        if (itemId == R.id.menu_export) {
            Intent intent = new Intent(MainPage.this, ExportDataPage.class);
            startActivity(intent);
        } else if (itemId == R.id.menu_my_goals) {
            Intent intent = new Intent(MainPage.this, MyGoalsPage.class);
            startActivity(intent);
        } else if (itemId == R.id.menu_achievments) {
            Intent intent = new Intent(MainPage.this, AchiementsPage.class);
            startActivity(intent);
        } else if (itemId == R.id.menu_coaching_mentoring) {
            Intent intent = new Intent(MainPage.this, CoachesPage.class);
            startActivity(intent);
        } else if (itemId == R.id.menu_feedback) {
            Intent intent = new Intent(MainPage.this, FeedbackPage.class);
            startActivity(intent);
        } else if (itemId == R.id.menu_logout) {
            Intent intent = new Intent(MainPage.this, LoginPage.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    // this function will load all the user selected areas and their Future & Current values to the Pie chart and the Table
    public void getAllUserAreas() {
        AssessAreaController assessAreaController = RetrofitClient.getRetrofitInstance().create(AssessAreaController.class);

        // get current logged user ID
        // save logged in user details to the mobile cache
        SharedPreferences sharedpreferences = getSharedPreferences("balanceBeacon",MODE_PRIVATE);
        int userId = sharedpreferences.getInt("userId", 0);

        Call<UserAssessResponse> call = assessAreaController.getAllUserAreas(userId);

        call.enqueue(new Callback<UserAssessResponse>() {
            @Override
            public void onResponse(Call<UserAssessResponse> call, Response<UserAssessResponse> response) {

                if (response.body().getResponseCode() == 200) {
                    Toast.makeText(MainPage.this, response.body().getResponseDescription(), Toast.LENGTH_SHORT).show();
                    userAssessResponse = response.body();
                    loadPieChart();
                    loadTable();

                    // set assessment id in the cache memory
                    SharedPreferences sharedpreferences = getSharedPreferences("balanceBeacon",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putInt("assessmentId", response.body().getAssessmentId());
                    editor.apply();

                    System.out.println("Assessment ID: " + sharedpreferences.getInt("assessmentId", 0));
                }
                else {
                    Toast.makeText(MainPage.this, response.body().getResponseDescription(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserAssessResponse> call, Throwable t) {
                Toast.makeText(MainPage.this, "Area loading failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadPieChart() {
        System.out.println("Load pie chart function starts");
        ArrayList<PieEntry> entries = new ArrayList<>();
        int areaLoopCount = userAssessResponse.getAssessmentPayloads().size();

        try {
            for (int i=0 ; i<areaLoopCount ; i++) {
                entries.add(new PieEntry((userAssessResponse.getAssessmentPayloads().get(i).getAreaCurrent()), userAssessResponse.getAssessmentPayloads().get(i).getAreaDescription()));
            }

            PieDataSet pieDataSet = new PieDataSet(entries, "");
            pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

            pieChart.setCenterTextSize(14);
            pieChart.getDescription().setEnabled(false);
            pieChart.getLegend().setEnabled(false);
            PieData pieData = new PieData(pieDataSet);
            pieChart.setData(pieData);

            pieChart.animateY(1200);
            pieChart.animateX(1000);
            pieChart.invalidate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // function to load table and the table data
    public void loadTable() {
        // the below line added to expand the column data accordingly
        tableLayout.setStretchAllColumns(true);
        tableLayout.removeAllViews();

        try {
            for (int i=0 ; i<userAssessResponse.getAssessmentPayloads().size() ; i++) {
                TableRow tableRow = new TableRow(MainPage.this);
                tableRow.setWeightSum(16);
                System.out.println("LOOP COUNT: " + i);
                View tableView = getLayoutInflater().inflate(R.layout.table_data, null);

                TextView areaText = tableView.findViewById(R.id.table_data_area);
                areaText.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 4f));


                TextView currentText = tableView.findViewById(R.id.table_data_current);
                currentText.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 4f));

                TextView futureText = tableView.findViewById(R.id.table_data_future);
                futureText.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 4f));

                TextView gapText = tableView.findViewById(R.id.table_data_gap);
                gapText.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 4f));

                areaText.setText(userAssessResponse.getAssessmentPayloads().get(i).getAreaDescription());
                currentText.setText("" + userAssessResponse.getAssessmentPayloads().get(i).getAreaCurrent());
                futureText.setText("" + userAssessResponse.getAssessmentPayloads().get(i).getAreaFuture());
                gapText.setText("" + (userAssessResponse.getAssessmentPayloads().get(i).getAreaFuture() - userAssessResponse.getAssessmentPayloads().get(i).getAreaCurrent()));

                System.out.println("Area desc: " + userAssessResponse.getAssessmentPayloads().get(i).getAreaDescription());
                tableRow.addView(tableView);
                tableLayout.addView(tableRow);
            }

            //
//            try {
//                File imageFile = MainPage.saveBitmap(this, bitmap);
//                MainPage.shareImage(this, imageFile);
//                Toast.makeText(MainPage.this, "Screenshot taken", Toast.LENGTH_SHORT).show();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Bitmap getBitmapFromView(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public static File saveBitmap(Context context, Bitmap bitmap) throws IOException {
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "layout_image.png");
        FileOutputStream outputStream = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        outputStream.flush();
        outputStream.close();
        return file;
    }

    public static void shareImage(Context context, File file) {
        if (file != null && file.exists()) {
            // Share the image file
            // Create the intent
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(Intent.createChooser(shareIntent, "Share layout as image"));
        } else {
            // Handle error
            Toast.makeText(context, "Error sharing image", Toast.LENGTH_SHORT).show();
        }
    }
}