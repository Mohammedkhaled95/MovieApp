package com.example.mohamed.malapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


public class MainActivity extends AppCompatActivity implements NameListener {

    boolean isTwoPane=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        MainActivityFragment mainActivityFragment=new MainActivityFragment();
        mainActivityFragment.setMlistener(this);


        if(savedInstanceState==null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new MainActivityFragment()).commit();
        }


    if (null!=findViewById(R.id.detailscontainer))
    {
        isTwoPane=true;

    }
    }

    @Override
    public void setSelectedMovie(movie selectedMovie) {

        //case mobile (one pane)
        if(!isTwoPane){

            Intent intent = new Intent(this, Details.class);
            Bundle bundle = new Bundle();
            bundle.putString("movieid", selectedMovie.getId() );
            bundle.putString("movieimg", selectedMovie.getPoster_path());
            bundle.putString("moviename", selectedMovie.getTitle());
            bundle.putString("movieyear", selectedMovie.getRelease_date());
            bundle.putString("movierate", String.valueOf(selectedMovie.getVote_average()));
            bundle.putString("movieoverview", selectedMovie.getOverview());
            intent.putExtras(bundle);
            startActivity(intent);

        }
        else{
            //case Tablet (Two Pane)
            DetailsFragment mdeDetailsFragment=new DetailsFragment();
            Bundle extras=new Bundle();
            extras.putString("movieid", selectedMovie.getId() );
            extras.putString("movieimg", selectedMovie.getPoster_path());
            extras.putString("moviename", selectedMovie.getTitle());
            extras.putString("movieyear", selectedMovie.getRelease_date());
            extras.putString("movierate", String.valueOf(selectedMovie.getVote_average()));
            extras.putString("movieoverview", selectedMovie.getOverview());
            mdeDetailsFragment.setArguments(extras);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detailscontainer,mdeDetailsFragment,"").commit();




        }


    }
}

