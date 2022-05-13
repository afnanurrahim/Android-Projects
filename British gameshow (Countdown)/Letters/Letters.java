package com.example.countdowngame;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Letters extends AppCompatActivity implements View.OnClickListener {
    Button[] letterButton;
    Button submitButton, clearButton,playAgain;
    TextView wordTextView,ansTextView,timerTextView;
    CardView cardView;
    ArrayList<String> wordsList;
    String wordChosen = "";
    HashMap<Button, Integer> contain;   // This is to check if the character of button is in the wordChosen or not. Also to get its position (for clear button)
    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letters);
        cardView =findViewById(R.id.cardView);
        cardView.setAlpha(0);

        contain = new HashMap<Button, Integer>();

        letterButton = new Button[10];
        letterButton[0] = findViewById(R.id.letterButton1);
        letterButton[0].setOnClickListener(this);
        letterButton[1] = findViewById(R.id.letterButton2);
        letterButton[1].setOnClickListener(this);
        letterButton[2] = findViewById(R.id.letterButton3);
        letterButton[2].setOnClickListener(this);
        letterButton[3] = findViewById(R.id.letterButton4);
        letterButton[3].setOnClickListener(this);
        letterButton[4] = findViewById(R.id.letterButton5);
        letterButton[4].setOnClickListener(this);
        letterButton[5] = findViewById(R.id.letterButton6);
        letterButton[5].setOnClickListener(this);
        letterButton[6] = findViewById(R.id.letterButton7);
        letterButton[6].setOnClickListener(this);
        letterButton[7] = findViewById(R.id.letterButton8);
        letterButton[7].setOnClickListener(this);
        letterButton[8] = findViewById(R.id.letterButton9);
        letterButton[8].setOnClickListener(this);
        letterButton[9] = findViewById(R.id.letterButton10);
        letterButton[9].setOnClickListener(this);

        for (int i = 0; i < 10; i++) {
            contain.put(letterButton[i], 0);
        }

        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(this);

        clearButton = findViewById(R.id.clearButton);
        clearButton.setOnClickListener(this);

        playAgain=findViewById(R.id.playAgain);
        playAgain.setOnClickListener(this);

        wordTextView = findViewById(R.id.wordTextView);
        ansTextView=findViewById(R.id.ansTextView);

        wordsList = new ArrayList<>();
        getEnglishWords();
        Log.i("Words", wordsList.get(4));
        generateWord();

        timerTextView=findViewById(R.id.timerTextView);
        timer=new Timer();
        timer.start(timerTextView,cardView);

        ansTextView.setText("TIME UP");

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submitButton:
                timer.stop();
                DownloadWordInfo downloadWordInfo = new DownloadWordInfo();
                downloadWordInfo.execute("https://api.dictionaryapi.dev/api/v2/entries/en/"+wordChosen);
                cardView.setAlpha(1);
                break;
            case R.id.clearButton:
                int l = wordChosen.length();
                for (Map.Entry<Button, Integer> entry : contain.entrySet()) {
                    if (entry.getValue() == l) {
                        contain.replace(entry.getKey(), 0);
                    }
                }
                wordChosen = wordChosen.substring(0, l - 1);
                break;
            case R.id.playAgain:
                Intent in2 = new Intent(Letters.this, Letters.class);
                startActivity(in2);
                break;
            default:
                Button b = (Button) v;
                if (contain.get(b) == 0) {
                    wordChosen += b.getText();
                    contain.replace(b, 0, wordChosen.length());
                }
                break;
        }
        wordTextView.setText(wordChosen);
    }

    public void getEnglishWords() {
        InputStream in = getResources().openRawResource(R.raw.words_list);
        BufferedReader buff = new BufferedReader(
                new InputStreamReader(in, StandardCharsets.UTF_8));
        String line = "";
        try {
            while ((line = buff.readLine()) != null) {
                if (line.length() > 4)
                    wordsList.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void generateWord() {
        try {
            char[] alphabet = "QWERTYUIOPLKJHGFDSAZXCVBNM".toCharArray();       // to fill empty spaces of character
            Random r = new Random();
            int c = r.nextInt(wordsList.size());
            String display = wordsList.get(c).toUpperCase();
            int l = display.length();
            Log.i("Words", display);
            for (int i = 0; i < 10; i++) {
                if (display.length() > 0) {
                    int j = r.nextInt(display.length());
                    letterButton[i].setText(Character.toString(display.charAt(j)));
                    display = display.substring(0, j) + display.substring(j + 1);
                } else {
                    int j = r.nextInt(26);
                    letterButton[i].setText(Character.toString(alphabet[j]));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class DownloadWordInfo extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... urls) {
            String webPage = "";
            try {
                URL url = new URL(urls[0]);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    webPage += line;
                }
                Log.i("Error", webPage);
                return true;
            }catch (Exception e){
                    e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean b) {
            super.onPostExecute(b);
            if (b){
                ansTextView.setText("You get "+wordChosen.length()+" points");
            }else{
                ansTextView.setText("Stated word doesn't exist");
            }

        }
    }
}