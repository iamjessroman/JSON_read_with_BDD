package com.example.jessi.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jessi on 14/1/2018.
 */

public class SQL extends SQLiteOpenHelper {
    public SQL(Context context) {
        super(context, "test_android.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase database) {
        String sql = "create table problemas (id integer primary key, respuesta_erronea3 text, " +
                "respuesta_erronea2 text, respuesta_erronea1 text, respuesta_correcta text, lon text, lat text, " +
                "enunciado text, category_id integer);";
        database.execSQL(sql);//ejecuta la instruccion SQL en la base
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS problemas");
        onCreate(database);
    }

}

