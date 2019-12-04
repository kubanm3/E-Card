package com.ECard;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.view.Menu;

public class dataList extends BaseActivity {

    RecyclerView dataList;
    DataAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dataList = (RecyclerView)findViewById(R.id.dataListView);
        dataList.setHasFixedSize(true);
        dataList.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = getAllItemsData();
        dataList.setAdapter(mAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_data_list, menu);
        return true;
    }
}
