package com.example.balancebeacon_fe.Components;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPage extends AppCompatActivity {

    // global variables
    PieChart pieChart;
    RadarChart radarChart;
    TableLayout tableLayout;

    ImageView mainPageContinueButton;
    UserAssessResponse userAssessResponse = new UserAssessResponse();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Objects.requireNonNull(getSupportActionBar()).setTitle("BalanceBeacon");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pieChart = findViewById(R.id.main_piechart);
//        radarChart = findViewById(R.id.main_radar_chart);
        tableLayout = findViewById(R.id.main_table_layout);
        mainPageContinueButton = findViewById(R.id.main_page_continue_button);

        getAllUserAreas();

        // this function works when clicking on the continue button
        mainPageContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPage.this, TipsPage.class);
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
            Intent intent = new Intent(MainPage.this, MainPage.class);
            startActivity(intent);
        } else if (itemId == R.id.menu_assessments) {
            Intent intent = new Intent(MainPage.this, AssessmentPage.class);
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
//                    loadRadarChart();
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
                entries.add(new PieEntry((userAssessResponse.getAssessmentPayloads().get(i).getAreaFuture()), userAssessResponse.getAssessmentPayloads().get(i).getAreaDescription()));
            }

            PieDataSet pieDataSet = new PieDataSet(entries, "Areas");
            pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

            PieData pieData = new PieData(pieDataSet);
            pieChart.setData(pieData);

            pieChart.getDescription().setEnabled(true);
            pieChart.animateY(1000);
            pieChart.invalidate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//    public void loadRadarChart() {
//        RadarChart radarChart = findViewById(R.id.test_redar_chart);
//
//        ArrayList<RadarEntry> userAreas = new ArrayList<>();
//
//        userAreas.add(new RadarEntry(10, "1000"));
//        userAreas.add(new RadarEntry(2, 2500));
//        userAreas.add(new RadarEntry(5, 2200));
//        userAreas.add(new RadarEntry(0, 2000));
//        userAreas.add(new RadarEntry(1, 999));
//        userAreas.add(new RadarEntry(6, 1890));
//
//        RadarDataSet radarDataSet = new RadarDataSet(userAreas, "Test areas");
//        radarDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
//        radarDataSet.setLineWidth(2f);
//        radarDataSet.setValueTextColor(Color.RED);
//        radarDataSet.setValueTextSize(14f);
//        radarDataSet.setFillColor(Color.GREEN);
//        radarDataSet.setDrawFilled(true);
//
////        radarChart.setx
//
//        RadarData radarData = new RadarData();
//        radarData.addDataSet(radarDataSet);
//
//        radarChart.animateY(1000);
//        radarChart.invalidate();
//
//        String[] labels = {"Family. Love. Kids", "Money", "Education", "Hobbies", "Love & Romance", "Personal Growth"};
//
//        XAxis xAxis = radarChart.getXAxis();
//        xAxis.mAxisMaximum = 10;
//        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
//
//        radarChart.setData(radarData);
//    }

    public void loadTable() {
        tableLayout.removeAllViews();



        try {
            for (int i=0 ; i<userAssessResponse.getAssessmentPayloads().size() ; i++) {
                TableRow tableRow = new TableRow(MainPage.this);
                System.out.println("LOOP COUNT: " + i);
                View tableView = getLayoutInflater().inflate(R.layout.table_data, null);

                TextView areaText = tableView.findViewById(R.id.table_data_area);
                TextView currentText = tableView.findViewById(R.id.table_data_current);
                TextView futureText = tableView.findViewById(R.id.table_data_future);
                TextView gapText = tableView.findViewById(R.id.table_data_gap);

                areaText.setText(userAssessResponse.getAssessmentPayloads().get(i).getAreaDescription());
                currentText.setText("" + userAssessResponse.getAssessmentPayloads().get(i).getAreaCurrent());
                futureText.setText("" + userAssessResponse.getAssessmentPayloads().get(i).getAreaFuture());
                gapText.setText("" + (userAssessResponse.getAssessmentPayloads().get(i).getAreaFuture() - userAssessResponse.getAssessmentPayloads().get(i).getAreaCurrent()));

                System.out.println("Area desc: " + userAssessResponse.getAssessmentPayloads().get(i).getAreaDescription());
                tableRow.addView(tableView);
                tableLayout.addView(tableRow);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}