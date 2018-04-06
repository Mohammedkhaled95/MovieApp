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
public class ReviewAdapter extends BaseAdapter {

    Context context;
    List<ReviewData> reviewDataList=new ArrayList<>();
    LayoutInflater inflater;

    public ReviewAdapter(Context context,List<ReviewData> reviewDataList) {
        this.reviewDataList = reviewDataList;
        this.context=context;
    }

    @Override
    public int getCount() {

        return reviewDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return reviewDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      View view=convertView;
        TextView author;
        TextView content;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=inflater.inflate(R.layout.review_item,null);
        author=(TextView)view.findViewById(R.id.author);
        content=(TextView) view.findViewById(R.id.content);
        Log.e("author in adapter",reviewDataList.get(position).getAuthor());
        author.setText(reviewDataList.get(position).getAuthor());
        content.setText(reviewDataList.get(position).getContent());
        return view;
    }
}
