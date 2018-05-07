package com.example.benbriggs.food_journal.adapters;

import android.content.Context;
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
 * A class that handles the inflation of the list of products on the historical Basket screen
 */
public class BasketAdapter  extends RecyclerView.Adapter<BasketAdapter.BasketViewHolder>{
    private Basket mBasket;
    private ArrayList<FoodItem> mFoodItems;

    public BasketAdapter(Basket Basket, Context context){
        mBasket = Basket;
        mFoodItems = mBasket.getProducts();
    }

    @Override
    public BasketAdapter.BasketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_product_list_item, parent, false);
        BasketAdapter.BasketViewHolder viewHolder = new BasketAdapter.BasketViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BasketAdapter.BasketViewHolder holder, int position) {
        holder.bindFoodItem(mFoodItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mFoodItems.size();
    }

    public class BasketViewHolder extends RecyclerView.ViewHolder{

        public TextView mProductName;

        public BasketViewHolder(View itemView) {
            super(itemView);
            mProductName = (TextView) itemView.findViewById(R.id.listMainProductName);
        }

        public void bindFoodItem(FoodItem foodItem){
            Log.v(TAG, "entered bindHole");
            mProductName.setText(foodItem.getProductName());
        }
    }
}