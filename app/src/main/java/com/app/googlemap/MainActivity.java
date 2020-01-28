package com.app.googlemap;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private SupportMapFragment mapFragment;
    private Polyline polyline;
    private Location location;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Task<Location> task;
    private Double lat =null,lang= null;
    boolean f = false;
    private Geocoder gcd;
    private TextView current_loc;
    private List<LatLng> latLngs = new ArrayList<>();
    private GoogleMap gMap;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        current_loc =findViewById(R.id.current_loc);
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        MapsInitializer.initialize(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
        if (!checkPermissions()) {
            Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show();
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
        else {
            f = true;
            task = fusedLocationProviderClient.getLastLocation();
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(final GoogleMap googleMap) {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            googleMap.addMarker(new MarkerOptions()
                            .position(latLng));
                            latLngs.add(latLng);
                            List<Address> addresses = null;
                            gcd = new Geocoder(MainActivity.this, Locale.getDefault());
                            try {
                                addresses = gcd.getFromLocation(latLng.latitude, latLng.longitude, 1);
                                current_loc.setText(""+addresses.get(0).getAddressLine(0).toString());
                                Log.i("Current_Loc->>>",  addresses.get(0).getAddressLine(0).toString());
                            } catch (IOException ex) {
                                Log.e("Error->>", ex.toString());
                                ex.printStackTrace();
                            }


                            if (latLngs.size() >= 2) {
                                LatLng origin = latLngs.get(0);
                                LatLng dest = latLngs.get(1);
    //                            String url = getUrl(origin, dest);
    //                            FetchUrl FetchUrl = new FetchUrl();
    //                            FetchUrl.execute(url);
                            }
                        }
                    });

                }
            });
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(final GoogleMap googleMap) {
                        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        task = fusedLocationProviderClient.getLastLocation();
                        task.addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if(location!=null) {
                                    lat = location.getLatitude();
                                    lang = location.getLongitude();
                                    Log.d("AndroidClarified",location.getLatitude()+" "+location.getLongitude());
                                }
                            }
                        });
                        task.addOnCompleteListener(new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(lat, lang)));
                                latLngs.add(new LatLng(lat, lang));
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lang),16));
                            }
                        });
                            googleMap.clear();
                        gcd = new Geocoder(MainActivity.this, Locale.getDefault());
                        try {
                            List<Address> addresses = gcd.getFromLocation(lat, lang, 1);
                            current_loc.setText(""+addresses.get(0).getAddressLine(0).toString());
                            Log.i("Current_Loc->>>",  addresses.get(0).getAddressLine(0).toString());
                        } catch (IOException e) {
                            Log.e("Error->>", e.toString());
                            e.printStackTrace();
                        }
                        //                        googleMap.addMarker(new MarkerOptions()
//                                .position(new LatLng(37.4233438, -122.0728817))
//                                .title("1st Position"));
//                        googleMap.addMarker(new MarkerOptions()
//                                .position(new LatLng(37.4629101,-122.2449094))
//                                .title("Facebook"));
//                        List<LatLng> latLngs = new ArrayList<>();
//                        latLngs.add(new LatLng(37.4233438, -122.0728817));
//                        latLngs.add(new LatLng(37.4629101,-122.2449094));
//                        latLngs.add(new LatLng(37.3092293, -122.1136845));
//                        latLngs.add(new LatLng(37.4233438, -122.0728817));
//                        PolylineOptions polylineOptions = new PolylineOptions().addAll(latLngs).clickable(true);
//                        polyline = googleMap.addPolyline(polylineOptions);
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] != 0)
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
    }

    private boolean checkPermissions()
    {
        return ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        gMap = googleMap;
        if (f){
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location!=null) {
                        lat = location.getLatitude();
                        lang = location.getLongitude();
                        Log.d("AndroidClarified",location.getLatitude()+" "+location.getLongitude());
                    }
                }
            });
            task.addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(lat, lang)));
                    latLngs.add(new LatLng(lat, lang));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lang),16));
                }
            });
        }

    }
    
}
