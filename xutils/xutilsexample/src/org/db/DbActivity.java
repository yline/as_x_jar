package org.db;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.base.BaseActivity;
import org.xutils.DbManager;
import org.xutils.x;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import f21.xutilsexample.R;

//一对多: (本示例的代码)
// 自己在多的一方(child)保存另一方的(parentId), 查找的时候用parentId查parent或child.

// 一对一:
// 在任何一边保存另一边的Id并加上唯一属性: @Column(name = "parentId", property = "UNIQUE")

// 多对多:
// 再建一个关联表, 保存两边的id. 查询分两步: 先查关联表得到id, 再查对应表的属性.
@ContentView(R.layout.activity_db)
public class DbActivity extends BaseActivity{
	private DbManager.DaoConfig daoConfig;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		daoConfig = new DbManager.DaoConfig();  // 可连缀
		daoConfig.setDbName("test");
		daoConfig.setDbDir(new File("/sdcard"));
		daoConfig.setDbVersion(1);
		daoConfig.setDbUpgradeListener(new DbManager.DbUpgradeListener() {
			@Override
			public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
				// TODO-> ...
				// db.addColumn(...);
				// db.dropTable(...);
				// ...
			}
		});
	}

	@ViewInject(R.id.tv_db_result)
	private TextView tv_db_result;  // 显示

	@Event(R.id.btn_test_db)  // 一对多，，，，本例是 一个Child 对应 name、id、parentId(映射到Parent)  db.saveBindingId(child);
	private void onTextDbClick(View view) {
		String temp = "";
		DbManager db = x.getDb(daoConfig);

		Child child = new Child();
		child.name = "childs name";

		///////////////////////////////////////parents  ->  child
		Parent test = null;
		/**
		 * "id"  Child 中的
		 * "in"	 operator-> "=", "LIKE","IN","BETWEEN"...
		 * value 参数
		 */
		try {
			test = db.selector(Parent.class).where("id", "In", new int[]{1, 3, 6}).findFirst();
		} catch (DbException e) {
			e.printStackTrace();
		}

		if (null != test) {
			child.parentId = test.getId();
			temp += "first parent temp" + test + "\n";
			tv_db_result.setText(temp);
		}

		//////////////////////////////////////////parents, 除了id其它全配置了，注意name为public类型
		Parent parent = new Parent();
		parent.name = "test" + System.currentTimeMillis();
		parent.setAdmin(true);
		parent.setEmail("657371126@qq.com");
		parent.setTime(new Date());
		parent.setDate(new java.sql.Date(new Date().getTime()));
		try {
			db.save(parent);
		} catch (DbException e) {
			e.printStackTrace();
		}

		///////////////////////////////////////////parents  ->  child
		try {
			db.saveBindingId(child);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}


	@Event(R.id.btn_selectorparentsome)
	private void DbTestSelectorParentSomeClick(View view) {
		tv_db_result.setText("");
		DbManager db = x.getDb(daoConfig);

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		calendar.add(Calendar.HOUR, 3);

		try {
			List<Parent> list = db.selector(Parent.class)  // id 小于54的 10个数
					.where("id", "<", 54)
					.and("time", ">", calendar.getTime())
					.orderBy("id")
					.limit(10).findAll();

			//	List<DbModel> dbModels = db.selector(Parent.class)
			//		.groupBy("name")
			//		.select("name", "count(name) as count").findAll();

			tv_db_result.append("FindSome + size = " + list.size() + "\n");
			// find
			for (Parent parent : list) {
				tv_db_result.append("parent.name->" + parent.name + "  \t");
				tv_db_result.append("parent.getId()->" + parent.getId() + "  \t");
				tv_db_result.append("parent.getEmail()->" + parent.getEmail() + "  \t");
				tv_db_result.append("parent.getDate()->" + parent.getDate() + "  \t");
				tv_db_result.append("parent.getTime()->" + parent.getTime() + "  \t");
				tv_db_result.append("\n");
			}
			tv_db_result.append("\n");
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	@Event(R.id.btn_findall)
	private void DbTestFindAllClick(View view) {
		tv_db_result.setText("");
		DbManager db = x.getDb(daoConfig);

		try {
			tv_db_result.append("childs" + "\n");
			List<Child> childs = db.findAll(Child.class);

			//	parent.name = "hahaha123";
			//	db.update(parent);
			//	Parent parent = db.findById(Parent.class, childs.get(0).parentId);

			// find
			for (Child child : childs) {
				tv_db_result.append("child.getId()->" + child.getId() + "  \t");
				tv_db_result.append("child.parentId->" + child.parentId + "  \t");
				tv_db_result.append("child.name->" + child.name + "  \t");
				tv_db_result.append("child.getEmail()->" + child.getEmail() + "  \t");
				tv_db_result.append("child.getText()->" + child.getText() + "  \t");
				tv_db_result.append("\n");
			}
			tv_db_result.append("\n");
		} catch (DbException e) {
			e.printStackTrace();
		}

		try {
			tv_db_result.append("parents" + "\n");
			List<Parent> parents = db.findAll(Parent.class);
			// find
			for (Parent parent : parents) {
				tv_db_result.append("parent.name->" + parent.name + "  \t");
				tv_db_result.append("parent.getId()->" + parent.getId() + "  \t");
				tv_db_result.append("parent.getEmail()->" + parent.getEmail() + "  \t");
				tv_db_result.append("parent.getDate()->" + parent.getDate() + "  \t");
				tv_db_result.append("parent.getTime()->" + parent.getTime() + "  \t");
				tv_db_result.append("\n");
			}
			tv_db_result.append("\n");
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	@Event(R.id.btn_delete_parent_all)
	private void DbTestDeleteParentAllClick(View view){
		DbManager db = x.getDb(daoConfig);
		try {
			db.delete(Parent.class);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	@Event(R.id.btn_delete_childall)
	private void DbTestDeleteChildAllClick(View view){
		DbManager db = x.getDb(daoConfig);
		try {
			db.delete(Child.class);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
}











































