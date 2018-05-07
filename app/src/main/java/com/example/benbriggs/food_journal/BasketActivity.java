package com.example.benbriggs.food_journal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.benbriggs.food_journal.adapters.BasketAdapter;
import com.example.benbriggs.food_journal.user.Basket;

/**
 * A class to control the screen showing the contents of a historical basket
 */
public class BasketActivity extends AppCompatActivity {
    public TextView mPercentageEnergyCal;
    public TextView mPercentageFat;
    public TextView mPercentageSaturates;
    public TextView mPercentageCarbohydrate;
    public TextView mPercentageSugars;
    public TextView mPercentageFibre;
//    public TextView mPercentageProtein;
    public TextView mPercentageSalt;

    public TextView mDays;
    public TextView mPeopleValue;
    private RecyclerView mRecyclerView;
    private Basket mBasket;

    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "BarcodeMain";

    /**
     * Method is run when the application is opened, sets up the page and loads the historical data
     * into user
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        createBindings();

        Intent intent = getIntent();
        mBasket = intent.getParcelableExtra("basket");
        RefreshRecyclerView();
        RefreshNutrition();
    }


    /**
     * Creates the bindings between the XML views and their relevant field variables
     */
    private void createBindings(){
        mRecyclerView           = findViewById(R.id.mainRecycler);
        mPercentageEnergyCal    = findViewById(R.id.energyValue);
        mPercentageFat          = findViewById(R.id.fatValue);
        mPercentageSaturates    = findViewById(R.id.saturatesValue);
        mPercentageCarbohydrate = findViewById(R.id.carbValue);
        mPercentageSugars       = findViewById(R.id.sugarValue);
        mPercentageFibre        = findViewById(R.id.fibreValue);
//        mPercentageProtein      = findViewById(R.id.proteinValue);
        mPercentageSalt         = findViewById(R.id.saltValue);
        mDays                   = findViewById(R.id.daysValue);
        mPeopleValue            = findViewById(R.id.numberOfpeople);
    }
    /**
     * Updates the recycler view to add items to the list
     */
    public void RefreshRecyclerView(){
        BasketAdapter adapter = new BasketAdapter(mBasket, this);
        mRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
    }

    /**
     * Calculates the percentages for each nutrient
     */
    public void RefreshNutrition(){
        mBasket.calculateColours();
        bindBasket(mBasket);
    }

    /**
     * Sets the display and values for each nutrient
     * @param basket - the basket to get the values from
     */
    public void bindBasket(Basket basket){
        fillInfo(mPercentageEnergyCal,      basket.getPercentageEnergyCal(),0);
        fillInfo(mPercentageFat,            basket.getPercentageFat(),1);
        fillInfo(mPercentageSaturates,      basket.getPercentageSaturates(),2);
        fillInfo(mPercentageCarbohydrate,   basket.getPercentageCarbohydrate(),3);
        fillInfo(mPercentageSugars,         basket.getPercentageSugars(),4);
        fillInfo(mPercentageFibre,          basket.getPercentageFibre(),5);
//        fillInfo(mPercentageProtein,        basket.getPercentageProtein(),6);
        fillInfo(mPercentageSalt,           basket.getPercentageSalt(),7);

        mDays.setText(Math.round(basket.getRecommendedDays()) + "");
        mPeopleValue.setText(basket.getNoPeople() + "");
    }

    /**
     * Fills the information for a TextView and decides which color should be given to the
     * background
     * @param tv - the view to fill
     * @param percentage - the value to fill tv with
     * @param index - the index of the nutrient
     */
    private void fillInfo(TextView tv, double percentage, int index){
        tv.setText(Math.round(percentage) + "%");
        int colour = mBasket.getColours()[index];
        if(tv.equals(mPercentageSalt)){
            if(colour == 0){
                tv.setBackground(ContextCompat.getDrawable(this,
                        R.drawable.last_bottom_green));
            }else if(colour == 1){
                tv.setBackground(ContextCompat.getDrawable(this,
                        R.drawable.last_bottom_amber));
            }else{
                tv.setBackground(ContextCompat.getDrawable(this,
                        R.drawable.last_bottom_red));
            }
        }else{
            if(colour == 0){
                tv.setBackground(ContextCompat.getDrawable(this, R.drawable.green_corner));
            }else if(colour == 1){
                tv.setBackground(ContextCompat.getDrawable(this, R.drawable.amber_corner));
            }else{
                tv.setBackground(ContextCompat.getDrawable(this, R.drawable.red_corner));
            }
        }
    }
}
