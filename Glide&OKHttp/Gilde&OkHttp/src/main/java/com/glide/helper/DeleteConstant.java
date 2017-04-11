package com.glide.helper;

import java.util.Random;

public class DeleteConstant
{
	public static final String url_default_avatar = "http://imgsrc.baidu.com/forum/w=580/sign=d07e03955ddf8db1bc2e7c6c3923dddb/33c254f40ad162d97ecd601112dfa9ec8b13cde2.jpg";

	public static final String url_icon_camera = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489573801544&di=1198b31d287352ddaad03e7c31da354f&imgtype=0&src=http%3A%2F%2Fimg2.web07.cn%2FUpPic%2FPng%2F201501%2F05%2F3948530516530511.png";

	private static Random random = new Random();

	public static String getUrlRec()
	{
		int position = random.nextInt(6);
		return getUrlRec(position);
	}

	public static String getUrlSquare()
	{
		int position = random.nextInt(13);
		return getUrlSquare(position);
	}

	private static String getUrlRec(int position)
	{
		switch (position)
		{
			case 0:
				return url_rec_car;
			case 1:
				return url_rec_fire;
			case 2:
				return url_rec_book;
			case 3:
				return url_rec_girl;
			case 4:
				return url_rec_view;
			case 5:
				return url_rec_cloud;
			default:
				return url_rec_car;
		}
	}

	/* 长方形 图片 */
	private static final String url_rec_car = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489481956694&di=cf938c1b74b0554a87cbaace91f10092&imgtype=jpg&src=http%3A%2F%2Fimg2.imgtn.bdimg.com%2Fit%2Fu%3D2805811859%2C1847778670%26fm%3D214%26gp%3D0.jpg";

	private static final String url_rec_fire = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489482031421&di=3b6c366706dea35d1a51775c8cda55ab&imgtype=0&src=http%3A%2F%2Fp15.qhimg.com%2Fd%2F_open360%2Fgame0418%2F6.jpg";

	private static final String url_rec_book = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489482085185&di=ce56b63becc32c925a4262add9d6ddaf&imgtype=0&src=http%3A%2F%2Fp18.qhimg.com%2Ft013e4646b62e132075.jpg";

	private static final String url_rec_girl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489482116430&di=553c100a593c043834bcdb624c9e8c78&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fzhidao%2Fpic%2Fitem%2Fb90e7bec54e736d10889e10d99504fc2d5626918.jpg";

	private static final String url_rec_view = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1713696268,3572035866&fm=23&gp=0.jpg";

	private static final String url_rec_cloud = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489482177297&di=a4e9e33dafc072ba3b988927cec04db2&imgtype=0&src=http%3A%2F%2Fp16.qhimg.com%2Fbdr%2F__85%2Fd%2F_open360%2Ffj0126%2F154.jpg";

	private static String getUrlSquare(int position)
	{
		switch (position)
		{
			case 0:
				return url_square_sky;
			case 1:
				return url_square_person;
			case 2:
				return url_square_beach;
			case 3:
				return url_square_sun;
			case 4:
				return url_square_house;
			case 5:
				return url_square_girl;
			case 6:
				return url_square_little;
			case 7:
				return url_square_watch;
			case 8:
				return url_square_medium;
			case 9:
				return url_square_white;
			case 10:
				return url_square_phone;
			case 11:
				return url_square_marry;
			case 12:
				return url_square_tree;
			default:
				return url_square_little;
		}
	}

	/* 正方形图片 */
	private static final String url_square_sky = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489483306216&di=72ffda741a2aec48be6675f2caed7d65&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fzhidao%2Fpic%2Fitem%2F21a4462309f79052cff992e40af3d7ca7acbd502.jpg";

	private static final String url_square_person = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489483357762&di=0034510ccf7ac580dc4d9edd3e141d18&imgtype=0&src=http%3A%2F%2Fimg3.duitang.com%2Fuploads%2Fitem%2F201312%2F05%2F20131205171905_2TAzM.jpeg";

	private static final String url_square_beach = "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3828494210,683540257&fm=23&gp=0.jpg";

	private static final String url_square_sun = "https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1489473348&di=702de9050f0c1ff0ecfb4d2995323271&src=http://pic1.ipadown.com/imgs/20120612125332798.png";

	private static final String url_square_house = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489483527281&di=54deaba5615a5b10a20899f58a207c47&imgtype=0&src=http%3A%2F%2Fbpic.ooopic.com%2F16%2F15%2F69%2F16156908-c70c4c43eafe3618d29d1f2208c47067.jpg";

	private static final String url_square_girl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489483632407&di=e68bf35bf6b501bc8bd1896e5183a4bc&imgtype=0&src=http%3A%2F%2Fg.hiphotos.baidu.com%2Fzhidao%2Fwh%253D600%252C800%2Fsign%3D20641dce29381f309e4c85af99316030%2Fac6eddc451da81cb8790ac3b5366d016082431d5.jpg";

	private static final String url_square_little = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489483680773&di=16b0dd1b0830a5ef67163a3cc988d1f9&imgtype=0&src=http%3A%2F%2Fimg1.3lian.com%2F2015%2Fa2%2F249%2Fd%2F282.jpg";

	private static final String url_square_watch = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489483719004&di=2725753c75e1e1c0fb813035194fe858&imgtype=0&src=http%3A%2F%2Fimg5.duitang.com%2Fuploads%2Fitem%2F201603%2F04%2F20160304221452_VzSKZ.jpeg";

	private static final String url_square_medium = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489483814265&di=e17ea60a204145c561839aaa3a005378&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201601%2F18%2F20160118175135_5HL2Y.thumb.700_0.png";

	private static final String url_square_white = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489483875230&di=c2beb1a2f1beb4c3d4b87183126a967b&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fw%253D580%2Fsign%3D48fadf39a41ea8d38a22740ca70b30cf%2F3daad93f8794a4c2061e8fa90cf41bd5ad6e3911.jpg";

	private static final String url_square_phone = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489483939762&di=21c7876fb0343112b8bacffe9316db51&imgtype=0&src=http%3A%2F%2Fproc.iifs.ifeng.com%2Fblog%2F2015%2F06%2F07%2Fl6KhypeTY7agapqZuLGge6rDu6LXnqdm2q%2521wfGVrbJRrbWSbaGiQwm2XZ5aX3mRmlJLCZJWPYZLBo6eX.jpg";

	private static final String url_square_marry = "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=4263090545,1119852889&fm=23&gp=0.jpg";

	private static final String url_square_tree = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3887848712,2191688090&fm=23&gp=0.jpg";
}
