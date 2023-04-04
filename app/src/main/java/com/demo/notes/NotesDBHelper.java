package com.demo.notes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class NotesDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "notes.db";
    private static final int DB_VERSION = 2;
    public NotesDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    // вызывается при создании таблицы в БД
    public void onCreate(SQLiteDatabase db) {
        // чтобы исполнить SQL-запрос, необходимо вызвать метод execSQL() и передать ему запрос
        db.execSQL(NotesContract.NotesEntry.CREATE_COMMAND);
    }
    @Override
    // вызывается при изменении БД (изменение версии БД)
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // если БД обновилась, то необходимо удалить старую таблицу и создать новую
        db.execSQL(NotesContract.NotesEntry.DROP_COMMAND);
        onCreate(db);
    }}