
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
