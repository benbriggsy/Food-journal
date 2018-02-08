package com.example.benbriggs.food_journal.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.benbriggs.food_journal.R;
import com.example.benbriggs.food_journal.user.FoodItem;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by benbriggs on 17/01/2018.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private ArrayList<FoodItem> mFoodItems;

    public HistoryAdapter(ArrayList<FoodItem> foodItems){
        mFoodItems = foodItems;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_list_item, parent, false);
        HistoryViewHolder viewHolder = new HistoryViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        holder.bindFoodItem(mFoodItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mFoodItems.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {

        public TextView mIngredients;
        public TextView mProductName;

        public HistoryViewHolder(View itemView) {
            super(itemView);

            mProductName = (TextView) itemView.findViewById(R.id.listProductName);
            mIngredients = (TextView) itemView.findViewById(R.id.listIngredients);

        }

        public void bindFoodItem(FoodItem foodItem){
            Log.v(TAG, "entered bindHole");
            mIngredients.setText(foodItem.getNutrientString());
            mProductName.setText(foodItem.getProductName());
        }
    }
}
