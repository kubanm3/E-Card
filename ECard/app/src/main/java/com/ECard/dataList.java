package com.ECard;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import static android.widget.LinearLayout.VERTICAL;

public class dataList extends BaseActivity {

    RecyclerView dataList;
    DataAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dataList = findViewById(R.id.dataListView);
        dataList.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.drawble_divider));
        dataList.addItemDecoration(decoration);
        mAdapter = getAllItemsData();
        dataList.setAdapter(mAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_data_list, menu);
        return true;
    }
}
