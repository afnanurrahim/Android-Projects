package com.example.timer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    final int[] time = {0};
    boolean textOnButton=true;
    SeekBar setTime;
    TextView textView;
    CountDownTimer count;
    MediaPlayer ring;
    @SuppressLint("SetTextI18n")
    public void start(View view){
        Button press=(Button) view;
        if (textOnButton){
            press.setText("STOP");
            textOnButton=false;
            setTime.setEnabled(false);
            count= new CountDownTimer(time[0]* 1000L,1000){
                @SuppressLint("SetTextI18n")
                @Override
                public void onTick(long millisUntilFinished) {
                    int min=(int)(millisUntilFinished/60000);
                    int sec=(int)millisUntilFinished;
                    textView.setText(Integer.toString(min)+":"+Integer.toString((sec-(min*60000))/1000));
                    time[0]=(int) millisUntilFinished/1000;
                }
                @Override
                public void onFinish() {
                    Toast.makeText(getApplicationContext(), "TIME UP !!!", Toast.LENGTH_SHORT).show();
                    setTime.setEnabled(true);
                    press.setText("START");
                    textOnButton=true;
                    time[0]=setTime.getProgress();
                    int minute=(int)(time[0]/60);
                    textView.setText(Integer.toString(minute)+":"+Integer.toString(time[0]-minute*60));
                    ring.start();

                }
            };
           count.start();
        }else{
            press.setText("START");
            textOnButton=true;
            setTime.setEnabled(true);
            count.cancel();
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        new CountDownTimer(20000,1000){
//
//            @Override
//            public void onTick(long millisUntilFinished) {
//                Toast.makeText(getApplicationContext(), ""+(millisUntilFinished/2000), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFinish() {
//                Toast.makeText(getApplicationContext(), "Let's GO", Toast.LENGTH_SHORT).show();
//            }
//        }.start();
        /*Handler handler=new Handler();
        Runnable run=new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "jfjffj", Toast.LENGTH_SHORT).show();
                handler.postDelayed(this, 5000);
            }
        };
        handler.post(run);*/
        ring=MediaPlayer.create(this,R.raw.alarm);
        textView=findViewById(R.id.textView);
        setTime=findViewById(R.id.setTime);
        setTime.setMax(300);
        setTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                time[0] =progress;
                int minute=(int)(progress/60);
                textView.setText(Integer.toString(minute)+":"+Integer.toString(progress-minute*60));
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