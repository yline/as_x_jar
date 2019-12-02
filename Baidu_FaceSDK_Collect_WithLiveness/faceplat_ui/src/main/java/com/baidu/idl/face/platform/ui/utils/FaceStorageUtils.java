package com.baidu.idl.face.platform.ui.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FaceStorageUtils {
    private static String sPath = null; // 图片路径

    /**
     * 图片路径
     *
     * @return /storage/emulated/0/Android/data/包名/files/image
     */
    public static synchronized String getPath(Context context) {
        if (null == sPath) {
            File dirFile = context.getExternalFilesDir("image");
            if (null != dirFile) {
                sPath = dirFile.getAbsolutePath();
            } else if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                sPath = Environment.getExternalStorageDirectory().getPath();
            } else {
                sPath = context.getFilesDir().getAbsolutePath();
            }
        }
        return sPath;
    }

    public static byte[] path2Bytes(String path, String name) {
        if (TextUtils.isEmpty(path) || TextUtils.isEmpty(name)) {
            return null;
        }

        File dirFile = new File(path);
        File file = createFile(dirFile, name);
        return null != file ? path2Bytes(file) : null;
    }

    /**
     * 从路径中读取bytes
     *
     * @param file 文件
     * @return bytes数组，null if Exception
     */
    private static byte[] path2Bytes(File file) {
        if (null == file) {
            return null;
        }

        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream();

            byte[] temp = new byte[1024];
            int n;
            while ((n = fis.read(temp)) != -1) {
                bos.write(temp, 0, n);
            }

            return bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != bos) {
                    bos.close();
                }

                if (null != fis) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * bytes转文件路径
     *
     * @param fileBytes bytes内容
     * @return 文件路径，null if Exception
     */
    public static String bytes2Path(String path, byte[] fileBytes) {
        if (null == path || null == fileBytes) {
            return null;
        }

        File dirFile = new File(path);
        File file = createFile(dirFile, System.currentTimeMillis() + ".jpg");
        if (null == file) {
            return null;
        }

        // 内容，转，文件，并返回文件路径
        return bytes2Path(fileBytes, file);
    }

    /**
     * bytes转文件路径
     *
     * @param fileBytes bytes内容
     * @param file      文件，要求路径上文件必须存在
     * @return 文件路径，null if Exception
     */
    private static String bytes2Path(byte[] fileBytes, File file) {
        if (null == fileBytes || null == file || !file.exists()) {
            return null;
        }

        FileOutputStream fos = null;
        BufferedOutputStream bos = null;

        try {
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);

            bos.write(fileBytes);

            return file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != bos) {
                    bos.close();
                }

                if (null != fos) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * 构建一个文件,真实的创建
     *
     * @param dirFile  文件的目录 such as /storage/emulated/0/Yline/Log/
     * @param fileName 文件名     such as log.txt
     * @return file or null
     */
    private static File createFile(File dirFile, String fileName) {
        if (null == dirFile || TextUtils.isEmpty(fileName)) {
            return null;
        }

        if (!dirFile.exists() || dirFile.isFile()) {
            dirFile.mkdirs();
        }

        File file = new File(dirFile, fileName);
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    return file;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return file;
        }

        return null;
    }

    /**
     * 删除路径上的文件，递归全删
     *
     * @param path 文件路径
     * @return true if delete success
     */
    public static boolean delete(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            boolean isSuccess = true;
            for (String childPath : file.list()) {
                isSuccess = isSuccess && delete(path + File.separator + childPath); // 删除子文件
            }

            isSuccess = isSuccess && file.delete(); // 删除自己
            return isSuccess;
        }

        if (file.exists()) {
            return file.delete();
        }
        return false;
    }
}
