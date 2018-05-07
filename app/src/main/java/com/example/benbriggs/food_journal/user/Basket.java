package com.example.benbriggs.food_journal.user;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A class to model a Basket which is a list of FoodItem objects, the class also deals with
 * all calculations to do with the aggregation of nutritional information in the Basket
 */
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
    private String mDateAsString;

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

    private int[] mColours = new int[8]; //0 = green, 1 = orange, 2 = red

    // the government guidelines for each nutrient
    private final double ENERGY = 2000, PROTEIN= 45, CARBS=230,
            SUGARS=90, FAT=70, SATURATES=20, FIBRE=24, SALT=6;

    /**
     * Creates a new empty Basket
     */
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
        mDate               = new Date();
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        mDateAsString = df.format(mDate);
        calculateColours();
    }

    public void addFoodItem(FoodItem foodItem){
        mProducts.add(0, foodItem);
    }

    public void removeFoodItem(int i){
        mProducts.remove(i);
        calcALL();
    }

    /**
     * Calculates all values related to the advice given for this basket
     */
    public void calcALL(){
        calculateTotals();
        calcRecommendedDays();
        calculatePercentages();
        calculateColours();
    }

    /**
     * Calculates the total of each nutrient in the basket and the total weight
     */
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

    /**
     * Calculates the percentage to be displayed for the basket.
     */
    private void calculatePercentages(){
        if(mDays == 0){
            mPercentageEnergyCal    = calculateEnergyPercentage(mRecommendedDays);
            mPercentageFat          = calculateFatsPercentage(mRecommendedDays);
            mPercentageSaturates    = calculateSatsPercentage(mRecommendedDays);
            mPercentageCarbohydrate = calculateCarbsPercentage(mRecommendedDays);
            mPercentageSugars       = calculateSugarPercentage(mRecommendedDays);
            mPercentageFibre        = calculateFibrePercentage(mRecommendedDays);
            mPercentageProtein      = ((mTotalProtein/mRecommendedDays)/PROTEIN) * 100;
            mPercentageSalt         = calculateSaltPercentage(mRecommendedDays);
        }
        if(mDays > 0){
            mPercentageEnergyCal    = calculateEnergyPercentage(mDays);
            mPercentageFat          = calculateFatsPercentage(mDays);
            mPercentageSaturates    = calculateSatsPercentage(mDays);
            mPercentageCarbohydrate = calculateCarbsPercentage(mDays);
            mPercentageSugars       = calculateSugarPercentage(mDays);
            mPercentageFibre        = calculateFibrePercentage(mDays);
            mPercentageProtein      = ((mTotalProtein/mRecommendedDays)/PROTEIN) * 100;
            mPercentageSalt         = calculateSaltPercentage(mDays);
        }
    }

    private double calculateEnergyPercentage(double days){
        return ((mTotalEnergyCal/days)/ENERGY) * 100;
    }

    private double calculateFibrePercentage(double days){
        return ((mTotalFibre/days)/FIBRE) * 100;
    }

    private double calculateSaltPercentage(double days){
        return ((mTotalSalt/days)/SALT) * 100;
    }

    private double calculateSugarPercentage(double days){
        double kcalInSugar = 4;
        return ((mTotalSugars*kcalInSugar/days)/ENERGY) * 100;
    }

    private double calculateCarbsPercentage(double days){
        double kcalInCarbs = 4;
        return ((mTotalCarbohydrate*kcalInCarbs/days)/ENERGY) * 100;
    }

    private double calculateFatsPercentage(double days){
        double kcalInFats = 9;
        return ((mTotalFat*kcalInFats/days)/ENERGY) * 100;
    }

    private double calculateSatsPercentage(double days){
        double kcalInSats = 9;
        return ((mTotalSaturates*kcalInSats/days)/ENERGY) * 100;
    }

    /**
     * Calculates the background colour that each nutrient should be with the current contents of
     * the Basket. Each nutrient has its own calculation based on advice given by a Dietician
     */
    public void calculateColours(){
        mColours[0] = calcEnergyColour();
        mColours[1] = calcFatColour();
        mColours[2] = calcSatColour();
        mColours[3] = calcCarbColour();
        mColours[4] = calcSugarColour();
        mColours[5] = calcFibreColour();
        mColours[6] = calcProteinColour();
        mColours[7] = calcSaltColour();
    }

    private int calcSaltColour() {
        if(mPercentageSalt <= 100){
            return 0;
        }else if(mPercentageSalt > 125){
            return 2;
        }else{
            return 1;
        }
    }

    private int calcProteinColour() {
        return 0;
    }

    private int calcFibreColour() {
        if(mPercentageFibre >= 100){
            return 0;
        }else if(mPercentageFibre < 80){
            return 2;
        }else{
            return 1;
        }
    }

    private int calcSugarColour() {
        if(mPercentageSugars < 5){
            return 0;
        }else if(mPercentageSugars > 10){
            return 2;
        }else{
            return 1;
        }
    }

    private int calcCarbColour() {
        if(mPercentageCarbohydrate > 50){
            return 0;
        }else if(mPercentageCarbohydrate < 40){
            return 2;
        }else{
            return 1;
        }
    }

    private int calcSatColour() {
        if(mPercentageSaturates < 35){
            return 0;
        }else if(mPercentageSaturates > 40){
            return 2;
        }else{
            return 1;
        }
    }

    private int calcFatColour() {
        if(mPercentageSaturates < 35){
            return 0;
        }else if(mPercentageSaturates > 40){
            return 2;
        }else{
            return 1;
        }
    }

    private int calcEnergyColour() {
        if(mPercentageEnergyCal < 80){
            return 1;
        }else if(mPercentageEnergyCal > 100){
            return 2;
        }else{
            return 0;
        }
    }

    /**
     * Calculate how many days a basket should be for.
     */
    private void calcRecommendedDays(){
        mRecommendedDays = ((mTotalEnergyCal / ENERGY) / mNoPeople);
        if(mRecommendedDays < 1){
            mRecommendedDays = 1;
        }
        // if the energy is more than 50% the recommended amount
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

    public int[] getColours() {
        return mColours;
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

    public int getNoPeople() {
        return mNoPeople;
    }

    public void setNoPeople(int noPeople) {
        mNoPeople = noPeople;
    }

    public String getDateAsString() {
        return mDateAsString;
    }

    public void setDateAsString(String dateAsString) {
        mDateAsString = dateAsString;
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
        dest.writeInt(this.mNoPeople);
        dest.writeString(this.mDateAsString);
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
        this.mNoPeople = in.readInt();
        this.mDateAsString = in.readString();
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
