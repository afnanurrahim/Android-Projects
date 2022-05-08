package com.example.sampleaudio;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    int length;
    MediaPlayer m2;
    public void play(View view){
        pause(m2);
        MediaPlayer mediaPlayer;
        Button press=(Button) view;
        if (press.getId()==R.id.pause){
            pause(m2);
            length=m2.getCurrentPosition();
        }else if (press.getId()==R.id.play){
            m2.seekTo(length);
            m2.start();
        }else{
            mediaPlayer = MediaPlayer.create(this, getResources().getIdentifier(press.getTag().toString(), "raw", getPackageName()));
            mediaPlayer.start();
            m2 = mediaPlayer;
        }
    }
    public void pause(MediaPlayer m2){
        if (m2!=null){
            m2.pause();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AudioManager manager;
        manager=(AudioManager) getSystemService(AUDIO_SERVICE);
        int max=manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int current=manager.getStreamVolume(AudioManager.STREAM_MUSIC);


        SeekBar volume=findViewById(R.id.seekBar);
        volume.setMax(max);
        volume.setProgress(current);
        volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                manager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}