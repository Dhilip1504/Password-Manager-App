package com.example.asfalis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class detailView extends AppCompatActivity {

    private TextView titleview,usernameview,passwordview,remarksview;
    private ImageView titlecopy, passcopy, usercopy, remarkscopy;
    String title,username,password,id,remarks;
    DatabaseHelper db;
    Constants.TransitionType type;
    Dialog epicDialog;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        setTitle("Credentials");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu3, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.delete){
            showpopup();
        }
        else if(item.getItemId() == R.id.update){
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(detailView.this);
            Intent intent = new Intent(detailView.this, updateWindow.class);
            intent.putExtra("ID", id);
            intent.putExtra("Title", title);
            intent.putExtra("Username", username);
            intent.putExtra("Password", password);
            intent.putExtra("Remarks", remarks);
            startActivity(intent, options.toBundle());
        }
        else if(item.getItemId() == R.id.share){
            String send = "Title : " + title + "\n" + "Username : " + username + "\n" + "Password : " + password + "\n" + "Remarks :" + remarks;
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, send);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        epicDialog = new Dialog(this);

        id = getIntent().getStringExtra("ID");
        title = getIntent().getStringExtra("Title");
        username = getIntent().getStringExtra("Username");
        password = getIntent().getStringExtra("Password");
        remarks = getIntent().getStringExtra("Remarks");
        db = new DatabaseHelper(detailView.this);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.detailtoolbar);
//        toolbar.setTitle("Details of " + title);
//        toolbar.inflateMenu(R.menu.menu3);
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                if(item.getItemId() == R.id.delete){
//                    AlertDialog.Builder builder = new AlertDialog.Builder(detailView.this);
//                    builder.setCancelable(true);
//                    builder.setTitle("Confirm");
//                    builder.setMessage("Are you sure you want to delete this?");
//                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    });
//                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            db.deleteProduct(id);
//                            Toast.makeText(detailView.this, "Deleted", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent("finish_activity");
//                            sendBroadcast(intent);
//                            Intent intent1 = new Intent(detailView.this, listview.class);
//                            startActivity(intent1);
//                            finish();
//                        }
//                    });
//                    builder.show();
//                }
//                else if(item.getItemId() == R.id.update){
//                    Intent intent = new Intent(detailView.this, updateWindow.class);
//                    intent.putExtra("ID", id);
//                    intent.putExtra("Title", title);
//                    intent.putExtra("Username", username);
//                    intent.putExtra("Password", password);
//                    startActivity(intent);
//                }
//                return true;
//            }
//        });

        titleview = findViewById(R.id.titleview);
        usernameview = findViewById(R.id.usernameview);
        passwordview = findViewById(R.id.passwordview);
        remarksview = findViewById(R.id.remarkview);
        titlecopy = findViewById(R.id.titlecopy);
        usercopy = findViewById(R.id.usercopy);
        passcopy = findViewById(R.id.passcopy);
        remarkscopy = findViewById(R.id.remarkscopy);

        titleview.setText(title);
        usernameview.setText(username);
        passwordview.setText(password);
        remarksview.setText(remarks);

        initAnimation();

        titlecopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("title", titleview.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(detailView.this, "Copied", Toast.LENGTH_SHORT).show();
            }
        });

        usercopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("username", usernameview.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(detailView.this, "Copied", Toast.LENGTH_SHORT).show();
            }
        });

        passcopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("password", passwordview.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(detailView.this, "Copied", Toast.LENGTH_SHORT).show();
            }
        });

        remarkscopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("remarks", remarksview.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(detailView.this, "Copied", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initAnimation() {
        Slide enterTransition = new Slide();
        Slide exitTransition = new Slide();
        exitTransition.excludeTarget(android.R.id.statusBarBackground, true);
        exitTransition.setDuration(500);
        enterTransition.excludeTarget(android.R.id.statusBarBackground, true);
        enterTransition.setDuration(500);
        exitTransition.setSlideEdge(Gravity.LEFT);
        enterTransition.setSlideEdge(Gravity.RIGHT);
        getWindow().setEnterTransition(enterTransition);
        getWindow().setExitTransition(exitTransition);
        getWindow().setReenterTransition(exitTransition);
        getWindow().setAllowEnterTransitionOverlap(false);
        getWindow().setAllowReturnTransitionOverlap(false);
    }

    private void showpopup(){
        epicDialog.setContentView(R.layout.epicdialog);
        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button yes = epicDialog.findViewById(R.id.yes);
        Button no = epicDialog.findViewById(R.id.no);


        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                epicDialog.dismiss();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                epicDialog.dismiss();
                db.deleteProduct(id);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(detailView.this);
                Toast.makeText(detailView.this, "Deleted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent("finish_activity");
                sendBroadcast(intent);
                finish();
//                Intent intent1 = new Intent(detailView.this, contentview.class);
//                startActivity(intent1, options.toBundle());
            }
        });

        epicDialog.show();
    }
}