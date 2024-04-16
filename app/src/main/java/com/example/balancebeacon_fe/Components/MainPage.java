package com.example.balancebeacon_fe.Components;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.balancebeacon_fe.Controllers.AssessAreaController;
import com.example.balancebeacon_fe.Models.UserAssessResponse;
import com.example.balancebeacon_fe.R;
import com.example.balancebeacon_fe.Shared.RetrofitClient;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPage extends AppCompatActivity {

    // global variables
    PieChart pieChart;
    TableLayout tableLayout;
    UserAssessResponse userAssessResponse = new UserAssessResponse();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        pieChart = findViewById(R.id.main_piechart);
        tableLayout = findViewById(R.id.main_table_layout);
        getAllUserAreas();
    }

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
                    assert response.body() != null;
                    loadPieChart();
                    loadTable();
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
        ArrayList<PieEntry> entries = new ArrayList<>();
        int areaLoopCount = userAssessResponse.getAssessmentPayloads().size();

        for (int i=0 ; i<areaLoopCount ; i++) {
            entries.add(new PieEntry(userAssessResponse.getAssessmentPayloads().get(i).getAreaCurrent(), userAssessResponse.getAssessmentPayloads().get(i).getAreaDescription()));
        }

        PieDataSet pieDataSet = new PieDataSet(entries, "Areas");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);

        pieChart.getDescription().setEnabled(false);
        pieChart.animateY(1000);
        pieChart.invalidate();
    }

    public void loadTable() {
        try {
            for (int i=0 ; i<userAssessResponse.getAssessmentPayloads().size() ; i++) {
                TableRow tableRow = new TableRow(MainPage.this);
                System.out.println("LOOP COUNT: " + i);
                View view = getLayoutInflater().inflate(R.layout.table_data, null);

                TextView areaText = view.findViewById(R.id.table_data_area);
                TextView currentText = view.findViewById(R.id.table_data_current);
                TextView futureText = view.findViewById(R.id.table_data_future);
                TextView gapText = view.findViewById(R.id.table_data_gap);

                areaText.setText(userAssessResponse.getAssessmentPayloads().get(i).getAreaDescription());
                currentText.setText("" + userAssessResponse.getAssessmentPayloads().get(i).getAreaCurrent());
                futureText.setText("" + userAssessResponse.getAssessmentPayloads().get(i).getAreaFuture());
                gapText.setText("" + (userAssessResponse.getAssessmentPayloads().get(i).getAreaFuture() - userAssessResponse.getAssessmentPayloads().get(i).getAreaCurrent()));

                tableRow.addView(view);
                tableLayout.addView(tableRow);
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}