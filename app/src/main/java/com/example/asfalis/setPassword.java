package com.example.asfalis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class setPassword extends AppCompatActivity {

    Button setpass;
    EditText pass, confirmpass;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static String PASSCODE = "passcode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);

        pass = findViewById(R.id.pass);
        confirmpass = findViewById(R.id.confirmPass);
        setpass = findViewById(R.id.setpass);

        setpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passw = pass.getText().toString();
                String cpassw = confirmpass.getText().toString();
                if(passw.isEmpty() || cpassw.isEmpty()){
                    Toast.makeText(setPassword.this, "Field Empty", Toast.LENGTH_SHORT).show();
                }
                else if(!passw.equals(cpassw)){
                    Toast.makeText(setPassword.this, "Fields does not match", Toast.LENGTH_SHORT).show();
                }
                else if(passw.length()<4){
                    Toast.makeText(setPassword.this, "Password must be atleast 4 characters long", Toast.LENGTH_SHORT).show();
                }
                else{
                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString(PASSCODE, passw);
                    editor.apply();
                    Toast.makeText(setPassword.this, "Password Set", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(setPassword.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}