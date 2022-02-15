package com.karim.fa_karimeljazzar_c0826750_android;

import static com.karim.fa_karimeljazzar_c0826750_android.MainActivity.databaseHelper;
import static com.karim.fa_karimeljazzar_c0826750_android.MainActivity.placeModels;
import static com.karim.fa_karimeljazzar_c0826750_android.MainActivity.selectedPlace;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.RequestResult;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.karim.fa_karimeljazzar_c0826750_android.Models.PlaceModel;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

public class DistanceMapsActivity extends AppCompatActivity {

    private static final int LOCATION_MIN_UPDATE_TIME = 10;
    private static final int LOCATION_MIN_UPDATE_DISTANCE = 1000;

    private MapView mapView;
    private GoogleMap googleMap;
    private Location location = null;


    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            drawMarkerLocation(location);
            locationManager.removeUpdates(locationListener);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        mapView = findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mapView_onMapReady(googleMap);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        getCurrentLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    private void initMap() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (googleMap != null) {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                googleMap.getUiSettings().setAllGesturesEnabled(true);
                googleMap.getUiSettings().setZoomControlsEnabled(true);
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 12);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 13);
            }
        }
    }

    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
                Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
            } else {
                location = null;
                if (isGPSEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_MIN_UPDATE_TIME, LOCATION_MIN_UPDATE_DISTANCE, locationListener);
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_MIN_UPDATE_TIME, LOCATION_MIN_UPDATE_DISTANCE, locationListener);
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
                if (location != null) {

                    drawMarkerLocation(location);
                }
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 12);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 13);
            }
        }
    }

    private void mapView_onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        initMap();
        getCurrentLocation();
    }

    private void drawMarkerLocation(Location location) {
        if (this.googleMap != null) {
            googleMap.clear();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("My location!");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            googleMap.addMarker(markerOptions);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            LatLng placeLocation = new LatLng(selectedPlace.getLatitude(), selectedPlace.getLongitude());
            MarkerOptions markerOptionsPlace = new MarkerOptions();
            markerOptionsPlace.position(placeLocation);
            markerOptionsPlace.title(selectedPlace.getName());
            markerOptionsPlace.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            googleMap.addMarker(markerOptionsPlace);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(placeLocation));

            float[] results = new float[1];
            Location.distanceBetween(latLng.latitude, latLng.longitude,
                    placeLocation.latitude, placeLocation.longitude,
                    results);

            Polyline line;
            PolylineOptions options = new PolylineOptions()
                    .color(Color.BLACK)
                    .width(10)
                    .add(markerOptions.getPosition(), markerOptionsPlace.getPosition());

            line = googleMap.addPolyline(options);
            View parentLayout = findViewById(android.R.id.content);

            Snackbar snackbar = Snackbar
                    .make(parentLayout,"Distance: " + results[0] + " m" , Snackbar.LENGTH_INDEFINITE);
            snackbar.show();

            //System.out.println("Distance: " + results[0]);
            getDestinationInfo(placeLocation,latLng);

        }
    }

    private void getDestinationInfo(LatLng latLngDestination, LatLng myLocation) {
        String serverKey = "AIzaSyBmns-WID6eHBEDrg1z4I31qZHlTVyuVT8"; // Api Key For Google Direction API \\
        final LatLng origin = myLocation;
        final LatLng destination = latLngDestination;
        //-------------Using AK Exorcist Google Direction Library---------------\\
        GoogleDirection.withServerKey(serverKey)
                .from(origin)
                .to(destination)
                .transportMode(TransportMode.DRIVING)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        String status = direction.getStatus();
                        if (status.equals(RequestResult.OK)) {
                            Route route = direction.getRouteList().get(0);
                            Leg leg = route.getLegList().get(0);
                            Info distanceInfo = leg.getDistance();
                            Info durationInfo = leg.getDuration();
                            String distance = distanceInfo.getText();
                            String duration = durationInfo.getText();

                            //------------Displaying Distance and Time-----------------\\
                            //showingDistanceTime(distance, duration); // Showing distance and time to the user in the UI \\
                            String message = "Total Distance is " + distance + " and Estimated Time is " + duration;
                            System.out.println(message);
//                            StaticMethods.customSnackBar(consumerHomeActivity.parentLayout, message,
//                                    getResources().getColor(R.color.colorPrimary),
//                                    getResources().getColor(R.color.colorWhite), 3000);

                            //--------------Drawing Path-----------------\\
                            ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
                            PolylineOptions polylineOptions = DirectionConverter.createPolyline(DistanceMapsActivity.this,
                                    directionPositionList, 5, getResources().getColor(R.color.black));
                            googleMap.addPolyline(polylineOptions);
                            //--------------------------------------------\\

                            //-----------Zooming the map according to marker bounds-------------\\
                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            builder.include(origin);
                            builder.include(destination);
                            LatLngBounds bounds = builder.build();

                            int width = getResources().getDisplayMetrics().widthPixels;
                            int height = getResources().getDisplayMetrics().heightPixels;
                            int padding = (int) (width * 0.20); // offset from edges of the map 10% of screen

                            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
                            googleMap.animateCamera(cu);
                            //------------------------------------------------------------------\\

                        } else if (status.equals(RequestResult.NOT_FOUND)) {
                            Toast.makeText(getApplicationContext(), "No routes exist", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        // Do something here
                        System.out.println("faileddddd");
                    }
                });
        //-------------------------------------------------------------------------------\\

    }



}