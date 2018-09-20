package com.example.andersondev.tcc_novo.database;

public class QueryDLL {

    public static String getCreateTableClient(){
        StringBuilder sql = new StringBuilder();
        sql.append(" CREATE TABLE IF NOT EXISTS Settings( ");
        sql.append("    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,");
        sql.append("    temperature DECIMAL(2,2),");
        sql.append("    bluetooth_mac VARCHAR(20) )");

        return sql.toString();
    }
}
