package com.example.benbriggs.food_journal;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.benbriggs.food_journal.user.Basket;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by benbriggs on 07/02/2018.
 */

public class BasketHistoryAdapter extends RecyclerView.Adapter<BasketHistoryAdapter.BasketHistoryViewHolder> {
    private ArrayList<Basket> mBaskets;

    public BasketHistoryAdapter(ArrayList<Basket> baskets){
        mBaskets = baskets;
    }

    @Override
    public BasketHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.basket_history_list_item, parent, false);
        BasketHistoryViewHolder viewHolder = new BasketHistoryViewHolder(view);
            return viewHolder;
    }

    @Override
    public void onBindViewHolder(BasketHistoryViewHolder holder, int position) {
            holder.bindBasket(mBaskets.get(position));
    }

    @Override
    public int getItemCount() {
            return mBaskets.size();
    }

    public class BasketHistoryViewHolder extends RecyclerView.ViewHolder {

        public TextView mPercentageEnergyCal;
        public TextView mPercentageFat;
        public TextView mPercentageSaturates;
        public TextView mPercentageCarbohydrate;
        public TextView mPercentageSugars;
        public TextView mPercentageFibre;
        public TextView mPercentageProtein;
        public TextView mPercentageSalt;

        public BasketHistoryViewHolder(View itemView) {
            super(itemView);

            mPercentageEnergyCal    = (TextView) itemView.findViewById(R.id.energyValue);
            mPercentageFat          = (TextView) itemView.findViewById(R.id.fatValue);
            mPercentageSaturates    = (TextView) itemView.findViewById(R.id.saturatesValue);
            mPercentageCarbohydrate = (TextView) itemView.findViewById(R.id.carbValue);
            mPercentageSugars       = (TextView) itemView.findViewById(R.id.sugarValue);
            mPercentageFibre        = (TextView) itemView.findViewById(R.id.fibreValue);
            mPercentageProtein      = (TextView) itemView.findViewById(R.id.proteinValue);
            mPercentageSalt         = (TextView) itemView.findViewById(R.id.saltValue);

        }

        public void bindBasket(Basket basket){
            Log.v(TAG, "entered bindHole");
            mPercentageEnergyCal.setText(basket.getPercentageEnergyCal() + "%");
            mPercentageFat.setText(basket.getPercentageFat() + "%");
            mPercentageSaturates.setText(basket.getPercentageSaturates() + "%");
            mPercentageCarbohydrate.setText(basket.getPercentageCarbohydrate() + "%");
            mPercentageSugars.setText(basket.getPercentageSugars() + "%");
            mPercentageFibre.setText(basket.getPercentageFibre() + "%");
            mPercentageProtein.setText(basket.getPercentageProtein() + "%");
            mPercentageSalt.setText(basket.getPercentageSalt() + "%");
        }
    }
}