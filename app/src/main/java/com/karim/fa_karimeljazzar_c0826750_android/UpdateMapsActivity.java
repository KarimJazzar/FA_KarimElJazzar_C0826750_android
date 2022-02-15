package com.karim.fa_karimeljazzar_c0826750_android;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karim.fa_karimeljazzar_c0826750_android.databinding.ActivityUpdateMapsBinding;

import static com.karim.fa_karimeljazzar_c0826750_android.MainActivity.databaseHelper;
import static com.karim.fa_karimeljazzar_c0826750_android.MainActivity.placeModels;
import static com.karim.fa_karimeljazzar_c0826750_android.MainActivity.selectedPlace;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karim.fa_karimeljazzar_c0826750_android.Models.PlaceModel;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

public class UpdateMapsActivity extends AppCompatActivity {

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
                googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(@NonNull LatLng latLng) {
                        Geocoder geocoder;
                        List<Address> addresses = new List<Address>() {
                            @Override
                            public int size() {
                                return 0;
                            }

                            @Override
                            public boolean isEmpty() {
                                return false;
                            }

                            @Override
                            public boolean contains(@Nullable Object o) {
                                return false;
                            }

                            @NonNull
                            @Override
                            public Iterator<Address> iterator() {
                                return null;
                            }

                            @NonNull
                            @Override
                            public Object[] toArray() {
                                return new Object[0];
                            }

                            @NonNull
                            @Override
                            public <T> T[] toArray(@NonNull T[] a) {
                                return null;
                            }

                            @Override
                            public boolean add(Address address) {
                                return false;
                            }

                            @Override
                            public boolean remove(@Nullable Object o) {
                                return false;
                            }

                            @Override
                            public boolean containsAll(@NonNull Collection<?> c) {
                                return false;
                            }

                            @Override
                            public boolean addAll(@NonNull Collection<? extends Address> c) {
                                return false;
                            }

                            @Override
                            public boolean addAll(int index, @NonNull Collection<? extends Address> c) {
                                return false;
                            }

                            @Override
                            public boolean removeAll(@NonNull Collection<?> c) {
                                return false;
                            }

                            @Override
                            public boolean retainAll(@NonNull Collection<?> c) {
                                return false;
                            }

                            @Override
                            public void clear() {

                            }

                            @Override
                            public Address get(int index) {
                                return null;
                            }

                            @Override
                            public Address set(int index, Address element) {
                                return null;
                            }

                            @Override
                            public void add(int index, Address element) {

                            }

                            @Override
                            public Address remove(int index) {
                                return null;
                            }

                            @Override
                            public int indexOf(@Nullable Object o) {
                                return 0;
                            }

                            @Override
                            public int lastIndexOf(@Nullable Object o) {
                                return 0;
                            }

                            @NonNull
                            @Override
                            public ListIterator<Address> listIterator() {
                                return null;
                            }

                            @NonNull
                            @Override
                            public ListIterator<Address> listIterator(int index) {
                                return null;
                            }

                            @NonNull
                            @Override
                            public List<Address> subList(int fromIndex, int toIndex) {
                                return null;
                            }
                        };
                        geocoder = new Geocoder(UpdateMapsActivity.this, Locale.getDefault());

                        try {
                            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        String address;
                        String country;
                        if(!addresses.isEmpty()){
                            address = addresses.get(0).getAddressLine(0);
                            country = addresses.get(0).getCountryName();
                        }else{
                            address = "Today's Date";
                            country = "because an invalid address was selected";
                        }

//                        String city = addresses.get(0).getLocality();
//                        String state = addresses.get(0).getAdminArea();


                        AlertDialog alertDialog = new AlertDialog.Builder(UpdateMapsActivity.this)
                                .setMessage("Are you sure you want to update the address to: " + address + ", " + country + "?")
                                .setTitle("Add Place")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    public void onClick(DialogInterface dialog, int which) {
                                        LocalDate localDate = LocalDate.now();
                                        if(address == "Today's Date"){
                                            MarkerOptions marker = new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).title(localDate.toString());
                                            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                                            googleMap.clear();
                                            googleMap.addMarker(marker);
                                            if(selectedPlace.getVisited() == 0){
                                                databaseHelper.updatePlace(selectedPlace.getId(), localDate.toString(), latLng.latitude, latLng.longitude, false, localDate.toString());
                                            }else{
                                                databaseHelper.updatePlace(selectedPlace.getId(), localDate.toString(), latLng.latitude, latLng.longitude, true, localDate.toString());
                                            }


                                        }else{
                                            MarkerOptions marker = new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).title(address + ", " + country);
                                            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                                            googleMap.clear();
                                            googleMap.addMarker(marker);
                                            if(selectedPlace.getVisited() == 0){
                                                databaseHelper.updatePlace(selectedPlace.getId(), address + ", " + country, latLng.latitude, latLng.longitude, false, localDate.toString());
                                            }else{
                                                databaseHelper.updatePlace(selectedPlace.getId(), address + ", " + country, latLng.latitude, latLng.longitude, true, localDate.toString());
                                            }

                                        }
                                    }
                                })
                                .setNegativeButton(android.R.string.no, null).setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                    }
                });
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
//            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//            MarkerOptions markerOptions = new MarkerOptions();
//            markerOptions.position(latLng);
//            markerOptions.title("My location!");
//            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//            googleMap.addMarker(markerOptions);
//            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));


                LatLng placeLocation = new LatLng(selectedPlace.getLatitude(), selectedPlace.getLongitude());
                MarkerOptions markerOptionsProduct = new MarkerOptions();
                markerOptionsProduct.position(placeLocation);
                markerOptionsProduct.title(selectedPlace.getName());
                markerOptionsProduct.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                googleMap.addMarker(markerOptionsProduct);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(placeLocation));


        }
    }




}