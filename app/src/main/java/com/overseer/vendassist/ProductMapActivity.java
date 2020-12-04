package com.overseer.vendassist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

public class ProductMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap sutdMap;
    SupportMapFragment mapFragment;
    public final static String PRODUCTS_KEY = "machineMap";
    HashMap<String, Integer> productAvailList;

    // XML file under values - map_key.xml
//<?xml version="1.0" encoding="utf-8"?>
//<resources>
//    <string name="map_key" translatable="false">
//    AIzaSyCmFyA_kiwRwW89rPYFlPRV1CnDOrSFFiE
//
//            </string>
//
//</resources>

    //Manifest - API KEY, Map Activity

//            <meta-data android:name="com.google.android.geo.API_KEY"
//    android:value="@string/map_key"></meta-data>


//            <activity android:name=".SchoolMap">
//            <intent-filter>
//                <action android:name="android.intent.action.MAIN" />
//
//                <category android:name="android.intent.category.LAUNCHER" />
//            </intent-filter>
//        </activity>



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_product_map);

        getSupportActionBar().setTitle("Vending Map");
        Intent intent = getIntent();
        productAvailList = (HashMap<String, Integer>) intent.getSerializableExtra(PRODUCTS_KEY);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.sutdMap);
        mapFragment.getMapAsync(this);
    }
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        sutdMap = googleMap;

        // Zoom in and out
        sutdMap.getUiSettings().setZoomControlsEnabled(true);
        sutdMap.setMinZoomPreference(17.0f); // Set a preference for minimum zoom (Zoom out).

        // Create a LatLngBounds that includes the SUTD
        final LatLngBounds SUTDboundary = new LatLngBounds(
                new LatLng(1.3399083998318655, 103.96179016386469), new LatLng(1.3431700319609827, 103.96568851809874));

        sutdMap.setLatLngBoundsForCameraTarget(SUTDboundary);
        sutdMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SUTDboundary.getCenter(), 17));

        //LatLng mid = new LatLng(1.3411993763738146, 103.96376100643006);

        // Coordinates for 5 vending machines
        LatLng building1 = new LatLng(1.3401828915886478, 103.96248548563857);
        //sutdMap.addMarker(new MarkerOptions().position(building1).title("Building 1"));

        LatLng building2 = new LatLng(1.3410003966189632, 103.96249814398001);
        //sutdMap.addMarker(new MarkerOptions().position(building2).title("Building 2"));

        LatLng sportsrecre = new LatLng(1.341648995194232, 103.96483232850782);
        //sutdMap.addMarker(new MarkerOptions().position(sportsrecre).title("Sports and Recreation Centre"));

        LatLng outsideMPH = new LatLng(1.3421160461702815, 103.96414900111195);
        //sutdMap.addMarker(new MarkerOptions().position(outsideMPH).title("Outside MPH"));

        LatLng hostel = new LatLng(1.3420022275495107, 103.96359493129133);
        //sutdMap.addMarker(new MarkerOptions().position(hostel).title("Hostel"));

        //sutdMap.moveCamera(CameraUpdateFactory.newLatLng(mid));

        //HashMap for vending machines
        HashMap<String, LatLng> vendingMachines = new HashMap<>();
        //TODO: Change HashMap String Key Name
        vendingMachines.put("BLD1", building1);           //Building 1
        vendingMachines.put("BLD2", building2);           //Building 2
        vendingMachines.put("SRC", sportsrecre);          //Sports and Recreation Centre
        vendingMachines.put("MPH", outsideMPH);           //MPH
        vendingMachines.put("HOSTEL", hostel);            //Hostel

        // Iteration
        for (Map.Entry<String, Integer> k: productAvailList.entrySet()) {
            if (vendingMachines.containsKey(k.getKey()) && (k.getValue() > 0)) {
                //TODO: Change to get .title from strings.xml
                int testId = Utils.findResIdByString(ProductMapActivity.this,k.getKey(),"POPUP");
                String filler = getString(testId);
                sutdMap.addMarker(new MarkerOptions().position(vendingMachines.get(k.getKey())).title(filler));
            }
        }

    }
}
