package com.example.asfalis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class changePassword extends AppCompatActivity {

    Button changepass, cancel;
    EditText oldpass, newpass, confirmpass;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String PASSCODE = "passcode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        oldpass = findViewById(R.id.oldPas);
        confirmpass = findViewById(R.id.confirmPas);
        changepass = findViewById(R.id.changepass);
        newpass = findViewById(R.id.pas);
        cancel = findViewById(R.id.canc);

        setupWindowAnimations();

        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                String oldpassw = sharedPreferences.getString(PASSCODE, "NOTHING");

                if(oldpassw.equals(oldpass.getText().toString())) {
                    String passw = newpass.getText().toString();
                    String cpassw = confirmpass.getText().toString();
                    if (passw.isEmpty() || cpassw.isEmpty()) {
                        Toast.makeText(changePassword.this, "Field Empty", Toast.LENGTH_SHORT).show();
                    } else if (!passw.equals(cpassw)) {
                        Toast.makeText(changePassword.this, "Fields does not match", Toast.LENGTH_SHORT).show();
                    } else if (passw.length() < 4) {
                        Toast.makeText(changePassword.this, "Password must be atleast 4 characters long", Toast.LENGTH_SHORT).show();
                    } else {
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString(PASSCODE, passw);
                        editor.apply();
                        Toast.makeText(changePassword.this, "Password Set", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(changePassword.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                else{
                    Toast.makeText(changePassword.this, "Old password is wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private  void setupWindowAnimations() {
        Slide enterTransition = new Slide();
        Fade exitTransition = new Fade();
        exitTransition.excludeTarget(android.R.id.statusBarBackground, true);
        exitTransition.setDuration(500);
        enterTransition.excludeTarget(android.R.id.statusBarBackground, true);
        enterTransition.setDuration(500);
        enterTransition.setSlideEdge(Gravity.RIGHT);
        getWindow().setEnterTransition(enterTransition);
        getWindow().setExitTransition(exitTransition);
        getWindow().setReenterTransition(exitTransition);
        getWindow().setAllowEnterTransitionOverlap(false);
        getWindow().setAllowReturnTransitionOverlap(false);
    }
}