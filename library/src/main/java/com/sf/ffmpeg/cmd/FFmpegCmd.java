package com.sf.ffmpeg.cmd;

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: FFmpegCmd.java
 * Author: Victor
 * Date: 2019/7/4 10:54
 * Description: ffmpeg移植Android执行ffmpeg命令（参考https://xucanhui.com/2018/10/26/android-invoke-ffmpeg-cmd/）
 * -----------------------------------------------------------------
 */
public class FFmpegCmd {
    static {
        System.loadLibrary("ffmpeg");
        System.loadLibrary("ffmpeg-cmd");
    }

    private static OnCmdExecListener sOnCmdExecListener;
    private static long sDuration;

    public static native int exec(int argc, String[] argv);

    public static native void exit();

    public static void exec(String[] cmds, long duration, OnCmdExecListener listener) {
        sOnCmdExecListener = listener;
        sDuration = duration;
        if (sOnCmdExecListener != null) {
            sOnCmdExecListener.onStart();
        }
        exec(cmds.length, cmds);
    }

    public static void onExecuted(int ret) {
        if (sOnCmdExecListener != null) {
            if (ret == 0) {
                sOnCmdExecListener.onProgress(sDuration);
                sOnCmdExecListener.onSuccess();
            } else {
                sOnCmdExecListener.onFailure(ret);
            }
        }
    }

    public static void onProgress(float progress)
    {
        if (sOnCmdExecListener != null)
        {
            if (sDuration != 0)
            {
                sOnCmdExecListener.onProgress(progress / (sDuration / 1000) * 0.95f);
            }
        }
    }


    public interface OnCmdExecListener
    {
        void onSuccess();

        void onFailure(int ret);

        void onProgress(float progress);

        void onStart();
    }
}
