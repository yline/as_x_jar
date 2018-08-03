package com.google.zxing.client.android.view;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.R;
import com.google.zxing.client.android.camera.CameraManager;
import com.yline.utils.UIScreenUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 替换成自定义的样子
 *
 * @author yline
 * @times 2018/8/3 -- 14:00
 */
public final class ViewfinderView extends View {
	private static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};
	
	private static final long ANIMATION_DELAY = 16L;
	
	private static final int CURRENT_POINT_OPACITY = 0xA0; // 扫描成功后，透明度
	
	private static final int MAX_RESULT_POINTS = 20;
	
	private static final int POINT_SIZE = 6;
	
	private final Paint paint;
	
	private Bitmap resultBitmap;
	
	private final int maskColor; // 扫描中，背景色
	private final int resultColor; // 扫描成功，背景色
	private final int laserColor;
	private final int resultPointColor;
	private int scannerAlpha;
	
	private List<ResultPoint> possibleResultPoints;
	private List<ResultPoint> lastPossibleResultPoints;
	
	// 新增
	private final int boundColor; // 边角颜色
	private final int boundLength; // 边角长度
	private final int boundWidth; // 边角宽度
	private Rect drawFrame; // 绘制的方框大小
	
	private Rect lineRect = new Rect(); // 移动的Rect
	private final Bitmap scanLineBitmap;
	
	// This constructor is used when the class is built from an XML resource.
	public ViewfinderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		// Initialize these once for performance rather than calling them every time in onDraw().
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		Resources resources = getResources();
		maskColor = resources.getColor(R.color.viewfinder_mask);
		resultColor = resources.getColor(R.color.viewfinder_result);
		laserColor = resources.getColor(R.color.viewfinder_laser);
		resultPointColor = resources.getColor(R.color.viewfinder_result_points);
		
		scannerAlpha = 0;
		possibleResultPoints = new ArrayList<>(5);
		lastPossibleResultPoints = null;
		
		// 新增
		boundColor = resources.getColor(R.color.viewfinder_bound_color);
		boundLength = UIScreenUtil.dp2px(context, 20);
		boundWidth = UIScreenUtil.dp2px(context, 4);
		
		scanLineBitmap = BitmapFactory.decodeResource(resources, R.drawable.view_finder_scan_line);
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	public void onDraw(Canvas canvas) {
		CameraManager cameraManager = CameraManager.getInstance();
		Rect frame = getFramingRect(cameraManager);
		Rect previewFrame = cameraManager.getFramingRectInPreview();
		if (frame == null || previewFrame == null) {
			return;
		}
		
		drawReadRect(canvas, frame);
		if (resultBitmap != null) {
			// Draw the opaque result bitmap over the scanning rectangle
			paint.setAlpha(CURRENT_POINT_OPACITY);
			canvas.drawBitmap(resultBitmap, null, frame, paint);
		} else {
			// 绘制红线，3px
			drawReadRectBound(canvas, frame);
			
			// drawReadLine(canvas, frame);
			drawReadLineScan(canvas, frame);
			
			drawResultPoint(canvas, previewFrame, previewFrame);
			
			// 准备下一次识别绘制
			// Request another update at the animation interval, but only repaint the laser line,
			// not the entire viewfinder mask.
			postInvalidateDelayed(ANIMATION_DELAY, frame.left - POINT_SIZE, frame.top - POINT_SIZE, frame.right + POINT_SIZE, frame.bottom + POINT_SIZE);
		}
	}
	
	/**
	 * 绘制 给用户看的预览Rect
	 *
	 * @param canvas 画布
	 * @param frame  给用户看的预览Rect
	 */
	private void drawReadRect(Canvas canvas, Rect frame) {
		int width = canvas.getWidth();
		int height = canvas.getHeight();
		
		// 背景色(有结果和无结果)、
		paint.setColor(resultBitmap != null ? resultColor : maskColor);
		
		// 扫描方框
		canvas.drawRect(0, 0, width, frame.top, paint);
		canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
		canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
		canvas.drawRect(0, frame.bottom + 1, width, height, paint);
	}
	
	/**
	 * 绘制 给用户看的预览Rect 的四个角
	 *
	 * @param canvas 画布
	 * @param frame  给用户看的预览Rect
	 */
	private void drawReadRectBound(Canvas canvas, Rect frame) {
		paint.setColor(boundColor);
		paint.setStyle(Paint.Style.FILL);
		
		int corWidth = boundWidth;
		int corLength = boundLength;
		
		// 左上角
		canvas.drawRect(frame.left, frame.top, frame.left + corWidth, frame.top + corLength, paint);
		canvas.drawRect(frame.left, frame.top, frame.left + corLength, frame.top + corWidth, paint);
		// 右上角
		canvas.drawRect(frame.right - corWidth, frame.top, frame.right, frame.top + corLength, paint);
		canvas.drawRect(frame.right - corLength, frame.top, frame.right, frame.top + corWidth, paint);
		// 左下角
		canvas.drawRect(frame.left, frame.bottom - corLength, frame.left + corWidth, frame.bottom, paint);
		canvas.drawRect(frame.left, frame.bottom - corWidth, frame.left + corLength, frame.bottom, paint);
		// 右下角
		canvas.drawRect(frame.right - corWidth, frame.bottom - corLength, frame.right, frame.bottom, paint);
		canvas.drawRect(frame.right - corLength, frame.bottom - corWidth, frame.right, frame.bottom, paint);
	}
	
	/**
	 * 绘制 给用户看的预览Rect 的 移动的横线
	 * 正弦函数（先加速再减速）
	 *
	 * @param canvas 画布
	 * @param frame  给用户看的预览Rect
	 */
	private void drawReadLineScan(Canvas canvas, Rect frame) {
		paint.setStyle(Paint.Style.FILL);
		
		if (lineRect.top >= frame.top && (lineRect.top + 10 < frame.bottom)) {
			int raiseDiff = (int) (3 * Math.sin(Math.PI * (lineRect.bottom - frame.top) / frame.height()) + 2);
			lineRect.offset(0, raiseDiff);
		} else {
			lineRect = new Rect(frame.left, frame.top, frame.right, frame.top + 10);
		}
		
		canvas.drawBitmap(scanLineBitmap, null, lineRect, paint);
	}
	
	/**
	 * 绘制扫描时，红线
	 *
	 * @param canvas 画布
	 * @param frame  给用户看的，方框的Rect
	 */
	private void drawReadLine(Canvas canvas, Rect frame) {
		paint.setColor(laserColor);
		paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
		scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
		int middle = frame.height() / 2 + frame.top;
		canvas.drawRect(frame.left + 2, middle - 1, frame.right - 1, middle + 2, paint);
	}
	
	/**
	 * 绘制，扫描时的，定位四个点（和摄像头识别的有关）
	 *
	 * @param canvas       画布
	 * @param frame        真实屏幕预览的Rect
	 * @param previewFrame 摄像头预览的Rect
	 */
	private void drawResultPoint(Canvas canvas, Rect frame, Rect previewFrame) {
		// 比例
		float scaleX = frame.width() / (float) previewFrame.width();
		float scaleY = frame.height() / (float) previewFrame.height();
		// 初始定位
		int frameLeft = frame.left;
		int frameTop = frame.top;
		
		// 绘制上次，识别的点位
		List<ResultPoint> currentLast = lastPossibleResultPoints;
		if (currentLast != null) {
			paint.setAlpha(CURRENT_POINT_OPACITY / 2);
			paint.setColor(resultPointColor);
			synchronized (currentLast) {
				float radius = POINT_SIZE / 2.0f;
				for (ResultPoint point : currentLast) {
					canvas.drawCircle(frameLeft + (int) (point.getX() * scaleX), frameTop + (int) (point.getY() * scaleY), radius, paint);
				}
			}
		}
		
		// 绘制新的识别点
		List<ResultPoint> currentPossible = possibleResultPoints;
		if (currentPossible.isEmpty()) {
			lastPossibleResultPoints = null;
		} else {
			possibleResultPoints = new ArrayList<>(5);
			lastPossibleResultPoints = currentPossible;
			paint.setAlpha(CURRENT_POINT_OPACITY);
			paint.setColor(resultPointColor);
			synchronized (currentPossible) {
				for (ResultPoint point : currentPossible) {
					canvas.drawCircle(frameLeft + (int) (point.getX() * scaleX), frameTop + (int) (point.getY() * scaleY), POINT_SIZE, paint);
				}
			}
		}
	}
	
	/**
	 * 将屏幕真实预览Rect，加工一下，变成给用户看的预览Rect
	 *
	 * @param cameraManager 获取真实预览Rect使用
	 * @return 给用户看的预览Rect
	 */
	private Rect getFramingRect(CameraManager cameraManager) {
		Rect frame = cameraManager.getFramingRect();
		if (null != frame) {
			if (null == drawFrame) {
				int widthOffset = frame.width() * 3 / 16;
				int heightOffset = frame.height() * 3 / 16;
				drawFrame = new Rect(frame.left + widthOffset, frame.top + heightOffset, frame.right - widthOffset, frame.bottom - heightOffset);
			}
			return drawFrame;
		}
		
		return null;
	}
	
	public void drawViewfinder() {
		Bitmap resultBitmap = this.resultBitmap;
		this.resultBitmap = null;
		if (resultBitmap != null) {
			resultBitmap.recycle();
		}
		invalidate();
	}
	
	/**
	 * Draw a bitmap with the result points highlighted instead of the live scanning display.
	 *
	 * @param barcode An image of the decoded barcode.
	 */
	public void drawResultBitmap(Bitmap barcode) {
		resultBitmap = barcode;
		invalidate();
	}
	
	public void addPossibleResultPoint(ResultPoint point) {
		List<ResultPoint> points = possibleResultPoints;
		synchronized (points) {
			points.add(point);
			int size = points.size();
			if (size > MAX_RESULT_POINTS) {
				// trim it
				points.subList(0, size - MAX_RESULT_POINTS / 2).clear();
			}
		}
	}
}
