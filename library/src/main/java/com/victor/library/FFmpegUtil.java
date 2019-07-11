package com.victor.library;

import android.annotation.SuppressLint;

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: FFmpegUtil.java
 * Author: Victor
 * Date: 2019/6/25 10:16
 * Description:
 * -----------------------------------------------------------------
 */
public class FFmpegUtil {
    /**
     * 使用ffmpeg命令行给视频添加水印
     * @param srcFile 源文件
     * @param waterMark 水印文件路径
     * @param targetFile 目标文件
     * @return 添加水印后的文件
     */
    public static  String[] addWaterMark(String srcFile, String waterMark, String targetFile, int waterMarkLoc) {
        //20:20 左上角
        //main_w-overlay_w-10:10 右上角
        //10:main_h-overlay_h-10 左下角
        //main_w-overlay_w-10 : main_h-overlay_h-10 右下角
        String loc = "20:20";
        switch (waterMarkLoc) {
            case 0: loc = "20:20"; break;
            case 1: loc = "main_w-overlay_w-20:20";break;
            case 2: loc = "20:main_h-overlay_h-20";break;
            case 3: loc = "main_w-overlay_w-20:main_h-overlay_h-20";break;

        }
        String waterMarkCmd = "ffmpeg -i %s -i %s -filter_complex overlay=%s %s";
        waterMarkCmd = String.format(waterMarkCmd, srcFile, waterMark, loc,targetFile);
        return waterMarkCmd.split(" ");//以空格分割为字符串数组
    }

    /**
     * 使用ffmpeg命令行进行视频剪切
     * @param srcFile 源文件
     * @param startTime 剪切的开始时间(单位为秒)
     * @param duration 剪切时长(单位为秒)
     * @param targetFile 目标文件
     * @return 剪切后的文件
     */
    @SuppressLint("DefaultLocale")
    public static  String[] cutVideo(String srcFile, int startTime, int duration, String targetFile){
        String cutVideoCmd = "-i %s -ss %d -t %d %s";
        cutVideoCmd = String.format(cutVideoCmd, srcFile, startTime, duration, targetFile);
        return cutVideoCmd.split(" ");//以空格分割为字符串数组
    }

    /**
     * 多画面拼接视频
     * @param input1 输入文件1
     * @param input2 输入文件2
     * @param isVertical 视频布局
     * @param targetFile 画面拼接文件
     *
     * @return 画面拼接的命令行
     */
    public static  String[] multiVideo(String input1, String input2, String targetFile, boolean isVertical){
        String multiVideo = "-i %s -i %s -filter_complex hstack %s";//hstack:水平拼接，默认
        if(isVertical){//vstack:垂直拼接
            multiVideo = multiVideo.replace("hstack", "vstack");
        }
        multiVideo = String.format(multiVideo, input1, input2, targetFile);
        return multiVideo.split(" ");
    }

    /**
     * 使用ffmpeg命令行进行视频截图
     * @param srcFile 源文件
     * @param size 图片尺寸大小
     * @param targetFile 目标文件
     * @return 截图后的文件
     */
    public static  String[] screenShot(String srcFile, String size, String targetFile){
        String screenShotCmd = "-i %s -f image2 -t 0.001 -s %s %s";
        screenShotCmd = String.format(screenShotCmd, srcFile, size, targetFile);
        return screenShotCmd.split(" ");//以空格分割为字符串数组
    }

    /**
     * 使用ffmpeg命令行进行视频转码
     * @param srcFile 源文件
     * @param targetFile 目标文件（后缀指定转码格式）
     * @return 转码后的文件
     */
    public static String[] transformVideo(String srcFile, String targetFile){
        //指定目标视频的帧率、码率、分辨率
//        String transformVideoCmd = "-i %s -r 25 -b 200 -s 1080x720 %s";
        String transformVideoCmd = "-i %s -vcodec copy -acodec copy %s";
        transformVideoCmd = String.format(transformVideoCmd, srcFile, targetFile);
        return transformVideoCmd.split(" ");//以空格分割为字符串数组
    }

    /**
     * 使用ffmpeg命令行进行图片合成视频
     * @param srcFile 源文件
     * @param targetFile 目标文件(mpg格式)
     * @return 合成的视频文件
     */
    @SuppressLint("DefaultLocale")
    public static  String[] pictureToVideo(String srcFile, String targetFile){
        //-f image2：代表使用image2格式，需要放在输入文件前面
        String combineVideo = "-f image2 -r 1 -i %s -vcodec mpeg4 %s";
        combineVideo = String.format(combineVideo, srcFile, targetFile);
        return combineVideo.split(" ");//以空格分割为字符串数组
    }

    /**
     * 使用ffmpeg命令行进行视频转成Gif动图
     * @param srcFile 源文件
     * @param startTime 开始时间
     * @param duration 截取时长
     * @param targetFile 目标文件
     * @return Gif文件
     */
    @SuppressLint("DefaultLocale")
    public static  String[] generateGif(String srcFile, int startTime, int duration, String targetFile){
        //String screenShotCmd = "ffmpeg -i %s -vframes %d -f gif %s";
        String screenShotCmd = "-i %s -ss %d -t %d -s 720x1280 -f gif %s";
        screenShotCmd = String.format(screenShotCmd, srcFile, startTime, duration, targetFile);
        return screenShotCmd.split(" ");//以空格分割为字符串数组
    }

    /**
     * // 去掉视频中的音频
     * @param srcFile
     * @param targetFile
     * @return
     */
    public static String[] cutVoiceOfVideo (String srcFile, String targetFile) {
        String cmd = "-i %s -vcodec copy -an %s";
        cmd = String.format(cmd,srcFile,targetFile);
        return cmd.split(" ");
    }

    /**
     * 提取视频中的音频
     * @param srcFile
     * @param targetFile
     * @return
     */
    public static String[] getVoiceByVideo (String srcFile, String targetFile) {
        String cmd = "-i %s -acodec copy -vn %s";
        cmd = String.format(cmd,srcFile,targetFile);
        return cmd.split(" ");
    }

    /**
     * 音视频合成
     * @param voiceFile
     * @param targetFile
     * @return
     */
    public static String[] syncVoiceAndVideo (String videoFile, String voiceFile, String targetFile) {
        String cmd = "-i concat:%s|%s -acodec copy %s";
        cmd = String.format(cmd,videoFile,voiceFile,targetFile);
        return cmd.split(" ");
    }
    /**
     * 合并视频
     * @param videoFile1
     * @param videoFile2
     * @param targetFile
     * @return
     */
    public static String[] mergeVideo (String videoFile1, String videoFile2, String targetFile) {
        String cmd = "-i %s -i %s -strict experimental -filter_complex [0:0][0:1][1:0][1:1]concat=n=2:v=1:a=1 %s";
        cmd = String.format(cmd,videoFile1,videoFile2,targetFile);
        return cmd.split(" ");
    }

    /**
     * 添加水印替换音乐
     * @param imgFile
     * @param voiceFile
     * @param targetFile
     * @return
     */
    public static String[] replaceVoiceAddWatermask (String imgFile, String voiceFile, String targetFile) {
        String cmd = "-loop 1 -i %s -i %s -strict experimental -s 1280x720 -r 25 -aspect 16:9 -vcodec mpeg4 -vcodec mpeg4 -ab 48000 -ac 2 -b 2097152 -ar 22050 -shortest %s";
        cmd = String.format(cmd,imgFile,voiceFile,targetFile);
        return cmd.split(" ");
    }
}
