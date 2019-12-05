package com.ECard;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder>  {
    public static Context mContext;
    private Cursor mCursor;

    public DataAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {
        public TextView nameText;
        public TextView companyText;

        public DataViewHolder(View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.textview_name_item);
            companyText = itemView.findViewById(R.id.textview_company_item);
        }
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.data_item, parent, false);
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        String name = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.DataEntry.DATA_NAME));
        String company = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.DataEntry.DATA_COMPANY_NAME));
        String id = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.DataEntry.DATA_ID));

        holder.nameText.setText(name);
        holder.companyText.setText(company);
        holder.itemView.setTag(id);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }
}
