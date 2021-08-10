package com.example.asfalis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class updateWindow extends AppCompatActivity {

    EditText titlet,usernameet,passwordet,remarkset;
    Button upbt,cancelbt;

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_window);

        db = new DatabaseHelper(updateWindow.this);

        titlet = findViewById(R.id.Titleet);
        usernameet = findViewById(R.id.Usernameet);
        passwordet = findViewById(R.id.Passwordet);
        remarkset = findViewById(R.id.Remarkset);
        upbt = findViewById(R.id.updatebt);
        cancelbt = findViewById(R.id.cancelbt);

        final String M_ID = getIntent().getStringExtra("ID");
        titlet.setText(getIntent().getStringExtra("Title"));
        usernameet.setText(getIntent().getStringExtra("Username"));
        passwordet.setText(getIntent().getStringExtra("Password"));
        remarkset.setText(getIntent().getStringExtra("Remarks"));


        cancelbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        upbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n_title = titlet.getText().toString();
                String n_username = usernameet.getText().toString();
                String n_password = passwordet.getText().toString();
                String n_remarks = remarkset.getText().toString();

                db.updateData(M_ID, n_title,n_username, n_password, n_remarks);
                Intent intent = new Intent("update_activity");
                sendBroadcast(intent);
                finish();
            }
        });

        initAnimation();

    }

    private void initAnimation() {
        Slide enterTransition = new Slide();
        enterTransition.excludeTarget(android.R.id.statusBarBackground, true);
        enterTransition.setDuration(500);
        enterTransition.setSlideEdge(Gravity.RIGHT);
        getWindow().setEnterTransition(enterTransition);
        getWindow().setAllowEnterTransitionOverlap(false);
    }
}