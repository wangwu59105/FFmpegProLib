#!/bin/bash

for arch in armeabi armeabi-v7a arm64-v8a x86 x86_64
do
    bash build_ffmpeg_merge.sh $arch
done

echo "armeabi armeabi-v7a arm64-v8a x86 x86_64 build all success"