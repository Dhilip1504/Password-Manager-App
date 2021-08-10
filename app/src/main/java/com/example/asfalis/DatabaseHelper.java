package com.example.asfalis;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "register.db";
    private static final String TABLE_NAME = "contents";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "name";
    private static final String COL_3 = "username";
    private static final String COL_4 = "password";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE contents (ID INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, username TEXT, password TEXT, remarks TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String name, String username, String password, String remarks){
        SQLiteDatabase db1 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("remarks", remarks);
        long res = db1.insert("contents",null,contentValues);
        db1.close();
        if(res==-1){
            return false;
        }
        else{
            return true;
        }
    }

    Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    public void deleteProduct(String id){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, "ID=?", new String[]{id});
    }

    void updateData(String M_ID, String n_title, String n_username, String n_password, String n_remarks){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", n_title);
        cv.put("username", n_username);
        cv.put("password", n_password);
        cv.put("remarks", n_remarks);

        long result = db.update(TABLE_NAME, cv, "ID=?", new String[]{M_ID});
        if(result == -1){
            Toast.makeText(context, "Failed to update! Try again", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context, "Successfully updated", Toast.LENGTH_SHORT).show();
        }
    }

}
