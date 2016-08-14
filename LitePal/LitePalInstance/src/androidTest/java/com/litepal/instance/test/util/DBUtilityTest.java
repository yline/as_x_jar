package com.litepal.instance.test.util;


import android.database.sqlite.SQLiteDatabase;

import com.litepal.instance.model.Book;
import com.litepal.instance.model.Cellphone;
import com.litepal.instance.test.LitePalTestCase;
import com.litepal.lib.tablemanager.Connector;
import com.litepal.lib.util.DBUtility;

import java.util.List;

public class DBUtilityTest extends LitePalTestCase
{

	SQLiteDatabase db;

	@Override
	protected void setUp() throws Exception
	{
		db = Connector.getDatabase();
	}

	public void testFindUniqueColumns()
	{
		List<String> uniqueColumns = DBUtility.findUniqueColumns(DBUtility.getTableNameByClassName(Cellphone.class.getName()), db);
		assertEquals(1, uniqueColumns.size());
		assertTrue(uniqueColumns.contains("serial"));
		uniqueColumns = DBUtility.findUniqueColumns(DBUtility.getTableNameByClassName(Book.class.getName()), db);
		assertEquals(0, uniqueColumns.size());
	}

}
