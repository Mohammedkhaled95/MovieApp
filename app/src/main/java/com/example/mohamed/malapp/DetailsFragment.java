package com.example.mohamed.malapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.squareup.picasso.Picasso;

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
public class DetailsFragment extends Fragment {

    List<ReviewData> reviewList=new ArrayList<>();
    List<TrailerData>trailerlist=new ArrayList<>();



    ReviewAdapter reviewAdapter;
    TrailerAdapter trailerAdapter;

    ExpandableHeightListView reviewlistview;
    ExpandableHeightListView trailerlistview;

    Button favourite;
    String str_image;
    String str_name;
    String str_year;
    String str_rate;
    String str_overview;
    String  _id;

    TextView moviename;
    ImageView movieimage;
    TextView movieyear;
    TextView movierate;
    TextView movieoverview;

    public DetailsFragment() {
    }
    movie mov;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         View view = inflater.inflate(R.layout.fragment_details, container, false);
        reviewlistview=(ExpandableHeightListView)view.findViewById(R.id.reviewlist);
        trailerlistview=(ExpandableHeightListView)view.findViewById(R.id.trailerlist);
        movieimage =(ImageView)view.findViewById(R.id.deatailsmovieimg);
        moviename=(TextView)view.findViewById(R.id.detailsmoviename);
        movieyear=(TextView)view.findViewById(R.id.detailsmovieyear);
        movierate=(TextView)view.findViewById(R.id.detailsmovierate);
        movieoverview=(TextView)view.findViewById(R.id.deatailsmovieoverview);
        favourite=(Button)view.findViewById(R.id.favourite);

        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHandler dbHandler=new DBHandler(getContext());
                mov=new movie();
                mov.setOverview(str_overview);
                mov.setRelease_date(str_year);
                mov.setVote_average(str_rate);
                mov.setTitle(str_name);
                mov.setId(_id);
                mov.setPoster_path(str_image);
                dbHandler.addMovie(mov);
               // dbHandler.addMovie(mov2);
                Toast.makeText(getActivity(), "Added to DB", Toast.LENGTH_SHORT).show();
            }
        });
        str_image= getArguments().getString("movieimg");
        str_name= getArguments().getString("moviename");
        str_year= getArguments().getString("movieyear");
        str_rate= getArguments().getString("movierate");
        str_overview= getArguments().getString("movieoverview");
        _id=getArguments().getString("movieid");

        trailerlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.youtube.com/watch?v="
                                +trailerlist.get(position).getKey()));
                startActivity(intent);
            }
        });
       Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w185/"+str_image).into(movieimage);
        Log.i("load img 2222232323 ",str_image);
        moviename.setText(str_name);
        movieyear.setText(str_year);
        movierate.setText(str_rate);
        movieoverview.setText(str_overview);

        FetchReview fetchReview=new FetchReview();
        FetchTrailer fetchTrailer=new FetchTrailer();
        fetchTrailer.execute("");
        fetchReview.execute("");
        return view;
    }
    public class FetchReview extends AsyncTask<String ,Void,String> {
        String reviewJsonStr ;
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL("http://api.themoviedb.org/3/movie/"+_id+"/reviews?api_key=aa4d60eb83769b263b7b00be84db5558");
                // Create the request to THEMovieDB, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    //trailerJsonString = null;
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
                    // trailerJsonString = null;
                }
                reviewJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the movie data, there's no point in attempting
                // to parse it.
                //  trailerJsonString = null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            return reviewJsonStr;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Json Parsing
            try {
                if(s!=null){
                JSONObject jsonObject=new JSONObject(s);
                JSONArray jsonArray =jsonObject.getJSONArray("results");

                for(int i=0;i<jsonArray.length();i++) {
                    ReviewData reviewData=new ReviewData();
                    JSONObject object = jsonArray.getJSONObject(i);
                    //reviewData.setId(object.getString("id"));
                    reviewData.setAuthor(object.getString("author"));
                    reviewData.setContent(object.getString("content"));
                    //reviewData.setUrl(object.getString("url"));
                    Log.i("Author in json", object.getString("author")+"");
                //    Toast.makeText(getActivity(), "if s = "+s, Toast.LENGTH_SHORT).show();
                    reviewList.add(reviewData);
                }
                }
                else
                {
                    Log.i("dataaa", "json str= "+reviewJsonStr);
                    Log.i("dataaa", "else s = "+s);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            reviewAdapter=new ReviewAdapter(getContext(),reviewList);
            reviewlistview.setAdapter(reviewAdapter);
            //reviewAdapter.notifyDataSetChanged();
        }
    }

    public class FetchTrailer extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String trailerJsonString = null;
            try {
                URL url = new URL("https://api.themoviedb.org/3/movie/"+_id+"/videos?api_key=aa4d60eb83769b263b7b00be84db5558"
                );
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    trailerJsonString = null;
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
                    trailerJsonString = null;
                }
                trailerJsonString = buffer.toString();
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                trailerJsonString = null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            return trailerJsonString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject=new JSONObject(s);
                JSONArray jsonArray=jsonObject.getJSONArray("results");
                for(int i=0;i<jsonArray.length();i++) {
                    TrailerData trailerData=new TrailerData();
                    JSONObject object=jsonArray.getJSONObject(i);
                    trailerData.setId(object.getString("id"));
                    trailerData.setKey(object.getString("key"));
                    trailerData.setName(object.getString("name"));
                    Log.e("trailer on json",object.getString("name"));
                    trailerlist.add(trailerData);
                    }
                } catch (JSONException e) {
                e.printStackTrace();
            }

            trailerAdapter=new TrailerAdapter(getContext(),trailerlist);
            trailerlistview.setAdapter(trailerAdapter);
            //trailerAdapter.notifyDataSetChanged();
        }
    }
}
