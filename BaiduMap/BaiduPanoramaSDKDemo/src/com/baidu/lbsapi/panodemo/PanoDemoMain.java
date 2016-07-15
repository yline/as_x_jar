package com.baidu.lbsapi.panodemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.lbsapi.BMapManager;
import com.baidu.lbsapi.model.BaiduPanoData;
import com.baidu.lbsapi.model.BaiduPoiPanoData;
import com.baidu.lbsapi.panoramaview.ImageMarker;
import com.baidu.lbsapi.panoramaview.OnTabMarkListener;
import com.baidu.lbsapi.panoramaview.PanoramaRequest;
import com.baidu.lbsapi.panoramaview.PanoramaView;
import com.baidu.lbsapi.panoramaview.PanoramaViewListener;
import com.baidu.lbsapi.panoramaview.TextMarker;
import com.baidu.lbsapi.tools.Point;

/**
 * 全景Demo主Activity
 */
public class PanoDemoMain extends Activity {

    private static final String LTAG = "BaiduPanoSDKDemo";

    private PanoramaView mPanoView;
    private TextView textTitle;
    private Button btnImageMarker, btnTextMarker;// 添加移除marker测试
    private Button btnIsShowArrow, btnArrowStyle01, btnArrowStyle02;// 全景其他功能测试
    private Button btnIsShowInoorAblum;

    private View seekPitchLayout, seekHeadingLayout, seekLevelLayout;
    private SeekBar seekPitch, seekHeading, seekLevel;// 俯仰角,偏航角,全景图缩放测试

    private boolean isAddImageMarker = false;
    private boolean isAddTextMarker = false;
    private boolean isShowArrow = false;
    private boolean isShowAblum = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 先初始化BMapManager
        initBMapManager();
        setContentView(R.layout.panodemo_main);

        initView();

        Intent intent = getIntent();
        if (intent != null) {
            testPanoByType(intent.getIntExtra("type", -1));
        }
    }

    private void initBMapManager() {
        PanoDemoApplication app = (PanoDemoApplication) this.getApplication();
        if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(app);
            app.mBMapManager.init(new PanoDemoApplication.MyGeneralListener());
        }
    }

    private void initView() {
        textTitle = (TextView) findViewById(R.id.panodemo_main_title);
        mPanoView = (PanoramaView) findViewById(R.id.panorama);
        btnImageMarker = (Button) findViewById(R.id.panodemo_main_btn_imagemarker);
        btnTextMarker = (Button) findViewById(R.id.panodemo_main_btn_textmarker);

        btnIsShowArrow = (Button) findViewById(R.id.panodemo_main_btn_showarrow);
        btnArrowStyle01 = (Button) findViewById(R.id.panodemo_main_btn_arrowstyle_01);
        btnArrowStyle02 = (Button) findViewById(R.id.panodemo_main_btn_arrowstyle_02);
        btnIsShowInoorAblum = (Button) findViewById(R.id.panodemo_main_btn_indoor_album);

        btnImageMarker.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isAddImageMarker) {
                    addImageMarker();
                    btnImageMarker.setText("删除图片标注");
                } else {
                    removeImageMarker();
                    btnImageMarker.setText("添加图片标注");
                }
                isAddImageMarker = !isAddImageMarker;
            }
        });

        btnTextMarker.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isAddTextMarker) {
                    addTextMarker();
                    btnTextMarker.setText("删除文字标注");
                } else {
                    removeTextMarker();
                    btnTextMarker.setText("添加文字标注");
                }
                isAddTextMarker = !isAddTextMarker;
            }
        });

        seekPitchLayout = findViewById(R.id.seekpitch_ly);
        seekHeadingLayout = findViewById(R.id.seekheading_ly);
        seekLevelLayout = findViewById(R.id.seeklevel_ly);
        seekPitch = (SeekBar) findViewById(R.id.seekpitch);
        seekLevel = (SeekBar) findViewById(R.id.seeklevel);
        seekHeading = (SeekBar) findViewById(R.id.seekheading);

        seekPitch.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mPanoView.setPanoramaPitch(progress - 90);
            }
        });
        seekHeading.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mPanoView.setPanoramaHeading(progress);
            }
        });
        seekLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mPanoView.setPanoramaZoomLevel(progress + 1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void testPanoByType(int type) {
        mPanoView.setShowTopoLink(true);
        hideMarkerButton();
        hideSeekLayout();
        hideOtherLayout();
        hideIndoorAblumLayout();
        if (type == PanoDemoActivity.PID) {
            textTitle.setText(R.string.demo_desc_panorama0);

            mPanoView.setPanoramaImageLevel(PanoramaView.ImageDefinition.ImageDefinitionHigh);
            String pid = "0900220000141205144547300IN";
            mPanoView.setPanorama(pid);
        } else if (type == PanoDemoActivity.GEO) {
            textTitle.setText(R.string.demo_desc_panorama1);

            double lat = 39.945;
            double lon = 116.404;
            mPanoView.setPanorama(lon, lat);
        } else if (type == PanoDemoActivity.MERCATOR) {
            textTitle.setText(R.string.demo_desc_panorama2);

            int mcX = 12971348;
            int mcY = 4826239;
            mPanoView.setPanorama(mcX, mcY);
        } else if (type == PanoDemoActivity.UID_STREET) {
            textTitle.setText(R.string.demo_desc_panorama3);

            mPanoView.setPanoramaZoomLevel(5);
            mPanoView.setArrowTextureByUrl("http://d.lanrentuku.com/down/png/0907/system-cd-disk/arrow-up.png");
            mPanoView.setPanoramaImageLevel(PanoramaView.ImageDefinition.ImageDefinitionMiddle);
            String uid = "1a30c5f8cbb55eff71210b02";
            mPanoView.setPanoramaByUid(uid, PanoramaView.PANOTYPE_STREET);
        } else if (type == PanoDemoActivity.UID_INTERIOR) {
            textTitle.setText(R.string.demo_desc_panorama4);
            showIndoorAblumLayout();

            mPanoView.setPanoramaByUid("28e700f15aae5418085cb3a7", PanoramaView.PANOTYPE_INTERIOR);

            btnIsShowInoorAblum.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (!isShowAblum) {
                        btnIsShowInoorAblum.setText("隐藏内景相册");
                        mPanoView.setIndoorAlbumVisible();
                    } else {
                        btnIsShowInoorAblum.setText("显示内景相册");
                        mPanoView.setIndoorAlbumGone();
                    }
                    isShowAblum = !isShowAblum;
                }
            });
        } else if (type == PanoDemoActivity.MARKER) {
            textTitle.setText(R.string.demo_desc_panorama5);

            showMarkerButton();
            mPanoView.setPanorama("0100220000130817164838355J5");
            mPanoView.setShowTopoLink(false);
        } else if (type == PanoDemoActivity.OTHER) {
            textTitle.setText(R.string.demo_desc_panorama7);

            showSeekLayout();
            showOtherLayout();

            mPanoView.setPanoramaImageLevel(PanoramaView.ImageDefinition.ImageDefinitionHigh);
            String pid = "0100220000130817164838355J5";
            mPanoView.setPanorama(pid);

            // 测试获取内景的相册描述信息和服务推荐描述信息
            testPanoramaRequest();

            btnIsShowArrow.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (!isShowArrow) {
                        mPanoView.setShowTopoLink(false);
                        btnIsShowArrow.setText("显示全景箭头");
                    } else {
                        mPanoView.setShowTopoLink(true);
                        btnIsShowArrow.setText("隐藏全景箭头");
                    }
                    isShowArrow = !isShowArrow;
                }
            });

            btnArrowStyle01.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mPanoView.setArrowTextureByUrl("http://d.lanrentuku.com/down/png/0907/system-cd-disk/arrow-up.png");
                }
            });

            btnArrowStyle02.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.street_arrow);
                    mPanoView.setArrowTextureByBitmap(bitmap);
                }
            });
        }

        // 测试回调函数,需要注意的是回调函数要在setPanorama()之前调用，否则回调函数可能执行异常
        mPanoView.setPanoramaViewListener(new PanoramaViewListener() {

            @Override
            public void onLoadPanoramaBegin() {
                Log.i(LTAG, "onLoadPanoramaStart...");
            }

            @Override
            public void onLoadPanoramaEnd(String json) {
                Log.i(LTAG, "onLoadPanoramaEnd : " + json);
            }

            @Override
            public void onLoadPanoramaError(String error) {
                Log.i(LTAG, "onLoadPanoramaError : " + error);
            }
        });
    }

    private void testPanoramaRequest() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                PanoramaRequest panoramaRequest = PanoramaRequest.getInstance(PanoDemoMain.this);

                String pid = "01002200001307201550572285B";
                Log.e(LTAG, "PanoramaRecommendInfo");
                Log.i(LTAG, panoramaRequest.getPanoramaRecommendInfo(pid).toString());

                String iid = "978602fdf6c5856bddee8b62";
                Log.e(LTAG, "PanoramaByIIdWithJson");
                Log.i(LTAG, panoramaRequest.getPanoramaByIIdWithJson(iid).toString());

                // 通过百度经纬度坐标获取当前位置相关全景信息，包括是否有外景，外景PID，外景名称等
                double lat = 40.029233;
                double lon = 116.32085;
                BaiduPanoData mPanoDataWithLatLon = panoramaRequest.getPanoramaInfoByLatLon(lon, lat);
                Log.e(LTAG, "PanoDataWithLatLon");
                Log.i(LTAG, mPanoDataWithLatLon.getDescription());

                // 通过百度墨卡托坐标获取当前位置相关全景信息，包括是否有外景，外景PID，外景名称等
                int x = 12948920;
                int y = 4842480;
                BaiduPanoData mPanoDataWithXy = panoramaRequest.getPanoramaInfoByMercator(x, y);

                Log.e(LTAG, "PanoDataWithXy");
                Log.i(LTAG, mPanoDataWithXy.getDescription());

                // 通过百度地图uid获取该poi下的全景描述信息，以此来判断此UID下是否有内景及外景
                String uid = "7c86f335bbcc18fc5fbe8669";
                BaiduPoiPanoData poiPanoData = panoramaRequest.getPanoramaInfoByUid(uid);
                Log.e(LTAG, "poiPanoData");
                Log.i(LTAG, poiPanoData.getDescription());
            }
        }).start();

    }

    // 隐藏添加删除标注按钮
    private void hideMarkerButton() {
        btnImageMarker.setVisibility(View.GONE);
        btnTextMarker.setVisibility(View.GONE);
    }

    // 显示添加删除标注按钮
    private void showMarkerButton() {
        btnImageMarker.setVisibility(View.VISIBLE);
        btnTextMarker.setVisibility(View.VISIBLE);
    }

    // 隐藏设置俯仰角偏航角SeekBar
    private void hideSeekLayout() {
        seekPitchLayout.setVisibility(View.GONE);
        seekHeadingLayout.setVisibility(View.GONE);
        seekLevelLayout.setVisibility(View.GONE);
    }

    // 显示设置俯仰角偏航角SeekBar
    private void showSeekLayout() {
        seekPitchLayout.setVisibility(View.VISIBLE);
        seekHeadingLayout.setVisibility(View.VISIBLE);
        seekLevelLayout.setVisibility(View.VISIBLE);
    }

    // 隐藏其他功能测试
    private void hideOtherLayout() {
        btnIsShowArrow.setVisibility(View.GONE);
        btnArrowStyle01.setVisibility(View.GONE);
        btnArrowStyle02.setVisibility(View.GONE);
    }

    // 显示其他功能测试
    private void showOtherLayout() {
        btnIsShowArrow.setVisibility(View.VISIBLE);
        btnArrowStyle01.setVisibility(View.VISIBLE);
        btnArrowStyle02.setVisibility(View.VISIBLE);
    }

    // 隐藏内景相册测试
    private void hideIndoorAblumLayout() {
        btnIsShowInoorAblum.setVisibility(View.GONE);
    }

    // 显示内景相册测试
    private void showIndoorAblumLayout() {
        btnIsShowInoorAblum.setVisibility(View.VISIBLE);
    }

    private ImageMarker marker1;
    private ImageMarker marker2;

    /**
     * 添加图片标注
     */
    private void addImageMarker() {
        // 天安门西南方向
        marker1 = new ImageMarker();
        marker1.setMarkerPosition(new Point(116.356329, 39.890534));
        marker1.setMarkerHeight(2.3f);
        marker1.setMarker(getResources().getDrawable(R.drawable.icon_marka));
        marker1.setOnTabMarkListener(new OnTabMarkListener() {

            @Override
            public void onTab() {
                Toast.makeText(PanoDemoMain.this, "图片MarkerA标注已被点击", Toast.LENGTH_SHORT).show();
            }
        });
        // 天安门东北方向
        marker2 = new ImageMarker();
        marker2.setMarkerPosition(new Point(116.427116, 39.929718));
        marker2.setMarker(getResources().getDrawable(R.drawable.icon_markb));
        marker2.setMarkerHeight(7);
        marker2.setOnTabMarkListener(new OnTabMarkListener() {

            @Override
            public void onTab() {
                Toast.makeText(PanoDemoMain.this, "图片MarkerB标注已被点击", Toast.LENGTH_SHORT).show();
            }
        });
        mPanoView.addMarker(marker1);
        mPanoView.addMarker(marker2);
    }

    /**
     * 删除图片标注
     */
    private void removeImageMarker() {
        mPanoView.removeMarker(marker1);
        mPanoView.removeMarker(marker2);
    }

    private TextMarker textMark1;
    private TextMarker textMark2;

    /**
     * 添加文本标注
     */
    private void addTextMarker() {
        // 天安门西北方向
        textMark1 = new TextMarker();
        textMark1.setMarkerPosition(new Point(116.399562, 39.916789));
        textMark1.setFontColor(0xFFFF0000);
        textMark1.setText("百度全景百度全景\\map pano\\你好marker");
        textMark1.setFontSize(12);
        textMark1.setBgColor(0xFFFFFFFF);
        textMark1.setPadding(10, 20, 15, 25);
        textMark1.setMarkerHeight(20.3f);
        textMark1.setOnTabMarkListener(new OnTabMarkListener() {

            @Override
            public void onTab() {
                Toast.makeText(PanoDemoMain.this, "textMark1标注已被点击", Toast.LENGTH_SHORT).show();
            }
        });
        // 天安门东南方向
        textMark2 = new TextMarker();
        textMark2.setMarkerPosition(new Point(116.409766, 39.911808));
        textMark2.setFontColor(Color.RED);
        textMark2.setText("你好marker");
        textMark2.setFontSize(12);
        textMark2.setBgColor(Color.BLUE);
        textMark2.setPadding(10, 20, 15, 25);
        textMark2.setMarkerHeight(10);
        textMark2.setOnTabMarkListener(new OnTabMarkListener() {

            @Override
            public void onTab() {
                Toast.makeText(PanoDemoMain.this, "textMark2标注已被点击", Toast.LENGTH_SHORT).show();
            }
        });
        mPanoView.addMarker(textMark1);
        mPanoView.addMarker(textMark2);
    }

    /**
     * 删除文本标注
     */
    private void removeTextMarker() {
        mPanoView.removeMarker(textMark1);
        mPanoView.removeMarker(textMark2);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mPanoView.destroy();
        super.onDestroy();
    }

}
