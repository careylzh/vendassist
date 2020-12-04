package com.overseer.vendassist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.WindowManager;

import java.lang.reflect.Array;
import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Dashboard extends AppCompatActivity implements VendingRecyclerAdapter.OnCardListener {
    //private final String SEND_INTENT_KEY = "productInfo";  transferred to VendingRecyclerAdapter.java
    private final String FIREBASE_PRODUCT_KEY = "Products", SHARED_PREF_TAG = "_fav";
    private final String PRODUCT_IMAGE_TAG = "_image";
    ArrayList<VendingRecyclerIntermediate> vendingItems = new ArrayList<>();
    RecyclerView vendingRecycler;
    RecyclerView.Adapter adapter;
    RecyclerView favouriteRecycler;
    RecyclerView.Adapter favAdapter;
    //ArrayList<String[]> favValues = new ArrayList<>();
    ArrayList<String[]> productValues = new ArrayList<>();
    Context context = Dashboard.this;
    SharedPreferences sharedPref;
    private Boolean fav;

    //String[] favItems = {};
    //String[] nonFavItems = {};
    ArrayList<VendingRecyclerIntermediate> favRecyclerItems = new ArrayList<>();

    //Resources resources = getResources();
    //TO DO
    //final int resourceId = resources.getIdentifier("two_of_diamonds","drawable", getPackageName()); //Names store in iterable object
    //imgView.setImageResource(resourceId);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle("Product Dashboard");
        setContentView(R.layout.activity_dashboard);
        sharedPref = context.getSharedPreferences("com.overseer.android.sharedPrefs",Context.MODE_PRIVATE);
        //Firebase References
        DatabaseReference mRootDatabaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference productList = mRootDatabaseRef.child(FIREBASE_PRODUCT_KEY); //for all products

        //hooks
        vendingRecycler = findViewById(R.id.recycler_view_vending);
        //generateVendingRecycler(productValues);

        favouriteRecycler = findViewById(R.id.recycler_view_favourites);
        //generateFavRecycler(productValues);

        productList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot i : snapshot.getChildren()){
                    String[] alpha = new String[2];
                    alpha[0] = i.getKey();
                    alpha[1] = i.getValue(String.class);
                    productValues.add(alpha);
                    System.out.println(alpha[0]+" + "+alpha[1]);
                    System.out.println(productValues.size());
                }

                //Must be called here since Database update occurs after activity has been generated
                generateVendingRecycler(productValues);
                generateFavRecycler(productValues);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    //recycler contains alot of cards view
    private void generateVendingRecycler(ArrayList<String[]> productValues){
        vendingItems.removeAll(vendingItems);
        vendingRecycler.setHasFixedSize(true); //fix the size of the recycler view such that only visible ones are loaded onto screen
        vendingRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)); //this means user dashboard is reference//cuz linear layout
        for (String[] eachStringArray : productValues){
            fav = sharedPref.getBoolean(eachStringArray[0]+SHARED_PREF_TAG, false);
            System.out.println(eachStringArray[0]+" + "+eachStringArray[1]);
            if(!fav) vendingItems.add(new VendingRecyclerIntermediate(Dashboard.this.getResources().getIdentifier(eachStringArray[0]+PRODUCT_IMAGE_TAG, "drawable",Dashboard.this.getPackageName()), eachStringArray[1], eachStringArray[0]));
            //Dashboard.this.getResources().getIdentifier(productId+"_image", "drawable",Dashboard.this.getPackageName())
        }

        adapter = new VendingRecyclerAdapter(vendingItems, this);
        vendingRecycler.setAdapter(adapter);
    }

    private void generateFavRecycler(ArrayList<String[]> productValues){
        favRecyclerItems.removeAll(favRecyclerItems);
        favouriteRecycler.setHasFixedSize(true); //fix the size of the recycler view such that only visible ones are loaded onto screen
        favouriteRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)); //this means user dashboard is reference//cuz linear layout is used

        for (String[] eachStringArray : productValues){
            fav = sharedPref.getBoolean(eachStringArray[0]+SHARED_PREF_TAG, false);
            if(fav) favRecyclerItems.add(new VendingRecyclerIntermediate(Dashboard.this.getResources().getIdentifier(eachStringArray[0]+PRODUCT_IMAGE_TAG, "drawable",Dashboard.this.getPackageName()), eachStringArray[1], eachStringArray[0]));
        }

        favAdapter = new VendingRecyclerAdapter(favRecyclerItems, this);
        favouriteRecycler.setAdapter(favAdapter);
    }

    //meaning display lays product info if lays is clicked, display pokka if pokka is clicked, etcs
    @Override
    public void onCardClick(int position) {
        /*
        Intent intent = new Intent(this,ProductInfoActivity.class);
        //dk how to use putExtra
        intent.putExtra(SEND_INTENT_KEY, "product1001");
        startActivity(intent);
        //finish();*/
    }

    @Override
    public void onResume(){
        super.onResume();
        generateVendingRecycler(productValues);
        generateFavRecycler(productValues);
        //Intent getFavIntent = new Intent();
        //getFavIntent.getExtras();
    }
}