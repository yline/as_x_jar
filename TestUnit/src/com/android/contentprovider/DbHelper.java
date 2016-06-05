package com.android.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper
{
    /**
     * 数据表名
     */
    public static final String TABLE_NAME = "history";
    /**
     * id行
     */
    public static final String FORMULA_COLUMN = "formula";
    /**
     * 数据行,result
     */
    public static final String RESULT_COLUMN = "result";
    
    private static final String DB_NAME = "history.db"; // 数据库名字
    
    private static final int DB_VERSION = 1; // 数据库版本
    
    private static final String HISTORY_TABLE_SQL = "create table " + TABLE_NAME + " ( " + FORMULA_COLUMN
        + " text primary key, " + RESULT_COLUMN + " integer , unique( " + FORMULA_COLUMN + " ) " + ")";
    
    public DbHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(HISTORY_TABLE_SQL);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        
    }
    
}
