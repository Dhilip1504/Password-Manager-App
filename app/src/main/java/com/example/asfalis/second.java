package com.example.asfalis;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;

import android.app.ActivityOptions;
import android.app.KeyguardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.transition.Fade;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class second extends AppCompatActivity {

    private TextView tv;
    private ImageView fpimg;
    private Button button;

    private KeyguardManager keyguardManager;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        tv = (TextView) findViewById(R.id.tv);
        fpimg = (ImageView) findViewById(R.id.fpimg);
        button = (Button) findViewById(R.id.auth);

        final Executor executor = Executors.newSingleThreadExecutor();

        final BiometricManager biometricManager = BiometricManager.from(this);


        final BiometricPrompt biometricPrompt = new BiometricPrompt.Builder(this)
                .setTitle("Finger print Authentication")
                .setSubtitle("Subtitle")
                .setDescription("Description")
                .setNegativeButton("Cancel", executor, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).build();

        final second activity = this;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE){
//                    opensecact();
//                }
                if(biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED){
                    Toast.makeText(second.this, "Please add fingerprint", Toast.LENGTH_SHORT).show();
                    opensecact();
                }
                else {
                    biometricPrompt.authenticate(new CancellationSignal(), executor, new BiometricPrompt.AuthenticationCallback() {
                        @Override
                        public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {

                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Toast.makeText(second.this, "Authenticated", Toast.LENGTH_SHORT).show();
                                    opensecact();
                                }
                            });
                        }
                    });
                }
            }
        });

    }

    public void opensecact() {
        fadewindow();
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(second.this);
        Intent intent = new Intent(this, secret.class);
        startActivity(intent, options.toBundle());
        finish();
    }

    private void fadewindow(){
        Fade enterTransition = new Fade();
        enterTransition.excludeTarget(android.R.id.statusBarBackground, true);
        enterTransition.setDuration(300);
        getWindow().setEnterTransition(enterTransition);
        getWindow().setExitTransition(enterTransition);
        getWindow().setReenterTransition(enterTransition);
    }
}