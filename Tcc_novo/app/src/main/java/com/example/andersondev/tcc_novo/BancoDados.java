package com.example.andersondev.tcc_novo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;


public class BancoDados extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_SETTINGS = "db_setting";

    private static final String TB_SETTINGS = "tb_settings";

    private static final String COL_ID = "id";
    private static final String COL_BLUETOOTH_MAC = "bluetooth_mac";
    private static final String COL_TEMPERATURE = "temperature";
    private Context context;
    public BancoDados(Context context) {
        super(context, DB_SETTINGS, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String COL_QUERY = "CREATE TABLE " + TB_SETTINGS + " ("
                +  COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_BLUETOOTH_MAC + " TEXT," +
                COL_TEMPERATURE + " DECIMAL(10,2));";
        Log.d("BANCO", COL_QUERY);
        db.execSQL(COL_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    void addSetting(Settings set){
        long resultado;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COL_BLUETOOTH_MAC, "FC:A8:9A:00:20:BA");
        values.put(COL_TEMPERATURE, 33.3);

        resultado = db.insert(TB_SETTINGS, null, values);

        db.close();
        if(resultado == -1)
            Toast.makeText(context, "Erro ao inserir", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Inserido", Toast.LENGTH_SHORT).show();
    }

    Cursor getSetting(){

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TB_SETTINGS;

        Cursor cursor = db.rawQuery(query, null);
        Log.d("BANCO GET", "WORKOU");
        if(cursor.moveToFirst()){

            return cursor;
        }

        return cursor;
    }

    boolean hasRow(){
        SQLiteDatabase db = this.getReadableDatabase();
        boolean aux = false;

        String query = "SELECT * FROM " + TB_SETTINGS;

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            if(cursor.getCount() > 0){
                aux = true;
            }
        }
        cursor.close();
        db.close();

        return aux;
    }


}
