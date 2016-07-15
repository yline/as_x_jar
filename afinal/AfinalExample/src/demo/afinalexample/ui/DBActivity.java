package demo.afinalexample.ui;

import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalDb;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import demo.afinalexample.R;

/**
 * reference
 * http://my.oschina.net/yangfuhai/blog/87459?fromerr=Txhw3ioL
 *
 * 鍏充簬娉ㄨВ锛岄渶瑕佹參鎱㈢湅銆傚彲浠ュ弬鑰冮儹闇栫殑blog,閲岄潰鐨勪竴瀵瑰澶氬涓�鐨勭悊璁�.
 * http://blog.csdn.net/guolin_blog/article/details/39207945
 */
public class DBActivity extends FinalActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_db);

		final EditText searchKeyET = (EditText)findViewById(R.id.db_et_search_key);
		final TextView searchResultTV = (TextView)findViewById(R.id.db_tv_searchresult);

		//寤鸿〃
		findViewById(R.id.constructdb).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// default name : afinal.db
				//	FinalDb.create(DBActivity.this);

				// create the database named by author, default name : afinal.db
				FinalDb db = FinalDb.create(DBActivity.this, "user.db");
			}
		});

		//鎻掑叆鏁版嵁
		findViewById(R.id.insertdata).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// FinalDb is singleton, which won't repeat creation
				FinalDb db = FinalDb.create(DBActivity.this, "user.db");
				DBUser user = new DBUser();
				user.setName("f21");
				user.setAge(21);

				// save and kept
				db.save(user);
			}
		});

		//鍒犻櫎鏁版嵁
		findViewById(R.id.db_btn_delete).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				//	鎷垮埌FinalDb瀵硅薄寮曠敤
				FinalDb db = FinalDb.create(DBActivity.this, "user.db");

				//	db.delete(user); //鏍规嵁瀵硅薄涓婚敭杩涜鍒犻櫎				
				//	db.deleteById(user, 1); //鏍规嵁涓婚敭鍒犻櫎鏁版嵁
				//	db.deleteByWhere(User.class, "name=AoTuMan"); //鑷畾涔墂here鏉′欢鍒犻櫎
				db.deleteAll(DBUser.class); // delete all
			}
		});

		// find
		findViewById(R.id.db_btn_search).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String searchKey = searchKeyET.getText().toString();

				//鎷垮埌FinalDb瀵硅薄寮曠敤
				FinalDb db = FinalDb.create(DBActivity.this, "user.db");

				//娉ㄦ剰杩欓噷"where"璇彞鍙傛暟鏄病鏈墂here鍏抽敭瀛楃殑
				// List<DBUser> resultList = db.findAllByWhere(DBUser.class, " name=\"" + searchKey + "\"");
				List<DBUser> resultList = db.findAll(DBUser.class); // look for the record through DBUser.class
				StringBuilder _sb = new StringBuilder();
				for(int i=0;i<resultList.size();i++) {
					_sb.append("id:" + resultList.get(i).getId()
							+ " name:" + resultList.get(i).getName()
							+ " age:" + resultList.get(i).getAge() + "\n");
				}

				searchResultTV.setText(_sb.toString());
			}
		});
	}

}
