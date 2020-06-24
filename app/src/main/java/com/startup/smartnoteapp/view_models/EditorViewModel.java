package com.startup.smartnoteapp.view_models;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.startup.smartnoteapp.room_db.AppDatabase;
import com.startup.smartnoteapp.room_db.Note;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EditorViewModel extends AndroidViewModel {


    Executor executor = Executors.newSingleThreadExecutor();
    AppDatabase appDatabase;
    public MutableLiveData<Note> liveNote = new MutableLiveData<>();

    public EditorViewModel(@NonNull Application application) {
        super(application);
        appDatabase = AppDatabase.getInstance(application.getApplicationContext());
    }


    public void loadNote(final int id) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Note note = appDatabase.notesDao().getNoteById(id);
                liveNote.postValue(note);
            }
        });
    }

    public void insertNote(final String noteText) {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                Note noteEntity = liveNote.getValue();
                if (noteEntity == null) {

                    if (TextUtils.isEmpty(noteText.trim())) {
                        return;
                    } else {
                        noteEntity = new Note(noteText.trim());
                    }

                } else {
                    noteEntity.setText(noteText.trim());

                }
                appDatabase.notesDao().insertNote(noteEntity);
            }
        });

    }

    public void deleteNote() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Note note = liveNote.getValue();
                appDatabase.notesDao().deleteNote(note);
            }
        });


    }


}
