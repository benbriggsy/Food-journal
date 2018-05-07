package com.example.benbriggs.food_journal.user;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A class to model a single nutrient for a FoodItem
 */
public class Nutrient implements Parcelable {
    private String Name;
    private Double valuePer100;
    private Double valuePerServing;

    Nutrient(String name, Double valuePer100, Double valuePerServing) {
        Name = name;
        this.valuePer100 = valuePer100;
        this.valuePerServing = valuePerServing;
    }

    public String getName() {
        return Name;
    }

    Double getValuePer100() {
        return valuePer100;
    }

    Double getValuePerServing() {
        return valuePerServing;
    }

    @Override
    public String toString() {
        return "Nutrient{" +
                "Name='" + Name + '\'' +
                ", valuePer100=" + valuePer100 +
                ", valuePerServing=" + valuePerServing +
                '}';
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
        dest.writeString(this.Name);
        dest.writeValue(this.valuePer100);
        dest.writeValue(this.valuePerServing);
    }

    private Nutrient(Parcel in) {
        this.Name = in.readString();
        this.valuePer100 = (Double) in.readValue(Double.class.getClassLoader());
        this.valuePerServing = (Double) in.readValue(Double.class.getClassLoader());
    }

    static final Creator<Nutrient> CREATOR = new Creator<Nutrient>() {
        @Override
        public Nutrient createFromParcel(Parcel source) {
            return new Nutrient(source);
        }

        @Override
        public Nutrient[] newArray(int size) {
            return new Nutrient[size];
        }
    };
}
