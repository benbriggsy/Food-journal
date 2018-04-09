package com.example.benbriggs.food_journal.user;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;


public class Basket implements Parcelable {
    private ArrayList<FoodItem> mProducts;

    private double mTotalEnergy;
    private double mTotalEnergyCal;
    private double mTotalFat;
    private double mTotalSaturates;
    private double mTotalCarbohydrate;
    private double mTotalSugars;
    private double mTotalFibre;
    private double mTotalProtein;
    private double mTotalSalt;

    private double mPercentageEnergyCal;
    private double mPercentageFat;
    private double mPercentageSaturates;
    private double mPercentageCarbohydrate;
    private double mPercentageSugars;
    private double mPercentageFibre;
    private double mPercentageProtein;
    private double mPercentageSalt;

    private double mTotalGandML;
    private double mWeightOrVolume;
    private int mNoPeople;

    private int mDays;
    private double mRecommendedDays;

    private Date mDate;

    private String[] NUTRIENT_NAMES = {
            "Energy (kJ)",
            "Energy (kcal)",
            "Fat (g)",
            "saturates (g)",
            "Carbohydrate (g)",
            "sugars (g)",
            "Fibre (g)",
            "Protein (g)",
            "Salt (g)"
    };

    private final double ENERGY = 2000, PROTEIN= 45, CARBS=230,
            SUGARS=90, FAT=70, SATURATES=20, FIBRE=24, SALT=6;

    public Basket() {
        mProducts           = new ArrayList<>();
        mTotalEnergy        = 0;
        mTotalEnergyCal     = 0;
        mTotalFat           = 0;
        mTotalSaturates     = 0;
        mTotalCarbohydrate  = 0;
        mTotalSugars        = 0;
        mTotalFibre         = 0;
        mTotalProtein       = 0;
        mTotalSalt          = 0;
        mTotalGandML        = 0;
        mWeightOrVolume     = 0;
        mDays               = 0;
        mNoPeople           = 1;
    }

    public Basket(int noPeople) {
        mProducts           = new ArrayList<>();
        mTotalEnergy        = 0;
        mTotalEnergyCal     = 0;
        mTotalFat           = 0;
        mTotalSaturates     = 0;
        mTotalCarbohydrate  = 0;
        mTotalSugars        = 0;
        mTotalFibre         = 0;
        mTotalProtein       = 0;
        mTotalSalt          = 0;
        mTotalGandML        = 0;
        mWeightOrVolume     = 0;
        mDays               = 0;
        mNoPeople           = noPeople;
    }

    public void addFoodItem(FoodItem foodItem){
        mProducts.add(foodItem);
    }

    public void calcALL(){
        calculateTotals();
        calcRecommendedDays();
        calculatePercentages();
    }

    private void calculateTotals(){
        setValuesToZero();
        for (FoodItem fi: mProducts) {
            Nutrient[] nutrients = fi.getNutrients();
            double multiplier = fi.getWeight()/100/mNoPeople;

            mTotalEnergy        += nutrients[0].getValuePer100() * multiplier;
            mTotalEnergyCal     += nutrients[1].getValuePer100() * multiplier;
            mTotalFat           += nutrients[2].getValuePer100() * multiplier;
            mTotalSaturates     += nutrients[3].getValuePer100() * multiplier;
            mTotalCarbohydrate  += nutrients[4].getValuePer100() * multiplier;
            mTotalSugars        += nutrients[5].getValuePer100() * multiplier;
            mTotalFibre         += nutrients[6].getValuePer100() * multiplier;
            mTotalProtein       += nutrients[7].getValuePer100() * multiplier;
            mTotalSalt          += nutrients[8].getValuePer100() * multiplier;
            mTotalGandML        += fi.getWeight();
        }
    }

    private void calculatePercentages(){
        if(mDays == 0){
            mPercentageEnergyCal    = ((mTotalEnergyCal/mRecommendedDays)/ENERGY) * 100;
            mPercentageFat          = ((mTotalFat/mRecommendedDays)/FAT) * 100;
            mPercentageSaturates    = ((mTotalSaturates/mRecommendedDays)/SATURATES) * 100;
            mPercentageCarbohydrate = ((mTotalCarbohydrate/mRecommendedDays)/CARBS) * 100;
            mPercentageSugars       = ((mTotalSugars/mRecommendedDays)/SUGARS) * 100;
            mPercentageFibre        = ((mTotalFibre/mRecommendedDays)/FIBRE) * 100;
            mPercentageProtein      = ((mTotalProtein/mRecommendedDays)/PROTEIN) * 100;
            mPercentageSalt         = ((mTotalSalt/mRecommendedDays)/SALT) * 100;
        }
        if(mDays > 0){
            mPercentageEnergyCal    = ((mTotalEnergyCal/mDays)/ENERGY) * 100;
            mPercentageFat          = ((mTotalFat/mDays)/FAT) * 100;
            mPercentageSaturates    = ((mTotalSaturates/mDays)/SATURATES) * 100;
            mPercentageCarbohydrate = ((mTotalCarbohydrate/mDays)/CARBS) * 100;
            mPercentageSugars       = ((mTotalSugars/mDays)/SUGARS) * 100;
            mPercentageFibre        = ((mTotalFibre/mDays)/FIBRE) * 100;
            mPercentageProtein      = ((mTotalProtein/mDays)/PROTEIN) * 100;
            mPercentageSalt         = ((mTotalFat/mDays)/SALT) * 100;
        }
    }

    private void calcRecommendedDays(){
        mRecommendedDays = ((mTotalEnergyCal / ENERGY) / mNoPeople);
        if(mRecommendedDays < 1){
            mRecommendedDays = 1;
        }
        if(mRecommendedDays - ((int)mRecommendedDays) > 0.5){
            mRecommendedDays = mRecommendedDays + 1;
        }
        mRecommendedDays = (int)mRecommendedDays;
    }

    private void setValuesToZero(){
        mTotalEnergy        = 0;
        mTotalEnergyCal     = 0;
        mTotalFat           = 0;
        mTotalSaturates     = 0;
        mTotalCarbohydrate  = 0;
        mTotalSugars        = 0;
        mTotalFibre         = 0;
        mTotalProtein       = 0;
        mTotalSalt          = 0;
        mTotalGandML        = 0;
        mWeightOrVolume     = 0;
    }

    @Override
    public String toString() {
        return "Basket{" +
                "mPercentageEnergyCal=" + mPercentageEnergyCal +
                ", mPercentageFat=" + mPercentageFat +
                ", mPercentageSaturates=" + mPercentageSaturates +
                ", mPercentageCarbohydrate=" + mPercentageCarbohydrate +
                ", mPercentageSugars=" + mPercentageSugars +
                ", mPercentageFibre=" + mPercentageFibre +
                ", mPercentageProtein=" + mPercentageProtein +
                ", mPercentageSalt=" + mPercentageSalt +
                ", mNoPeople=" + mNoPeople +
                ", mDays=" + mDays +
                ", mRecommendedDays=" + mRecommendedDays +
                '}';
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public void setDays(int days) {
        mDays = days;
    }

    public double getPercentageEnergyCal() {
        return mPercentageEnergyCal;
    }

    public double getPercentageFat() {
        return mPercentageFat;
    }

    public double getPercentageSaturates() {
        return mPercentageSaturates;
    }

    public double getPercentageCarbohydrate() {
        return mPercentageCarbohydrate;
    }

    public double getPercentageSugars() {
        return mPercentageSugars;
    }

    public double getPercentageFibre() {
        return mPercentageFibre;
    }

    public double getPercentageProtein() {
        return mPercentageProtein;
    }

    public double getPercentageSalt() {
        return mPercentageSalt;
    }

    public int getDays() {
        return mDays;
    }

    public double getRecommendedDays() {
        return mRecommendedDays;
    }

    public ArrayList<FoodItem> getProducts() {
        return mProducts;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.mProducts);
        dest.writeDouble(this.mTotalEnergy);
        dest.writeDouble(this.mTotalEnergyCal);
        dest.writeDouble(this.mTotalFat);
        dest.writeDouble(this.mTotalSaturates);
        dest.writeDouble(this.mTotalCarbohydrate);
        dest.writeDouble(this.mTotalSugars);
        dest.writeDouble(this.mTotalFibre);
        dest.writeDouble(this.mTotalProtein);
        dest.writeDouble(this.mTotalSalt);
        dest.writeDouble(this.mPercentageEnergyCal);
        dest.writeDouble(this.mPercentageFat);
        dest.writeDouble(this.mPercentageSaturates);
        dest.writeDouble(this.mPercentageCarbohydrate);
        dest.writeDouble(this.mPercentageSugars);
        dest.writeDouble(this.mPercentageFibre);
        dest.writeDouble(this.mPercentageProtein);
        dest.writeDouble(this.mPercentageSalt);
        dest.writeDouble(this.mTotalGandML);
        dest.writeDouble(this.mWeightOrVolume);
        dest.writeInt(this.mDays);
        dest.writeDouble(this.mRecommendedDays);
        dest.writeStringArray(this.NUTRIENT_NAMES);
    }

    private Basket(Parcel in) {
        this.mProducts = in.createTypedArrayList(FoodItem.CREATOR);
        this.mTotalEnergy = in.readDouble();
        this.mTotalEnergyCal = in.readDouble();
        this.mTotalFat = in.readDouble();
        this.mTotalSaturates = in.readDouble();
        this.mTotalCarbohydrate = in.readDouble();
        this.mTotalSugars = in.readDouble();
        this.mTotalFibre = in.readDouble();
        this.mTotalProtein = in.readDouble();
        this.mTotalSalt = in.readDouble();
        this.mPercentageEnergyCal = in.readDouble();
        this.mPercentageFat = in.readDouble();
        this.mPercentageSaturates = in.readDouble();
        this.mPercentageCarbohydrate = in.readDouble();
        this.mPercentageSugars = in.readDouble();
        this.mPercentageFibre = in.readDouble();
        this.mPercentageProtein = in.readDouble();
        this.mPercentageSalt = in.readDouble();
        this.mTotalGandML = in.readDouble();
        this.mWeightOrVolume = in.readDouble();
        this.mDays = in.readInt();
        this.mRecommendedDays = in.readDouble();
        this.NUTRIENT_NAMES = in.createStringArray();
    }

    static final Parcelable.Creator<Basket> CREATOR = new Parcelable.Creator<Basket>() {
        @Override
        public Basket createFromParcel(Parcel source) {
            return new Basket(source);
        }

        @Override
        public Basket[] newArray(int size) {
            return new Basket[size];
        }
    };
}
