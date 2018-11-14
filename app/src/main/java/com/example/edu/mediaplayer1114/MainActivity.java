package com.example.edu.mediaplayer1114;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    MediaPlayer mediaPlayer;
    MediaRecorder myAudioRecorder;
    Button play, pause, record, recordStop, recordPlay;
    String outputFile = null;

    private final int REQUEST_CODE = 10;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 || grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("", "Permission has been granted by user");
                }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
        }

        play = findViewById(R.id.buttonPlay);
        play.setOnClickListener(this);
        pause = findViewById(R.id.buttonPause);
        pause.setOnClickListener(this);
        mediaPlayer = MediaPlayer.create(this, R.raw.waste_it_on_me);
//        mediaPlayer.start(); 이것으로 동작할 수 있다.

        record = findViewById(R.id.buttonRecord);
        record.setOnClickListener(this);
        recordStop = findViewById(R.id.buttonRecordStop);
        recordStop.setOnClickListener(this);
        recordPlay = findViewById(R.id.buttonRecordPlay);
        recordPlay.setOnClickListener(this);


        recordStop.setEnabled(false);
        recordPlay.setEnabled(false);
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonPlay:
                mediaPlayer.start();
                pause.setEnabled(true);
                play.setEnabled(false);
                break;

            case R.id.buttonPause:
                mediaPlayer.pause();
                pause.setEnabled(false);
                play.setEnabled(true);
                break;

            case R.id.buttonRecord:
                try {
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
                    record.setEnabled(false);
                    recordStop.setEnabled(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.buttonRecordStop:
                recordStop.setEnabled(false);
                recordPlay.setEnabled(true);
                myAudioRecorder.stop();
                myAudioRecorder.release();
                myAudioRecorder = null;
                break;

            case R.id.buttonRecordPlay:
               MediaPlayer m = new MediaPlayer();
                try {
                    m.setDataSource(outputFile);
                    m.prepare(); // 이게 빠져서 에러가 계속 났었음.
                    m.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

        }
    }

}
