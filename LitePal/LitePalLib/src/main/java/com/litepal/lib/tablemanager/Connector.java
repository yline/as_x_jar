/*
 * Copyright (C)  Tony Green, LitePal Framework Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.litepal.lib.tablemanager;

import android.database.sqlite.SQLiteDatabase;

import com.litepal.lib.LitePalApplication;
import com.litepal.lib.exceptions.InvalidAttributesException;
import com.litepal.lib.parser.LitePalAttr;
import com.litepal.lib.parser.LitePalParser;

/**
 * The connector to connect database provided by LitePal. Users can use this
 * class to get the instance of SQLiteDatabase. But users still need to write
 * their own CRUD logic by the returned SQLiteDatabase. It will be improved in
 * the future.
 *
 * @author Tony Green
 * @since 1.0
 */
public class Connector
{

	/**
	 * LitePalAttr model.
	 */
	private static LitePalAttr mLitePalAttr;

	/**
	 * The quote of LitePalHelper.
	 */
	private static LitePalOpenHelper mLitePalHelper;

	/**
	 * Get a writable SQLiteDatabase.
	 * <p/>
	 * There're a lot of ways to operate database in android. But LitePal
	 * doesn't support using ContentProvider currently. The best way to use
	 * LitePal well is get the SQLiteDatabase instance and use the methods like
	 * SQLiteDatabase#save, SQLiteDatabase#update, SQLiteDatabase#delete,
	 * SQLiteDatabase#query in the SQLiteDatabase class to do the database
	 * operation. It will be improved in the future.
	 *
	 * @return A writable SQLiteDatabase instance
	 * @throws com.litepal.lib.exceptions.InvalidAttributesException
	 */
	public synchronized static SQLiteDatabase getWritableDatabase()
	{
		LitePalOpenHelper litePalHelper = buildConnection();
		return litePalHelper.getWritableDatabase();
	}

	/**
	 * Get a readable SQLiteDatabase.
	 * <p/>
	 * There're a lot of ways to operate database in android. But LitePal
	 * doesn't support using ContentProvider currently. The best way to use
	 * LitePal well is get the SQLiteDatabase instance and use the methods like
	 * SQLiteDatabase#query in the SQLiteDatabase class to do the database
	 * query. It will be improved in the future.
	 *
	 * @return A readable SQLiteDatabase instance.
	 * @throws com.litepal.lib.exceptions.InvalidAttributesException
	 */
	public synchronized static SQLiteDatabase getReadableDatabase()
	{
		LitePalOpenHelper litePalHelper = buildConnection();
		return litePalHelper.getReadableDatabase();
	}

	/**
	 * Call getDatabase directly will invoke the getWritableDatabase method by
	 * default.
	 * <p/>
	 * This is method is alias of getWritableDatabase.
	 *
	 * @return A writable SQLiteDatabase instance
	 * @throws com.litepal.lib.exceptions.InvalidAttributesException
	 */
	public static SQLiteDatabase getDatabase()
	{
		return getWritableDatabase();
	}

	/**
	 * Build a connection to the database. This progress will analysis the
	 * litepal.xml file, and will check if the fields in LitePalAttr are valid,
	 * and it will open a SQLiteOpenHelper to decide to create tables or update
	 * tables or doing nothing depends on the version attributes.
	 * <p/>
	 * After all the stuffs above are finished. This method will return a
	 * LitePalHelper object.Notes this method could throw a lot of exceptions.
	 *
	 * @return LitePalHelper object.
	 * @throws com.litepal.lib.exceptions.InvalidAttributesException
	 */
	private static LitePalOpenHelper buildConnection()
	{
		if (mLitePalAttr == null)
		{
			LitePalParser.parseLitePalConfiguration();
			mLitePalAttr = LitePalAttr.getInstance();
		}
		if (mLitePalAttr.checkSelfValid())
		{
			if (mLitePalHelper == null)
			{
				String dbName = mLitePalAttr.getDbName();
				if ("external".equalsIgnoreCase(mLitePalAttr.getStorage()))
				{
					dbName = LitePalApplication.getContext().getExternalFilesDir("") + "/databases/" + dbName;
				}
				mLitePalHelper = new LitePalOpenHelper(dbName, mLitePalAttr.getVersion());
			}
			return mLitePalHelper;
		}
		else
		{
			throw new InvalidAttributesException("Uncaught invalid attributes exception happened");
		}
	}

}
