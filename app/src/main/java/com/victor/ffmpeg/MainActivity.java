package com.victor.ffmpeg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.victor.library.FFmpegCmd;
import com.victor.library.FFmpegUtil;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "MainActivity";
    private FFmpegCmd mFFmpegCmd;
    private TextView mTvResult;
    private Button mBtnRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

    }

    private void initialize () {
        mFFmpegCmd = new FFmpegCmd();

        mTvResult = findViewById(R.id.sample_text);
        mBtnRun = findViewById(R.id.btn_run);

        mBtnRun.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_run:
//                exec("ffmpeg -i /sdcard/DCIM/Camera/dy.mp4 /sdcard/ffmpeg_out_video.ts");
                File file = new File("/sdcard/ffmpeg_out_video.mp4");
                if (file.exists()) {
                    file.delete();
                }
                String[] command =  FFmpegUtil.addWaterMark("/sdcard/DCIM/Camera/dy.mp4","/sdcard/logo1.png","/sdcard/output_water.mp4",2);
//                String[] command = FFmpegUtil.screenShot("/sdcard/DCIM/Camera/dy.mp4","1080x720","/sdcard/ffmpeg/output_shot.jpg");
//                String[] command =  FFmpegUtil.multiVideo("/sdcard/DCIM/Camera/dy.mp4","/sdcard/end.mp4","/sdcard/ffmpeg/output_multi.mp4",false);
//                String[] command =  FFmpegUtil.transformVideo("/sdcard/DCIM/Camera/dy.mp4","/sdcard/ffmpeg/output_transform.ts");
//                String[] command =  FFmpegUtil.pictureToVideo("/sdcard/Pictures/1560138792324.jpg","/sdcard/ffmpeg/output_picture.mp4");
//                String[] command =  FFmpegUtil.generateGif("/sdcard/DCIM/Camera/dy.mp4",0,5,"/sdcard/ffmpeg/output_gif.gif");
//                String[] command =  FFmpegUtil.cutVoiceOfVideo("/sdcard/DCIM/Camera/dy.mp4","/sdcard/ffmpeg/output_mute.mp4");
//                String[] command =  FFmpegUtil.getVoiceByVideo("/sdcard/DCIM/Camera/dy.mp4","/sdcard/ffmpeg/output_voice.aac");
//                String[] command =  FFmpegUtil.syncVoiceAndVideo("/sdcard/DCIM/Camera/dy.mp4","/sdcard/ffmpeg/output_voice.aac","/sdcard/ffmpeg/output_sync.mp4");
//                String[] command =  FFmpegUtil.mergeVideo("/sdcard/DCIM/Camera/dy.mp4","/sdcard/end.mp4","/sdcard/ffmpeg/output_concat.mp4");
//                String[] command =  FFmpegUtil.replaceVoiceAddWatermask("/sdcard/Pictures/1560138792324.jpg","/sdcard/dlj.mp3","/sdcard/ffmpeg/output_voice_water.mp4");

                String[] argv = "ffmpeg -y -ss 2 -t 8 -accurate_seek -i /sdcard/dy.mp4 -codec copy /sdcard/ffmpeg_out_video.mp4".split(" ");
                exec(argv);
//                exec(command);
                break;
        }
    }

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
}
