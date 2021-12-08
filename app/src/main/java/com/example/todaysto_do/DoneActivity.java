package com.example.todaysto_do;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DoneActivity extends AppCompatActivity {
    private final int ONE_DAY = 24 * 60 * 60 * 1000;
    private ArrayList<MainData> doneList, noneList, todoList;

    private ImageView iv_back_main;
    private TextView tv_title_done;

    private RecyclerView doneRecyclerView, noneRecyclerView;
    private DoneAdapter doneAdapter, noneAdapter;
    private LinearLayoutManager doneLayoutManager, noneLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        this.noneList = new ArrayList<MainData>();

        Intent intent = getIntent();
        this.doneList = (ArrayList<MainData>) intent.getSerializableExtra("doneList");

        // set components
        this.doneRecyclerView = (RecyclerView) findViewById(R.id.rv_done);
        this.noneRecyclerView = (RecyclerView) findViewById(R.id.rv_none);
        this.iv_back_main = (ImageView) findViewById(R.id.iv_back_main);
        this.tv_title_done = (TextView) findViewById(R.id.tv_title_done);

        // set onClickListener
        this.iv_back_main.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        // set RecyclerView
        this.doneLayoutManager = new LinearLayoutManager(this);
        this.noneLayoutManager = new LinearLayoutManager(this);
        this.doneRecyclerView.setLayoutManager(doneLayoutManager);
        this.doneRecyclerView.setNestedScrollingEnabled(false);
        this.noneRecyclerView.setLayoutManager(noneLayoutManager);
        this.noneRecyclerView.setNestedScrollingEnabled(false);

        this.doneAdapter = new DoneAdapter(doneList);
        this.noneAdapter = new DoneAdapter(noneList);

        this.doneRecyclerView.setAdapter(doneAdapter);
        this.noneRecyclerView.setAdapter(noneAdapter);

        String tmpt = "Done (" + doneList.size() + ")";
        tv_title_done.setText(tmpt);
    }
}