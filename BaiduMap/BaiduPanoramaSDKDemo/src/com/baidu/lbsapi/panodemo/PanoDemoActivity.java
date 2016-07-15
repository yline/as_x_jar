package com.baidu.lbsapi.panodemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 */
public class PanoDemoActivity extends Activity {

    public static final int PID = 0;// PID方式
    public static final int GEO = 1;// 经纬度方式
    public static final int MERCATOR = 2;// 墨卡托方式
    public static final int UID_STREET = 3;// UID方式展示外景
    public static final int UID_INTERIOR = 4;// UID方式展示内景
    public static final int MARKER = 5;// 标注
    public static final int COORDINATE_CONVERTER = 6;// 坐标转换测试
    public static final int OTHER = 7;// 其他测试

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.panodemo);

        ListView mListView = (ListView) findViewById(R.id.panodemo_list);
        // 添加ListItem，设置事件响应
        mListView.setAdapter(new DemoListAdapter());
        mListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int index, long arg3) {
                onListItemClick(index);
            }
        });
    }

    private void onListItemClick(int index) {
        Intent intent = null;
        intent = new Intent(PanoDemoActivity.this, demos[index].demoClass);
        intent.putExtra("type", demos[index].type);
        this.startActivity(intent);
    }

    private static final DemoInfo[] demos = {
            new DemoInfo(PID, R.string.demo_title_panorama, R.string.demo_desc_panorama0,
                    PanoDemoMain.class),
            new DemoInfo(GEO, R.string.demo_title_panorama, R.string.demo_desc_panorama1,
                    PanoDemoMain.class),
            new DemoInfo(MERCATOR, R.string.demo_title_panorama, R.string.demo_desc_panorama2,
                    PanoDemoMain.class),
            new DemoInfo(UID_STREET, R.string.demo_title_panorama, R.string.demo_desc_panorama3,
                    PanoDemoMain.class),
            new DemoInfo(UID_INTERIOR, R.string.demo_title_panorama, R.string.demo_desc_panorama4,
                    PanoDemoMain.class),
            new DemoInfo(MARKER, R.string.demo_title_panorama, R.string.demo_desc_panorama5,
                    PanoDemoMain.class),
            new DemoInfo(COORDINATE_CONVERTER, R.string.demo_title_panorama, R.string.demo_desc_panorama6,
                    PanoDemoCoordinate.class),
            new DemoInfo(OTHER, R.string.demo_title_panorama, R.string.demo_desc_panorama7,
                    PanoDemoMain.class) };

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
    	// 建议在APP整体退出之前调用MapApi的destroy()函数，不要在每个activity的OnDestroy中调用，
    	// 避免MapApi重复创建初始化，提高效率
        PanoDemoApplication app = (PanoDemoApplication) this.getApplication();
        if (app.mBMapManager != null) {
            // app.mBMapManager.destroy();
            app.mBMapManager = null;
        }
        super.onDestroy();
        System.exit(0);
    }

    private class DemoListAdapter extends BaseAdapter {
        public DemoListAdapter() {
            super();
        }

        @Override
        public View getView(int index, View convertView, ViewGroup parent) {
            convertView = View.inflate(PanoDemoActivity.this, R.layout.panodemo_list_item, null);
            TextView title = (TextView) convertView.findViewById(R.id.item_title);
            TextView desc = (TextView) convertView.findViewById(R.id.item_desc);

            title.setText(demos[index].title);
            desc.setText(demos[index].desc);
            return convertView;
        }

        @Override
        public int getCount() {
            return demos.length;
        }

        @Override
        public Object getItem(int index) {
            return demos[index];
        }

        @Override
        public long getItemId(int id) {
            return id;
        }
    }

    private static class DemoInfo {
        private final int title;
        private final int desc;
        private final int type;
        private final Class<? extends Activity> demoClass;

        public DemoInfo(int type, int title, int desc, Class<? extends Activity> demoClass) {
            this.title = title;
            this.desc = desc;
            this.type = type;
            this.demoClass = demoClass;
        }
    }
}