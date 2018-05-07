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
 * A class that handles the inflation of the list of baskets on the Basket history screen
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

            mPercentageEnergyCal    = itemView.findViewById(R.id.energyValue);
            mPercentageFat          = itemView.findViewById(R.id.fatValue);
            mPercentageSaturates    = itemView.findViewById(R.id.saturatesValue);
            mPercentageCarbohydrate = itemView.findViewById(R.id.carbValue);
            mPercentageSugars       = itemView.findViewById(R.id.sugarValue);
            mPercentageFibre        = itemView.findViewById(R.id.fibreValue);
//            mPercentageProtein      = itemView.findViewById(R.id.proteinValue);
            mPercentageSalt         = itemView.findViewById(R.id.saltValue);

            mPeople                 = itemView.findViewById(R.id.numberOfpeople);
            mDays                   = itemView.findViewById(R.id.daysValue);
            mDate                   = itemView.findViewById(R.id.dateValue);

        }

        /**
         * Sets the display and values for each nutrient
         * @param basket - the basket to get the values from
         */
        public void bindBasket(Basket basket){
            basket.calculateColours();
            fillInfo(mPercentageEnergyCal,      basket.getPercentageEnergyCal(),0, basket);
            fillInfo(mPercentageFat,            basket.getPercentageFat(),1, basket);
            fillInfo(mPercentageSaturates,      basket.getPercentageSaturates(),2, basket);
            fillInfo(mPercentageCarbohydrate,   basket.getPercentageCarbohydrate(),3, basket);
            fillInfo(mPercentageSugars,         basket.getPercentageSugars(),4, basket);
            fillInfo(mPercentageFibre,          basket.getPercentageFibre(),5, basket);
//            fillInfo(mPercentageProtein,        basket.getPercentageProtein(),6, basket);
            fillInfo(mPercentageSalt,           basket.getPercentageSalt(),7, basket);

            mDays.setText(Math.round(basket.getRecommendedDays()) + "");
            mPeople.setText(basket.getNoPeople()+"");
            mDate.setText(basket.getDateAsString());
        }

        /**
         * Fills the information for a TextView and decides which color should be given to the
         * background
         * @param tv - the view to fill
         * @param percentage - the value to fill tv with
         * @param index - the index of the nutrient
         */
        private void fillInfo(TextView tv, double percentage, int index, Basket basket){
            tv.setText(Math.round(percentage) + "%");
            int colour = basket.getColours()[index];
            if(tv.equals(mPercentageSalt)){
                if(colour == 0){
                    tv.setBackground(ContextCompat.getDrawable(mContext,
                            R.drawable.last_bottom_green));
                }else if(colour == 1){
                    tv.setBackground(ContextCompat.getDrawable(mContext,
                            R.drawable.last_bottom_amber));
                }else{
                    tv.setBackground(ContextCompat.getDrawable(mContext,
                            R.drawable.last_bottom_red));
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


        /**
         * A method to allow a Basket in the list to be opened
         * @param view - the view relating to the Basket that will be opened
         */
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, BasketActivity.class);
            intent.putExtra("user", mUser);
            intent.putExtra("basket", mUser.getBasketHistory().get(getAdapterPosition()));
            mContext.startActivity(intent);
        }
    }
}