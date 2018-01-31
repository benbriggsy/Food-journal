package com.example.benbriggs.food_journal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.benbriggs.food_journal.user.User;

public class HistoryActivityActivity extends AppCompatActivity {

    public class HistoryActivity  extends AppCompatActivity {
        private RecyclerView mRecyclerView;
        private User mUser;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_history);

            createBindings();

            Intent intent = getIntent();
            mUser = intent.getParcelableExtra("user");

            HistoryAdapter adapter = new HistoryAdapter(mUser.getHistory());
            mRecyclerView.setAdapter(adapter);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(layoutManager);

            mRecyclerView.setHasFixedSize(true);
        }

        private void createBindings(){
            mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        }
    }

}
