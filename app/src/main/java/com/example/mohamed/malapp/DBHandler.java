package com.example.mohamed.malapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by mohamed on 11/27/16.
 */
public class DBHandler extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "movie.db";
    public static final String TABLE_FAVOURITE = "favourites";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_POSTER_PATH = "posterpath";
    public static final String COLUMN_TITLE = "name";
    public static final String COLUMN_RELEASEDATE = "year";
    public static final String COLUMN_RATE = "rate";
    public static final String COLUMN_OVERVIEW = "view";



    public DBHandler(Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + TABLE_FAVOURITE + " ( " +
                COLUMN_ID + " TEXT PRIMARY KEY ," +
                COLUMN_TITLE + " TEXT ," +
                COLUMN_POSTER_PATH + " TEXT ," +
                COLUMN_RATE + " TEXT ," +
                COLUMN_RELEASEDATE + " TEXT ,"+
                COLUMN_OVERVIEW + " TEXT )";

        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_FAVOURITE);
        onCreate(db);
    }
    public void addMovie(movie movie){
        ContentValues values=   new ContentValues();
        values.put(COLUMN_ID,movie.getId());
        values.put(COLUMN_TITLE,movie.getTitle());
        values.put(COLUMN_POSTER_PATH,movie.getPoster_path());
        values.put(COLUMN_RATE,movie.getVote_average());
        values.put(COLUMN_RELEASEDATE,movie.getRelease_date());
        SQLiteDatabase db=getWritableDatabase();
        db.insert(TABLE_FAVOURITE,null,values);
        db.close();
    }

    public ArrayList<movie> getAllMovies(){
        ArrayList<movie> offlinemovies =new ArrayList<>();
        String myQuery = "SELECT  * FROM " + TABLE_FAVOURITE;
        SQLiteDatabase db=this.getReadableDatabase();

        Cursor cursor=db.rawQuery(myQuery,null);

        if(cursor!=null){
            if(cursor.moveToFirst())
            {
                do {
                    movie  offlinemovie=new movie();
                    offlinemovie.setId(cursor.getString(0));
                    offlinemovie.setTitle(cursor.getString(1));
                    offlinemovie.setPoster_path(cursor.getString(2));
                    offlinemovie.setVote_average(cursor.getString(3));
                    offlinemovie.setRelease_date(cursor.getString(4));
                    offlinemovie.setOverview(cursor.getString(5));
                    offlinemovies.add(offlinemovie);
                }
                while (cursor.moveToNext());
            }
        }
        return offlinemovies;

    }
}
