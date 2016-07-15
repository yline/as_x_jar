package baidumapsdk.demo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.opengl.GLUtils;
import android.os.Bundle;
import android.util.Log;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapDrawFrameCallback;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;

/**
 * 此demo用来展示如何在地图绘制的每帧中再额外绘制一些用户自己的内容
 */
public class OpenglDemo extends Activity implements OnMapDrawFrameCallback {

	private static final String LTAG = OpenglDemo.class.getSimpleName();

	// 地图相关
	MapView mMapView;
	BaiduMap mBaiduMap;
	Bitmap bitmap;
	private LatLng latlng1 = new LatLng(39.97923, 116.357428);
	LatLng latlng2 = new LatLng(39.94923, 116.397428);
	LatLng latlng3 = new LatLng(39.96923, 116.437428);
	private List<LatLng> latLngPolygon;
	{
		latLngPolygon = new ArrayList<LatLng>();
		latLngPolygon.add(latlng1);
		latLngPolygon.add(latlng2);
		latLngPolygon.add(latlng3);
	}

	private float[] vertexs;
	private FloatBuffer vertexBuffer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_opengl);
		// 初始化地图
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setOnMapDrawFrameCallback(this);
		bitmap = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.ground_overlay);
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		// onResume 纹理失效
		textureId = -1;
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		mMapView.onDestroy();
		super.onDestroy();
	}

	public void onMapDrawFrame(GL10 gl, MapStatus drawingMapStatus) {
		if (mBaiduMap.getProjection() != null) {
			calPolylinePoint(drawingMapStatus);
			drawPolyline(gl, Color.argb(255, 255, 0, 0), vertexBuffer, 10, 3,
					drawingMapStatus);
			drawTexture(gl, bitmap, drawingMapStatus);
		}
	}

	public void calPolylinePoint(MapStatus mspStatus) {
		PointF[] polyPoints = new PointF[latLngPolygon.size()];
		vertexs = new float[3 * latLngPolygon.size()];
		int i = 0;
		for (LatLng xy : latLngPolygon) {
			polyPoints[i] = mBaiduMap.getProjection().toOpenGLLocation(xy,
					mspStatus);
			vertexs[i * 3] = polyPoints[i].x;
			vertexs[i * 3 + 1] = polyPoints[i].y;
			vertexs[i * 3 + 2] = 0.0f;
			i++;
		}
		for (int j = 0; j < vertexs.length; j++) {
			Log.d(LTAG, "vertexs[" + j + "]: " + vertexs[j]);
		}
		vertexBuffer = makeFloatBuffer(vertexs);
	}

	private FloatBuffer makeFloatBuffer(float[] fs) {
		ByteBuffer bb = ByteBuffer.allocateDirect(fs.length * 4);
		bb.order(ByteOrder.nativeOrder());
		FloatBuffer fb = bb.asFloatBuffer();
		fb.put(fs);
		fb.position(0);
		return fb;
	}

	private void drawPolyline(GL10 gl, int color, FloatBuffer lineVertexBuffer,
			float lineWidth, int pointSize, MapStatus drawingMapStatus) {

		gl.glEnable(GL10.GL_BLEND);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		float colorA = Color.alpha(color) / 255f;
		float colorR = Color.red(color) / 255f;
		float colorG = Color.green(color) / 255f;
		float colorB = Color.blue(color) / 255f;

		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, lineVertexBuffer);
		gl.glColor4f(colorR, colorG, colorB, colorA);
		gl.glLineWidth(lineWidth);
		gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, pointSize);

		gl.glDisable(GL10.GL_BLEND);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
	int textureId = -1;
	/**
	 * 使用opengl坐标绘制
	 * 
	 * @param gl
	 * @param bitmap
	 * @param drawingMapStatus
	 */
	public void drawTexture(GL10 gl, Bitmap bitmap, MapStatus drawingMapStatus) {
		PointF p1 = mBaiduMap.getProjection().toOpenGLLocation(latlng2,
				drawingMapStatus);
		PointF p2 = mBaiduMap.getProjection().toOpenGLLocation(latlng3,
				drawingMapStatus);
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 3 * 4);
		byteBuffer.order(ByteOrder.nativeOrder());
		FloatBuffer vertices = byteBuffer.asFloatBuffer();
		vertices.put(new float[] { p1.x, p1.y, 0.0f, p2.x, p1.y, 0.0f, p1.x,
				p2.y, 0.0f, p2.x, p2.y, 0.0f });

		ByteBuffer indicesBuffer = ByteBuffer.allocateDirect(6 * 2);
		indicesBuffer.order(ByteOrder.nativeOrder());
		ShortBuffer indices = indicesBuffer.asShortBuffer();
		indices.put(new short[] { 0, 1, 2, 1, 2, 3 });

		ByteBuffer textureBuffer = ByteBuffer.allocateDirect(4 * 2 * 4);
		textureBuffer.order(ByteOrder.nativeOrder());
		FloatBuffer texture = textureBuffer.asFloatBuffer();
		texture.put(new float[] { 0, 1f, 1f, 1f, 0f, 0f, 1f, 0f });

		indices.position(0);
		vertices.position(0);
		texture.position(0);

		// 生成纹理
		if (textureId == -1) {
			int textureIds[] = new int[1];
			gl.glGenTextures(1, textureIds, 0);
			textureId = textureIds[0];
			Log.d(LTAG, "textureId: " + textureId);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
					GL10.GL_NEAREST);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
					GL10.GL_NEAREST);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
		}
	
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

		// 绑定纹理ID
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertices);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texture);

		gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 6, GL10.GL_UNSIGNED_SHORT,
				indices);

		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_BLEND);
	}
}
