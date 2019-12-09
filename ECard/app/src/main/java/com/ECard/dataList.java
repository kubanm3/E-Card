package com.ECard;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import static android.widget.LinearLayout.VERTICAL;

public class dataList extends BaseActivity {
    public static String EXTRA_NAME = "com.ECard.EXTRA_NAME";
    public static String EXTRA_COMPANY_NAME = "com.ECard.EXTRA_COMPANY_NAME";
    public static String EXTRA_ADDRESS = "com.ECard.EXTRA_ADDRESS";
    public static String EXTRA_EMAIL = "com.ECard.EXTRA_EMAIL";
    public static String EXTRA_PHONE = "com.ECard.EXTRA_PHONE";
    public static String EXTRA_LAYOUT_NAME = "com.ECard.EXTRA_LAYOUT_NAME";

    RecyclerView dataList;
    DataAdapter mAdapter;
    private Drawable iconDelete;
    private ColorDrawable backgroundDelete;

    private Drawable iconEdit;
    private ColorDrawable backgroundEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        iconDelete = ContextCompat.getDrawable(this,
                R.drawable.ic_delete_sweep_white_36dp);
        backgroundDelete = new ColorDrawable(Color.RED);

        iconEdit = ContextCompat.getDrawable(this,
                R.drawable.ic_edit_white_36dp);
        backgroundEdit = new ColorDrawable(Color.GREEN);

        dataList = findViewById(R.id.dataListView);
        dataList.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration decoration =
                new DividerItemDecoration(getApplicationContext(), VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.drawble_divider));
        dataList.addItemDecoration(decoration);
        mAdapter = getAllItemsData();
        dataList.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }


            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                myDb.deleteDataData((String) viewHolder.itemView.getTag());
                mAdapter = getAllItemsData();
                dataList.setAdapter(mAdapter);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                View itemView = viewHolder.itemView;

                int iconMargin = (itemView.getHeight() - iconDelete.getIntrinsicHeight()) / 2;
                int iconTop =
                        itemView.getTop() + (itemView.getHeight() - iconDelete.getIntrinsicHeight()) / 2 - 10;
                int iconBottom = iconTop + iconDelete.getIntrinsicHeight();

                if (dX < 0) { // Swiping to the left
                    int iconLeft =
                            itemView.getRight() - iconMargin - iconDelete.getIntrinsicWidth();
                    int iconRight = itemView.getRight() - iconMargin - 10;
                    iconDelete.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                    backgroundDelete.setBounds(itemView.getRight() + ((int) dX),itemView.getTop() - 36,
                            itemView.getRight(), itemView.getBottom());
                } else { // view is unSwiped
                    iconDelete.setBounds(0, 0, 0, 0);
                    backgroundDelete.setBounds(0, 0, 0, 0);
                }

                backgroundDelete.draw(c);
                iconDelete.draw(c);
            }
        }).attachToRecyclerView(dataList);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;

            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                openDataControlActivityToEdit(viewHolder.itemView.getTag().toString());
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                View itemView = viewHolder.itemView;

                int iconMargin = (itemView.getHeight() - iconEdit.getIntrinsicHeight()) / 2;
                int iconTop = itemView.getTop() + (itemView.getHeight() - iconEdit.getIntrinsicHeight()) / 2 - 10;
                int iconBottom = iconTop + iconEdit.getIntrinsicHeight();

                if (dX > 0) { // Swiping to the right
                    int iconLeft = itemView.getLeft() + iconMargin;
                    int iconRight = itemView.getLeft() + iconMargin + iconEdit.getIntrinsicWidth();
                    iconEdit.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                    backgroundEdit.setBounds(itemView.getLeft() - 24, itemView.getTop() - 36,
                            itemView.getLeft() + ((int) dX), itemView.getBottom());
                } else { // view is unSwiped
                    iconEdit.setBounds(0, 0, 0, 0);
                    backgroundEdit.setBounds(0, 0, 0, 0);
                }

                backgroundEdit.draw(c);
                iconEdit.draw(c);
            }
        }).attachToRecyclerView(dataList);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                Toast.makeText(this, "Autor: Jakub Legutko", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.showFromList:
                viewAll();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_data_list, menu);
        return true;
    }

    public void openDataControlActivityToEdit(String id) {
        Cursor res = myDb.getDataData(Integer.valueOf(id));

        if (res.getCount() == 0) {
            return;
        }

        res.moveToFirst();
        String name = res.getString(0);
        String companyName = res.getString(1);
        String address = res.getString(2);
        String email = res.getString(3);
        String phone = res.getString(4);
        String layoutName = res.getString(5);

        Intent intent = new Intent();
        intent.putExtra(EXTRA_NAME, name);
        intent.putExtra(EXTRA_COMPANY_NAME, companyName);
        intent.putExtra(EXTRA_ADDRESS, address);
        intent.putExtra(EXTRA_EMAIL, email);
        intent.putExtra(EXTRA_PHONE, phone);
        intent.putExtra(EXTRA_LAYOUT_NAME, layoutName);

        setResult(RESULT_OK, intent);
        finish();
    }


}
