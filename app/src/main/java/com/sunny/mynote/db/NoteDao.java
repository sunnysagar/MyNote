package com.sunny.mynote.db;



import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.sunny.mynote.model.NoteViewModel;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert(onConflict = REPLACE)
    void insert(NoteViewModel notes);

    @Query("SELECT * FROM notes ORDER BY id DESC")
    List<NoteViewModel> getAll();

    @Query("UPDATE notes SET title= :title,notes = :notes WHERE ID = :id ")
    void update(int id, String title, String notes);

    @Delete
    void delete(NoteViewModel notes);

    @Query("UPDATE notes SET pinned= :pin where ID = :id")
    void pin(int id, boolean pin);
}
