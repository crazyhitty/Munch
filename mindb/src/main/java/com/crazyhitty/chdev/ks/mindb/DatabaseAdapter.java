package com.crazyhitty.chdev.ks.mindb;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Kartik_ch on 10/4/2015.
 */
public class DatabaseAdapter {
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDatabaseHelper;

    public DatabaseAdapter(Context context, String dataBaseName) {
        this.mContext = context;
        mDatabaseHelper = new DatabaseHelper(this.mContext, dataBaseName);
    }

    public DatabaseAdapter createDatabase() throws SQLException {
        try {
            mDatabaseHelper.createDataBase();
        } catch (IOException mIOException) {
            Log.e("Error", mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public DatabaseAdapter open() throws SQLException {
        try {
            mDatabaseHelper.openDataBase();
            mDatabaseHelper.close();
            mDatabase = mDatabaseHelper.getReadableDatabase();
        } catch (SQLException mSQLException) {
            Log.e("OpenDb", "open >>" + mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close() {
        mDatabaseHelper.close();
    }

    //queries that return the cursor data
    public Cursor selectQuery(String query) {
        Cursor cursor = null;
        try {
            if (mDatabase.isOpen()) {
                mDatabase.close();
            }
            mDatabase = mDatabaseHelper.getWritableDatabase();
            cursor = mDatabase.rawQuery(query, null);
        } catch (Exception e) {
            System.out.println("DATABASE ERROR " + e);
        }
        return cursor;
    }

    //queries that execute data changes in database
    public void executeQuery(String query) {
        try {
            if (mDatabase.isOpen()) {
                mDatabase.close();
            }
            mDatabase = mDatabaseHelper.getWritableDatabase();
            mDatabase.execSQL(query);
        } catch (Exception e) {
            System.out.println("DATABASE ERROR " + e);
        }
    }
}
