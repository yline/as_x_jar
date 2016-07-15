package org.http.ioc;

import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.http.app.DefaultParamsBuilder;

/**
 * 地址：http://appserver.1035.mobi/MobiSoft/Group_List?id=110022&size=12
 */
@HttpRequest(
		host = "http://appserver.1035.mobi/MobiSoft",
		path = "Group_List",
		builder = DefaultParamsBuilder.class/*可选参数, 控制参数构建过程, 定义参数签名, SSL证书等*/)
public class BaiduParams extends RequestParams {

	// 这里可以确定一些确定要写的参数之后直接赋值就可以了
	public String id;
	public String size;

	//    // 数组参数 aa=1&aa=2&aa=4
	//    public int[] aa = new int[]{1, 2, 4};
	//    public List<String> bb = new ArrayList<String>();
	//
	//    public BaiduParams() {
	//        bb.add("a");
	//        bb.add("c");
	//    }
}
