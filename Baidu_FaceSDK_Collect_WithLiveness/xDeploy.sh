#!/bin/sh
##############################################################################################
# 导出sdk功能
# 作者：yangrui09
#
##############################################################################################
echo   ------- Begin -------

# config agile compile 
ANDROID_SDK_HOME=/home/scmtools/buildkit/android-sdk
if [ -d "$ANDROID_SDK_HOME" ]; then  
    echo "ANDROID_SDK_HOME exist,so current is aigile"
	JAVA_HOME=/home/scmtools/buildkit/jdk-1.8u92
    ANDROID_NDK_HOME=/home/scmtools/buildkit/android-ndk-r12b
	JAVA_GRADLE=/home/scmtools/buildkit/gradle/gradle-2.4/bin
	PATH=$JAVA_GRADLE:$ANDROID_SDK_HOME:$JAVA_HOME:$ANDROID_NDK_HOME:$PATH 
fi  

clear
date

#定义当前工作空间
BDFACE_WORKSPACE_DIR=`pwd`
BDFACE_WORKSPACE="${BDFACE_WORKSPACE_DIR}"
BDFACE_WORKSPACE_OUTPUT="${BDFACE_WORKSPACE_DIR}/output"
workpath=$(cd `dirname $0`; pwd)
outputpath=$workpath"/output"
outputsdklibspath=$outputpath"/libs"
outputsdkassetpath=$outputpath"/assets"
outputsdkdemopath=$outputpath"/demo"

echo "java evn:" `java -version`
echo 1.------- create dir -------

rm -rf $outputpath
mkdir $outputpath
echo $outputpath
rm -rf $outputsdklibspath
mkdir $outputsdklibspath
rm -rf $outputsdkassetpath
mkdir $outputsdkassetpath
rm -rf $outputsdkdemopath
mkdir $outputsdkdemopath

echo   ---------------------------------------------------------------
echo 2 ------- begin build -------
echo "${BDFACE_WORKSPACE}"
cd "${BDFACE_WORKSPACE}"
echo  -------  build clean-------
chmod +x $workpath"/gradlew"
./gradlew clean
echo  -------  build assembleRelease-------
./gradlew assembleRelease

echo   ---------------------------------------------------------------
echo 3 ------- copy assets -------
sdkassetpath=$workpath"/app/src/main/assets"
cp $sdkassetpath/*.*  $outputsdkassetpath
sdkassetpath=$workpath"/faceplatform/src/main/assets"
cp $sdkassetpath/*.*  $outputsdkassetpath

echo   ---------------------------------------------------------------
echo 4.1 ------- copy FaceSDK lib -------

#faceplatform
faceplatformjarspath=$workpath"/faceplatform/build/intermediates/bundles/default/libs/*"
faceplatformclassspath=$workpath"/faceplatform/build/intermediates/bundles/default/classes.jar"
faceplatformaarpath=$workpath"/faceplatform/build/outputs/aar/*"


#faceplatform-ui
faceplatform_uisopath=$workpath"/faceplatform-ui/build/intermediates/bundles/default/jni/*"
faceplatform_uiclassspath=$workpath"/faceplatform-ui/build/intermediates/bundles/default/classes.jar"
faceplatform_uiaarpath=$workpath"/faceplatform-ui/build/outputs/aar/*"

ls $workpath"/faceplatform-ui/build/intermediates/bundles/default/jni/armeabi-v7a/"
cp -rf $faceplatform_uisopath $outputsdklibspath
echo ====after copy==
ls $outputsdklibspath"/armeabi-v7a/"


cp -rf $faceplatformaarpath $outputsdklibspath
cp -rf $faceplatform_uiaarpath $outputsdklibspath

cp -rf $faceplatformjarspath $outputsdklibspath
cp -f $faceplatformclassspath $outputsdklibspath"/faceplatfrom.jar"


cp -f $faceplatform_uiclassspath $outputsdklibspath"/faceplatfrom_ui.jar"



echo   ---------------------------------------------------------------
echo 4.2 ------- copy demo  -------
demoapppath=$workpath"/app"
buildgradlepath=$workpath"/build.gradle"
gradlepath=$workpath"/gradle"
gradlepropertiespath=$workpath"/gradle.properties"
gradlewpath=$workpath"/gradlew"
localpropertiespath=$workpath"/local.properties"
settingsgradlepath=$workpath"/settings.gradle"
cp -r -f $demoapppath $outputsdkdemopath
cp $buildgradlepath $outputsdkdemopath
cp -r $gradlepath $outputsdkdemopath
cp $gradlepropertiespath $outputsdkdemopath
cp $gradlewpath $outputsdkdemopath
cp $localpropertiespath $outputsdkdemopath
cp $settingsgradlepath $outputsdkdemopath

echo   ---------------------------------------------------------------
echo 4.3 copy sdklib to demo

cp -r $outputsdklibspath  $outputsdkdemopath"/app/"

echo
echo 4.4 copy others
democoverpath=$workpath"/tools/demo/"
cp -f $democoverpath"settings.gradle" $outputsdkdemopath"/settings.gradle"
cp -f $democoverpath"/app/build.gradle" $outputsdkdemopath"/app/build.gradle"

echo   ---------------------------------------------------------------
echo 5.0 ------- create apk demo -------


demoapkpath=$demoapppath"/build/outputs/apk/*"
cp -rf $demoapkpath $outputsdkdemopath
#删除demo中的build目录
rm -r $outputsdkdemopath"/app/build/"
rm -r $outputsdkdemopath"/build/"

echo   ---------------------------------------------------------------
echo 6.0 ------- to zip -------
cd $outputpath
time="`date +%Y%m%d%H%M%S`"
#SMT_${filename}_${channel}_${vn}_${vc}_${time}.apk
zip -r IDL_SDK_UI_${time}_R.zip .

# clean old products
function BDFaceCleanOldProducts()
{
    # output最终输出产物路径
    #rm -rf "${BDFACE_WORKSPACE_OUTPUT}"
    #mkdir -p "${BDFACE_WORKSPACE_OUTPUT}"

    # 清除多余文件
    rm -rf $outputsdklibspath
    rm -rf $outputsdkassetpath
    rm -rf $outputsdkdemopath
    #rm -rf $outputpath"/app-debug.apk"
}

# copy需要zip的产物
function BDFaceCopyProducts()
{
    faceOutputAll=$outputpath"/*"
    cp -rf $faceOutputAll "${BDFACE_WORKSPACE_OUTPUT}"
}

echo 7.0 -------clear temp file -------
BDFaceCleanOldProducts
echo   ------- copy final file to directory whitch name  output  -------
BDFaceCopyProducts

echo   ------- End -------