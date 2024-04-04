package com.example.balancebeacon_fe.Components;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.balancebeacon_fe.Controllers.AreaController;
import com.example.balancebeacon_fe.Models.Areas;
import com.example.balancebeacon_fe.R;
import com.example.balancebeacon_fe.Shared.Enums;
import com.example.balancebeacon_fe.Shared.RetrofitClient;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LandingPage extends AppCompatActivity {

    // global variables
    TableLayout landingChooseTable;
    TableLayout landingSelectedTable;
    ImageView landingPageRefresh;
    ImageView landingBackButton;
    ImageView landingPageContinueButton;
    List<Integer> selectedAreasArray = new ArrayList<Integer>();
    List<Areas> allDatabaseAreasArray = new ArrayList<Areas>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_landing_page);

//        landingChooseContainer = findViewById(R.id.landing_choose_container);
        landingChooseTable = findViewById(R.id.landing_choose_table);
        landingSelectedTable = findViewById(R.id.landing_selected_table);
        landingPageRefresh = findViewById(R.id.landing_page_refresh_button);
        landingBackButton = findViewById(R.id.landing_back_button);
        landingPageContinueButton = findViewById(R.id.landing_continue_button);
        landingSelectedTable.setVisibility(View.INVISIBLE);

        loadChooseAreas();

        // clicking on the refresh button
        landingPageRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // remove all the data in the selected areas table
                landingSelectedTable.removeAllViews();
                landingChooseTable.removeAllViews();
                selectedAreasArray.clear();
                loadChooseAreas();
            }
        });

        // clicking on the back button
        landingBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LandingPage.this, LoginPage.class);
                startActivity(intent);
            }
        });

        // clicking on the continue button
        landingPageContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedAreasArray.size() < 1) {
                    Toast.makeText(LandingPage.this, "Choose at least one area", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(LandingPage.this, "Assessment successful", Toast.LENGTH_LONG).show();
                }
            }
        });
    }



    /**
     * function to load all the Areas in the table layout
     */
    public void loadChooseAreas() {
        landingPageRefresh.setVisibility(View.INVISIBLE);
        landingChooseTable.removeAllViews();
        landingSelectedTable.removeAllViews();
        AreaController areaController = RetrofitClient.getRetrofitInstance().create(AreaController.class);
        Call<List<Areas>> call = areaController.getAllAreas();

        call.enqueue(new Callback<List<Areas>>() {
            @Override
            public void onResponse(Call<List<Areas>> call, Response<List<Areas>> response) {
                Toast.makeText(LandingPage.this, "Success", Toast.LENGTH_LONG).show();
                allDatabaseAreasArray = response.body();

                // fill the choose table
                int sizeOfAreas = response.body().size();
                int numberOfRows = sizeOfAreas / 3;
                int extraAreas = 0;
                int areaCounter = 0;

                // main rows (3 area items filled rows)
                for (int i=0 ; i<numberOfRows ; i++) {
                    TableRow tableRow = new TableRow(LandingPage.this);
                    for (int j=0 ; j<3 ; j++) {
                        System.out.println("areaCounter: " + areaCounter);

                        // create a view of the area_linear_text layout design (green color small box)
                        View view = getLayoutInflater().inflate(R.layout.area_label_choose, null);

                        // set up the TextView inside the above created View
                        TextView areaLinearText = view.findViewById(R.id.area_linear_text);

                        // assigned the Area Description to above created TextView
                        areaLinearText.setText(response.body().get(areaCounter).getAreaDescription());

                        // set a Area ID as the tag of the above created View
                        view.setTag(response.body().get(areaCounter).getAreaId());
                        view.setClickable(true);

                        // create a clickable event
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                clickOnChooseAreaTable(view, Enums.DoInvisible);
                            }
                        });

                        tableRow.addView(view);
                        areaCounter++;
                    }
                    landingChooseTable.addView(tableRow);
                }

                // extra rows (3 area items NOT filled rows)
                if (sizeOfAreas % 3 > 0) {
                    System.out.println("Line 64");
                    extraAreas = (sizeOfAreas % 3);
                    int extraAreaCounter = areaCounter;

                    TableRow tableRowExtra = new TableRow(LandingPage.this);

                    for (int k=0 ; k<extraAreas ; k++) {

                        // create a view of the area_linear_text layout design (green color small box)
                        View view = getLayoutInflater().inflate(R.layout.area_label_choose, null);

                        // set up the TextView inside the above created View
                        TextView areaLinearText = view.findViewById(R.id.area_linear_text);

                        // assigned the Area Description to above created TextView
                        areaLinearText.setText(response.body().get(extraAreaCounter).getAreaDescription());

                        // set a Area ID as the tag of the above created View
                        view.setTag(response.body().get(extraAreaCounter).getAreaId());
                        tableRowExtra.addView(view);

                        // create a clickable event
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                clickOnChooseAreaTable(view, Enums.DoInvisible);
                            }
                        });
                        extraAreaCounter++;
                    }
                    landingChooseTable.addView(tableRowExtra);
                }
            }

            @Override
            public void onFailure(Call<List<Areas>> call, Throwable t) {
                Toast.makeText(LandingPage.this, "Failed!", Toast.LENGTH_LONG).show();
            }
        });
    }

    // this function runs when clicking on the each area
    // change the color of the area button
    // get the ID of the clicked area button
    public void clickOnChooseAreaTable(View view, Enums action) {

        // set visible the refresh button
        landingPageRefresh.setVisibility(View.VISIBLE);
        landingSelectedTable.setVisibility(View.VISIBLE);

        // limit user to select only 8 areas
        if (selectedAreasArray.size() >= 8) {
            Toast.makeText(LandingPage.this, "You have already chosen 8 areas", Toast.LENGTH_LONG).show();
        }
        else {
            System.out.println(view.getTag());
            if (action == Enums.DoInvisible) {
                selectedAreasArray.add((Integer) view.getTag());
                view.setVisibility(View.INVISIBLE);
                System.out.println(selectedAreasArray);
                loadSelectedAreas(selectedAreasArray);
            }
            else {
                view.setVisibility(View.VISIBLE);
            }
        }

    }

    /**
     * function to load selected areas to the Selected table in the same User Interface
     */
    public void loadSelectedAreas(List<Integer> selectedAreasArray) {
        landingSelectedTable.removeAllViews();
        ArrayList<Areas> userChosenAreas = new ArrayList<Areas>();

        for (int i = 0; i<allDatabaseAreasArray.size() ; i++) {
            for (int j = 0 ; j<selectedAreasArray.size() ; j++) {
                if (allDatabaseAreasArray.get(i).getAreaId() == selectedAreasArray.get(j)) {
                    userChosenAreas.add(allDatabaseAreasArray.get(i));
                }
            }
        }

        // fill the selected table
        int sizeOfAreas = userChosenAreas.size();
        int numberOfRows = sizeOfAreas / 3;
        int extraAreas = 0;
        int areaCounter = 0;
        System.out.println("sizeOfAreas: " + sizeOfAreas);
        System.out.println("numberOfRows: " + numberOfRows);

        // main rows (3 area items filled rows)
        for (int i=0 ; i<numberOfRows ; i++) {
            TableRow tableRow = new TableRow(LandingPage.this);
            for (int j=0 ; j<3 ; j++) {
                System.out.println("areaCounter: " + areaCounter);

                // create a view of the area_linear_text layout design (green color small box)
                View view = getLayoutInflater().inflate(R.layout.area_label_selected, null);

                // set up the TextView inside the above created View
                TextView areaLinearText = view.findViewById(R.id.area_linear_selected_text);

                // assigned the Area Description to above created TextView
                areaLinearText.setText(userChosenAreas.get(areaCounter).getAreaDescription());

                // set a Area ID as the tag of the above created View
                view.setTag(userChosenAreas.get(areaCounter).getAreaId());
                view.setClickable(true);

                // create a clickable event
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        clickOnChooseAreaTable(view, Enums.DoInvisible);
                    }
                });

                tableRow.addView(view);
                areaCounter++;
            }
            landingSelectedTable.addView(tableRow);
        }

        // extra rows (3 area items NOT filled rows)
        if (sizeOfAreas % 3 > 0) {
            System.out.println("Line 64");
            extraAreas = (sizeOfAreas % 3);
            int extraAreaCounter = areaCounter;

            TableRow tableRowExtra = new TableRow(LandingPage.this);

            for (int k=0 ; k<extraAreas ; k++) {

                // create a view of the area_linear_text layout design (green color small box)
                View view = getLayoutInflater().inflate(R.layout.area_label_selected, null);

                // set up the TextView inside the above created View
                TextView areaLinearText = view.findViewById(R.id.area_linear_selected_text);

                // assigned the Area Description to above created TextView
                areaLinearText.setText(userChosenAreas.get(extraAreaCounter).getAreaDescription());

                // set a Area ID as the tag of the above created View
                view.setTag(userChosenAreas.get(extraAreaCounter).getAreaId());
                tableRowExtra.addView(view);

                // create a clickable event
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        clickOnChooseAreaTable(view, Enums.DoInvisible);
                    }
                });
                extraAreaCounter++;
            }
            landingSelectedTable.addView(tableRowExtra);
        }
    }
}