package com.startup.smartnoteapp.room_db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "Notes_DB";

    private static volatile AppDatabase instance;

    private static final Object LOCK = new Object();

    public abstract NotesDao notesDao();

    public static AppDatabase getInstance(Context context) {

        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {

                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DATABASE_NAME).build();

                }
            }
        }
        return instance;

    }
}