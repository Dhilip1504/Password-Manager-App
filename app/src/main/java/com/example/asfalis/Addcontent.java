package com.example.asfalis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class Addcontent extends AppCompatActivity {

    DatabaseHelper db;

    private EditText title,username,password,remarks;
    private Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcontent);

        db = new DatabaseHelper(this);
        title = findViewById(R.id.Title);
        username = findViewById(R.id.Username);
        password = findViewById(R.id.Password);
        remarks = findViewById(R.id.Remarks);
        add = findViewById(R.id.addingbutton);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = title.getText().toString();
                String s2 = username.getText().toString();
                String s3 = password.getText().toString();
                String s4 = remarks.getText().toString();

                if(s1.equals("")||s3.equals("")){
                    Toast.makeText(getApplicationContext(),"Fields are Empty",Toast.LENGTH_SHORT).show();
                }
                else{
                    Boolean insert = db.addData(s1,s2,s3,s4);
                    if(insert==true){
                        Toast.makeText(getApplicationContext(),"Added successfully",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Failed! Try again",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}