package com.eventbase.androidcodetest;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.eventbase.androidcodetest.Database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // start fetching movies from db
        new RemoteDataTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    ProgressDialog mProgressDialog;
    private List<MovieInformation> movieInformationList = new ArrayList<MovieInformation>();

    // query the SQLLite DB for basic information about all the movies
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                DatabaseHelper DB = new DatabaseHelper(MainActivity.this);
                SQLiteDatabase sqlDB = DB.getWritableDatabase();

                Cursor cursor = sqlDB.query("movie",
                        new String[]{"uid", "name", "thumbnail_url", "short_desc"},
                        null, null, null, null, null);

                if (cursor != null) {

                    int uidIndex = cursor.getColumnIndex("uid");
                    int nameIndex = cursor.getColumnIndex("name");
                    int thumbnailURLIndex = cursor.getColumnIndex("thumbnail_url");
                    int shortDescIndex = cursor.getColumnIndex("short_desc");

                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {

                        // build the MovieInformation object based on the query results and add to List
                        MovieInformation info = new MovieInformation();
                        info.uid = cursor.getInt(uidIndex);
                        info.Name = cursor.getString(nameIndex);
                        info.ShortDesc = cursor.getString(shortDescIndex);
                        info.ThumbnailURL = cursor.getString(thumbnailURLIndex);

                        movieInformationList.add(info);

                        cursor.moveToNext();
                    }
                }
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            // get the ListView and set the adapter on it, passing in the List of MovieInformation objects
            ListView listview = (ListView) findViewById(R.id.listview);
            ListViewAdapter adapter = new ListViewAdapter(MainActivity.this, movieInformationList);

            listview.setAdapter(adapter);
        }
    }
}
