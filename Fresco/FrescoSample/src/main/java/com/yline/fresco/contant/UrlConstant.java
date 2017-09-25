package com.yline.fresco.contant;

import android.util.Log;

import java.util.Random;

public class UrlConstant {
    // 64_64_png
    public static final int Png_64_64_Max = 1;
    public static final String Png_64_64_format = "https://raw.githubusercontent.com/yline/as_lib_sdk/master/pic/64_64_png/%s.png";

    // 128_128_png
    public static final int Png_128_128_Max = 1;
    public static final String Png_128_128_format = "https://raw.githubusercontent.com/yline/as_lib_sdk/master/pic/128_128_png/%s.png";

    // 256_256_png
    public static final int Png_256_256_Max = 1;
    public static final String Png_256_256_format = "https://raw.githubusercontent.com/yline/as_lib_sdk/master/pic/256_256_png/%s.png";

    // 300_150_jpg
    public static final int Jpg_300_150_Max = 1;
    public static final String Jpg_300_150_format = "https://raw.githubusercontent.com/yline/as_lib_sdk/master/pic/300_150_jpg/%s.jpg";

    // 420_300_png
    public static final int Png_420_300_Max = 1;
    public static final String Png_420_300_format = "https://raw.githubusercontent.com/yline/as_lib_sdk/master/pic/420_300_png/%s.png";

    // 420_420_jpg
    public static final int Jpg_420_420_Max = 8;
    public static final String Jpg_420_420_format = "https://raw.githubusercontent.com/yline/as_lib_sdk/master/pic/420_420_jpg/%s.jpg";

    // 480_640_jpg
    public static final int Jpg_480_640_Max = 7;
    public static final String Jpg_480_640_format = "https://raw.githubusercontent.com/yline/as_lib_sdk/master/pic/480_640_jpg/%s.jpg";

    // 640_480_jpg
    public static final int Jpg_640_480_Max = 1;
    public static final String Jpg_640_480_format = "https://raw.githubusercontent.com/yline/as_lib_sdk/master/pic/640_480_jpg/%s.jpg";

    // 640_1366_jpg
    public static final int Jpg_640_1366_Max = 1;
    public static final String Jpg_640_1366_format = "https://raw.githubusercontent.com/yline/as_lib_sdk/master/pic/640_1366_jpg/%s.jpg";

    // 960_640_jpg
    public static final int Jpg_960_640_Max = 8;
    public static final String Jpg_960_640_format = "https://raw.githubusercontent.com/yline/as_lib_sdk/master/pic/960_640_jpg/%s.jpg";

    // 1024_1024_png
    public static final int Png_1024_1024_Max = 1;
    public static final String Png_1024_1024_format = "https://raw.githubusercontent.com/yline/as_lib_sdk/master/pic/1024_1024_png/%s.png";

    // 1360_768_jpg
    public static final int Jpg_1366_768_Max = 3;
    public static final String Jpg_1366_768_format = "https://raw.githubusercontent.com/yline/as_lib_sdk/master/pic/1366_768_jpg/%s.jpg";

    // 1440_900_jpg
    public static final int Jpg_1440_900_Max = 1;
    public static final String Jpg_1440_900_format = "https://raw.githubusercontent.com/yline/as_lib_sdk/master/pic/1440_900_jpg/%s.jpg";

    // 1920_1280_jpg
    public static final int Jpg_1920_1280_Max = 9;
    public static final String Jpg_1920_1280_format = "https://raw.githubusercontent.com/yline/as_lib_sdk/master/pic/1920_1280_jpg/%s.jpg";

    // gif
    public static final int Gif_Max = 3;
    public static final String Gif_format = "https://raw.githubusercontent.com/yline/as_lib_sdk/master/pic/gif/%s.gif";

    // static webp
    public static final int Webp_Static_Max = 2;
    public static final String Webp_Static_format = "https://raw.githubusercontent.com/yline/as_lib_sdk/master/pic/static_webp/%s.webp";

    // dynamic webp
    public static final int Webp_Dynamic_Max = 1;
    public static final String Webp_Dynamic_format = "https://raw.githubusercontent.com/yline/as_lib_sdk/master/pic/dynamic_webp/%s.webp";

    public static Random random = new Random();

    // 分文件夹
    public static String getPng_64_64(int position) {
        return get(Png_64_64_format, Png_64_64_Max, position);
    }

    public static String getPng_128_128(int position) {
        return get(Png_128_128_format, Png_128_128_Max, position);
    }

    public static String getPng_256_256(int position) {
        return get(Png_256_256_format, Png_256_256_Max, position);
    }

    public static String getJpg_300_150(int position) {
        return get(Jpg_300_150_format, Jpg_300_150_Max, position);
    }

    public static String getPng_420_300(int position) {
        return get(Png_420_300_format, Png_420_300_Max, position);
    }

    public static String getJpg_420_420(int position) {
        return get(Jpg_420_420_format, Jpg_420_420_Max, position);
    }

    public static String getJpg_480_640(int position) {
        return get(Jpg_480_640_format, Jpg_480_640_Max, position);
    }

    public static String getJpg_640_480(int position) {
        return get(Jpg_640_480_format, Jpg_640_480_Max, position);
    }

    public static String getJpg_640_1366(int position) {
        return get(Jpg_640_1366_format, Jpg_640_1366_Max, position);
    }

    public static String getJpg_960_640(int position) {
        return get(Jpg_960_640_format, Jpg_960_640_Max, position);
    }

    public static String getPng_1024_1024(int position) {
        return get(Png_1024_1024_format, Png_1024_1024_Max, position);
    }

    public static String getJpg_1366_768(int position) {
        return get(Jpg_1366_768_format, Jpg_1366_768_Max, position);
    }

    public static String getJpn_1440_900(int position) {
        return get(Jpg_1440_900_format, Jpg_1440_900_Max, position);
    }

    public static String getJpn_1920_1280(int position) {
        return get(Jpg_1920_1280_format, Jpg_1920_1280_Max, position);
    }

    public static String getGif(int position) {
        return get(Gif_format, Gif_Max, position);
    }

    public static String getWebp_Static(int position) {
        return get(Webp_Static_format, Webp_Static_Max, position);
    }

    public static String getWebp_Dynamic(int position) {
        return get(Webp_Dynamic_format, Webp_Dynamic_Max, position);
    }

    // 长方形
    public static String getUrlRec() {
        final int position = random.nextInt(Integer.MAX_VALUE);
        final int caseIndex = position % 3;

        switch (caseIndex) {
            case 0:
                return getJpg_640_1366(position);
            case 1:
                return getJpg_960_640(position);
            case 3:
                return getJpn_1440_900(position);
            case 4:
                return getJpn_1920_1280(position);
            default:
                return getJpg_640_1366(position);
        }
    }

    // 正方形
    public static String getUrlSquare() {
        final int position = random.nextInt(Integer.MAX_VALUE);
        final int caseIndex = position % 5;

        switch (caseIndex) {
            case 0:
                return getPng_64_64(position);
            case 1:
                return getPng_128_128(position);
            case 2:
                return getPng_256_256(position);
            case 4:
                return getPng_1024_1024(position);
            default:
                return getPng_64_64(position);
        }
    }

    // 随机产生一个
    public static String getUrl() {
        final int position = random.nextInt(Integer.MAX_VALUE);
        final int caseIndex = position % 15;

        switch (caseIndex) {
            case 0:
                return getPng_64_64(position);
            case 1:
                return getPng_128_128(position);
            case 2:
                return getPng_256_256(position);
            case 4:
                return getJpg_300_150(position);
            case 5:
                return getPng_420_300(position);
            case 6:
                return getJpg_420_420(position);
            case 7:
                return getJpg_480_640(position);
            case 8:
                return getJpg_640_480(position);
            case 9:
                return getJpg_640_1366(position);
            case 10:
                return getJpg_960_640(position);
            case 11:
                return getPng_1024_1024(position);
            case 12:
                return getJpg_1366_768(position);
            case 13:
                return getJpn_1440_900(position);
            case 14:
                return getJpn_1920_1280(position);
            default:
                return getPng_64_64(position);
        }
    }

    // 衍生出来的
    public static String getPng_64_64() {
        int position = random.nextInt(Integer.MAX_VALUE);
        return getPng_64_64(position);
    }

    public static String getPng_128_128() {
        int position = random.nextInt(Integer.MAX_VALUE);
        return getPng_128_128(position);
    }

    public static String getPng_256_256() {
        int position = random.nextInt(Integer.MAX_VALUE);
        return getPng_256_256(position);
    }

    public static String getJpg_300_150() {
        int position = random.nextInt(Integer.MAX_VALUE);
        return getJpg_300_150(position);
    }

    public static String getPng_420_300() {
        int position = random.nextInt(Integer.MAX_VALUE);
        return getPng_420_300(position);
    }

    public static String getJpg_420_420() {
        int position = random.nextInt(Integer.MAX_VALUE);
        return getJpg_420_420(position);
    }

    public static String getJpg_480_640() {
        int position = random.nextInt(Integer.MAX_VALUE);
        return getJpg_480_640(position);
    }

    public static String getJpg_640_480() {
        int position = random.nextInt(Integer.MAX_VALUE);
        return getJpg_640_480(position);
    }

    public static String getJpg_960_640() {
        int position = random.nextInt(Integer.MAX_VALUE);
        return getJpg_960_640(position);
    }

    public static String getJpg_640_1366() {
        int position = random.nextInt(Integer.MAX_VALUE);
        return getJpg_640_1366(position);
    }

    public static String getPng_1024_1024() {
        int position = random.nextInt(Integer.MAX_VALUE);
        return getPng_1024_1024(position);
    }

    public static String getJpg_1366_768() {
        int position = random.nextInt(Integer.MAX_VALUE);
        return getJpg_1366_768(position);
    }

    public static String getJpn_1440_900() {
        int position = random.nextInt(Integer.MAX_VALUE);
        return getJpn_1440_900(position);
    }

    public static String getJpn_1920_1280() {
        int position = random.nextInt(Integer.MAX_VALUE);
        return getJpn_1920_1280(position);
    }

    public static String getGif() {
        int position = random.nextInt(Integer.MAX_VALUE);
        return getGif(position);
    }

    public static String getWebp_Static() {
        int position = random.nextInt(Integer.MAX_VALUE);
        return get(Webp_Static_format, Webp_Static_Max, position);
    }

    public static String getWebp_Dynamic() {
        int position = random.nextInt(Integer.MAX_VALUE);
        return get(Webp_Dynamic_format, Webp_Dynamic_Max, position);
    }

    // 基本方法

    /**
     * 校验位置
     */
    private static int assertPosition(int position, int max) {
        if (position < 1) {
            position = position > 0 ? position : -position;
            return position % max + 1;
        }

        if (position > max) {
            return position % max + 1;
        }

        return position;
    }

    /**
     * 生成一个String
     *
     * @param format   上面定义的 Format
     * @param max      上面定义的 Max
     * @param position 位置
     * @return 可访问的png位置
     */
    public static String get(String format, int max, int position) {
        int newPosition = assertPosition(position, max);
        String resultString = String.format(format, String.format("%02d", newPosition));
        Log.i("xxx-url", "get: urlString = " + resultString);

        return resultString;
    }
}
