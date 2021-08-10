package com.example.asfalis;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Searchcontent extends AppCompatActivity {

    EditText titletv;
    Button searchbt;
    String Tit;

    DatabaseHelper db;
    StringBuffer buffer = new StringBuffer();
    StringBuffer clean = new StringBuffer();
    String idpass,uspass,titpass,passpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchcontent);

        searchbt = findViewById(R.id.searchbt);
        titletv = findViewById(R.id.titletv);

        db = new DatabaseHelper(Searchcontent.this);

        searchbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tit = titletv.getText().toString();
                storeData(Tit);
                if(buffer.length() != 0) {
                    showMessage("Data", buffer.toString());
                }
                else{
                    Toast.makeText(Searchcontent.this, "No Matching data Found", Toast.LENGTH_SHORT).show();
                }
                buffer.delete(0, buffer.length());
            }
        });
    }

    void storeData(String Tit){
        Cursor cursor = db.readAllData();
        if(cursor.getCount() == 0){
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        } else{
            while(cursor.moveToNext()){
                String word = cursor.getString(1);
                if(word.equals(Tit)) {
                    buffer.append("Title   : " + cursor.getString(1) + "\n");
                    buffer.append("Username: " + cursor.getString(2) + "\n");
                    buffer.append("Password: " + cursor.getString(3));
                    idpass = cursor.getString(0);
                    titpass = cursor.getString(1);
                    uspass = cursor.getString(2);
                    passpass = cursor.getString(3);
                    break;
                }
            }
        }
    }

    public void showMessage(String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Searchcontent.this, updateWindow.class);
                intent.putExtra("ID", idpass);
                intent.putExtra("Title", titpass);
                intent.putExtra("Username", uspass);
                intent.putExtra("Password", passpass);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showMessage2();
            }
        });
        builder.show();
    }

    public void showMessage2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to delete this?");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.deleteProduct(idpass);
                Toast.makeText(Searchcontent.this, "Deleted", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }
}