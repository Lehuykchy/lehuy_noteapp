package com.example.noteapp.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String TABLE_NAMEFOLDER = "folders";
    private static final String KEY_IDFOLDER = "idfolder";
    private static final String KEY_NAMEFOLDER = "namefolder";

    public DatabaseHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("database", "Create table");
        String create_folder_table = String.format("CREATE TABLE " + TABLE_NAMEFOLDER + " ("
                + KEY_IDFOLDER + " INTEGER PRIMARY KEY, "
                + KEY_NAMEFOLDER+ " TEXT);");
        db.execSQL(create_folder_table);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Xoá bảng cũ
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAMEFOLDER);
        //Tiến hành tạo bảng mới
        onCreate(db);
    }

    public void addFolder(Folder folder) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAMEFOLDER, folder.getNameFolder());
        db.insert(TABLE_NAMEFOLDER, null, values);
        db.close();

        Log.i("database", "thanh  cong");
    }

    public List<Folder> getAllFolder(){
        List<Folder> folders= new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAMEFOLDER;

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while(cursor.isAfterLast() == false) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);

            Folder folder = new Folder(id, name);
            folders.add(folder);
            cursor.moveToNext();
        }
        cursor.close();
        return folders;
    }
}
