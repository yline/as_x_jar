package com.baidu.tts.sample.check;

import android.content.Context;

import com.yline.application.SDKManager;

import java.io.File;
import java.util.TreeSet;

public class JniCheck implements CheckInterceptor {

    @Override
    public StringBuilder check(StringBuilder lastLog) {
        lastLog.append("--------------------检查4个so文件是否存在--------------------").append("\n");

        Context context = SDKManager.getApplication();
        String[] soNameArray = {"libbd_etts.so", "libBDSpeechDecoder_V1.so", "libbdtts.so", "libgnustl_shared.so"};

        String path = context.getApplicationInfo().nativeLibraryDir;
        lastLog.append("Jni so文件目录 ").append(path).append("\n");

        File[] files = new File(path).listFiles();
        TreeSet<String> treeSet = new TreeSet<>();
        if (files != null) {
            for (File file : files) {
                if (file.canRead()) {
                    treeSet.add(file.getName());
                }
            }
        }

        lastLog.append("Jni目录内文件: ").append(treeSet.toString()).append("\n");
        boolean isLoss = false;
        for (String name : soNameArray) {
            if (!treeSet.contains(name)) {
                isLoss = true;
                lastLog.append("缺少可读的so文件：").append(name).append("\n");
                lastLog.append("如果您的app内没有其它so文件，请复制demo里的src/main/jniLibs至同名目录。")
                        .append("如果app内有so文件，请合并目录放一起(注意目录取交集，多余的目录删除)。").append("\n");
            }
        }

        if (!isLoss){
            lastLog.append("通过").append("\n");
        }

        return lastLog;
    }
}
