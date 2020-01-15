package com.ECard;

import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
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

public class LayoutList extends BaseActivity {
    RecyclerView layoutList;
    LayoutAdapter mAdapter;

    private Drawable iconDelete;
    private ColorDrawable backgroundDelete;

    private Drawable iconEdit;
    private ColorDrawable backgroundEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        iconDelete = ContextCompat.getDrawable(this,
                R.drawable.ic_delete_sweep_white_36dp);
        backgroundDelete = new ColorDrawable(Color.RED);

        iconEdit = ContextCompat.getDrawable(this,
                R.drawable.ic_edit_white_36dp);
        backgroundEdit = new ColorDrawable(Color.GREEN);

        layoutList = findViewById(R.id.layoutListView);
        layoutList.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration decoration =
                new DividerItemDecoration(getApplicationContext(), VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.drawble_divider));
        layoutList.addItemDecoration(decoration);
        mAdapter = getAllItemsLayout();
        layoutList.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }


            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                DeleteData((String) viewHolder.itemView.getTag());
                mAdapter = getAllItemsLayout();
                layoutList.setAdapter(mAdapter);
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
        }).attachToRecyclerView(layoutList);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                openLayoutControlActivityToEdit(viewHolder.itemView.getTag().toString());
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
        }).attachToRecyclerView(layoutList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter = getAllItemsLayout();
        layoutList.setAdapter(mAdapter);
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
            case R.id.layoutList:
                Intent intentAddLayout = new Intent(this, LayoutControl.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intentAddLayout, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                } else {
                    startActivity(intentAddLayout);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_layout_list, menu);
        return true;
    }

    public void openLayoutControlActivityToEdit(String id) {
        Cursor res = myDb.getLayoutData(Integer.valueOf(id));

        if (res.getCount() == 0) {
            return;
        }

        res.moveToFirst();
        String name = res.getString(1);
        String orientation = res.getString(2);
        String name_pos_x = res.getString(3);
        String name_pos_y = res.getString(4);
        String name_font = res.getString(5);
        String company_pos_x = res.getString(6);
        String company_pos_y = res.getString(7);
        String company_font = res.getString(8);
        String address_pos_x = res.getString(9);
        String address_pos_y = res.getString(10);
        String address_font = res.getString(11);
        String email_pos_x = res.getString(12);
        String email_pos_y = res.getString(13);
        String email_font = res.getString(14);
        String phone_pos_x = res.getString(15);
        String phone_pos_y = res.getString(16);
        String phone_font = res.getString(17);

        Intent intent = new Intent(this, LayoutControl.class);
        intent.putExtra(EXTRA_ID_LAYOUT, id);
        intent.putExtra(EXTRA_NAME_LAYOUT, name);
        intent.putExtra(EXTRA_ORIENTATION, orientation);
        intent.putExtra(EXTRA_NAME_POS_X, name_pos_x);
        intent.putExtra(EXTRA_NAME_POS_Y, name_pos_y);
        intent.putExtra(EXTRA_NAME_FONT, name_font);
        intent.putExtra(EXTRA_COMPANY_POS_X, company_pos_x);
        intent.putExtra(EXTRA_COMPANY_POS_Y, company_pos_y);
        intent.putExtra(EXTRA_COMPANY_FONT, company_font);
        intent.putExtra(EXTRA_ADDRESS_POS_X, address_pos_x);
        intent.putExtra(EXTRA_ADDRESS_POS_Y, address_pos_y);
        intent.putExtra(EXTRA_ADDRESS_FONT, address_font);
        intent.putExtra(EXTRA_EMAIL_POS_X, email_pos_x);
        intent.putExtra(EXTRA_EMAIL_POS_Y, email_pos_y);
        intent.putExtra(EXTRA_EMAIL_FONT, email_font);
        intent.putExtra(EXTRA_PHONE_POS_X, phone_pos_x);
        intent.putExtra(EXTRA_PHONE_POS_Y, phone_pos_y);
        intent.putExtra(EXTRA_PHONE_FONT, phone_font);

        setResult(RESULT_OK, intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivity(intent);
        }
    }


}
