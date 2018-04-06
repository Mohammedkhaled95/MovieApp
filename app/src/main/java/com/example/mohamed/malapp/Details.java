package com.example.mohamed.malapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class Details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent i=getIntent();
        Bundle b=i.getExtras();
        DetailsFragment detailsFragment=new DetailsFragment();
        detailsFragment.setArguments(b);

        if(savedInstanceState==null)
        {
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.detailscontainer,detailsFragment).commit();
        }

    }

}
