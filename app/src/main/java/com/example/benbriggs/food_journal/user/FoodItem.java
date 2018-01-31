package com.example.benbriggs.food_journal.user;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class FoodItem implements Parcelable {
    String mProductName;
    ArrayList<String> mIngredients;
    Date mTimeAdded;
    private HashMap<String, Nutrient> mNutrientHashMap;

    private final String[] NUTRIENT_NAMES = {
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

    public FoodItem(String jsonData) throws JSONException, NotFoodOrDrinkException, NotTescoOwnBrandException {
        mIngredients = new ArrayList<>();
        JSONObject productData = new JSONObject(jsonData);
        JSONArray products = productData.getJSONArray("products");
        checkUsableProduct(products);
        JSONArray arr = products.getJSONObject(0).getJSONArray("ingredients");
        for(int i = 0; i < arr.length(); i++){
            mIngredients.add(arr.getString(i));
        }
        mProductName = products.getJSONObject(0).getString("description");


        JSONArray nutrientJSON = products.getJSONObject(0).getJSONObject("calcNutrition").getJSONArray("calcNutrients");
        for (int i = 0; i < nutrientJSON.length(); i++) {
            String name = nutrientJSON.getJSONObject(i).getString("name");
            double valuePer100 = Double.parseDouble(nutrientJSON.getJSONObject(i).getString("valuePer100"));
            double valuePerServing = Double.parseDouble(nutrientJSON.getJSONObject(i).getString("valuePerServing"));
            mNutrientHashMap.put(NUTRIENT_NAMES[i], new Nutrient(name, valuePer100, valuePerServing));
        }
        mTimeAdded = new Date();
    }

    public FoodItem(String productName, String ingredients){
        mProductName = productName;
        mIngredients = new ArrayList<>();

        String[] split = ingredients.split(", ");
        for(String s : split){
            mIngredients.add(s);
        }
    }

    private void checkUsableProduct(JSONArray products) throws JSONException,
            NotFoodOrDrinkException, NotTescoOwnBrandException {
        if(!products.getJSONObject(0).getJSONObject("productCharacteristics").getBoolean("isFood") &&
                !products.getJSONObject(0).getJSONObject("productCharacteristics").getBoolean("isDrink")){
            throw new NotFoodOrDrinkException();
        }
        if(!products.getJSONObject(0).getString("brand").equals("TESCO")){
            throw new NotTescoOwnBrandException(products.getJSONObject(0).getString("brand"));
        }
    }

    public String getProductName() {
        return mProductName;
    }

    public void setProductName(String productName) {
        mProductName = productName;
    }

    public ArrayList<String> getIngredients() {
        return mIngredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        mIngredients = ingredients;
    }

    public HashMap<String, Nutrient> getNutrientHashMap() {
        return mNutrientHashMap;
    }

    @Override
    public String toString() {
        return "mProductName='" + mProductName + '\'' +
                ", mIngredients=" + mIngredients +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mProductName);
        dest.writeStringList(this.mIngredients);
        dest.writeLong(this.mTimeAdded != null ? this.mTimeAdded.getTime() : -1);
        dest.writeSerializable(this.mNutrientHashMap);
    }

    protected FoodItem(Parcel in) {
        this.mProductName = in.readString();
        this.mIngredients = in.createStringArrayList();
        long tmpMTimeAdded = in.readLong();
        this.mTimeAdded = tmpMTimeAdded == -1 ? null : new Date(tmpMTimeAdded);
        this.mNutrientHashMap = (HashMap<String, Nutrient>) in.readSerializable();
    }

    public static final Creator<FoodItem> CREATOR = new Creator<FoodItem>() {
        @Override
        public FoodItem createFromParcel(Parcel source) {
            return new FoodItem(source);
        }

        @Override
        public FoodItem[] newArray(int size) {
            return new FoodItem[size];
        }
    };
}
