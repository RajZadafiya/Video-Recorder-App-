package com.example.myvideorecorder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    private static int CAMERA_PERMISSION_CODE = 100;
    private static int VIDEO_RECORD_CODE = 101;
    private Uri videopath;
    VideoView videoView;
    Button play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = findViewById(R.id.play);
        videoView = findViewById(R.id.videoView);

        if (iscamerapresentinphone()){
            Log.d("VIDEO_RECORD_TAG","Camera is detected...");
            getCamerapermission();
        }else {
            Log.d("VIDEO_RECORD_TAG","No Camera is detected...");
        }

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaController mediaController= new MediaController(MainActivity.this);
                mediaController.setAnchorView(videoView);
                videoView.setMediaController(mediaController);
                videoView.setVideoURI(videopath);
                videoView.requestFocus();
                videoView.start();
            }
        });


    }

    public void recordvideobtnpressed(View view){
        recordVideo();

    }

    private boolean iscamerapresentinphone(){
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
            return true;
        }else {
            return false;
        }
    }

    private void getCamerapermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION_CODE);
        }
    }

    private void recordVideo(){
        Intent i = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(i,VIDEO_RECORD_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VIDEO_RECORD_CODE) {
            if (resultCode == RESULT_OK) {

                videopath = data.getData();
                Log.d("VIDEO_RECORD_TAG","Video is Recorded and Available at path " + videopath );
            }else if (resultCode == RESULT_CANCELED){
                Log.d("VIDEO_RECORD_TAG","Recorded Video is Cancelled ");
            }else {
                Log.d("VIDEO_RECORD_TAG","Recorded Video has got some error");
            }


        }
    }
}