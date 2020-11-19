package com.overseer.vendassist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Dashboard extends AppCompatActivity implements VendingRecyclerAdapter.OnCardListener {
    ArrayList<VendingRecyclerIntermediate> vendingItems = new ArrayList<>();
    RecyclerView vendingRecycler;
    RecyclerView.Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard);
        //hooks
        vendingRecycler = findViewById(R.id.recycler_view_vending);
        generateVendingRecycler();
    }
    //recycler contains alot of cards view
    private void generateVendingRecycler(){
        vendingRecycler.setHasFixedSize(true); //fix the size of the recycler view such that only visible ones are loaded onto screen
        vendingRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)); //this means user dashboard is reference//cuz linear layout is used

        vendingItems.add(new VendingRecyclerIntermediate(R.drawable.lays_bbq_chips, "Lay's BBQ Chips 28.3g", "Healthy af"));
        vendingItems.add(new VendingRecyclerIntermediate(R.drawable.lays_classic_chips, "Lay's Classic Chips", "Delicious af."));
        vendingItems.add(new VendingRecyclerIntermediate(R.drawable.pokka_green_tea, "Pokka Green Tea 500ml", "Super healthy"));
        vendingItems.add(new VendingRecyclerIntermediate(R.drawable.lays_bbq_chips, "Lay's BBQ Chips 28.3g", "Healthy af"));
        vendingItems.add(new VendingRecyclerIntermediate(R.drawable.lays_classic_chips, "4", "Delicious af."));
        vendingItems.add(new VendingRecyclerIntermediate(R.drawable.pokka_green_tea, "5", "Super healthy"));
        vendingItems.add(new VendingRecyclerIntermediate(R.drawable.lays_bbq_chips, "6", "Healthy af"));
        vendingItems.add(new VendingRecyclerIntermediate(R.drawable.lays_classic_chips, "7", "Delicious af."));
        vendingItems.add(new VendingRecyclerIntermediate(R.drawable.pokka_green_tea, "8", "Super healthy"));

        adapter = new VendingRecyclerAdapter(vendingItems, this);
        vendingRecycler.setAdapter(adapter);
    }

    //this is the method which sets the respective activity upon respective click of each card
    //meaning display lays product info if lays is clicked, display pokka if pokka is clicked, etcs
    @Override
    public void onCardClick(int position) {
        vendingItems.get(position);
        Intent intent = new Intent(this,ProductInfoActivity.class);
        //dk how to use putExtra
        //        intent.putExtra("delicious", "test, still dk how to pass object to new activity");
        startActivity(intent);
    }
}
