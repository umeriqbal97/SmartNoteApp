package com.startup.smartnoteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.startup.smartnoteapp.room_db.AppDatabase;
import com.startup.smartnoteapp.room_db.Note;
import com.startup.smartnoteapp.view_models.EditorViewModel;

public class EditorActivity extends AppCompatActivity {
    EditText editText;
    EditorViewModel editorViewModel;
    Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        editText = findViewById(R.id.et_text);

        editorViewModel = ViewModelProviders.of(this).get(EditorViewModel.class);

        editorViewModel.liveNote.observe(this, new Observer<Note>() {
            @Override
            public void onChanged(Note note) {
                editText.setText(note.getText());
                editText.setSelection(editText.getText().length());
            }
        });

        bundle = getIntent().getExtras();

        if (bundle == null) {
            setTitle("New Note");
        } else {
            setTitle("Edit Note");
            int id = bundle.getInt("UPDATE");
            editorViewModel.loadNote(id);
        }
    }


    public void btnSave(View view) {
        if (TextUtils.isEmpty(editText.getText())) {
            Toast.makeText(this, "Please write something", Toast.LENGTH_SHORT).show();
        } else {


            new Thread(new Runnable() {
                @Override
                public void run() {
                    editorViewModel.insertNote(editText.getText().toString());
                }
            }).start();
            finish();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        editorViewModel.insertNote(editText.getText().toString());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.del_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.f_del) {
            if (bundle == null) {
                Toast.makeText(this, "Note has not been created yet", Toast.LENGTH_SHORT).show();
            } else {
                editorViewModel.deleteNote();
                finish();
            }


        }

        return super.onOptionsItemSelected(item);
    }
}
