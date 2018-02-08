/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.benbriggs.food_journal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.benbriggs.food_journal.user.Basket;
import com.example.benbriggs.food_journal.user.FileStorageController;
import com.example.benbriggs.food_journal.user.FoodItem;
import com.example.benbriggs.food_journal.user.NotFoodOrDrinkException;
import com.example.benbriggs.food_journal.user.NotTescoOwnBrandException;
import com.example.benbriggs.food_journal.user.User;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // use a compound button so either checkbox or switch widgets work.
    private CompoundButton useFlash;
    private RecyclerView mRecyclerView;
    private String mJsonString;
    private User mUser;
    private Basket mBasket;
    private FileStorageController mFileStorageController;

    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "BarcodeMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createBindings();
        setListeners();

        mUser = new User();
        mBasket = new Basket();
        mFileStorageController = new FileStorageController(mUser);
        try {
            mFileStorageController.readJSONString(mFileStorageController.readStorageFile(this.getApplicationContext()));
            mUser = mFileStorageController.getUser();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void getTescoInfo(String gtin) {
        String APIkey = "07cd78ffe44d46abb15cdbbbd7d1bc4d";
        String forecastURL = "https://dev.tescolabs.com/product/" +  "?gtin=" + gtin;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(forecastURL)
                .addHeader("Ocp-Apim-Subscription-Key", APIkey)
                .build();

        Log.v(TAG, "here we go...");
        okhttp3.Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
                try {
                    String jsonData = response.body().string();
                    Log.v(TAG, jsonData);
                    if (response.isSuccessful()) {

                        mJsonString = jsonData;
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Log.v(TAG, mJsonString);
                                try {
                                    newEntryScanned(mJsonString);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    }
                } catch (IOException e) {
                    Log.e(TAG, "Exception caught: ", e);
                }
            }
        });
    }

    private void newEntryScanned(String jsonData) throws JSONException, IOException {
        FoodItem foodItem = null;

        try {

            foodItem = new FoodItem(jsonData);
            mUser.addFoodItem(foodItem);
            Log.v(TAG, mUser.toString());
            mFileStorageController.saveUserToFile(this.getApplicationContext());
            mBasket.addFoodItem(foodItem);
            mBasket.calcALL();

            RefreshRecyclerView();

            Log.v("BASKET", mBasket.toString());

        } catch (NotTescoOwnBrandException | NotFoodOrDrinkException e) {

//            productName.setText(e.getMessage());
//            ingredients.setText("please scan another Tesco product");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        Log.v(TAG, v.getId() + "");


        if (v.getId() == R.id.historyButton) {
            // launch barcode activity.
            Intent intent = new Intent(this, HistoryActivity.class);
            intent.putExtra("user", mUser);
            Log.v(TAG, "hello");
            startActivity(intent);
        }

        if (v.getId() == R.id.read_barcode) {
            // launch barcode activity for tesco.
            Intent intent = new Intent(this, BarcodeCaptureActivity.class);
            intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
            intent.putExtra(BarcodeCaptureActivity.UseFlash, useFlash.isChecked());

            startActivityForResult(intent, RC_BARCODE_CAPTURE);
        }

        if (v.getId() == R.id.read_barcode2) {
            // launch barcode activity.
//            Intent intent = new Intent(this, OcrCaptureActivity.class);
//
//            startActivityForResult(intent, 9001);
            mUser.addBasket(mBasket);
            mBasket = new Basket();
            RefreshRecyclerView();
        }
    }

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * {@link #RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * activity is re-starting.
     * <p/>
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult
     * @see #createPendingResult
     * @see #setResult(int)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    String gtin  = barcode.rawValue;
                    Log.d(TAG, "Barcode readStorageFile: " + barcode.displayValue);
                    getTescoInfo(gtin);
                } else {
                    //statusMessage.setText(R.string.barcode_failure);
                    Toast.makeText(this, "No barcode captured", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
                Toast.makeText(this, "Unsuccessful", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void createBindings(){
        useFlash = (CompoundButton) findViewById(R.id.use_flash);
        mRecyclerView = (RecyclerView) findViewById(R.id.mainRecycler);
    }

    private void setListeners(){
        findViewById(R.id.read_barcode).setOnClickListener(this);
        findViewById(R.id.read_barcode2).setOnClickListener(this);
        findViewById(R.id.historyButton).setOnClickListener(this);
    }

    public void RefreshRecyclerView(){
        MainProductAdapter adapter = new MainProductAdapter(mBasket.getProducts());
        mRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
    }
}
