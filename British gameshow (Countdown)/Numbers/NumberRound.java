package com.example.countdowngame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;

public class NumberRound extends AppCompatActivity implements View.OnClickListener{
    Button[] numButton;
    Button add,subtract,multiply,divide,openBracket,closeBracket,clear,submit,playAgain;
    TextView expression,targetTextVeiw,ansTextView,timerTextView;
    CardView cardView;
    String expressionString="",answer="";
    Timer timer;
    int target;
    HashMap<Integer,String> map=new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.number_round);
        cardView=findViewById(R.id.cardView);
        cardView.setAlpha(0);
        timerTextView=findViewById(R.id.timerTextView);

        target=0;
        numButton=new Button[6];
        numButton[0]=findViewById(R.id.num1);
        numButton[1]=findViewById(R.id.num2);
        numButton[2]=findViewById(R.id.num3);
        numButton[3]=findViewById(R.id.num4);
        numButton[4]=findViewById(R.id.num5);
        numButton[5]=findViewById(R.id.num6);

        add=findViewById(R.id.add);
        add.setOnClickListener(this);
        subtract=findViewById(R.id.subtract);
        subtract.setOnClickListener(this);
        multiply=findViewById(R.id.multiply);
        multiply.setOnClickListener(this);
        divide=findViewById(R.id.divide);
        divide.setOnClickListener(this);
        openBracket=findViewById(R.id.open);
        openBracket.setOnClickListener(this);
        closeBracket=findViewById(R.id.close);
        closeBracket.setOnClickListener(this);

        clear=findViewById(R.id.clear);
        clear.setOnClickListener(this);
        submit=findViewById(R.id.submit);
        submit.setOnClickListener(this);
        playAgain=findViewById(R.id.playAgain);
        playAgain.setOnClickListener(this);

        targetTextVeiw=findViewById(R.id.targetTextView);
        expression=findViewById(R.id.expression);
        ansTextView=findViewById(R.id.ansTextView);

        int[] nums =sixNumGenerate();
        for (int i = 0; i < 6; i++) {
            numButton[i].setText(String.valueOf(nums[i]));
            numButton[i].setOnClickListener(this);
        }
        target=targetNumber(nums);
        targetTextVeiw.setText(String.valueOf(target));


        timer=new Timer();
        timer.start(timerTextView,cardView);

        ansTextView.setText("TIME UP");
    }
    int[] sixNumGenerate(){
        int[] nums=new int[6];
        int[] longNum ={25,50,75,100};
        int[] shortNum ={1,2,3,4,5,6,7,8,9,10};
        Random r=new Random();
        int c=r.nextInt(4)+1;    // num of long integers
        for (int i = 0; i < c; i++) {
            int fromLong=r.nextInt(4);
            while (longNum[fromLong]==0){
                fromLong=r.nextInt(4);
            }
            nums[i]=longNum[fromLong];
            longNum[fromLong]=0;
        }
        for (int i = c; i < 6; i++) {
            int fromShort=r.nextInt(10);
            while(shortNum[fromShort]==0)
                fromShort=r.nextInt(10);
            nums[i]=shortNum[fromShort];
            shortNum[fromShort]=0;
        }
        return nums;
    }
    int targetNumber(int[] nums){
        char[] c={'+','-','*','/'};
        Random r=new Random();
        ArrayList<Integer> list=new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            list.add(nums[i]);
        }
        int multiplyCount=0,divideCount=0;
        for (int i = 0; i < nums.length; i++) {
            int n=r.nextInt(list.size());
            switch (c[r.nextInt(4)]){
                case '+':
                    target+=list.get(n);
                    answer+="+"+Integer.toString(list.get(n))+")";
                    list.remove(n);
                    break;
                case '*':
                    if (multiplyCount<1 && i!=0){
                        target*=list.get(n);
                        answer+="*"+Integer.toString(list.get(n))+")";
                        list.remove(n);
                        multiplyCount++;
                    }else {
                        i--;
                    }
                    break;
                case '-':
                    if (target>list.get(n)){
                        target-=list.get(n);
                        answer+="-"+Integer.toString(list.get(n))+")";
                        list.remove(n);
                    }else{
                        i--;
                    }
                    break;
                case '/':
                    if(divideCount< 1&& i!=0 && target%list.get(n)==0){
                        target/=list.get(n);
                        answer+="/"+Integer.toString(list.get(n))+")";
                        list.remove(n);
                        divideCount++;
                    }else{
                        i--;
                    }
            }
        }
        answer="(((((("+answer.substring(1);
        return target;
    }

    @Override
    public void onClick(View v) {
        Button b=(Button) v;
        switch (v.getId()){
            case R.id.clear:
                int e=expressionString.length()-1;
                if (e>=0 && expressionString.charAt(e)<48) {    // to clear operations from textview
                    expressionString = expressionString.substring(0, e);
                }else{
                    while (e>=0 && expressionString.charAt(e) > 47) {   // to clear numbers from textView
                        expressionString = expressionString.substring(0, e);
                        e--;
                        map.remove(map.size());
                    }
                }
                break;
            case R.id.submit:
                timer.stop();
                if(map.size()==6 && new EvaluateString().check(expressionString)){
                    ansTextView.setText("You are Correct ");
                }else {
                    ansTextView.setText("WRONG!!\n\n Answer:\n "+answer);
                }
                cardView.setAlpha(1);
                break;
            case R.id.playAgain:
                Intent in = new Intent(NumberRound.this, NumberRound.class);
                startActivity(in);
                break;
            default:
                if (!map.containsValue(b.getText().toString()) && b.getText().toString().charAt(0)>48) {
                    map.put(map.size()+1,b.getText().toString());
                    expressionString += b.getText().toString();
                }else if(b.getText().toString().charAt(0)<48){
                    expressionString += b.getText().toString();
                }
        }
        expression.setText(expressionString);
    }

    class EvaluateString
    {
        boolean check(String s){
            try {
                if(evaluate(s)==target)
                    return true;
                return false;
            }catch (Exception e){
                return false;
            }
        }
        public int evaluate(String expression)
        {
            char[] tokens = expression.toCharArray();
            Stack<Integer> values = new Stack<Integer>();
            Stack<Character> ops = new Stack<Character>();

            for (int i = 0; i < tokens.length; i++)
            {
                if (tokens[i] == ' ')
                    continue;
                if (tokens[i] >= '0' && tokens[i] <= '9'){
                    StringBuffer sbuf = new StringBuffer();
                    while (i < tokens.length && tokens[i] >= '0' && tokens[i] <= '9')
                        sbuf.append(tokens[i++]);
                    values.push(Integer.parseInt(sbuf.toString()));
                    i--;
                }
                else if (tokens[i] == '(')
                    ops.push(tokens[i]);

                else if (tokens[i] == ')')
                {
                    while (ops.peek() != '(')
                        values.push(applyOp(ops.pop(),values.pop(),values.pop()));
                    ops.pop();
                }

                else if (tokens[i] == '+' ||
                        tokens[i] == '-' ||
                        tokens[i] == '*' ||
                        tokens[i] == '/')
                {
                    while (!ops.empty() && hasPrecedence(tokens[i],ops.peek()))
                        values.push(applyOp(ops.pop(),values.pop(),values.pop()));
                    ops.push(tokens[i]);
                }
            }

            while (!ops.empty())
                values.push(applyOp(ops.pop(),values.pop(),values.pop()));
            return values.pop();
        }

        public boolean hasPrecedence(char op1, char op2)
        {
            if (op2 == '(' || op2 == ')')
                return false;
            if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-'))
                return false;
            else
                return true;
        }
        public int applyOp(char op,int b, int a)
        {
            switch (op)
            {
                case '+':
                    return a + b;
                case '-':
                    return a - b;
                case '*':
                    return a * b;
                case '/':
                    if (b == 0)
                        throw new
                                UnsupportedOperationException("Cannot divide by zero");
                    return a / b;
            }
            return 0;
        }
    }
}