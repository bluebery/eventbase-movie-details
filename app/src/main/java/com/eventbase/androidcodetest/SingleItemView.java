package com.eventbase.androidcodetest;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.eventbase.androidcodetest.Database.DatabaseHelper;

/**
 * Created by Bluebery on 7/30/2015.
 */

public class SingleItemView extends Activity {

    private MovieInformation movieInformation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.singleitemview);

        // get the uid from the intent and start a task to query the information & download image
        Intent i = getIntent();
        new RemoteDataTask().execute(i.getIntExtra("uid", 1));
    }

    // queries the SQL lite db for the movie information about a specific movie
    private class RemoteDataTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {

            int uid = params[0];

            try {

                DatabaseHelper DB = new DatabaseHelper(SingleItemView.this);
                SQLiteDatabase sqlDB = DB.getWritableDatabase();

                Cursor cursor = sqlDB.query("movie",
                        new String[]{"name", "duration", "image_url", "desc"},
                        "uid=" + uid, null, null, null, null);

                if (cursor != null) {

                    int nameIndex = cursor.getColumnIndex("name");
                    int durationIndex = cursor.getColumnIndex("duration");
                    int imageURLIndex = cursor.getColumnIndex("image_url");
                    int descIndex = cursor.getColumnIndex("desc");

                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {

                        movieInformation = new MovieInformation();
                        movieInformation.Name = cursor.getString(nameIndex);
                        movieInformation.Duration = cursor.getInt(durationIndex);
                        movieInformation.Desc = cursor.getString(descIndex);
                        movieInformation.ImageURL = cursor.getString(imageURLIndex);

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

            ((TextView)findViewById(R.id.name)).setText(movieInformation.Name);
            ((TextView)findViewById(R.id.duration)).setText(movieInformation.Duration + " minutes");
            ((TextView)findViewById(R.id.description)).setText(movieInformation.Desc);

            // asks the ImageLoader to render the bitmap found at the given url in the given ImageView (async)
            new ImageLoader(SingleItemView.this).DisplayImage(movieInformation.ImageURL, (ImageView) findViewById(R.id.image), (ProgressBar) findViewById(R.id.progress));
        }
    }
}
