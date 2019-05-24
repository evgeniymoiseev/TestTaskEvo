package com.evgeniy.moiseev.testtaskevo;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface NoteDao {
    @Insert
    void insertNote(Note note);

    @Query("SELECT * FROM note_table WHERE content LIKE :filter ORDER BY date COLLATE NOCASE ASC")
    DataSource.Factory<Integer, Note> getFilterNotesAsc(String filter);

    @Query("SELECT * FROM note_table WHERE content LIKE :filter ORDER BY date COLLATE NOCASE DESC")
    DataSource.Factory<Integer, Note> getFilterNotesDesc(String filter);

    @Delete
    void deleteNote(Note note);

    @Query("SELECT * FROM note_table WHERE nid=:id")
    Note selectNoteById(long id);

    @Update
    void updateNote(Note note);
}
