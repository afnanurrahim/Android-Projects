package com.example.mathgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    String answer="";
    int count=0,correct=0;
    Button option1,option2,option3,option4;
    CardView cardView;
    TextView scoreTextView,timeTextView;
    public void gameLayout(View view){
        setContentView(R.layout.game);
        question(view);
        scoreTextView=findViewById(R.id.scoreTextView);
        timeTextView=findViewById(R.id.timeTextView);
        cardView=findViewById(R.id.cardView);
        cardView.setAlpha(0);
        new CountDownTimer(31000,1000){
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                timeTextView.setText(Integer.toString((int)millisUntilFinished/1000));
            }
            @Override
            public void onFinish() {
            cardView.setAlpha(1);
                Toast.makeText(getApplicationContext(), "time over", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }
    @SuppressLint("SetTextI18n")
    public void question(View view){
        option1=findViewById(R.id.button1);
        option2=findViewById(R.id.button2);
        option3=findViewById(R.id.button3);
        option4=findViewById(R.id.button4);
        TextView quesTextView=findViewById(R.id.quesTextView);
        ArrayList<String> optionsArray=new ArrayList<String>();
        Random r=new Random();
        int num1=r.nextInt(50);
        int num2=r.nextInt(50);
        double ans=0;
        int operation=r.nextInt(4);
        switch (operation){
            case 0: ans=num1+num2;
                    quesTextView.setText(num1+" + "+num2);
                break;
            case 1: ans=num1-num2;
                    quesTextView.setText(num1+" - "+num2);
                break;
            case 2: ans=num1*num2;
                    quesTextView.setText(num1+" * "+num2);
                break;
            case 3: ans=(double) num1/num2;
                    quesTextView.setText(num1+" / "+num2);
                break;
            default:
        }
        DecimalFormat d=new DecimalFormat("#.00");
        answer=d.format(ans);
        int option=r.nextInt(4);
        for (int i = 0; i < 4; i++) {
            if (i==option){
                optionsArray.add(answer);
            }else {
                int a=r.nextInt(100);
                while (a==ans){
                    a=r.nextInt(100);
                }
                optionsArray.add(Integer.toString(a)+".00");
            }
        }
        option1.setText(optionsArray.get(0));
        option2.setText(optionsArray.get(1));
        option3.setText(optionsArray.get(2));
        option4.setText(optionsArray.get(3));
//        String wrongAnswer1=d.format(r.nextInt(15)+ans+1);
//        String wrongAnswer2=d.format(ans-r.nextInt(15));
//        String wrongAnswer3=d.format(r.nextInt(15)+ans+operation);
//
//
//        switch (option){
//            case 0: option1.setText(answer);
//                    option2.setText(wrongAnswer1);
//                    option3.setText(wrongAnswer2);
//                    option4.setText(wrongAnswer3);
//                 break;
//            case 1: option2.setText(answer);
//                option1.setText(wrongAnswer1);
//                option3.setText(wrongAnswer2);
//                option4.setText(wrongAnswer3);
//                break;
//            case 2: option3.setText(answer);
//                option2.setText(wrongAnswer1);
//                option1.setText(wrongAnswer2);
//                option4.setText(wrongAnswer3);
//                break;
//            case 3: option4.setText(answer);
//                option2.setText(wrongAnswer1);
//                option3.setText(wrongAnswer2);
//                option1.setText(wrongAnswer3);
//                break;
//            default:
//        }
        count++;
    }
    @SuppressLint("SetTextI18n")
    public void choose(View view){
        Button press=(Button) view;
        if (press.getText()==answer){
            correct++;
            scoreTextView.setText(correct+"/"+count);
        }else {
            scoreTextView.setText(correct+"/"+count);
        }
        question(view);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}