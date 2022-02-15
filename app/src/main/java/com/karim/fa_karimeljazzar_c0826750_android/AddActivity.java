package com.karim.fa_karimeljazzar_c0826750_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {
    EditText address, longi, lati, country;
    Button add;
    CheckBox checkVisit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        address = findViewById(R.id.addAddr);
        longi = findViewById(R.id.addLong);
        lati = findViewById(R.id.addLat);
        add = findViewById(R.id.addP);
        country = findViewById(R.id.addCountry);
        checkVisit = findViewById(R.id.isVisited);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String namePlace = address.getText().toString().trim();
                String longitude = longi.getText().toString().trim();
                String latitude = lati.getText().toString().trim();


                if (namePlace.isEmpty()) {
                    address.setError("Name field cannot be empty");
                    address.requestFocus();
                    return;
                }

                if (longitude.isEmpty()) {
                    longi.setError("Longitude cannot be empty");
                    longi.requestFocus();
                    return;
                }

                if (latitude.isEmpty()) {
                    lati.setError("Latitude cannot be empty");
                    lati.requestFocus();
                    return;
                }




            }
        });
    }
}