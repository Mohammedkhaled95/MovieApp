package com.example.mohamed.malapp;

/**
 * Created by mohamed on 11/15/16.
 */
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by mohamed on 11/3/16.
 */
public class Adapter  extends BaseAdapter {
    Context context;
    List<movie> mymovies;
    LayoutInflater inflater;

    public Adapter(Context con, List paths) {
        context=con;
        mymovies=paths;

    }

    @Override
    public int getCount() {

        return mymovies.size();
    }

    @Override
    public Object getItem(int position) {
        return mymovies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;//mymovies.get(position).getId();
    }
    /*
        public void add(movie m){
            mymovies.add(m);
        }
    */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=convertView;
        ImageView poster;
        if (convertView==null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        view = inflater.inflate(R.layout.grid_item, null,false);
        poster=(ImageView)view.findViewById(R.id.movieimg);
        //poster=(ImageView)view.findViewById(R.id.movieimg);
        Log.i("mypaths...","http://image.tmdb.org/t/p/w185/"+mymovies.get(position));
        Picasso.with(context).load("http://image.tmdb.org/t/p/w185/"+
                mymovies.get(position).getPoster_path()).into(poster);
        return view;
    }
}

