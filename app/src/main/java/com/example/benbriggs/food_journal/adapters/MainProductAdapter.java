package com.example.benbriggs.food_journal.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.benbriggs.food_journal.R;
import com.example.benbriggs.food_journal.user.Basket;
import com.example.benbriggs.food_journal.user.FoodItem;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by benbriggs on 08/02/2018.
 */

public class MainProductAdapter extends RecyclerView.Adapter<MainProductAdapter.MainProductViewHolder>{
    private Basket mBasket;
    private ArrayList<FoodItem> mFoodItems;

    public MainProductAdapter(Basket Basket){
        mBasket = Basket;
        mFoodItems = mBasket.getProducts();
    }

    @Override
    public MainProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_product_list_item, parent, false);
        MainProductViewHolder viewHolder = new MainProductViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MainProductViewHolder holder, int position) {
        holder.bindFoodItem(mFoodItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mFoodItems.size();
    }

    public class MainProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView mProductName;

        public MainProductViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mProductName = (TextView) itemView.findViewById(R.id.listMainProductName);
        }

        public void bindFoodItem(FoodItem foodItem){
            Log.v(TAG, "entered bindHole");
            mProductName.setText(foodItem.getProductName());
        }

        @Override
        public void onClick(View view) {
            //mFoodItems.remove(getAdapterPosition());
            Log.v("clicked item", String.valueOf(getAdapterPosition()));
        }
    }
}
