package com.evgeniy.moiseev.testtaskevo;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

public class NoteViewModel extends AndroidViewModel {
    private static final int PAGE_SIZE = 20;

    private NoteRepository mRepository;

    public NoteViewModel(Application application) {
        super(application);
        mRepository = new NoteRepository(application);
    }

    public LiveData<PagedList<Note>> getAllFilterNotesAsc(String filter) {
        return new LivePagedListBuilder<>(mRepository.getAllFilterNotesAsc("%" + filter + "%"),
                new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)
                        .setPageSize(PAGE_SIZE).build()).build();
    }

    public LiveData<PagedList<Note>> getAllFilterNotesDesc(String filter) {
        return new LivePagedListBuilder<>(mRepository.getAllFilterNotesDesc("%" + filter + "%"),
                new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)
                        .setPageSize(PAGE_SIZE).build()).build();
    }

    public void insertNote(Note note) {
        mRepository.insertNote(note);
    }

    public String getNoteContent(long id) {
        Note note = mRepository.getNote(id);
        return note.content;
    }

    public void updateNote(long id, Note note) {
        Note n = note;
        n.setId(id);
        mRepository.updateNote(n);
    }

    public void deleteNote(long id, Note note) {
        Note n = note;
        note.setId(id);
        mRepository.deleteNote(n);
    }

}
