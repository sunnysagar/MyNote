package com.sunny.mynote;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.sunny.mynote.model.NoteViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNoteActivity extends AppCompatActivity {

    EditText editTextTitle, editTextNotes;
    ImageView imageViewSave;

    NoteViewModel notes;

    boolean isOldNote = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_note);

        editTextTitle = findViewById(R.id.editTextTitle);

        editTextNotes = findViewById(R.id.editTextNotes);

        notes = new NoteViewModel();
        try{
            notes = (NoteViewModel) getIntent().getSerializableExtra("oldNote");
            editTextTitle.setText(notes.getTitle());
            editTextNotes.setText(notes.getNotes());

            isOldNote = true;
        }catch (Exception e){
            e.printStackTrace();
        }


        imageViewSave = findViewById(R.id.imageViewSave);

        imageViewSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextTitle.getText().toString();
                String description = editTextNotes.getText().toString();

                if(description.isEmpty()){
                    Toast.makeText(AddNoteActivity.this, "Please add notes", Toast.LENGTH_SHORT).show();
                    return;
                }

                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM, yyyy HH:mm a");
                Date date = new Date();

                if(!isOldNote){
                    notes = new NoteViewModel();
                }


                notes.setTitle((title));
                notes.setNotes(description);
                notes.setDate(formatter.format(date));

                Intent intent = new Intent();
                intent.putExtra("note", notes);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

    }
}