package com.ECard;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

class LayoutAdapter extends RecyclerView.Adapter<LayoutAdapter.LayoutViewHolder> {
    public static Context mContext;
    private Cursor mCursor;

    public OnLayoutListener mOnLayoutListener;


    public LayoutAdapter(Context context, Cursor cursor, OnLayoutListener onLayoutListener) {
        mContext = context;
        mCursor = cursor;
        this.mOnLayoutListener = onLayoutListener;
    }

    public class LayoutViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nameText;
        OnLayoutListener onLayoutListener;

        public LayoutViewHolder(View itemView, OnLayoutListener onLayoutListener) {
            super(itemView);

            nameText = itemView.findViewById(R.id.textview_name_layout_item);
            this.onLayoutListener = onLayoutListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onLayoutListener.onDataClick(getAdapterPosition());
        }
    }

    public interface OnLayoutListener {
        void onDataClick(int position);
    }

    @Override
    public LayoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.layout_item, parent, false);
        return new LayoutViewHolder(view, mOnLayoutListener);
    }

    @Override
    public void onBindViewHolder(LayoutViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        String name = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.LayoutEntry.LAYOUTS_NAME));
        String id = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.LayoutEntry.LAYOUTS_ID));

        holder.nameText.setText(name);
        holder.itemView.setTag(id);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }
}
