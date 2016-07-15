#!/bin/bash
cd ../BaiduNaviSDK
chmod +x build.sh
./build.sh

if [ $? -ne 0 ]
then  
exit 1
fi

cd ../BaiduNaviSDKDemo
ant clean
rm -rf bin

if [ $? -ne 0 ]
then  
exit 1
fi

date +%m%d%H%M>assets/build

ant release

if [ $? -ne 0 ]
then  
exit 1
fi

