package com.evgeniy.moiseev.testtaskevo;

import android.app.Application;
import android.os.AsyncTask;

import androidx.paging.DataSource;


public class NoteRepository {
    private NoteDao mNoteDao;

    NoteRepository(Application application) {
        NoteRoomDatabase db = NoteRoomDatabase.getDatabase(application);
        mNoteDao = db.noteDao();
    }

    public DataSource.Factory<Integer, Note> getAllFilterNotesAsc(String filter) {
        return mNoteDao.getFilterNotesAsc(filter);
    }

    public DataSource.Factory<Integer, Note> getAllFilterNotesDesc(String filter) {
        return mNoteDao.getFilterNotesDesc(filter);
    }

    public void insertNote(Note note) {
        new insertNoteAsyncTask(mNoteDao).execute(note);
    }

    private static class insertNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao mAsyncTaskDao;

        insertNoteAsyncTask(NoteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Note... params) {
            mAsyncTaskDao.insertNote(params[0]);
            return null;
        }
    }

    public Note getNote(long id) {
        try {
            return new getNoteByIdAsyncTask(mNoteDao).execute(id).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class getNoteByIdAsyncTask extends AsyncTask<Long, Void, Note> {
        private NoteDao mAsyncTaskDao;

        public getNoteByIdAsyncTask(NoteDao dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Note doInBackground(Long... ids) {
            return mAsyncTaskDao.selectNoteById(ids[0]);
        }
    }

    public void updateNote(Note note) {
        new updateNoteAsyncTask(mNoteDao).execute(note);
    }

    private static class updateNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao mAsyncTaskDao;

        updateNoteAsyncTask(NoteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Note... params) {
            mAsyncTaskDao.updateNote(params[0]);
            return null;
        }
    }

    public void deleteNote(Note note) {
        new deleteNoteAsyncTask(mNoteDao).execute(note);
    }

    private static class deleteNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao mAsyncTaskDao;

        deleteNoteAsyncTask(NoteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Note... params) {
            mAsyncTaskDao.deleteNote(params[0]);
            return null;
        }
    }
}
