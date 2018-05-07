package com.example.benbriggs.food_journal.user;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * A class to model a FoodItem that has a list of ingredients, a list of nutrients and a product
 * name
 */
public class FoodItem implements Parcelable {
    private String mProductName;
    private ArrayList<String> mIngredients;
    private Date mTimeAdded;
    private Nutrient[] mNutrients;
    private double mWeight;
    private final double KJTOKCAL = 0.239006;

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

    /**
     * Creates a FoodItem from a given JSON response from the Tesco Labs API
     * @param jsonData - the JSON response
     * @throws JSONException
     * @throws NotFoodOrDrinkException
     * @throws NotTescoOwnBrandException
     */
    public FoodItem(String jsonData) throws JSONException, NotFoodOrDrinkException,
            NotTescoOwnBrandException {
        mIngredients = new ArrayList<>();
        mNutrients = new Nutrient[NUTRIENT_NAMES.length];
        JSONObject productData = new JSONObject(jsonData);
        JSONArray products = productData.getJSONArray("products");
        checkUsableProduct(products);

        //make sure ingredients exists
        if(products.getJSONObject(0).has("ingredients")) {
            JSONArray arr = products.getJSONObject(0).getJSONArray("ingredients");
            for (int i = 0; i < arr.length(); i++) {
                mIngredients.add(arr.getString(i));
            }
        }else{
            mIngredients.add("none");
        }

        mProductName = products.getJSONObject(0).getString("description");

        //need to check here for what format the weight is in.
        String unitsOM = products.getJSONObject(0).getJSONObject("qtyContents")
                .getString("quantityUom");

        //calculate the weight in grams
        if(unitsOM.equals("ml") || unitsOM.equals("g")){
            mWeight = products.getJSONObject(0).getJSONObject("qtyContents")
                    .getDouble("totalQuantity");
        }else if(unitsOM.equals("kg") || unitsOM.equals("l")){
            mWeight = products.getJSONObject(0).getJSONObject("qtyContents")
                    .getDouble("totalQuantity")*1000;
        }else{
            throw new NotTescoOwnBrandException();
        }

        //get each nutrient and add it to the list of nutrients for this FoodItem
        JSONArray nutrientJSON = products.getJSONObject(0).getJSONObject("calcNutrition")
                .getJSONArray("calcNutrients");
        for (int i = 0; i < NUTRIENT_NAMES.length; i++) {
            String name = nutrientJSON.getJSONObject(i).getString("name");
            if(name.equals("Energy (kcal)")){
                JSONObject current = nutrientJSON.getJSONObject(i-1);
                double valuePer100 = Double.parseDouble(current
                        .getString("valuePer100"))*KJTOKCAL;
                double valuePerServing = Double.parseDouble(current
                        .getString("valuePerServing"))*KJTOKCAL;
                mNutrients[i] = new Nutrient(name, valuePer100, valuePerServing);
            }else {
                double valuePer100 = Double.parseDouble(nutrientJSON.getJSONObject(i)
                        .getString("valuePer100"));
                double valuePerServing = Double.parseDouble(nutrientJSON.getJSONObject(i)
                        .getString("valuePerServing"));
                mNutrients[i] = new Nutrient(name, valuePer100, valuePerServing);
            }
        }
        mTimeAdded = new Date();
    }

    /**
     * Create a FoodItem from strings, and a list of nutrients
     * @param productName - the name of the FoodItem
     * @param ingredients - a comma separated list of ingredients
     * @param nutrients - an array of Nutrients
     */
    public FoodItem(String productName, String ingredients, Nutrient[] nutrients){
        mProductName = productName;
        mIngredients = new ArrayList<>();
        mNutrients = nutrients;

        //split the ingredients string
        String[] split = ingredients.split(", ");
        for(String s : split){
            mIngredients.add(s);
        }
    }

    /**
     * Create a FoodItem from strings, a list of nutrients and the weight
     * @param productName - the name of the FoodItem
     * @param ingredients - a comma separated list of ingredients
     * @param nutrients - an array of Nutrients
     * @param weight - The total weight of the product
     */
    public FoodItem(String productName, String ingredients, Nutrient[] nutrients, Double weight){
        mProductName = productName;
        mIngredients = new ArrayList<>();
        mNutrients = nutrients;
        mWeight = weight;

        String[] split = ingredients.split(", ");
        for(String s : split){
            mIngredients.add(s);
        }
    }

    /**
     * A method that checks whether a product is compatible with this app from a JSONArray
     * @param products - The array to check
     * @throws JSONException
     * @throws NotFoodOrDrinkException - thrown if the item is not food or drink
     * @throws NotTescoOwnBrandException - thrown if the product is the wrong brand
     */
    private void checkUsableProduct(JSONArray products) throws JSONException,
            NotFoodOrDrinkException, NotTescoOwnBrandException {
        if(!products.getJSONObject(0).getJSONObject("productCharacteristics")
                .getBoolean("isFood") &&
                !products.getJSONObject(0).getJSONObject("productCharacteristics")
                        .getBoolean("isDrink")){
            throw new NotFoodOrDrinkException();
        }
        if(!products.getJSONObject(0).getString("brand").equals("TESCO") &&
                !products.getJSONObject(0).getString("brand").equals("TESCO VALUE")){
            throw new NotTescoOwnBrandException(products.getJSONObject(0)
                    .getString("brand"));
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

    public Nutrient[] getNutrients() {
        return mNutrients;
    }

    public String getNutrientString() {
        return mNutrients[1].toString();
    }

    public double getWeight() {
        return mWeight;
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

    /**
     * Write object to parcelable to make it fast to pass between activities.
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mProductName);
        dest.writeStringList(this.mIngredients);
        dest.writeLong(this.mTimeAdded != null ? this.mTimeAdded.getTime() : -1);
        dest.writeTypedArray(this.mNutrients, flags);
        dest.writeStringArray(this.NUTRIENT_NAMES);
    }

    protected FoodItem(Parcel in) {
        this.mProductName = in.readString();
        this.mIngredients = in.createStringArrayList();
        long tmpMTimeAdded = in.readLong();
        this.mTimeAdded = tmpMTimeAdded == -1 ? null : new Date(tmpMTimeAdded);
        this.mNutrients = in.createTypedArray(Nutrient.CREATOR);
        this.NUTRIENT_NAMES = in.createStringArray();
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
