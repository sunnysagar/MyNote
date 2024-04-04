package com.sunny.mynote;

import androidx.cardview.widget.CardView;

import com.sunny.mynote.model.NoteViewModel;

public interface NotesClickListener {

    void onClick(NoteViewModel notes);
    void onLongClick(NoteViewModel notes, CardView cardView);
}
