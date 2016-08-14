package com.litepal.instance.test.crud.save;


import com.litepal.instance.model.IdCard;
import com.litepal.instance.model.Teacher;
import com.litepal.instance.test.LitePalTestCase;

import junit.framework.Assert;

public class One2OneUniSaveTest extends LitePalTestCase
{

	private Teacher t;

	private IdCard i;

	private void init()
	{
		t = new Teacher();
		t.setTeacherName("Will");
		t.setTeachYears(10);
		t.setAge(40);
		i = new IdCard();
		i.setNumber("9997777121");
		i.setAddress("shanghai road");
	}

	public void testSaveIdCardFirst()
	{
		init();
		t.setIdCard(i);
		i.save();
		t.save();
		assertFK(t, i);
	}

	public void testSaveTeacherFirst()
	{
		init();
		t.setIdCard(i);
		t.save();
		i.save();
		assertFK(t, i);
	}

	public void testBuildNullAssociations()
	{
		init();
		t.setIdCard(null);
		t.save();
		i.save();
		isDataExists(getTableName(t), t.getId());
		isDataExists(getTableName(i), i.getId());
	}

	public void testSaveFast()
	{
		init();
		t.setIdCard(i);
		t.saveFast();
		i.saveFast();
		assertFalse(isFKInsertCorrect(getTableName(t), getTableName(i), t.getId(), i.getId()));
	}

	private void assertFK(Teacher t, IdCard i)
	{
		Assert.assertTrue(isFKInsertCorrect(getTableName(t), getTableName(i), t.getId(), i.getId()));
	}

}
