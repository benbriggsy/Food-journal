package com.example.benbriggs.food_journal.user;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by benbriggs on 29/11/2017.
 */

public class User implements Parcelable{

    private ArrayList<FoodItem> mHistory;

    public User(){
        mHistory = new ArrayList<>();
    }

    public void addFoodItem(FoodItem item){
        mHistory.add(item);
    }

    public ArrayList<FoodItem> getHistory() {
        return mHistory;
    }

    public void setHistory(ArrayList<FoodItem> history) {
        mHistory = history;
    }

    @Override
    public String toString() {
        return "User{" +
                "mHistory=" + mHistory +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.mHistory);
    }

    protected User(Parcel in) {
        this.mHistory = in.createTypedArrayList(FoodItem.CREATOR);
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
