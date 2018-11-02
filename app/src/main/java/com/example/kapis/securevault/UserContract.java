package com.example.kapis.securevault;

import android.provider.BaseColumns;

public class UserContract {

    private UserContract() {
    }

    public static class UserEntry implements BaseColumns {
        public static final String COL_FULL_NAME = "FirstName";
        public static final String COL_EMAIL = "Email";
        public static final String COL_PASS = "Password";
    }


    public static final String TABLE_NAME = "user_table";


    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    UserEntry._ID + " INTEGER PRIMARY KEY," +
                    UserEntry.COL_FULL_NAME + " TEXT," +
                    UserEntry.COL_EMAIL + " TEXT," +
                    UserEntry.COL_PASS + " TEXT )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
}
