package com.karim.fa_karimeljazzar_c0826750_android;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.karim.fa_karimeljazzar_c0826750_android.Adapters.PlaceAdapter;
import com.karim.fa_karimeljazzar_c0826750_android.Helpers.DatabaseHelper;
import com.karim.fa_karimeljazzar_c0826750_android.Models.PlaceModel;

import java.time.LocalDate;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    SwipeMenuListView placesLV;
    public static ArrayList<PlaceModel> placeModels;
    public static PlaceModel selectedPlace;
    public static DatabaseHelper databaseHelper;
    TextView dateAdded, addressS;
    Button showMap, addAddress;
    CheckBox hasVisited;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateAdded = findViewById(R.id.dateAdded);
        showMap = findViewById(R.id.showMap);
        hasVisited = findViewById(R.id.visitedBox);
        //addAddress = findViewById(R.id.addAddress);
        addressS = findViewById(R.id.addressSelected);

        placesLV = findViewById(R.id.placesLV);
        placeModels = new ArrayList<>();

        TextView textView = new TextView(this);
        textView.setText("        Address                longitude              Latitude           Visited");
        placesLV.addHeaderView(textView);

        databaseHelper = new DatabaseHelper(this);

        Cursor cursor = databaseHelper.getPlaces();

        if(cursor.moveToFirst()){
            do{
                placeModels.add(new PlaceModel(cursor.getInt(0),cursor.getString(1),cursor.getDouble(2),cursor.getDouble(3),cursor.getInt(4),cursor.getString(5)));
            }while (cursor.moveToNext());
        }
        cursor.close();

        if(placeModels.isEmpty()){
            LocalDate localDate = LocalDate.now();
            databaseHelper.addPlace("Ottawa", 50.15002545, 		-96.88332178, true, localDate.toString());
            cursor = databaseHelper.getPlaces();

            if(cursor.moveToFirst()){
                do{
                    placeModels.add(new PlaceModel(cursor.getInt(0),cursor.getString(1),cursor.getDouble(2),cursor.getDouble(3),cursor.getInt(4),cursor.getString(5)));
                }while (cursor.moveToNext());
            }
            cursor.close();
        }

        databaseHelper.getWritableDatabase();
        selectedPlace = placeModels.get(0);
        if(selectedPlace.getVisited() == 1){
            hasVisited.setChecked(true);
        }else{
            hasVisited.setChecked(false);
        }

        PlaceAdapter placeAdapter = new PlaceAdapter(this, placeModels);
        placesLV.setAdapter(placeAdapter);

        dateAdded.setText(selectedPlace.getDateAdded());

        placesLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPlace = placeModels.get(position-1);
                dateAdded.setText(placeModels.get(position-1).getDateAdded());
                if(selectedPlace.getVisited() == 1){
                    hasVisited.setChecked(true);
                }else{
                    hasVisited.setChecked(false);
                }
                addressS.setText(selectedPlace.getName());
            }
        });

        hasVisited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hasVisited.isChecked()){
                    databaseHelper.updatePlace(selectedPlace.getId(), selectedPlace.getName(), selectedPlace.getLatitude(),selectedPlace.getLongitude(),true,selectedPlace.getDateAdded());
                }else{
                    databaseHelper.updatePlace(selectedPlace.getId(), selectedPlace.getName(), selectedPlace.getLatitude(),selectedPlace.getLongitude(),false,selectedPlace.getDateAdded());
                }
                reloadPlaces();
            }
        });

        showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MapsActivity.class));
            }
        });

//        addAddress.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(),AddActivity.class));
//            }
//        });
        //addAddress.setVisibility(View.INVISIBLE);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem updateItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                updateItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                updateItem.setWidth(170);
                // set item title
                updateItem.setTitle("Update");
                // set item title fontsize
                updateItem.setTitleSize(16);
                // set item title font color
                updateItem.setTitleColor(Color.WHITE);
                updateItem.setBackground(new ColorDrawable(Color.rgb(06,
                        89, 255)));
                // add to menu
                menu.addMenuItem(updateItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        placesLV.setMenuCreator(creator);

        placesLV.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // update
                        selectedPlace = placeModels.get(position);
                        startActivity(new Intent(getApplicationContext(),UpdateMapsActivity.class));
                        break;
                    case 1:
                        // delete
                        databaseHelper.deletePlace(placeModels.get(position).getId());
                        reloadPlaces();
                        selectedPlace = placeModels.get(0);
                        reloadCheckBox();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        addressS.setText(selectedPlace.getName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectedPlace = placeModels.get(0);
        reloadPlaces();
        reloadCheckBox();
        addressS.setText(selectedPlace.getName());
    }

    private void reloadCheckBox(){
        if(selectedPlace.getVisited() == 1){
            hasVisited.setChecked(true);
        }else{
            hasVisited.setChecked(false);
        }
    }

    private void reloadPlaces(){
        placeModels.clear();
        Cursor cursor = databaseHelper.getPlaces();
        if(cursor.moveToFirst()){
            do{
                placeModels.add(new PlaceModel(cursor.getInt(0),cursor.getString(1),cursor.getDouble(2),cursor.getDouble(3),cursor.getInt(4),cursor.getString(5)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        PlaceAdapter placeAdapter = new PlaceAdapter(this, placeModels);
        placesLV.setAdapter(placeAdapter);
        placesLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPlace = placeModels.get(position-1);
                dateAdded.setText(placeModels.get(position-1).getDateAdded());
                if(selectedPlace.getVisited() == 1){
                    hasVisited.setChecked(true);
                }else{
                    hasVisited.setChecked(false);
                }
                addressS.setText(selectedPlace.getName());
            }
        });


    }
}