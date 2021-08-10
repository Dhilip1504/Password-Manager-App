package com.example.asfalis;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.KeyguardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private TextView  tvexpression, Ans, equal;
    private int[] numericButtons = {R.id.tvZero, R.id.tvOne, R.id.tvTwo, R.id.tvThree, R.id.tvFour, R.id.tvFive, R.id.tvSix, R.id.tvSeven, R.id.tvEight, R.id.tvNine};
    private int[] operatorButtons = {R.id.tvPlus, R.id.tvMinus, R.id.tvMultiply, R.id.tvDivide};
    private ImageView back;
    private boolean lastNumeric;
    private boolean stateError;
    private boolean lastDot;
    private boolean equalclicked = false;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static String PASSCODE = "passcode";
    String passcode, Answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final MainActivity activity = this;

        Ans = findViewById(R.id.tvAns);
        tvexpression = findViewById(R.id.tvexpression);
        equal = findViewById(R.id.tvEquals);

        equal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEqual();
            }
        });

        load_data();

        Ans.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                opensecact();
                return true;
            }
        });

        Ans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(Answer==null)) {
                    if (stateError) {
                        tvexpression.setText(Answer);
                        stateError = false;
                    } else {
                        tvexpression.append(Answer);
                    }
                    lastNumeric = true;
                    equalclicked = false;
                }
            }
        });


    }

    public void opensecact() {
        String data = tvexpression.getText().toString();
        if(data.equals(passcode)) {
            tvexpression.setText("");
            Intent intent = new Intent(this, second.class);
            startActivity(intent);
        }
    }

    public void back_space(View view){
        String input = tvexpression.getText().toString();
        if(!input.equals("")) {
            String newText = input.substring(0, input.length() - 1);
            input = newText;
            tvexpression.setText(input);
            equalclicked = false;
        }
    }

    public void load_data(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        passcode = sharedPreferences.getString(PASSCODE, "NOTHING");
        if(passcode.equals("NOTHING")){
            Intent intent = new Intent(MainActivity.this, setPassword.class);
            startActivity(intent);
            finish();
        }
    }

    public void setNumericOnClickListener(View v){

        TextView textView = (TextView) v;
        if(stateError | equalclicked){
            tvexpression.setText(textView.getText().toString());
            stateError = false;
        } else{
            String t = tvexpression.getText().toString();
            t = t + textView.getText().toString();
            tvexpression.setText(t);
        }

        lastNumeric = true;
        equalclicked = false;
    }

    public void setOperatorOnClickListener(View v){

        if(v.getId() == R.id.tvDot){

            if(lastNumeric && !stateError && !lastDot){
                tvexpression.append(".");
                lastNumeric = false;
                lastDot = true;
                equalclicked = false;
            }

        } else if(v.getId() == R.id.tvMultiply){

            if (lastNumeric && !stateError) {
                String t = tvexpression.getText().toString();
                t = t + "*";
                tvexpression.setText(t);
                lastNumeric = false;
                lastDot = false;
                equalclicked = false;
            }

        } else {

            if (lastNumeric && !stateError) {
                TextView textView = (TextView) v;
                String t = tvexpression.getText().toString();
                t = t + textView.getText().toString();
                tvexpression.setText(t);
                lastNumeric = false;
                lastDot = false;
                equalclicked = false;
            }
        }

    }

    public void onEqual(){
        if(lastNumeric && !stateError){
            String txt = tvexpression.getText().toString();

            Expression expression = new ExpressionBuilder(txt).build();
            try {
                double result = expression.evaluate();
                tvexpression.setText(Double.toString(result));
                lastDot = true;
                Answer = Double.toString(result);
                equalclicked = true;
            } catch (ArithmeticException ex){
                tvexpression.setText("Error");
                stateError = true;
                lastNumeric = false;
                equalclicked = true;
            }
        }
    }

    public void clearscr(View v){
        tvexpression.setText("");
        tvexpression.setText("");
        lastNumeric = false;
        stateError = false;
        lastDot = false;
    }

}