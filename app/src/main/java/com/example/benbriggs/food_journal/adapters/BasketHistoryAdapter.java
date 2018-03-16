package com.example.benbriggs.food_journal.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.benbriggs.food_journal.R;
import com.example.benbriggs.food_journal.user.Basket;

import java.util.ArrayList;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gda_list_item, parent, false);
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


        public TextView mDays;

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

            mDays                   = (TextView) itemView.findViewById(R.id.daysValue);

        }

        public void bindBasket(Basket basket){
            fillInfo(mPercentageEnergyCal,      basket.getPercentageEnergyCal());
            fillInfo(mPercentageFat,            basket.getPercentageFat());
            fillInfo(mPercentageSaturates,      basket.getPercentageSaturates());
            fillInfo(mPercentageCarbohydrate,   basket.getPercentageCarbohydrate());
            fillInfo(mPercentageSugars,         basket.getPercentageSugars());
            fillInfo(mPercentageFibre,          basket.getPercentageFibre());
            fillInfo(mPercentageProtein,        basket.getPercentageProtein());
            fillInfo(mPercentageSalt,           basket.getPercentageSalt());

            mDays.setText(Math.round(basket.getRecommendedDays()) + "");
        }

        private void fillInfo(TextView tv, double percentage){
            tv.setText(Math.round(percentage) + "%");

            if(Math.abs(percentage - 100) <= 10){
                tv.setBackgroundColor(Color.GREEN);
            }else if(Math.abs(percentage - 100) <= 20){
                tv.setBackgroundColor(Color.YELLOW);
            }else{
                tv.setBackgroundColor(Color.RED);
            }
        }
    }
}