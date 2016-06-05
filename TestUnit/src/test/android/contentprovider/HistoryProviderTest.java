package test.android.contentprovider;

import com.android.contentprovider.DbHelper;
import com.android.contentprovider.HistoryProvider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.test.ProviderTestCase2;

public class HistoryProviderTest extends ProviderTestCase2<HistoryProvider>
{
    
    public HistoryProviderTest()
    {
        super(HistoryProvider.class, HistoryProvider.AUTHORIY);
    }
    
    public HistoryProviderTest(Class<HistoryProvider> providerClass, String providerAuthority)
    {
        super(providerClass, providerAuthority);
    }
    
    public void testInsertOneItem()
    {
        ContentResolver contentResolver = getMockContentResolver();
        // 插入数据之前size = 0
        Cursor cursor = contentResolver.query(HistoryProvider.HISTORY_URI, null, null, null, null);
        assertEquals(0, cursor.getCount());
        cursor.close();
        
        // 插入一条数据
        final String formula = "3+5";
        final int result = 8;
        
        ContentValues values = new ContentValues();
        values.put(DbHelper.FORMULA_COLUMN, formula);
        values.put(DbHelper.RESULT_COLUMN, result);
        // 插入数据
        contentResolver.insert(HistoryProvider.HISTORY_URI, values);
        
        // 验证是否插入成功
        cursor = contentResolver.query(HistoryProvider.HISTORY_URI, null, null, null, null);
        assertEquals(1, cursor.getCount());
        
        // 移动到第一项
        cursor.moveToFirst();
        
        // 检测数据
        assertEquals(formula, cursor.getString(0));
        assertEquals(result, cursor.getInt(1));
        
        // 关闭光标
        cursor.close();
    }
}
