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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import static android.widget.LinearLayout.VERTICAL;

public class LayoutList extends BaseActivity {
    public static String EXTRA_ID_LAYOUT = "com.ECard.EXTRA_LAYOUT_ID";
    public static String EXTRA_NAME_LAYOUT = "com.ECard.EXTRA_LAYOUT_NAME";
    public static String EXTRA_ORIENTATION = "com.ECard.EXTRA_ORIENTATION";
    public static String EXTRA_NAME_POS_X = "com.ECard.EXTRA_NAME_POS_X";
    public static String EXTRA_NAME_POS_Y = "com.ECard.EXTRA_NAME_POS_Y";
    public static String EXTRA_NAME_FONT = "com.ECard.EXTRA_NAME_FONT";
    public static String EXTRA_COMPANY_POS_X = "com.ECard.EXTRA_COMPANY_POS_X";
    public static String EXTRA_COMPANY_POS_Y = "com.ECard.EXTRA_COMPANY_POS_Y";
    public static String EXTRA_COMPANY_FONT = "com.ECard.EXTRA_COMPANY_FONT";
    public static String EXTRA_ADDRESS_POS_X = "com.ECard.EXTRA_ADDRESS_POS_X";
    public static String EXTRA_ADDRESS_POS_Y = "com.ECard.EXTRA_ADDRESS_POS_Y";
    public static String EXTRA_ADDRESS_FONT = "com.ECard.EXTRA_ADDRESS_FONT";
    public static String EXTRA_EMAIL_POS_X = "com.ECard.EXTRA_EMAIL_POS_X";
    public static String EXTRA_EMAIL_POS_Y = "com.ECard.EXTRA_EMAIL_POS_Y";
    public static String EXTRA_EMAIL_FONT = "com.ECard.EXTRA_EMAIL_FONT";
    public static String EXTRA_PHONE_POS_X = "com.ECard.EXTRA_PHONE_POS_X";
    public static String EXTRA_PHONE_POS_Y = "com.ECard.EXTRA_PHONE_POS_Y";
    public static String EXTRA_PHONE_FONT = "com.ECard.EXTRA_PHONE_FONT";

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
                myDb.deleteDataLayout((String) viewHolder.itemView.getTag());
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
        Log.d("przesyłanedane", name + "; ");
        String orientation = res.getString(2);
        Log.d("przesyłanedane", orientation + "; ");
        String name_pos_x = res.getString(3);
        Log.d("przesyłanedane", name_pos_x + "; ");
        String name_pos_y = res.getString(4);
        Log.d("przesyłanedane", name_pos_y + "; ");
        String name_font = res.getString(5);
        Log.d("przesyłanedane", name_font + "; ");
        String company_pos_x = res.getString(6);
        Log.d("przesyłanedane", company_pos_x + "; ");
        String company_pos_y = res.getString(7);
        Log.d("przesyłanedane", company_pos_y + "; ");
        String company_font = res.getString(8);
        Log.d("przesyłanedane", company_font + "; ");
        String address_pos_x = res.getString(9);
        Log.d("przesyłanedane", address_pos_x + "; ");
        String address_pos_y = res.getString(10);
        Log.d("przesyłanedane", address_pos_y + "; ");
        String address_font = res.getString(11);
        Log.d("przesyłanedane", address_font + "; ");
        String email_pos_x = res.getString(12);
        Log.d("przesyłanedane", email_pos_x + "; ");
        String email_pos_y = res.getString(13);
        Log.d("przesyłanedane", email_pos_y + "; ");
        String email_font = res.getString(14);
        Log.d("przesyłanedane", email_font + "; ");
        String phone_pos_x = res.getString(15);
        Log.d("przesyłanedane", phone_pos_x + "; ");
        String phone_pos_y = res.getString(16);
        Log.d("przesyłanedane", phone_pos_y + "; ");
        String phone_font = res.getString(17);
        Log.d("przesyłanedane", phone_font + "; ");

        Intent intent = new Intent(this, AddLayout.class);
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
