package com.example.countdowngame;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;

import  javax.net.ssl.HttpsURLConnection;
import  java.io.IOException;

public class Conundrum extends AppCompatActivity {
    ArrayList<String> wordsList;
    EditText wordEditText;
    TextView meaningTextView,jumbledWordTextView,ansTextView,timerTextView;
    CardView cardView;
    Button submit,playAgain;
    String word,definition;
    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conundrum);
        cardView=findViewById(R.id.cardView);
        cardView.setAlpha(0);
        wordsList=new ArrayList<>();
        wordEditText=findViewById(R.id.wordEditText);
        meaningTextView=findViewById(R.id.meaningTextView);
        jumbledWordTextView=findViewById(R.id.jumbledWordTextView);
        ansTextView=findViewById(R.id.ansTextView);

        getEnglishWords();
        getWord();

        submit=findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.stop();
                cardView.setAlpha(1);
                Log.i(word,Integer.toString(word.length()));
//                Log.i(wordEditText.getText().toString(),IwordEditText.getText().toString().length());
                if (wordEditText.getText().toString().equals(word)){
                    ansTextView.setText("You are correct");
                }else {
                    ansTextView.setText("You are incorrect\n\n"+"Answer: "+word);
                }
            }
        });

        playAgain=findViewById(R.id.playAgain);
        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in3 = new Intent(Conundrum.this, Conundrum.class);
                startActivity(in3);
            }
        });

        timerTextView=findViewById(R.id.timerTextView);
        timer=new Timer();
        timer.start(timerTextView,cardView);

        ansTextView.setText("TIME UP");
    }
    public void getEnglishWords() {         // list of words we choose conundrum from
        InputStream in = getResources().openRawResource(R.raw.words_list);
        BufferedReader buff = new BufferedReader(
                new InputStreamReader(in, StandardCharsets.UTF_8));
        String line = "";
        try {
            while ((line = buff.readLine()) != null) {
                if (line.length() > 5)
                    wordsList.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void getWord(){      // to get the word for conundrum
        Random r=new Random();
        int w=r.nextInt(wordsList.size());
        word=wordsList.get(w);     // to remove the " " from the string
        Log.i("WORD",word);
                DownloadWordMeaning downloadWordMeaning = new DownloadWordMeaning();
                downloadWordMeaning.execute();
        scramble();
    }

    public void scramble()
    {
        Random r=new Random();
        // Convert your string into a simple char array:
        char a[] = word.toCharArray();

        // Scramble the letters using the standard Fisher-Yates shuffle,
        for( int i=0 ; i<a.length ; i++ )
        {
            int j = r.nextInt(a.length);
            // Swap letters
            char temp = a[i]; a[i] = a[j];  a[j] = temp;
        }

        jumbledWordTextView.setText(new String(a));
    }

    class DownloadWordMeaning extends AsyncTask<Void, Void, String> {      // meaning of chosen word

        @Override
        protected String doInBackground(Void... voids) {
            final String language = "en-gb";
            final String fields = "definitions";
            final String strictMatch = "false";
            final String temp=word;
            final String word_id = temp.toLowerCase();
            final String restUrl = "https://od-api.oxforddictionaries.com:443/api/v2/entries/" + language + "/" + word_id + "?" + "fields=" + fields + "&strictMatch=" + strictMatch;       //TODO: replace with your own app id and app key
            final String app_id = "84232de1";
            final String app_key = "28c4d6f9ab8a0646494da08c3b7609a7";

            try {
                URL url = new URL(restUrl);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("app_id", app_id);
                urlConnection.setRequestProperty("app_key", app_key);
                // read the output from the server
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                return stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("WORD",s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray=jsonObject.getJSONArray("results");
                JSONObject jsonObject1=new JSONObject(jsonArray.getJSONObject(0).toString());
                JSONArray jsonArray1= jsonObject1.getJSONArray("lexicalEntries");
                JSONObject jsonObject2=new JSONObject(jsonArray1.getJSONObject(0).toString());
                JSONArray jsonArray2= jsonObject2.getJSONArray("entries");
                JSONObject jsonObject3=new JSONObject(jsonArray2.getJSONObject(0).toString());
                JSONArray jsonArray3= jsonObject3.getJSONArray("senses");
                JSONObject jsonObject4=new JSONObject(jsonArray3.getJSONObject(0).toString());
                JSONArray jsonArray4= jsonObject4.getJSONArray("definitions");
                definition=jsonArray4.toString();

                meaningTextView.setText(definition);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}