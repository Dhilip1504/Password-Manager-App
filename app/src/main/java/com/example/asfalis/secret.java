package com.example.asfalis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Pair;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class secret extends AppCompatActivity {

    private Button add,view;
    private ImageView logoShared;
    DatabaseHelper db;
    StringBuffer buffer = new StringBuffer();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        setTitle("Asfalis");
        inflater.inflate(R.menu.menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.cp){
            setupWindowAnimations();
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(secret.this);
            Intent intent = new Intent(secret.this, changePassword.class);
            startActivity(intent, options.toBundle());
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secret);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.secrettoolbar);
//        toolbar.setTitle("Asfalis");
//        toolbar.inflateMenu(R.menu.menu2);
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                if(item.getItemId() == R.id.cp){
//                    Intent intent = new Intent(secret.this, changePassword.class);
//                    startActivity(intent);
//                }
//                return true;
//            }
//        });
        add = (Button) findViewById(R.id.addbutton);
        view = (Button) findViewById(R.id.viewbutton);
        logoShared = (ImageView) findViewById(R.id.logoShared);

        db = new DatabaseHelper(secret.this);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fadewindow();
                Pair[] pair = new Pair[2];
                pair[0] = new Pair<View, String>(logoShared, "logo_shared");
                pair[1] = new Pair<View, String>(add, "addbutton");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(secret.this, pair);
                Intent intent = new Intent(secret.this, Addcontent.class);
                startActivity(intent, options.toBundle());
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupWindowAnimations();
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(secret.this);
                Intent intent = new Intent(secret.this, contentview.class);
                startActivity(intent, options.toBundle());
            }
        });


    }

    void storeData(){
        Cursor cursor = db.readAllData();
        if(cursor.getCount() == 0){
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        } else{
            while(cursor.moveToNext()){
                buffer.append(cursor.getString(0)+"\n");
                buffer.append("Title   : "+cursor.getString(1)+"\n");
                buffer.append("Username: "+cursor.getString(2)+"\n");
                buffer.append("Password: "+cursor.getString(3)+"\n\n\n");
            }
        }
    }

    public void showMessage(String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
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

    private void fadewindow(){
        Fade enterTransition = new Fade();
        enterTransition.excludeTarget(android.R.id.statusBarBackground, true);
        enterTransition.setDuration(600);
        getWindow().setEnterTransition(enterTransition);
        getWindow().setExitTransition(enterTransition);
        getWindow().setReenterTransition(enterTransition);

    }

}