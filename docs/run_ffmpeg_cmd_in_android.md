## 在Android项目中调用FFmpeg命令

## Step1: 创建支持C++的工程如下图：

- ![image](https://github.com/Victor2018/FFmpegProLib/raw/master/docs/img/create_project.png)

## Step2: 拷贝编译的文件到Android项目
将编译生成的include文件夹覆盖到项目中src/main/cpp/include目录,在libs目录创建armeabi-v7a目录并拷贝编译的libffmpeg.so到此目录,如下图：

- ![image](https://github.com/Victor2018/FFmpegProLib/raw/master/docs/img/project_files.png)

## Step3:配置library模块的build.gradle

android {
    compileSdkVersion 28



    defaultConfig {
        minSdkVersion 15
        targetSdkVersion 28
        versionCode 1
        versionName "1.0"

        testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"

        externalNativeBuild {
            cmake {
                cppFlags ""
            }
            ndk {
                abiFilters "armeabi-v7a"
            }
        }
        sourceSets {
            main {
                //库地址
                jniLibs.srcDirs = ['libs']
            }
        }
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }

    externalNativeBuild {
        cmake {
            path "CMakeLists.txt"
        }
    }
}

## Step4: 配置CMakeLists.txt文件


cmake_minimum_required(VERSION 3.4.1)

add_library(ffmpeg-cmd
        SHARED
        src/main/cpp/cmdutils.c
        src/main/cpp/ffmpeg.c
        src/main/cpp/ffmpeg_filter.c
        src/main/cpp/ffmpeg_opt.c
        src/main/cpp/ffmpeg_cmd.c
        src/main/cpp/ffmpeg_thread.c
        )

find_library(log-lib
            log)

#获取上级目录
get_filename_component(PARENT_DIR ${CMAKE_SOURCE_DIR} PATH)
set(LIBRARY_DIR ${PARENT_DIR}/library)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")
set(CMAKE_VERBOSE_MAKEFILE on)

add_library(ffmpeg
           SHARED
           IMPORTED)

set_target_properties(ffmpeg
                    PROPERTIES IMPORTED_LOCATION
                    ${LIBRARY_DIR}/libs/${ANDROID_ABI}/libffmpeg.so
                    )

#头文件
include_directories(${LIBRARY_DIR}/libs/${ANDROID_ABI}/include src/main/cpp src/main/cpp/include)

target_link_libraries(
        ffmpeg-cmd
        ffmpeg
        -landroid #native_window
        -ljnigraphics #bitmap
        -lOpenSLES #openSLES
        ${log-lib})

## Step5 : 修改FFmpeg源码
- 修改ffmpeg.c中main方法名为exec：
// 修改前
int main(int argc, char **argv)
// 修改后
int exec(int argc, char **argv)

- 在ffmpeg.c的ffmpeg_cleanup 函数执行结束前重新初始化
filtergraphs = NULL;
nb_filtergraphs = 0;
output_files = NULL;
nb_output_files = 0;
output_streams = NULL;
nb_output_streams = 0;
input_files = NULL;
nb_input_files = 0;
input_streams = NULL;
nb_input_streams = 0;

- 在ffmpeg.c的print_report函数中添加代码实现FFmpeg命令执行进度的回调
static void print_report(int is_last_report, int64_t timer_start, int64_t cur_time) {

    // 省略其他代码...

    // 定义已处理的时长
    float mss;

    secs = FFABS(pts) / AV_TIME_BASE;
    us = FFABS(pts) % AV_TIME_BASE;
    // 获取已处理的时长
    mss = secs + ((float) us / AV_TIME_BASE);

    // 调用ffmpeg_progress将进度传到Java层，代码后面定义
    ffmpeg_progress(mss);

    // 省略其他代码...
}

- 在ffmpeg.h头文件中增加方法
int exec(int argc, char **argv);

- 修改cmdutils.c中的exit_program函数
void exit_program(int ret) {
    if (program_exit)
        program_exit(ret);

    // 退出线程(该函数后面定义)
    ffmpeg_thread_exit(ret);

    // 删掉下面这行代码，不然执行结束，应用会crash
    //exit(ret);
}

## Step5:编写JNI调用FFmpeg命令,在项目中查看如下代码
src/main/cpp/ffmpeg_cmd.h
src/main/cpp/ffmpeg_cmd.c
src/main/cpp/ffmpeg_thread.h
src/main/cpp/ffmpeg_thread.c
src/main/java/com.victor.library.FFmpegCmd.java

## Step6: 调用FFmpeg命令
String[] argv = "ffmpeg -y -ss 2 -t 8 -accurate_seek -i /sdcard/DCIM/Camera/dy.mp4 -codec copy /sdcard/ffmpeg_out_video.mp4".split(" ");
private void exec (String[] argv) {
        mFFmpegCmd.exec(argv,0, new FFmpegCmd.OnCmdExecListener() {
            @Override
            public void onSuccess() {
                Log.e(TAG,"exec()-onSuccess......");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTvResult.setText("onSuccess()......");
                    }
                });
            }

            @Override
            public void onFailure(final int ret) {
                Log.e(TAG,"exec()-onFailure......ret = " + ret);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTvResult.setText("onFailure()......ret = " + ret);
                    }
                });
            }

            @Override
            public void onProgress(final float progress) {
                Log.e(TAG,"exec()-progress......" + progress);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTvResult.setText("onProgress()......progress = " + progress);
                    }
                });
            }
        });
    }

## Note:FFmpegUtil工具可以生成一些常用ffmpeg命令

## 参考
- https://xucanhui.com/2018/10/26/android-invoke-ffmpeg-cmd/
- https://github.com/byhook/ffmpeg4android/



