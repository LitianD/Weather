package com.app.zlt.perfectweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.VideoView;

import com.app.zlt.perfectweather.R;

public class BeginActivity extends Activity {

    private Button passButton;//跳过按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_begin);

        //设置背景视频
        final VideoView videoview=(VideoView)findViewById(R.id.videoview);
        final String videopath = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.video2).toString();
        //final String videopath = Uri.parse(getExternalFilesDir(null) + File.separator + "video.mp4").toString();
        videoview.setVideoPath(videopath);
        videoview.start();
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
            }
        });
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoview.setVideoPath(videopath);
                videoview.start();
            }
        });

        passButton = (Button)findViewById(R.id.pass);
        passButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BeginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
