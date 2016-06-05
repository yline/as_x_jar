package com.android.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * 仅测试使用,功能不完善
 * @author YLine
 * @2016年6月5日 @上午9:18:55
 */
public class HistoryProvider extends ContentProvider
{
    public static final String AUTHORIY = "com.android.contentprovider";
    
    private static final String CONTENT = "content://";
    
    /**
     * 该ContentProvider所返回的数据类型定义,数据集合
     */
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORIY;
    
    /**
     * 单项数据
     */
    public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/vnd." + AUTHORIY;
    
    /**
     * 数据集合操作时的URI
     */
    public static final Uri HISTORY_URI = Uri.parse(CONTENT + AUTHORIY + "/" + DbHelper.TABLE_NAME);
    
    // 操作分类
    static final int HISTORY = 1;
    
    static final int HISTORY_ITEM = 2;
    
    static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    
    private SQLiteDatabase mDatabase;
    
    static
    {
        uriMatcher.addURI(AUTHORIY, "history", HISTORY);
        uriMatcher.addURI(AUTHORIY, "history/*", HISTORY_ITEM);
    }
    
    @Override
    public boolean onCreate()
    {
        mDatabase = new DbHelper(getContext()).getWritableDatabase();
        return true;
    }
    
    /**
     * @return The URI for the newly inserted item if success; null if failed 
     */
    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        long newId = mDatabase.insert(DbHelper.TABLE_NAME, null, values);
        if (newId > 0)
        {
            Uri returnUri = Uri.parse(CONTENT + AUTHORIY + "/" + DbHelper.TABLE_NAME + "/" + newId);
            return returnUri;
        }
        return null;
    }
    
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        return 0;
    }
    
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        return 0;
    }
    
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        Cursor cursor = null;
        switch (uriMatcher.match(uri))
        {
            case HISTORY:
                cursor =
                    mDatabase.query(DbHelper.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case HISTORY_ITEM:
                String formula = uri.getPathSegments().get(1);
                cursor = mDatabase.query(DbHelper.TABLE_NAME,
                    projection,
                    "formula = ?",
                    new String[] {formula},
                    null,
                    null,
                    sortOrder);
                break;
            default:
                break;
        }
        return cursor;
    }
    
    @Override
    public String getType(Uri uri)
    {
        switch (uriMatcher.match(uri))
        {
            case HISTORY:
                return CONTENT_TYPE;
            case HISTORY_ITEM:
                return CONTENT_TYPE_ITEM;
            default:
                break;
        }
        return null;
    }
    
}
