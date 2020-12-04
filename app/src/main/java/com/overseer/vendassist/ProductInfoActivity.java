package com.overseer.vendassist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class ProductInfoActivity extends AppCompatActivity {

    //Constants
    private final String GET_INTENT_KEY ="productInfo";
    private final String SEND_NAME_KEY = "productName";
    private final String SEND_INTENT_KEY = "machineMap";
    private final String SHARED_PREF_LOC = "com.overseer.android.sharedPrefs";
    private final String RETURN_INTENT_KEY = "favIntent";
    private final int REC_MACHINE_AVG = 5;

    private Button findButton;
    private Button trackButton;
    private ImageView imageView,infoView;
    private TextView availText;

    Context context = ProductInfoActivity.this;
    SharedPreferences sharedPref;

    HashMap<String,Integer> machineMap = new HashMap<>();
    private String debugText="";
    private String productId;
    private String favStatus;
    private Boolean fav;
    private int imgResId,titleResId,infoResId;

    private DatabaseReference mRootDatabaseRef;
    private DatabaseReference productRef;
    private ValueEventListener listener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_product_info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Retrieve Intent data from Dashboard
        Intent intentIn = getIntent();
        Bundle b = intentIn.getExtras();
        final String productId = (String) b.get(GET_INTENT_KEY);
        final String productName = ((String) b.get(SEND_NAME_KEY))+" Information";
        Resources resRef = ProductInfoActivity.this.getResources();
        String packageName = ProductInfoActivity.this.getPackageName();

        //Setup for Activity UI
        imgResId = resRef.getIdentifier(productId+"_image","drawable",packageName);
        //titleResId = resRef.getIdentifier(productId+"_title","string",packageName);
        infoResId = resRef.getIdentifier(productId+"_info","drawable",packageName);

        getSupportActionBar().setTitle(productName);

        findButton = findViewById(R.id.findButton);
        trackButton = findViewById(R.id.trackButton);
        imageView = findViewById(R.id.imageView);
        infoView = findViewById(R.id.infoView);
        availText = findViewById(R.id.availText);

        imageView.setImageResource(imgResId);
        infoView.setImageResource(infoResId);

        sharedPref = context.getSharedPreferences(SHARED_PREF_LOC,Context.MODE_PRIVATE);

        //Database RReferences
        mRootDatabaseRef = FirebaseDatabase.getInstance().getReference();
        productRef = mRootDatabaseRef.child(productId); //for product 1

        //System.out.println(sharedPref.getBoolean("Product1_fav",false));
        fav = sharedPref.getBoolean(productId+"_fav",false);
        if(fav){
            trackButton.setText(R.string.favOn);
        }
        else{
            trackButton.setText(R.string.favOff);
        }
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                machineMap.clear();
                int max = 0, sum=0;
                String outText;
                for (DataSnapshot i : snapshot.getChildren()) {
                    machineMap.put((String)i.getKey(),i.getValue(Integer.class)); //array to show which machine has how many of that product (Nani?)
                    if (i.getValue(Integer.class)>max){
                        max=i.getValue(Integer.class);
                    }
                    sum+=i.getValue(Integer.class);
                }


                if (REC_MACHINE_AVG<=max){
                    outText = "Availability: High";
                    availText.setTextColor(Color.GREEN);
                }
                else if(max == 0){
                    outText = "Availability: None";
                    availText.setTextColor(Color.RED);
                }
                else{
                    outText = "Availability: Low";
                    availText.setTextColor(getResources().getColor(R.color.orange));
                }
                //outText = "Availability: "+Utils.availStatusCheck(REC_MACHINE_AVG,max);
                availText.setText(outText);
                /*System.out.println("After Change");
                for (Map.Entry jk : machineMap.entrySet()) {
                    System.out.println(jk.getKey()+" : "+jk.getValue() + "\n");
                }*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        productRef.addValueEventListener(listener);
        /*productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                machineMap.clear();
                int max = 0, sum=0;
                String outText;
                for (DataSnapshot i : snapshot.getChildren()) {
                    machineMap.put((String)i.getKey(),i.getValue(Integer.class)); //array to show which machine has how many of that product (Nani?)
                    if (i.getValue(Integer.class)>max){
                        max=i.getValue(Integer.class);
                    }
                    sum+=i.getValue(Integer.class);
                }


                if (REC_MACHINE_AVG<=max){
                    outText = "Availability: High";
                    availText.setTextColor(Color.GREEN);
                }
                else if(max == 0){
                    outText = "Availability: None";
                    availText.setTextColor(Color.RED);
                }
                else{
                    outText = "Availability: Low";
                    availText.setTextColor(Color.YELLOW);
                }
                //outText = "Availability: "+Utils.availStatusCheck(REC_MACHINE_AVG,max);
                availText.setText(outText);
                System.out.println("After Change");
                for (Map.Entry jk : machineMap.entrySet()) {
                    System.out.println(jk.getKey()+" : "+jk.getValue() + "\n");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(getApplicationContext(), ProductMapActivity.class);
                //System.out.println("Critical Out");
                mapIntent.putExtra(SEND_INTENT_KEY, machineMap);
                startActivity(mapIntent);
            }
        });

        trackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPref.edit();
                if(fav){
                    fav = false;
                    editor.putBoolean(productId+"_fav", false);
                    trackButton.setText(R.string.favOff);
                }
                else{
                    fav = true;
                    editor.putBoolean(productId+"_fav", true);
                    trackButton.setText(R.string.favOn);
                }
                editor.apply();
                //Intent returnIntent = new Intent(getApplicationContext(), Dashboard.class);
                //returnIntent.putExtra(RETURN_INTENT_KEY,fav);
                //System.out.println("Fav: "+fav);
                //System.out.println(sharedPref.getBoolean("Product1_fav",false));

            }
        });
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        System.out.println("After Resume");
        for (Map.Entry jk : machineMap.entrySet()) {
            System.out.println(jk.getKey()+" : "+jk.getValue() + "\n");
        }
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        productRef.removeEventListener(listener);
    }
}
