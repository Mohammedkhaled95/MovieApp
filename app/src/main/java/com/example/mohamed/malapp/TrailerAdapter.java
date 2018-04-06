package com.example.mohamed.malapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohamed on 12/1/16.
 */
public class TrailerAdapter extends BaseAdapter {
    Context context;
    List<TrailerData> trailerDataList = new ArrayList<>();
    LayoutInflater inflater;

    public TrailerAdapter(Context context, List<TrailerData> trailerDataList) {
        this.trailerDataList = trailerDataList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return trailerDataList.size();
    }
    @Override
    public Object getItem(int position) {
        return trailerDataList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        TextView name;
            inflater = (LayoutInflater) context.
                    getSystemService(context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.trailer_item, null);
        name = (TextView) view.findViewById(R.id.traileritem);
        Log.e("name on adapter##",trailerDataList.get(position).getName());
        name.setText(trailerDataList.get(position).getName());
        return view;
    }
}