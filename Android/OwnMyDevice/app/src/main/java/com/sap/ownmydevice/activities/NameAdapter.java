package com.sap.ownmydevice.activities;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.sap.mobi.ownmydevice.R;


/**
 * Created by i300291 on 27/07/15.
 */
public class NameAdapter extends CursorAdapter {
    public NameAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.name_list_item, parent, false);

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView memberNameView = (TextView)view.findViewById(R.id.member_name);
        memberNameView.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));
    }
}
