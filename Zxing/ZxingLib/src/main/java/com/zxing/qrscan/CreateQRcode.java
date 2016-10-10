package com.zxing.qrscan;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;

/**
 * 创建一个QRCode
 * @author yline 2016/10/10 --> 8:05
 * @version 1.0.0
 */
public class CreateQRcode
{
	private static int QR_WIDTH = 500;

	private static int QR_HEIGHT = 500;

	public static void CreateQRcodeOn(String string, ImageView sweepImageView)
	{
		try
		{
			if (string == null || string.length() < 1 || "".equals(string))
			{
				return;
			}
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			BitMatrix bitMa = new QRCodeWriter().encode(string, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);

			int[] pixels = new int[QR_WIDTH * QR_HEIGHT];

			for (int y = 0; y < QR_HEIGHT; y++)
			{
				for (int x = 0; x < QR_WIDTH; x++)
				{
					if (bitMa.get(x, y))
					{
						pixels[y * QR_WIDTH + x] = 0xff000000;
					}
					else
					{
						pixels[y * QR_WIDTH + x] = 0xffffffff;
					}
				}
			}
			Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);

			sweepImageView.setImageBitmap(bitmap);
		}
		catch (WriterException e)
		{
			e.printStackTrace();
			return;
		}
	}
}
