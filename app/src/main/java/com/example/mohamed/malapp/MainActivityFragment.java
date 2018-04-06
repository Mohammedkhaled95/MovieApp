package com.example.mohamed.malapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    GridView movie_grid;
    private static NameListener mlistener;
    public static void setMlistener(NameListener listener) {
        mlistener = listener;
    }
    SharedPreferences.Editor editor;
    Adapter adapter;
    SharedPreferences sharedPref;
    String type;
    movie sentmov=new movie();
    List<movie> moviesList = new ArrayList<movie>();
    movietask task;

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        movie_grid = (GridView) view.findViewById(R.id.gridView);

        sharedPref = this.getActivity().getPreferences(Context.MODE_PRIVATE);
        type = sharedPref.getString(getString(R.string.sharedpref), "popular");
        adapter = new Adapter(getContext(),moviesList);
        adapter.notifyDataSetChanged();
        movie_grid.setAdapter(adapter);
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        task = new movietask(type);
        //  task = new movietask("popular");

        if (isNetworkAvailable()) {
            task.execute(type);
            //   task.execute("popular");

        } else {

            Toast.makeText(getActivity(), "sorry , no network connection", Toast.LENGTH_SHORT).show();


        }


        movie_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               sentmov=moviesList.get(position);

               mlistener.setSelectedMovie(sentmov);}

        });


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.menu_main, menu);  // Use filter.xml from step 1
//        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        editor = sharedPref.edit();
        if (item.getItemId() == R.id.top_rated) {
            editor.putString(getString(R.string.sharedpref), "top_rated");
            task= new movietask("top_rated");
            task.execute("top_rated");
        } else if (item.getItemId() == R.id.popular) {
            editor.putString(getString(R.string.sharedpref), "popular").commit();
            task= new movietask("popular");
            task.execute("popular");
        }
        else {
            DBHandler handler = new DBHandler(getContext());
            moviesList = handler.getAllMovies();
            adapter = new Adapter(getContext(), moviesList);
            movie_grid.setAdapter(adapter);
            movie_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    sentmov = moviesList.get(position);
                    mlistener.setSelectedMovie(sentmov);
                }
            });
        }
        editor.commit();

        return super.onOptionsItemSelected(item);
    }

    public class movietask extends AsyncTask<String, Void, String> {


        public movietask(String typ) {
            type = typ;
        }

        String forecastJsonStr;

        @Override
        protected String doInBackground(String... params) {

            Log.i(" main activity fragment", "doInBackground: ");
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                String myURL;
                myURL = "http://api.themoviedb.org/3/movie/" + params[0] + "?api_key=aa4d60eb83769b263b7b00be84db5558";
                Log.i("linke -------->", myURL);
                /* if (params[0]=="popular"){
                    myURL="http://api.themoviedb.org/3/movie/"+type+"?api_key=aa4d60eb83769b263b7b00be84db5558";

                }
                else if (params[0]=="toprated"){
                    myURL="http://api.themoviedb.org/3/movie/"+type+"?api_key=aa4d60eb83769b263b7b00be84db5558";
                }*/

                URL url = new URL(myURL);

                // Create the request to THEMovieDB, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();

                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    // Nothing to do.
                    //forecastJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    // forecastJsonStr = null;
                }
                forecastJsonStr = buffer.toString();
                Log.i("main data", forecastJsonStr);

            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the movie data, there's no point in attempting
                // to parse it.
                //  forecastJsonStr = null;
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                        e.printStackTrace();
                    }
                }
            }
            return forecastJsonStr;
        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            moviesList= new ArrayList<>();
            Log.i("main  Data", forecastJsonStr+"");
            try {
                JSONObject jsonObject = new JSONObject(data);
                JSONArray jsonArray = jsonObject.getJSONArray("results");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    sentmov = new movie();
                    sentmov.setPoster_path(object.getString(("poster_path")));
                    sentmov.setOverview(object.getString("overview"));
                    sentmov.setRelease_date(object.getString("release_date"));
                    sentmov.setId(object.getString("id"));
                    sentmov.setTitle(object.getString("title"));
                    sentmov.setVote_average(object.getString("vote_average"));
                    moviesList.add(sentmov);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            adapter = new Adapter(getContext(),moviesList);
            adapter.notifyDataSetChanged();
            movie_grid.setAdapter(adapter);
        }
    }
}
