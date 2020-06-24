package com.startup.smartnoteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.startup.smartnoteapp.adapter.AdapterClass;
import com.startup.smartnoteapp.room_db.Note;
import com.startup.smartnoteapp.view_models.NoteViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MYTAG";
    RecyclerView recyclerView;
    AdapterClass adapterClass;
    List<Note> list = new ArrayList<>();
    NoteViewModel noteViewModel;
    private int position;

    /* onCreate() **********************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRecyclerView();
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        registerForContextMenu(recyclerView);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                Log.d(TAG, "onChanged: " + notes.toString());
                list.clear();
                list.addAll(notes);

                if(adapterClass==null){
                    Log.d(TAG, "onChanged: 44 ");
                    showRecyclerView();
                }else{

                    Log.d(TAG, "onChanged: ");
                    position=adapterClass.getPosition();
                    adapterClass.notifyDataSetChanged();
                }
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        // Observing Live Data...

    }
    /* *********************************************************/

    /*Drag and Drop , Delete and Archive --------------------------------------------------------------------------------- */
    Note deletedItem = null;


    /**
     * @param dragDirs -> drag & drop
     * @param swipeDirs -> swap right and left
     */


    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT) {

        /*Drag and drop*/
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {
            return false;
        }


        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            final int position1 = viewHolder.getAdapterPosition();
            switch (direction) {
                /*Delete*/
                case ItemTouchHelper.LEFT:
                    deletedItem = list.get(position1);
                    list.remove(position1);
                    noteViewModel.deleteNote(deletedItem);
                    break;

            }
        }

        // Archive and delete icons
        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorRed))
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorRed))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete_black_24dp)
                    .create()
                    .decorate();


            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };
    /*------------------------------------------------------------------------------------------------------------------*/


    // BREAK


    /*==========================================================================================================*/
    void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

    }

    void showRecyclerView() {
        adapterClass = new AdapterClass(this, list);
        recyclerView.setAdapter(adapterClass);
    }

    // Open Editor Fab...
    public void openEditor(View view) {
        Intent intent = new Intent(MainActivity.this, EditorActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.simple_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.del_all:
                deleteAllNotes();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteAllNotes() {
        noteViewModel.deleteAllNotes();
    }


    /*==========================================================================================================*/


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.edit_del_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            // Delete...
            case R.id.del:
                Note note = list.get(position);
                noteViewModel.deleteNote(note);
                return true;
            // Edit...
            case R.id.edit:
                Note noteModel = list.get(position);
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                intent.putExtra("UPDATE", noteModel.getId());
                startActivity(intent);
                return true;
            //
            default:
                return super.onContextItemSelected(item);

        }

    }

}
