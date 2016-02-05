package com.crazyhitty.chdev.ks.mindb;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kartik_ch on 8/29/2015.
 */
public class DatabaseOperations {
    //String title, url, body;
    private Context mContext;
    private String mDatabaseName;

    public DatabaseOperations(Context context, String databaseName) {
        this.mContext = context;
        this.mDatabaseName = databaseName;
    }

    public void saveDataInDB(String tableName, String[] columnNames, String[] values) throws Exception {
        DbOperationsAsync dbOperationsAsync = new DbOperationsAsync("save", tableName, null, null, columnNames, values, null);
        dbOperationsAsync.execute();
    }

    public List<String> retrieveFromDB(String tableName, String columnName) throws Exception {
        return retrieveFromDBAsync(tableName, columnName);
    }

    public Cursor retrieveFromDB(String tableName, String[] columnNames) throws Exception {
        return retrieveFromDBAsync(tableName, columnNames);
    }

    public Cursor retrieveFromDBCondition(String tableName, String[] columnNames, String condition) throws Exception {
        return retrieveFromDBConditionAsync(tableName, columnNames, condition);
    }

    public Cursor retrieveAllFromDB(String tableName) throws Exception {
        return retrieveAllFromDBAsync(tableName);
    }

    public Cursor retrieveAllFromDBCondition(String tableName, String condition) throws Exception {
        return retrieveAllFromDBConditionAsync(tableName, condition);
    }

    public void deleteFromDB(String tableName, String columnName, String value) throws Exception {
        deleteFromDBAsync(tableName, columnName, value);
    }

    public void deleteAllFromDB(String tableName) throws Exception {
        deleteAllFromDBAsync(tableName);
    }

    public void editDataInDB(String tableName, String[] columnNames, String[] values, String condition) throws Exception {
        DatabaseAdapter dbAdapter = new DatabaseAdapter(mContext, mDatabaseName);
        dbAdapter.createDatabase();
        dbAdapter.open();

        String updatedValues = "";

        for (int i = 0; i < columnNames.length; i++) {
            updatedValues += columnNames[i] + "='" + values[i].replace("'", "''") + "',";
        }

        updatedValues = updatedValues.substring(0, updatedValues.length() - 1);
        String query = "UPDATE " + tableName + " SET " + updatedValues + " " + condition;
        dbAdapter.executeQuery(query);
        dbAdapter.close();

        Log.e("DATABASE EDIT", query);
    }

    private void saveDataInDBAsync(String tableName, String[] columnNames, String[] values) throws Exception {
        DatabaseAdapter dbAdapter = new DatabaseAdapter(mContext, mDatabaseName);
        dbAdapter.createDatabase();
        dbAdapter.open();
        String queryColumns = "";
        String queryValues = "";
        for (String value : columnNames) {
            queryColumns += value + ",";
        }
        for (String value : values) {
            // ' (single apostrophe) doesn't work with sqlite database in insertion, instead of it, use ''(double apostrophe).
            //tldr : store ' as '' otherwise it won't work
            queryValues += "'" + value.replace("'", "''") + "',";
        }
        queryColumns = queryColumns.substring(0, queryColumns.length() - 1);
        queryValues = queryValues.substring(0, queryValues.length() - 1);
        String query = "INSERT INTO " + tableName + " (" + queryColumns + ") " +
                "VALUES (" + queryValues + ")";
        dbAdapter.executeQuery(query);
        dbAdapter.close();
        Log.e("DATABASE", "VALUES SAVED INTO DB");
    }

    private List<String> retrieveFromDBAsync(String tableName, String columnName) throws Exception {
        List<String> values = new ArrayList<>();
        DatabaseAdapter dbAdapter = new DatabaseAdapter(mContext, mDatabaseName);
        dbAdapter.createDatabase();
        dbAdapter.open();
        String query = "SELECT " + columnName + " FROM " + tableName;
        Cursor cursor = dbAdapter.selectQuery(query);
        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    values.add(cursor.getString(cursor.getColumnIndex(columnName)));
                } while (cursor.moveToNext());
            }
        }
        dbAdapter.close();
        Log.e("DATABASE", "VALUES RETRIEVED FROM DB");
        return values;
    }

    private Cursor retrieveFromDBAsync(String tableName, String[] columnNames) throws Exception {
        String queryColumns = "";
        for (String value : columnNames) {
            queryColumns += value + ",";
        }
        queryColumns = queryColumns.substring(0, queryColumns.length() - 1);

        DatabaseAdapter dbAdapter = new DatabaseAdapter(mContext, mDatabaseName);
        dbAdapter.createDatabase();
        dbAdapter.open();
        String query = "SELECT " + queryColumns + " FROM " + tableName;
        Cursor cursor = dbAdapter.selectQuery(query);
        Log.e("DATABASE", "SELECTED CURSOR RETRIEVED FROM DB");
        return cursor;
    }

    private List<String> retrieveFromDBConditionAsync(String tableName, String columnName, String condition) throws Exception {
        List<String> values = new ArrayList<>();
        DatabaseAdapter dbAdapter = new DatabaseAdapter(mContext, mDatabaseName);
        dbAdapter.createDatabase();
        dbAdapter.open();
        String query = "SELECT " + columnName + " FROM " + tableName + " " + condition;
        Cursor cursor = dbAdapter.selectQuery(query);
        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    values.add(cursor.getString(cursor.getColumnIndex(columnName)));
                } while (cursor.moveToNext());
            }
        }
        dbAdapter.close();
        Log.e("DATABASE", "VALUES RETRIEVED FROM DB");
        return values;
    }

    private Cursor retrieveFromDBConditionAsync(String tableName, String[] columnNames, String condition) throws Exception {
        String queryColumns = "";
        for (String value : columnNames) {
            queryColumns += value + ",";
        }
        queryColumns = queryColumns.substring(0, queryColumns.length() - 1);

        DatabaseAdapter dbAdapter = new DatabaseAdapter(mContext, mDatabaseName);
        dbAdapter.createDatabase();
        dbAdapter.open();
        String query = "SELECT " + queryColumns + " FROM " + tableName + " " + condition;
        Cursor cursor = dbAdapter.selectQuery(query);
        Log.e("DATABASE", "SELECTED CURSOR RETRIEVED FROM DB");
        return cursor;
    }

    private Cursor retrieveAllFromDBAsync(String tableName) throws Exception {
        DatabaseAdapter dbAdapter = new DatabaseAdapter(mContext, mDatabaseName);
        dbAdapter.createDatabase();
        dbAdapter.open();
        String query = "SELECT * FROM " + tableName;
        Cursor cursor = dbAdapter.selectQuery(query);
        Log.e("DATABASE", "CURSOR RETRIEVED FROM DB");
        return cursor;
    }

    private Cursor retrieveAllFromDBConditionAsync(String tableName, String condition) {
        DatabaseAdapter dbAdapter = new DatabaseAdapter(mContext, mDatabaseName);
        dbAdapter.createDatabase();
        dbAdapter.open();
        String query = "SELECT * FROM " + tableName + " " + condition;
        Cursor cursor = dbAdapter.selectQuery(query);
        Log.e("DATABASE", "CURSOR RETRIEVED FROM DB");
        return cursor;
    }

    private void deleteFromDBAsync(String tableName, String columnName, String value) throws Exception {
        DatabaseAdapter dbAdapter = new DatabaseAdapter(mContext, mDatabaseName);
        dbAdapter.createDatabase();
        dbAdapter.open();
        String query = "DELETE FROM " + tableName + " " +
                "WHERE " + columnName + "='" + value + "'";
        dbAdapter.executeQuery(query);
        dbAdapter.close();
        Log.e("DATABASE DELETE", query);
    }

    private void deleteAllFromDBAsync(String tableName) throws Exception {
        DatabaseAdapter dbAdapter = new DatabaseAdapter(mContext, mDatabaseName);
        dbAdapter.createDatabase();
        dbAdapter.open();
        String query = "DELETE FROM " + tableName + "";
        dbAdapter.executeQuery(query);
        dbAdapter.close();
        Log.e("DATABASE", "ALL VALUES DELETED FROM DB");
    }

    public class DbOperationsAsync extends AsyncTask<String, Integer, String> {

        private String mOperation;
        private String mTableName, mColumnName, mValue, mCondition;
        private String[] mColumnNames, mValues;

        public DbOperationsAsync(String mOperation, String mTableName, String mColumnName, String mValue, String[] mColumnNames, String[] mValues, String mCondition) {
            this.mOperation = mOperation;
            this.mTableName = mTableName;
            this.mColumnName = mColumnName;
            this.mValue = mValue;
            this.mColumnNames = mColumnNames;
            this.mValues = mValues;
            this.mCondition = mCondition;
        }

        @Override
        protected String doInBackground(String... strings) {
            switch (mOperation) {
                case "save":
                    try {
                        saveDataInDBAsync(mTableName, mColumnNames, mValues);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "retrieve_list":
                    try {
                        retrieveFromDBAsync(mTableName, mColumnName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "retrieve_cursor":
                    try {
                        retrieveFromDBAsync(mTableName, mColumnNames);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "retrieve_list_condition":
                    try {
                        retrieveFromDBConditionAsync(mTableName, mColumnName, mCondition);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "retrieve_cursor_condition":
                    try {
                        retrieveFromDBConditionAsync(mTableName, mColumnNames, mCondition);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "retrieve_all_cursor":
                    try {
                        retrieveAllFromDBAsync(mTableName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "delete":
                    try {
                        deleteFromDBAsync(mTableName, mColumnName, mValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "delete_all":
                    try {
                        deleteAllFromDBAsync(mTableName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
            return "success";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("success")) {
                //
            }
        }
    }
}
