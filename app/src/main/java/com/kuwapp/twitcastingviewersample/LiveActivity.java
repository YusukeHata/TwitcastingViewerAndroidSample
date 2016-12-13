package com.kuwapp.twitcastingviewersample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.VideoView;

public class LiveActivity extends AppCompatActivity {

    private static final String KEY_HLS = "key_hls";

    public static void startActivity(Context context, String hlsUrl) {
        Intent intent = new Intent(context, LiveActivity.class);
        intent.putExtra(KEY_HLS, hlsUrl);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        String hlsUrl = getIntent().getStringExtra(KEY_HLS);
        VideoView videoView = (VideoView) findViewById(R.id.video_view);
        videoView.setVideoPath(hlsUrl);
        videoView.setOnPreparedListener(mediaPlayer -> mediaPlayer.start());
    }

}
