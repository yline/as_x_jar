package demo.afinalexample.ui;

import android.util.Log;

/**
 * ��־,�Դ� F21 - ��־
 */
public class F21LogTool {
	private static final String tagAdd = "F21 - ";
	private String stag;

	/**
	 * �½���ӡlog
	 * @param tag log��ǩ
	 */
	public F21LogTool() {
		this.stag = tagAdd;
	}
	
	/**
	 * �½���ӡlog
	 * @param tag log��ǩ
	 */
	public F21LogTool(String tag) {
		this.stag = tagAdd + tag;
	}

	/**
	 * verbose����
	 * ��ɫ
	 */
	public void v(String msg){
		Log.v(stag, msg);
	}
	
	public void v(String tag,String msg){
		Log.v(tagAdd + tag, msg);
	}
	
	/**
	 * debug	����debugʱ�����
	 * ��ɫ
	 */
	public void d(String msg){
		Log.d(stag, msg);
	}
	
	public void d(String tag,String msg){
		Log.d(tagAdd + tag, msg);
	}
	
	/**
	 * ����������Ϣ	information
	 * ��ɫ
	 */
	public void i(String msg){
		Log.i(stag, msg);
	}
	
	public void i(String tag,String msg){
		Log.i(tagAdd + tag, msg);
	}
	
	/**
	 * warn
	 * ��ɫ
	 */
	public void w(String msg){
		Log.w(stag, msg);
	}
	
	public void w(String tag,String msg){
		Log.w(tagAdd + tag, msg);
	}
	
	/**
	 * ���صĴ�����Ϣ	error
	 * ��ɫ
	 */
	public void e(String msg){
		Log.e(stag, msg);
	}
	
	public void e(String tag,String msg){
		Log.e(tagAdd + tag, msg);
	}
}
