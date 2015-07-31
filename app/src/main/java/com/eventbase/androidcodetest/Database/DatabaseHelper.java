package com.eventbase.androidcodetest.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by brandon on 14/05/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String DB_NAME = "EventBase.sqlite";
    private final File dbFile;
    private final Context mContext;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        mContext = context;
        dbFile = context.getDatabasePath(DB_NAME);
        if (noDatabaseAvailable()) {
            copyDatabase();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    private void copyDatabase() {
        try {
            //Create db file
            this.getReadableDatabase();

            InputStream databaseInStream = mContext.getAssets().open(DB_NAME);
            OutputStream databaseOutStream = new FileOutputStream(dbFile.getPath());

            byte[] buffer = new byte[1024];
            int length;
            while ((length = databaseInStream.read(buffer)) > 0) {
                databaseOutStream.write(buffer, 0, length);
            }

            databaseOutStream.flush();
            databaseOutStream.close();
            databaseInStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if no database has been created
     *
     * @return true if no database exists yet
     */
    private boolean noDatabaseAvailable() {
        return !dbFile.exists();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
