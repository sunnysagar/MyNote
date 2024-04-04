package com.sunny.mynote;

import static java.util.Locale.filter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sunny.mynote.adapter.NoteListAdapter;
import com.sunny.mynote.db.RoomDB;
import com.sunny.mynote.model.NoteViewModel;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity  implements PopupMenu.OnMenuItemClickListener{

    RecyclerView recyclerView;
    NoteListAdapter noteListAdapter;
    List<NoteViewModel> notes = new ArrayList<>();
    RoomDB database;
    FloatingActionButton fabAdd;

    SearchView SearchViewNote;
    
    NoteViewModel selectedNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        fabAdd = findViewById(R.id.fabAdd);

        SearchViewNote = findViewById(R.id.SearchViewNote);


        database = RoomDB.getInstance(this);
        notes = database.noteDao().getAll();

        updateRecycler(notes);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivityForResult(intent, 101);
            }
        });

        SearchViewNote.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               filter(newText);
                return false;
            }
        });

    }

    private void filter(String newText){
        List<NoteViewModel> filteredList = new ArrayList<>();
        for(NoteViewModel singleNote : notes)
        {
            if(singleNote.getTitle().toLowerCase().contains(newText.toLowerCase())
            || singleNote.getNotes().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(singleNote);
            }
        }
        noteListAdapter.filterList(filteredList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==101){
            if(resultCode == Activity.RESULT_OK){
                NoteViewModel newNotes = (NoteViewModel) data.getSerializableExtra("note");
                database.noteDao().insert(newNotes);
                notes.clear();;
                notes.addAll(database.noteDao().getAll());
                noteListAdapter.notifyDataSetChanged();
            }
        }
        else if(requestCode == 102){
            if(resultCode==Activity.RESULT_OK){
                NoteViewModel newNotes = (NoteViewModel) data.getSerializableExtra("note");
                database.noteDao().update(newNotes.getID(),newNotes.getTitle(),newNotes.getNotes());
                notes.clear();;
                notes.addAll(database.noteDao().getAll());
                noteListAdapter.notifyDataSetChanged();
            }
        }
    }

    private void updateRecycler(List<NoteViewModel> notes) {
        recyclerView.setHasFixedSize((true));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        noteListAdapter = new NoteListAdapter(MainActivity.this, notes, notesClickListener);

        recyclerView.setAdapter(noteListAdapter);
    }

    private final NotesClickListener notesClickListener = new NotesClickListener() {
        @Override
        public void onClick(NoteViewModel notes) {
            Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
            intent.putExtra("oldNote", notes);
            startActivityForResult(intent, 102);  //for editing code is 102


        }

        @Override
        public void onLongClick(NoteViewModel notes, CardView cardView) {
            selectedNotes = new NoteViewModel();
            selectedNotes = notes;
            showPopUp(cardView);

        }
    };

    private void showPopUp(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case (R.id.pin):
                if(selectedNotes.isPinned()){
                    database.noteDao().pin(selectedNotes.getID(), false);
                    Toast.makeText(this, "Unpinned!", Toast.LENGTH_SHORT).show();
                }
                else{
                    database.noteDao().pin(selectedNotes.getID(), true);
                    Toast.makeText(this, "Pinned", Toast.LENGTH_SHORT).show();
                }
                notes.clear();
                notes.addAll(database.noteDao().getAll());
                noteListAdapter.notifyDataSetChanged();
                return true;
                
            case R.id.delete:
                database.noteDao().delete(selectedNotes);
                notes.remove(selectedNotes);
                noteListAdapter.notifyDataSetChanged();;
                Toast.makeText(this, "Note is deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }

    }
}