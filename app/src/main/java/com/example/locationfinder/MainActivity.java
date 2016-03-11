package com.example.locationfinder;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements LocationListener {

    public static final int MIN_DISTANCE = 100;
    public static final int MIN_TIME = 1000;
    public static final int REQUEST_LOCATION = 50;
    static String type;
    static String latitude;
    static String longitude;
    LocationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void startOperation(View view) {

        int id = view.getId();
        switch (id) {
            case R.id.hostpital:
                type = "hospital";
                break;
            case R.id.bank:
                type = "bank";
                break;
            case R.id.atm:
                type = "atm";
                break;
            case R.id.coffee:
                type = "cafe";
                break;
            case R.id.movie:
                type = "movie_theater";
                break;
            case R.id.restaurant:
                type = "restaurant";
                break;
            case R.id.club:
                type = "night_club";
                break;
            case R.id.bar:
                type = "bar";
                break;
            case R.id.shoppingMall:
                type = "shopping_mall";
                break;
            case R.id.school:
                type = "school";
                break;
            case R.id.hinduTemple:
                type = "hindu_temple";
                break;
            case R.id.church:
                type = "church";
                break;
            case R.id.gas:
                type = "gas_station";
                break;
            case R.id.bus:
                type = "bus_station";
                break;
            case R.id.airport:
                type = "airport";
                break;
            case R.id.train:
                type = "train_station";
                break;

        }
        Toast.makeText(this, "latitude" + MainActivity.latitude + "\n longitude" + MainActivity.longitude + "\n type" + MainActivity.type, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LocListActivity.class);
        startActivity(intent);
    }


    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        MainActivity.latitude = String.valueOf(latitude);
        MainActivity.longitude = String.valueOf(longitude);
        Toast.makeText(this, "latitude" + latitude + "\n longitude" + longitude, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }
}