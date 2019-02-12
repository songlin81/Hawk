package com.chubbymobile.wwh.hawk.Logon;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.VideoView;

import com.chubbymobile.wwh.hawk.R;

public class LoginActivity extends AppCompatActivity
{
    private VideoView mVideoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        mVideoView = (VideoView) findViewById(R.id.videoview);
        mVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video));
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //mute video
                mp.setVolume(0f, 0f);
                mVideoView.start();
            }
        });
        //loop to play
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mVideoView.start();
            }
        });
    }

    //restart vid once back
    @Override
    protected void onRestart() { initView(); super.onRestart(); }

    //avoid playing while exiting
    @Override protected void onStop() {
        mVideoView.stopPlayback();
        super.onStop();
    }
}
