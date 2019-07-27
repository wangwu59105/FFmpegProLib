#!/bin/bash

export AOSP_API=android-21
export AOSP_ABI=armeabi-v7a
export AOSP_ARCH=arch-arm
export FF_EXTRA_CFLAGS=""
export FF_CFLAGS="-O3 -Wall -pipe -ffast-math -fstrict-aliasing -Werror=strict-aliasing -Wno-psabi -Wa,--noexecstack -DANDROID"

#填写你具体的ndk解压目录
export NDK=/Users/victor/Documents/DevelopTools/android-ndk-r10e
export PLATFORM=$NDK/platforms/$AOSP_API/$AOSP_ARCH
export TOOLCHAIN=$NDK/toolchains/arm-linux-androideabi-4.9/prebuilt/darwin-x86_64


#编译后的文件会放置在 当前路径下的android/$AOSP_ABI／下
export PREFIX=$(pwd)/android/$AOSP_ABI
export ADDI_CFLAGS="-marm"
#./configure 即为ffmpeg 根目录下的可执行文件configure
#你可以在ffmpeg根目录下使用./configure --hellp 查看 ./configure后可填入的参数。

./configure \
	--prefix=$PREFIX \
	--enable-cross-compile \
	--disable-runtime-cpudetect \
	--disable-asm \
	--arch=$AOSP_ABI \
	--target-os=android \
	--cc=$TOOLCHAIN/bin/arm-linux-androideabi-gcc \
	--cross-prefix=$TOOLCHAIN/bin/arm-linux-androideabi- \
	--disable-stripping \
	--nm=$TOOLCHAIN/bin/arm-linux-androideabi-nm \
	--sysroot=$PLATFORM \
	--enable-gpl \
	--enable-nonfree \
	--disable-shared \
	--enable-static \
	--enable-version3 \
	--enable-pthreads \
	--enable-small \
	--disable-vda \
	--disable-iconv \
	--disable-encoders \
	--enable-neon \
	--enable-yasm \
	--enable-encoder=mpeg4 \
	--enable-encoder=mjpeg \
	--enable-encoder=png \
	--enable-nonfree \
	--enable-muxers \
	--enable-muxer=mov \
	--enable-muxer=mp4 \
	--enable-muxer=h264 \
	--enable-muxer=avi \
	--enable-decoder=h264 \
	--enable-decoder=mpeg4 \
	--enable-decoder=mjpeg \
	--enable-decoder=png \
	--enable-demuxer=image2 \
	--enable-demuxer=h264 \
	--enable-demuxer=avi \
	--enable-demuxer=mpc \
	--enable-demuxer=mpegts \
	--enable-demuxer=mov \
	--enable-parser=ac3 \
	--enable-parser=h264 \
	--enable-protocols \
	--enable-zlib \
	--enable-avfilter \
	--enable-avresample \
	--enable-postproc \
	--enable-avdevice \
	--disable-outdevs \
	--disable-ffprobe \
	--disable-ffplay \
	--disable-ffmpeg \
	--disable-ffserver \
	--disable-debug \
	--disable-symver \
	--disable-stripping \
	--extra-cflags="$FF_EXTRA_CFLAGS  $FF_CFLAGS" \
	--extra-ldflags=""
        
	
make clean
make -j8
make install


#合并

$TOOLCHAIN/bin/arm-linux-androideabi-ld -rpath-link=$PLATFORM/usr/lib -L$PLATFORM/usr/lib -L$PREFIX/lib -soname libffmpeg.so -shared -nostdlib -Bsymbolic --whole-archive --no-undefined -o $PREFIX/libffmpeg.so \
    libavcodec/libavcodec.a \
    libavfilter/libavfilter.a \
    libavresample/libavresample.a \
    libswresample/libswresample.a \
    libavformat/libavformat.a \
    libavutil/libavutil.a \
    libswscale/libswscale.a \
    libpostproc/libpostproc.a \
    libavdevice/libavdevice.a \
    -lc -lm -lz -ldl -llog --dynamic-linker=/system/bin/linker $TOOLCHAIN/lib/gcc/arm-linux-androideabi/4.9/libgcc.a


#编译完成拷贝需要的文件到android/$AOSP_ABI/指定目录
mkdir android/$AOSP_ABI/include/compat

cp compat/va_copy.h android/$AOSP_ABI/include/compat/va_copy.h

cp libavcodec/mathops.h android/$AOSP_ABI/include/libavcodec/mathops.h

cp libavdevice/avdevice.h android/$AOSP_ABI/include/libavdevice/avdevice.h
cp libavdevice/version.h android/$AOSP_ABI/include/libavdevice/version.h

cp libavformat/ffm.h android/$AOSP_ABI/include/libavformat/ffm.h
cp libavformat/network.h android/$AOSP_ABI/include/libavformat/network.h
cp libavformat/os_support.h android/$AOSP_ABI/include/libavformat/os_support.h
cp libavformat/url.h android/$AOSP_ABI/include/libavformat/url.h


cp libavutil/libm.h android/$AOSP_ABI/include/libavutil/libm.h
cp libavutil/internal.h android/$AOSP_ABI/include/libavutil/internal.h
cp libavutil/reverse.h android/$AOSP_ABI/include/libavutil/reverse.h
cp libavutil/timer.h android/$AOSP_ABI/include/libavutil/timer.h


cp libpostproc/postprocess.h android/$AOSP_ABI/include/libpostproc/postprocess.h
cp libpostproc/version.h android/$AOSP_ABI/include/libpostproc/version.h

#cpp

mkdir android/$AOSP_ABI/cpp

cp cmdutils.c android/$AOSP_ABI/cpp/cmdutils.c
cp cmdutils.h android/$AOSP_ABI/cpp/cmdutils.h
cp cmdutils_common_opts.h android/$AOSP_ABI/cpp/cmdutils_common_opts.h

cp config.h android/$AOSP_ABI/cpp/config.h

cp ffmpeg.h android/$AOSP_ABI/cpp/ffmpeg.h
cp ffmpeg.c android/$AOSP_ABI/cpp/ffmpeg.c
cp ffmpeg_filter.c android/$AOSP_ABI/cpp/ffmpeg_filter.c
cp ffmpeg_opt.c android/$AOSP_ABI/cpp/ffmpeg_opt.c
