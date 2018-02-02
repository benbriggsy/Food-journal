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
        foodItem.put("nutrients", nutrientsToJSON(fi));
        return foodItem;
    }

    public JSONArray nutrientsToJSON(FoodItem fi){
        JSONArray ja = new JSONArray();
        for(Nutrient n : fi.getNutrients()){
            JSONObject jo = new JSONObject();
            jo.put("name", n.getName());
            jo.put("valuePer100", n.getValuePer100());
            jo.put("valuePerServing", n.getValuePerServing());
            ja.add(jo);
        }
        return ja;
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
                    foodItemJSON.get("ingredients").toString(), JSONToNutrients((JSONArray)foodItemJSON.get("nutrients")));
            foodItems.add(foodItem);
        }
        mUser.setHistory(foodItems);
    }

    public Nutrient[] JSONToNutrients(JSONArray ja){
        Nutrient[] nutrients = new Nutrient[NUTRIENT_NAMES.length];
        for (int i = 0; i < ja.size(); i++){
            JSONObject jo = (JSONObject) ja.get(i);
            nutrients[i] = new Nutrient((String) jo.get("name"), (Double) jo.get("valuePer100"), (Double) jo.get("valuePerServing"));
        }
        return nutrients;
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

