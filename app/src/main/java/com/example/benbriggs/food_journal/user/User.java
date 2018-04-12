package com.example.benbriggs.food_journal.user;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by benbriggs on 29/11/2017.
 */

public class User implements Parcelable{

    private ArrayList<FoodItem> mHistory;
    private ArrayList<Basket> mBasketHistory;

    public User(){
        mHistory        = new ArrayList<>();
        mBasketHistory  = new ArrayList<>();
    }

    public void addFoodItem(FoodItem item){
        mHistory.add(item);
    }

    public void addBasket(Basket basket){
        mBasketHistory.add(0, basket);

        Log.v("User", "Amount of baskets: " + mBasketHistory.size());
    }

    public ArrayList<FoodItem> getHistory() {
        return mHistory;
    }

    public ArrayList<Basket> getBasketHistory() {
        return mBasketHistory;
    }

    public void setHistory(ArrayList<FoodItem> history) {
        mHistory = history;
    }

    public void setBasketHistory(ArrayList<Basket> basketHistory) {
        mBasketHistory = basketHistory;
    }

    @Override
    public String toString() {
        return "User{" +
                "mHistory=" + mHistory +
                ",\nmBasketHistory=" + mBasketHistory +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.mHistory);
        dest.writeTypedList(this.mBasketHistory);
    }

    protected User(Parcel in) {
        this.mHistory = in.createTypedArrayList(FoodItem.CREATOR);
        this.mBasketHistory = in.createTypedArrayList(Basket.CREATOR);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
