package com.example.andersondev.tcc_novo.domain.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.andersondev.tcc_novo.domain.entities.Settings;

import java.util.ArrayList;
import java.util.List;

public class SettingsRepository {

    private SQLiteDatabase conn;

    public SettingsRepository(SQLiteDatabase conn){
        this.conn = conn;
    }

    public void insert(Settings set){

        ContentValues contentValues = new ContentValues();
        contentValues.put("temperature", set.temperature);
        contentValues.put("bluetooth_mac", set.bluetooth_mac);

        conn.insertOrThrow("Settings",null,contentValues);
    }

    public void delete(int id){

        String[] param = new String[1];
        param[0] = String.valueOf(id);
        conn.delete("Settings", "id = ?",param);

    }

    public void update(Settings setting){
        ContentValues contentValues = new ContentValues();
        contentValues.put("temperature", setting.temperature);
        contentValues.put("bluetooth_mac", setting.bluetooth_mac);

        String[] param = new String[1];
        param[0] = String.valueOf(setting.id);

        conn.update("Settings", contentValues, "id = ?", param);
    }

    public List<Settings> searchAll(){

        List<Settings> settings = new ArrayList<Settings>();

        StringBuilder query = new StringBuilder();
        query.append(" SELECT * FROM Settings ");

        Cursor result = conn.rawQuery(query.toString(), null);

        if(result.getCount() > 0){
            result.moveToFirst();

            do{
                Settings set = new Settings();

                set.id = result.getInt(result.getColumnIndexOrThrow("id"));
                set.temperature = result.getFloat(result.getColumnIndexOrThrow("temperature"));
                set.bluetooth_mac = result.getString(result.getColumnIndexOrThrow("bluetooth_mac"));

                settings.add(set);
            }while(result.moveToNext());
        }
        return settings;
    }

    public Settings getSettings(int id){
        Settings set = new Settings();

        StringBuilder query = new StringBuilder();

        query.append(" SELECT * FROM settings ");
        query.append(" WHERE id = ?");

        String[] param = new String[1];
        param[0] = String.valueOf(id);

        Cursor result = conn.rawQuery(query.toString(), param);
        if(result.getCount() > 0){
            result.moveToFirst();
            set.id = result.getInt(result.getColumnIndexOrThrow("id"));
            set.temperature = result.getFloat(result.getColumnIndexOrThrow("temperature"));
            set.bluetooth_mac = result.getString(result.getColumnIndexOrThrow("bluetooth_mac"));

            return set;
        }

        return null;
    }
}
