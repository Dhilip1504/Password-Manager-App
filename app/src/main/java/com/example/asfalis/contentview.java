package com.example.asfalis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class contentview extends AppCompatActivity implements UserAdapter.SelectedUser{
    DatabaseHelper db;
    Toolbar toolbar;
    RecyclerView recyclerView;

    UserAdapter userAdapter;

    List<String> idi = new ArrayList<>();
    List<String> username = new ArrayList<>();
    List<String> password = new ArrayList<>();
    List<String> titl = new ArrayList<>();
    List<String> remarks = new ArrayList<>();
    int selectedindex;

    String idpass,uspass,titpass,passpass,remarkspass;

    List<TitleList> titleList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contentview);
        db = new DatabaseHelper(contentview.this);

        recyclerView = findViewById(R.id.recyclerview);
        toolbar = findViewById(R.id.viewtoolbar);

        this.setSupportActionBar(toolbar);

        storeData();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userAdapter = new UserAdapter(titleList, this);

        recyclerView.setAdapter(userAdapter);

        layoutAnimation(recyclerView);

        setupWindowAnimations();

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if(action.equals("finish_activity")){
                    changed();
                }
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter("finish_activity"));

        BroadcastReceiver broadcastReceiver1 = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if(action.equals("update_activity")){
                    storeData();
                }
            }
        };
        registerReceiver(broadcastReceiver1, new IntentFilter("update_activity"));

        setupWindowAnimations();
    }

    private void layoutAnimation(RecyclerView recyclerview){
        Context context = recyclerview.getContext();
        LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animated);

        recyclerview.setLayoutAnimation(layoutAnimationController);
        recyclerview.getAdapter().notifyDataSetChanged();
        recyclerview.scheduleLayoutAnimation();
    }

    void storeData(){
        idi.clear();
        titl.clear();
        titleList.clear();
        username.clear();
        password.clear();
        remarks.clear();
        Cursor cursor = db.readAllData();
        if(cursor.getCount() == 0){
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        } else{
            while(cursor.moveToNext()){
                TitleList title = new TitleList(cursor.getString(1));
                idi.add(cursor.getString(0));
                titl.add(cursor.getString(1));
                titleList.add(title);
                username.add(cursor.getString(2));
                password.add(cursor.getString(3));
                remarks.add(cursor.getString(4));
            }
        }
    }

    @Override
    public void selectedUser(TitleList titleList) {

        String s1 = titleList.getTitle();
        int g = idi.size();
        for(int i=0;i<g;i++){
            selectedindex = i;
            String s2 = titl.get(i);
            if(s1.equals(s2)){
                idpass = idi.get(i);
                titpass = s1;
                uspass = username.get(i);
                passpass = password.get(i);
                remarkspass = remarks.get(i);
            }
        }
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(contentview.this);
        Intent intent = new Intent(contentview.this, detailView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra("ID", idpass);
        intent.putExtra("Title", titpass);
        intent.putExtra("Username", uspass);
        intent.putExtra("Password", passpass);
        intent.putExtra("Remarks", remarkspass);
        startActivity(intent, options.toBundle());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
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

                userAdapter.getFilter().filter(newText);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.search){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private  void setupWindowAnimations() {
        Slide enterTransition = new Slide();
        Fade exitTransition = new Fade();
        exitTransition.excludeTarget(android.R.id.statusBarBackground, true);
        exitTransition.setDuration(500);
        enterTransition.excludeTarget(android.R.id.statusBarBackground, true);
        enterTransition.setDuration(450);
        enterTransition.setSlideEdge(Gravity.RIGHT);
        getWindow().setEnterTransition(enterTransition);
        getWindow().setExitTransition(exitTransition);
        getWindow().setReenterTransition(exitTransition);
        getWindow().setAllowEnterTransitionOverlap(false);
        getWindow().setAllowReturnTransitionOverlap(false);
    }

    public void changed(){
        titleList.remove(selectedindex);
        userAdapter.notifyDataSetChanged();
    }

}