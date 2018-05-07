package com.example.benbriggs.food_journal.user;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 *
 */
public class User implements Parcelable{

    private ArrayList<FoodItem> mHistory;
    private ArrayList<Basket> mBasketHistory;

    /**
     * creates a user with an empty history and basket history
     */
    public User(){
        mHistory        = new ArrayList<>();
        mBasketHistory  = new ArrayList<>();
    }

    /**
     * Adds a food item to the users history
     * @param item
     */
    public void addFoodItem(FoodItem item){
        mHistory.add(item);
    }

    /**
     * Adds a basket to the users basket history
     * @param basket
     */
    public void addBasket(Basket basket){
        mBasketHistory.add(0, basket);
    }

    ArrayList<FoodItem> getHistory() {
        return mHistory;
    }

    public ArrayList<Basket> getBasketHistory() {
        return mBasketHistory;
    }

    void setHistory(ArrayList<FoodItem> history) {
        mHistory = history;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Write object to parcelable to make it fast to pass between activities.
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.mHistory);
        dest.writeTypedList(this.mBasketHistory);
    }

    private User(Parcel in) {
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
