package com.example.benbriggs.food_journal.user;


import android.content.Context;
import android.util.Log;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by benbriggs on 29/11/2017.
 */

public class FileStorageController {
    User mUser;
    private final String USER = "user.json";

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

    public FileStorageController(User user){
        mUser = user;
    }

    public void saveUserToFile(Context ctxt) throws IOException {
        JSONObject root = new JSONObject();
        JSONArray user = new JSONArray();

        for(FoodItem fi : mUser.getHistory()){
            user.add(foodItemToJSON(fi));
        }

        root.put("userHistory", user);

        Log.d("Controller", root.toString());

        writeObjectToFile(root, ctxt);
    }

    public JSONObject foodItemToJSON(FoodItem fi){
        JSONObject foodItem = new JSONObject();
        String ingString = "";
        for(String ingredient : fi.getIngredients()){
            ingString = ingString + ingredient + ", ";
        }
        ingString = ingString.substring(0, ingString.length() - 2);
        foodItem.put("ingredients", ingString);
        foodItem.put("product", fi.getProductName());
        for(int i = 0; i < NUTRIENT_NAMES.length; i++) {
            foodItem.put(NUTRIENT_NAMES[i], fi.getNutrientHashMap().get(NUTRIENT_NAMES[i]));
        }
        return foodItem;
    }

    public void writeObjectToFile(JSONObject obj, Context ctxt) throws IOException {
        FileOutputStream fos = ctxt.openFileOutput(USER, Context.MODE_PRIVATE);
        fos.write(obj.toString().getBytes());
        fos.close();
    }

    public void readJSONString(String json) throws ParseException {
        ArrayList<FoodItem> foodItems = new ArrayList<>();
        JSONParser parser = new JSONParser();
        JSONObject root = (JSONObject) parser.parse(json);
        JSONArray inner = (JSONArray) root.get("userHistory");

        Iterator i = inner.iterator();
        while (i.hasNext()) {
            JSONObject foodItemJSON = (JSONObject) i.next();
            FoodItem foodItem = new FoodItem(foodItemJSON.get("product").toString(),
                    foodItemJSON.get("ingredients").toString());
            foodItems.add(foodItem);
        }
        mUser.setHistory(foodItems);
    }

    public String readStorageFile(Context context) {
        try {
            FileInputStream fis = context.openFileInput(USER);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            fis.close();
            isr.close();
            bufferedReader.close();
            return sb.toString();
        } catch (FileNotFoundException fileNotFound) {
            Log.d("Controller", "file wasn't found");
            return "nothing here yet";
        } catch (IOException ioException) {
            return null;
        }
    }

    public User getUser() {
        return mUser;
    }
}

