package com.overseer.vendassist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class VendingRecyclerAdapter extends RecyclerView.Adapter<VendingRecyclerAdapter.TrendingViewContainer> {

    //global on card listener is mOnCardListener
    ArrayList<VendingRecyclerIntermediate> vendingItems;
    private OnCardListener mOnCardListener;
    //fetch data
    public VendingRecyclerAdapter(ArrayList<VendingRecyclerIntermediate> vendingItems, OnCardListener onCardListener) {
        this.vendingItems = vendingItems;
        this.mOnCardListener = onCardListener;
    }

    @NonNull
    @Override
    public TrendingViewContainer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //gets elements from xml featured_card_design and convert them to java objects
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.vending_card_design, parent, false);
        return new TrendingViewContainer(view, mOnCardListener);
    }

    @Override
    //TODO: what does binding do?
    public void onBindViewHolder(@NonNull TrendingViewContainer holder, int position) {

        VendingRecyclerIntermediate vendingRecyclerIntermediateInstance = vendingItems.get(position);
        holder.image.setImageResource(vendingRecyclerIntermediateInstance.getImage());
        holder.title.setText(vendingRecyclerIntermediateInstance.getTitle());
        holder.description.setText(vendingRecyclerIntermediateInstance.getDescription());
}

    @Override
    public int getItemCount() {
        return vendingItems.size();
    }

    public static class TrendingViewContainer extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView image;
        TextView title, description;
        OnCardListener onCardListener;


        public TrendingViewContainer(@NonNull View itemView, OnCardListener onCardListener) {
            super(itemView);
            //design hooks
            image = itemView.findViewById(R.id.recycler_card_image);
            title = itemView.findViewById(R.id.recycler_card_title);
            description = itemView.findViewById(R.id.recycler_card_description);
            this.onCardListener = onCardListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onCardListener.onCardClick(getAdapterPosition());

        }
    }
    public interface OnCardListener {
        public void onCardClick(int position);
    }
}
