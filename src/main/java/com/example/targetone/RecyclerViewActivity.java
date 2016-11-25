package com.example.targetone;


import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends AppCompatActivity {

    private List<MenuItems> mainItems;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recyclerview);

        rv=(RecyclerView)findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData();
        initializeAdapter();

        rv.addOnItemTouchListener(
                new RecyclerItemClickListener(getBaseContext(), rv ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        if (position == 0) {
                            Intent intent = new Intent(RecyclerViewActivity.this,
                                    RangeCategoryActivity.class);
                            startActivity(intent);
                        }
                        if (position == 1) {
                            Intent intent = new Intent(RecyclerViewActivity.this,
                                    AboutActivity.class);
                            startActivity(intent);
                        }
                        if (position == 2) {
                            Intent intent = new Intent(RecyclerViewActivity.this,
                                    FeedbackActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

    }

    private void initializeData(){
        mainItems = new ArrayList<>();
        mainItems.add(new MenuItems("Полигон", "Перейти к управлению", R.drawable.points_icon));
        mainItems.add(new MenuItems("О программе", "Руководство пользователя", R.drawable.about_icon));
        mainItems.add(new MenuItems("Контакты", "Свяжитесь с нами", R.drawable.email_icon));
    }

    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(mainItems);
        rv.setAdapter(adapter);

    }

}