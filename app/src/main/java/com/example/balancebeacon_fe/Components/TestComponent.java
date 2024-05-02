package com.example.balancebeacon_fe.Components;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.balancebeacon_fe.R;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class TestComponent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_component);

        RadarChart radarChart = findViewById(R.id.test_redar_chart);

        ArrayList<RadarEntry> userAreas = new ArrayList<>();

        userAreas.add(new RadarEntry(10, "1000"));
        userAreas.add(new RadarEntry(2, 2500));
        userAreas.add(new RadarEntry(5, 2200));
        userAreas.add(new RadarEntry(0, 2000));
        userAreas.add(new RadarEntry(1, 999));
        userAreas.add(new RadarEntry(6, 1890));

        RadarDataSet radarDataSet = new RadarDataSet(userAreas, "Test areas");
        radarDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        radarDataSet.setLineWidth(2f);
        radarDataSet.setValueTextColor(Color.RED);
        radarDataSet.setValueTextSize(14f);
        radarDataSet.setFillColor(Color.GREEN);
        radarDataSet.setDrawFilled(true);

//        radarChart.setx

        RadarData radarData = new RadarData();
        radarData.addDataSet(radarDataSet);

        radarChart.animateY(1000);
        radarChart.invalidate();

        String[] labels = {"Family. Love. Kids", "Money", "Education", "Hobbies", "Love & Romance", "Personal Growth"};

        XAxis xAxis = radarChart.getXAxis();
        xAxis.mAxisMaximum = 10;
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        radarChart.setData(radarData);
    }
}