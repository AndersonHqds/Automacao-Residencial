package com.example.andersondev.tcc_novo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;


public class BancoDados extends SQLiteOpenHelper {

    //TABLE SETTINGS
    private static final int DB_VERSION = 1;
    private static final String DB_SMARTHOUSE = "db_smarthouse";
    private static final String TB_SETTINGS = "tb_settings";
    private static final String COL_ID = "id";
    private static final String COL_BLUETOOTH_MAC = "bluetooth_mac";
    private static final String COL_TEMPERATURE = "temperature";
    private static final String COL_ISINHOME = "isinhome";

    //TABLE CONSUMO
    private static final String TB_CONSUMO = "tb_consumo";
    private static final String COL_CONSUMO_ID = "id";
    private static final String COL_TIPO = "tipo";
    private static final String COL_DATA = "data";
    private static final String COL_GASTO = "gastototal";
    private static final String COL_LITROS = "litros";
    private Context context;
    public BancoDados(Context context) {
        super(context, DB_SMARTHOUSE, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String COL_QUERY = "CREATE TABLE " + TB_SETTINGS + " ("
                +  COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_BLUETOOTH_MAC + " TEXT," +
                COL_TEMPERATURE + " DECIMAL(10,2), " + COL_ISINHOME + " BOOLEAN ); ";
        String COL_QUERY2 = "CREATE TABLE " + TB_CONSUMO + " (" + COL_CONSUMO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TIPO +" TINYINT, " + COL_DATA + " DATE, " + COL_GASTO + " DECIMAL(10,2))";
        Log.d("BANCO", COL_QUERY);
        db.execSQL(COL_QUERY);
        db.execSQL(COL_QUERY2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    void addSetting(Settings set){
        long resultado;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COL_BLUETOOTH_MAC, set.getBluetooth_mac());
        values.put(COL_TEMPERATURE, 33.3);

        resultado = db.insert(TB_SETTINGS, null, values);

        db.close();
        if(resultado == -1)
            Toast.makeText(context, "Erro ao inserir", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Inserido", Toast.LENGTH_SHORT).show();
    }

    void updateSetting(Settings set){
        long resultado;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_ID, set.getId());
        values.put(COL_BLUETOOTH_MAC, set.getBluetooth_mac());
        values.put(COL_TEMPERATURE, set.getTemperature());
        values.put(COL_ISINHOME, set.isInHome);
        resultado = db.update(TB_SETTINGS, values, "id=" + set.getId(), null);

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

    void addConsumo(Consumo cons){
        long resultado;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COL_TIPO, cons.tipo);
        values.put(COL_DATA, cons.data);
        values.put(COL_GASTO, cons.gastoTotal);

        resultado = db.insert(TB_CONSUMO, null, values);

        db.close();
        if(resultado == -1)
            Toast.makeText(context, "Erro ao inserir", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Inserido", Toast.LENGTH_SHORT).show();
    }

    Cursor getConsumo(int month, int tipo){

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT DISTINCT SUM(gastototal) as total, strftime('%d', data) as dia FROM " + TB_CONSUMO + " WHERE strftime('%m', data) = '" + month + "' AND tipo = " + tipo + " GROUP BY dia";

        Cursor cursor = db.rawQuery(query, null);
        Log.v("Cursor OBJ", DatabaseUtils.dumpCursorToString(cursor));

        return cursor;
    }


    boolean hasRowSetting(){
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
