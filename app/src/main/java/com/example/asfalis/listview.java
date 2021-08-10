package com.example.asfalis;

import androidx.appcompat.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.slice.Slice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.transition.Slide;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
//import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class listview extends AppCompatActivity {

    DatabaseHelper db;
    List<String> idi = new ArrayList<>();
    List<String> title = new ArrayList<>();
    List<String> username = new ArrayList<>();
    List<String> password = new ArrayList<>();
    ArrayAdapter<String> adapter;

    StringBuffer buffer = new StringBuffer();

    String idpass,uspass,titpass,passpass;
    ActionMode actionMode;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        setTitle("Credentials");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here to search");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        db = new DatabaseHelper(listview.this);
        final ListView lv = (ListView) findViewById(R.id.lv);

        detailView detailview;


//        Toolbar toolbar = (Toolbar) findViewById(R.id.listviewtoolbar);
//        toolbar.setTitle("Credentials");
//        toolbar.inflateMenu(R.menu.menu);
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                if(item.getItemId() == R.id.refresh){
//                    Toast.makeText(listview.this, "Refreshed", Toast.LENGTH_SHORT).show();
//                    finish();
//                    startActivity(getIntent());
//                }
//                else if(item.getItemId() == R.id.search){
//
//                    actionMode = listview.this.startActionMode(new ContextualCallback());
//                    SearchView searchView = (SearchView)
//
//                }
//                return true;
//            }
//        });

        Context context = getApplicationContext();
        storeData();
        adapter = new ArrayAdapter<String >(this, R.layout.list_layout, title);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(listview.this);
                int itemPosition = position;
                String value = (String) lv.getItemAtPosition(position);
                buffer.append("Title   : "+title.get(itemPosition)+"\n");
                buffer.append("Username: "+username.get(itemPosition)+"\n");
                buffer.append("Password: "+password.get(itemPosition));
                int g = idi.size();
                String s1 = adapter.getItem(position);
                for(int i=0;i<g;i++){
                    String s2 = title.get(i);
                    if(s1.equals(s2)){
                        idpass = idi.get(i);
                        titpass = s1;
                        uspass = username.get(i);
                        passpass = password.get(i);
                    }
                }
                Intent intent = new Intent(listview.this, detailView.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.putExtra("ID", idpass);
                intent.putExtra("Title", titpass);
                intent.putExtra("Username", uspass);
                intent.putExtra("Password", passpass);
                intent.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SLideJava);
                startActivity(intent, options.toBundle());
                //showMessage("Data", buffer.toString());
                buffer.delete(0, buffer.length());

            }
        });

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if(action.equals("finish_activity")){
                    finish();
                }
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter("finish_activity"));

        setupWindowAnimations();

    }

    void storeData(){
        Cursor cursor = db.readAllData();
        if(cursor.getCount() == 0){
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        } else{
            while(cursor.moveToNext()){
                idi.add(cursor.getString(0));
                title.add(cursor.getString(1));
                username.add(cursor.getString(2));
                password.add(cursor.getString(3));
            }
        }
    }

    private  void setupWindowAnimations() {
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

//    public void showMessage(String title, String Message){
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setCancelable(true);
//        builder.setTitle(title);
//        builder.setMessage(Message);
//        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Intent intent = new Intent(listview.this, updateWindow.class);
//                intent.putExtra("ID", idpass);
//                intent.putExtra("Title", titpass);
//                intent.putExtra("Username", uspass);
//                intent.putExtra("Password", passpass);
//                startActivity(intent);
//            }
//        });
//        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                showMessage2();
//            }
//        });
//        builder.show();
//    }
//
//    public void showMessage2() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setCancelable(true);
//        builder.setTitle("Confirm");
//        builder.setMessage("Are you sure you want to delete this?");
//        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                db.deleteProduct(idpass);
//                Toast.makeText(listview.this, "Deleted", Toast.LENGTH_SHORT).show();
//            }
//        });
//        builder.show();
//    }

}