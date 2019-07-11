#include "libavcodec/avcodec.h"
#include "ffmpeg_thread.h"
#include "android_log.h"

pthread_t ntid;
char **argvs = NULL;
int num=0;


void *thread(void *arg)
{   //执行
    int result = exec(num, argvs);
    LOGE("ffmpeg_thread_exec()......result = %s",strerror(result));
    ffmpeg_thread_exit(result);
    return ((void *)0);
}
/**
 * 新建子线程执行ffmpeg命令
 */
int ffmpeg_thread_run_cmd(int cmdnum,char **argv){
    num=cmdnum;
    argvs=argv;
    LOGE("ffmpeg_thread_ffmpeg_thread_run_cmd_exec()......cmdnum = %s",strerror(cmdnum));
    int temp = pthread_create(&ntid,NULL,thread,NULL);
    if(temp!=0)
    {
        LOGE("can't create thread: %s ",strerror(temp));
        return 1;
    }
    LOGE("create thread succes: %s ",strerror(temp));
    return 0;
}

static void (*ffmpeg_callback)(int ret);
/**
 * 注册线程回调
 */
void ffmpeg_thread_callback(void (*cb)(int ret)){
    ffmpeg_callback = cb;
}

/**
 * 退出线程
 */
void ffmpeg_thread_exit(int ret){
    if (ffmpeg_callback) {
        ffmpeg_callback(ret);
    }
    pthread_exit("ffmpeg_thread_exit");
}

/**
 * 取消线程
 */
void ffmpeg_thread_cancel(){
    void *ret=NULL;
    pthread_join(ntid, &ret);
}
