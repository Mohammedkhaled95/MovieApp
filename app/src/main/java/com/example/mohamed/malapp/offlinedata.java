package com.example.mohamed.malapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohamed on 12/2/16.
 */
public class offlinedata extends Fragment {

    GridView offlineGrid;
    List<String> posters;

    public offlinedata(List<String> posters) {
        this.posters = posters;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.offline_data,container,false);

//posters=new DBHandler(getActivity()).getAllMovies()
        offlineGrid=(GridView)view.findViewById(R.id.offlinegrid);
        List<movie> offlinelist=new ArrayList<>();
        posters=new ArrayList<>();
        Adapter adp=new Adapter(getActivity(),posters);
        offlineGrid.setAdapter(adp);



        return view;
    }
}
