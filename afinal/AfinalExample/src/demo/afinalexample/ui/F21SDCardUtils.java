package demo.afinalexample.ui;

import java.io.File;

import android.os.Environment;
import android.os.StatFs;

/**
 * f21 δʹ�ù�
 * SD����صĸ�����
 */
public class F21SDCardUtils {
	
	public F21SDCardUtils() {
		/**
		 * ʵ����ʧ��
		 */
		throw new UnsupportedOperationException("cannot be instantiated");
	}
	
	/**
	 * �ж�SDCard�Ƿ����
	 * 
	 * @return
	 */
	public static boolean isSDCardEnable(){
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);

	}
	
	/**
	 * ��ȡSD��·��
	 * 
	 * @return ��б��
	 */
	public static String getSDCardPath(){
		return Environment.getExternalStorageDirectory().getAbsolutePath()
				+ File.separator;
	}
	
	/**
	 * ��ȡSD����ʣ������ ��λbyte
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static long getSDCardAllSize(){
		if (isSDCardEnable()){
			StatFs stat = new StatFs(getSDCardPath());
			// ��ȡ���е����ݿ������
			long availableBlocks = (long) stat.getAvailableBlocks() - 4;
			// ��ȡ�������ݿ�Ĵ�С��byte��
			long freeBlocks = stat.getAvailableBlocks();
			return freeBlocks * availableBlocks;
		}
		return 0;
	}
	
	/**
	 * ��ȡָ��·�����ڿռ��ʣ����������ֽ�������λbyte
	 * 
	 * @param filePath
	 * @return �����ֽ� SDCard���ÿռ䣬�ڲ��洢���ÿռ�
	 */
	@SuppressWarnings("deprecation")
	public static long getFreeBytes(String filePath){
		// �����sd�����µ�·�������ȡsd����������
		if (filePath.startsWith(getSDCardPath())){
			filePath = getSDCardPath();
		} else{ 	// ������ڲ��洢��·�������ȡ�ڴ�洢�Ŀ�������
			filePath = Environment.getDataDirectory().getAbsolutePath();
		}
		StatFs stat = new StatFs(filePath);
		long availableBlocks = (long) stat.getAvailableBlocks() - 4;
		return stat.getBlockSize() * availableBlocks;
	}
	
	/**
	 * ��ȡϵͳ�洢·��
	 * 
	 * @return
	 */
	public static String getRootDirectoryPath(){
		return Environment.getRootDirectory().getAbsolutePath();
	}
	
}


