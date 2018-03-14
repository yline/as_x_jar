# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\study_adt_studio\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keep class * implements java.io.Serializable{ public *;}

-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.**{*;}

#-----------------------------------基础配置-----------------------------------------
#压缩时：指定代码的压缩级别，值在0-7之间
-optimizationpasses 5
#优化时：不优化输入的类文件
#-dontoptimize
#混淆时：包名不混合大小写；
-dontusemixedcaseclassnames
#指定 不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
#不做预校验
-dontpreverify
#混淆时是否记录日志
-verbose
# 混淆时，所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#保护给定的可选属性，例如LineNumberTable, LocalVariableTable, SourceFile, Deprecated, Synthetic, Signature, InnerClass
#不混淆泛型
-keepattributes Signature
#不混淆Annotation
-keepattributes *Annotation*
#抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable
#忽略警告，避免打包时某些警告出现
#-ignorewarning
#------记录生成的日志数据,build/outputs/mapping/release目录下-------
#apk 包内所有 class 的内部结构
-dump class_files.txt
#混淆前后的映射
-printmapping mapping.txt
#未混淆的类和成员
-printseeds seeds.txt
#列出从 apk 中删除的代码
-printusage unused.txt