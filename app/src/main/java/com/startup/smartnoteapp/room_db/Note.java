package com.startup.smartnoteapp.room_db;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "notes")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String text;


    @Ignore
    public Note() {
    }

    @Ignore
    public Note(String text) {
        this.text = text;
    }


    public Note(int id, String text) {
        this.id = id;

        this.text = text;

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @NonNull
    @Override
    public String toString() {
        return "\nTodo{" +
                "uid=" + id +
                ", text='" + text + '}';
    }
}
