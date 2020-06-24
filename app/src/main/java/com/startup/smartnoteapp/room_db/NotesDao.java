package com.startup.smartnoteapp.room_db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(Note noteEntity);


    @Query("SELECT * FROM notes ORDER BY text DESC ")
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM notes WHERE id = :id")
    Note getNoteById(int id);


    @Delete
    void deleteNote(Note noteEntity);


    @Query("DELETE FROM notes")
    int deleteAllNotes();


//    @Query("SELECT COUNT(*) FROM notes")
//    int getCount();

}
