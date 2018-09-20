package com.example.andersondev.tcc_novo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.renderscript.ScriptIntrinsicYuvToRGB;

public class DataOpenHelper extends SQLiteOpenHelper {

    public DataOpenHelper(Context context) {
        super(context,"Settings",null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(QueryDLL.getCreateTableClient());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
