package com.app.googlemap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class AskActivity extends AppCompatActivity {
    private BottomSheetBehavior bottomSheetBehav;
    private CardView restaurants;
    private RequestQueue requestQueue;
    Location location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);
        try {
            getSupportActionBar().hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
        requestQueue = Volley.newRequestQueue(this);
        restaurants = findViewById(R.id.restaurants);
        View nestedScrollView = findViewById(R.id.nestedScroll);
        bottomSheetBehav = BottomSheetBehavior.from(nestedScrollView);
        getPlace();
        bottomSheetBehav.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                String state = "";

                switch (i) {
                    case BottomSheetBehavior.STATE_DRAGGING: {
                        state = "DRAGGING";
                        break;
                    }
                    case BottomSheetBehavior.STATE_SETTLING: {
                        state = "SETTLING";
                        break;
                    }
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        state = "EXPANDED";
                        break;
                    }
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        state = "COLLAPSED";
                        break;
                    }
                    case BottomSheetBehavior.STATE_HIDDEN: {
                        state = "HIDDEN";
                        break;
                    }
                }
                bottomSheetBehav.setPeekHeight(100);
                //Toast.makeText(AskActivity.this, "Bottom Sheet State Changed to: " + state, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
        restaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehav.setPeekHeight(300);
                if (bottomSheetBehav.isHideable()){
                    bottomSheetBehav.setHideable(false);
                }
            }
        });
    }
    private String getPlace(){
        double lat, lang ;
        if (location!=null){
            lat = location.getLatitude();
            lang = location.getLongitude();
            Log.i("LatLang ", ""+lat+", "+lang);
        }
        String url_ = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=restaurants+in+sector+18+noida&key="+getString(R.string.googleapikey_map_place);
        StringRequest stringRequest = new StringRequest(url_, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Request->>", "Response-> "+response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Request->>", "Error-> "+error);
            }
        });
        requestQueue.add(stringRequest);
       return "";
    }


}
