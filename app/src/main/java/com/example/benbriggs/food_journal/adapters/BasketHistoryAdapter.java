package com.example.benbriggs.food_journal.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.benbriggs.food_journal.BasketActivity;
import com.example.benbriggs.food_journal.R;
import com.example.benbriggs.food_journal.user.Basket;
import com.example.benbriggs.food_journal.user.User;

import java.util.ArrayList;

/**
 * Created by benbriggs on 07/02/2018.
 */

public class BasketHistoryAdapter extends RecyclerView.Adapter<BasketHistoryAdapter.BasketHistoryViewHolder> {
    private ArrayList<Basket> mBaskets;
    private Context mContext;
    private User mUser;

    public BasketHistoryAdapter(User user, Context context){
        mBaskets = user.getBasketHistory();
        mContext = context;
        mUser    = user;
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

    public class BasketHistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mPercentageEnergyCal;
        public TextView mPercentageFat;
        public TextView mPercentageSaturates;
        public TextView mPercentageCarbohydrate;
        public TextView mPercentageSugars;
        public TextView mPercentageFibre;
        public TextView mPercentageProtein;
        public TextView mPercentageSalt;

        public TextView mPeople;
        public TextView mDate;
        public TextView mDays;

        public BasketHistoryViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mPercentageEnergyCal    = (TextView) itemView.findViewById(R.id.energyValue);
            mPercentageFat          = (TextView) itemView.findViewById(R.id.fatValue);
            mPercentageSaturates    = (TextView) itemView.findViewById(R.id.saturatesValue);
            mPercentageCarbohydrate = (TextView) itemView.findViewById(R.id.carbValue);
            mPercentageSugars       = (TextView) itemView.findViewById(R.id.sugarValue);
            mPercentageFibre        = (TextView) itemView.findViewById(R.id.fibreValue);
            mPercentageProtein      = (TextView) itemView.findViewById(R.id.proteinValue);
            mPercentageSalt         = (TextView) itemView.findViewById(R.id.saltValue);

            mPeople                 = (TextView) itemView.findViewById(R.id.numberOfpeople);
            mDays                   = (TextView) itemView.findViewById(R.id.daysValue);
            mDate                   = (TextView) itemView.findViewById(R.id.dateValue);

        }

        public void bindBasket(Basket basket){
            basket.calculateColours();
            fillInfo(mPercentageEnergyCal,      basket.getPercentageEnergyCal(),0, basket);
            fillInfo(mPercentageFat,            basket.getPercentageFat(),1, basket);
            fillInfo(mPercentageSaturates,      basket.getPercentageSaturates(),2, basket);
            fillInfo(mPercentageCarbohydrate,   basket.getPercentageCarbohydrate(),3, basket);
            fillInfo(mPercentageSugars,         basket.getPercentageSugars(),4, basket);
            fillInfo(mPercentageFibre,          basket.getPercentageFibre(),5, basket);
            fillInfo(mPercentageProtein,        basket.getPercentageProtein(),6, basket);
            fillInfo(mPercentageSalt,           basket.getPercentageSalt(),7, basket);

            mDays.setText(Math.round(basket.getRecommendedDays()) + "");
            mPeople.setText(basket.getNoPeople()+"");
            mDate.setText(basket.getDateAsString());
        }

        private void fillInfo(TextView tv, double percentage, int index, Basket basket){

            tv.setText(Math.round(percentage) + "%");
            int colour = basket.getColours()[index];
            if(tv.equals(mPercentageSalt)){
                if(colour == 0){
                    tv.setBackground(ContextCompat.getDrawable(mContext, R.drawable.last_bottom_green));
                }else if(colour == 1){
                    tv.setBackground(ContextCompat.getDrawable(mContext, R.drawable.last_bottom_amber));
                }else{
                    tv.setBackground(ContextCompat.getDrawable(mContext, R.drawable.last_bottom_red));
                }
            }else{
                if(colour == 0){
                    tv.setBackground(ContextCompat.getDrawable(mContext, R.drawable.green_corner));
                }else if(colour == 1){
                    tv.setBackground(ContextCompat.getDrawable(mContext, R.drawable.amber_corner));
                }else{
                    tv.setBackground(ContextCompat.getDrawable(mContext, R.drawable.red_corner));
                }
            }
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, BasketActivity.class);
            intent.putExtra("user", mUser);
            intent.putExtra("basket", mUser.getBasketHistory().get(getAdapterPosition()));
            mContext.startActivity(intent);
        }
    }
}