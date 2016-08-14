package com.litepal.instance.test.crud.query;


import android.database.Cursor;
import android.test.AndroidTestCase;

import com.litepal.instance.model.Book;
import com.litepal.lib.crud.DataSupport;
import com.litepal.lib.exceptions.DataSupportException;
import com.litepal.lib.util.DBUtility;

public class QueryBySQLTest extends AndroidTestCase
{

	private Book book;

	private String bookTable;

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		bookTable = DBUtility.getTableNameByClassName(Book.class.getName());
		book = new Book();
		book.setBookName("数据库");
		book.setPages(300);
		book.save();
	}

	public void testQueryBySQL()
	{
		Cursor cursor = DataSupport.findBySQL("select * from " + bookTable);
		assertTrue(cursor.getCount() > 0);
		cursor.close();
	}

	public void testQueryBySQLWithPlaceHolder()
	{
		Cursor cursor = DataSupport.findBySQL("select * from " + bookTable + " where id=? and bookname=? and pages=?", String.valueOf(book.getId()), "数据库", "300");
		assertTrue(cursor.getCount() == 1);
		cursor.moveToFirst();
		String bookName = cursor.getString(cursor.getColumnIndexOrThrow("bookname"));
		int pages = cursor.getInt(cursor.getColumnIndexOrThrow("pages"));
		assertEquals(bookName, "数据库");
		assertEquals(pages, 300);
		cursor.close();
	}

	public void testQueryBySQLWithWrongParams()
	{
		try
		{
			DataSupport.findBySQL("select * from " + bookTable + " where id=? and bookname=? and pages=?", String.valueOf(book.getId()), "数据库");
			fail();
		}
		catch (DataSupportException e)
		{
			assertEquals("The parameters in conditions are incorrect.", e.getMessage());
		}
		Cursor cursor = DataSupport.findBySQL(new String[]{});
		assertNull(cursor);
		cursor = DataSupport.findBySQL();
		assertNull(cursor);
	}

}
