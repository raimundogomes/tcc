package br.ufrn.imd.sgr.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;

/**
 * Created by netou on 07/11/2016.
 */

public class BaseDao {

    protected SQLiteDatabase database;

    protected static BaseDao instance;

    public static SimpleDateFormat FORMATE_DATE = new SimpleDateFormat(
            "dd/MM/yyyy - HH:mm:ss");

    protected BaseDao() {
    }

    public static BaseDao getInstance(Context context) {

        if (instance == null) {

            instance = new BaseDao(context);
        }

        return instance;
    }

    private BaseDao(Context context) {
        SgrBD helper = new SgrBD(context);
        database = helper.getWritableDatabase();
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }
}
