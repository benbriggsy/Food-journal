package com.example.benbriggs.food_journal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.benbriggs.food_journal.adapters.BasketHistoryAdapter;
import com.example.benbriggs.food_journal.user.User;

/**
 * Class to control the Basket history screen
 */
public class HistoryActivity  extends AppCompatActivity {
    private RecyclerView mRecyclerView;

    /**
     * The method that instantiates the class by setting the view and creating a list of saved
     * Baskets
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        createBindings();

        Intent intent = getIntent();
        User user = intent.getParcelableExtra("user");

        BasketHistoryAdapter adapter = new BasketHistoryAdapter(user, this);
        mRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);
    }

    private void createBindings(){
        mRecyclerView = findViewById(R.id.recyclerView);
    }
}
