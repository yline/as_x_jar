package com.zxing.demo.capture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.client.android.R;
import com.google.zxing.client.result.ParsedResultType;

import java.text.DateFormat;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;

public class CaptureResultView extends RelativeLayout {
	private static final Collection<ResultMetadataType> DISPLAYABLE_METADATA_TYPES =
			EnumSet.of(ResultMetadataType.ISSUE_NUMBER,
					ResultMetadataType.SUGGESTED_PRICE,
					ResultMetadataType.ERROR_CORRECTION_LEVEL,
					ResultMetadataType.POSSIBLE_COUNTRY);
	
	public CaptureResultView(Context context) {
		this(context, null);
	}
	
	public CaptureResultView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}
	
	public CaptureResultView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		LayoutInflater.from(context).inflate(R.layout.view_result_capture, this);
	}
	
	public void setData(Bitmap barcode, BarcodeFormat format, ParsedResultType resultType, long timestamp, Map<ResultMetadataType, Object> metadata, String encodeContent) {
		ImageView barcodeImageView = findViewById(R.id.view_result_barcode);
		if (barcode == null) {
			barcodeImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.launcher_icon));
		} else {
			barcodeImageView.setImageBitmap(barcode);
		}
		
		TextView formatTextView = findViewById(R.id.view_result_format);
		formatTextView.setText(format.toString());
		
		TextView typeTextView = findViewById(R.id.view_result_type);
		typeTextView.setText(resultType.toString());
		
		DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
		TextView timeTextView = findViewById(R.id.view_result_time);
		timeTextView.setText(formatter.format(timestamp));
		
		TextView metaTextView = findViewById(R.id.view_result_meta);
		View metaTextViewLabel = findViewById(R.id.view_result_meta_label);
		metaTextView.setVisibility(View.GONE);
		metaTextViewLabel.setVisibility(View.GONE);
		if (metadata != null) {
			StringBuilder metadataText = new StringBuilder(20);
			for (Map.Entry<ResultMetadataType, Object> entry : metadata.entrySet()) {
				if (DISPLAYABLE_METADATA_TYPES.contains(entry.getKey())) {
					metadataText.append(entry.getValue()).append('\n');
				}
			}
			if (metadataText.length() > 0) {
				metadataText.setLength(metadataText.length() - 1);
				metaTextView.setText(metadataText);
				metaTextView.setVisibility(View.VISIBLE);
				metaTextViewLabel.setVisibility(View.VISIBLE);
			}
		}
		
		TextView contentsTextView = findViewById(R.id.view_result_contents);
		contentsTextView.setText(encodeContent);
		int scaledSize = Math.max(22, 32 - encodeContent.length() / 4);
		contentsTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, scaledSize);
	}
}
