package com.evgeniy.moiseev.testtaskevo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements NoteAdapter.OnNoteClickListener, SearchView.OnQueryTextListener {
    private static final String ALL_ENTRIES = "";
    private Menu menu;
    private NoteViewModel mNoteViewModel;
    private RecyclerView mRecyclerView;
    private NoteAdapter mAdapter;
    private SearchView mSearchView;
    private boolean desc = true;

    private View.OnClickListener fabListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, NoteDetailsActivity.class);
            intent.putExtra(NoteDetailsActivity.EXTRA_FLAG, NoteDetailsActivity.FLAG_CREATE);
            startActivity(intent);
        }
    };
    private Observer<PagedList<Note>> mObserver = new Observer<PagedList<Note>>() {
        @Override
        public void onChanged(PagedList<Note> notes) {
            mAdapter.submitList(notes);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mNoteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(fabListener);

        mSearchView = findViewById(R.id.searchView);
        mSearchView.setOnQueryTextListener(this);

        mRecyclerView = findViewById(R.id.recyclerView);
        mAdapter = new NoteAdapter(this);
        mNoteViewModel.getAllFilterNotesDesc(ALL_ENTRIES).observe(this, mObserver);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuDesc:
                if (!mNoteViewModel.getAllFilterNotesDesc(ALL_ENTRIES).hasObservers()) {
                    mNoteViewModel.getAllFilterNotesAsc(ALL_ENTRIES).removeObservers(this);
                    mNoteViewModel.getAllFilterNotesDesc(ALL_ENTRIES).observe(this, mObserver);
                    desc = true;
                }
                return true;
            case R.id.menuAsc:
                if (!mNoteViewModel.getAllFilterNotesAsc(ALL_ENTRIES).hasObservers()) {
                    mNoteViewModel.getAllFilterNotesDesc(ALL_ENTRIES).removeObservers(this);
                    mNoteViewModel.getAllFilterNotesAsc(ALL_ENTRIES).observe(this, mObserver);
                    desc = false;
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onNoteClicked(long id) {
        Intent intent = new Intent(MainActivity.this, NoteDetailsActivity.class);
        intent.putExtra(NoteDetailsActivity.EXTRA_FLAG, NoteDetailsActivity.FLAG_EDIT);
        intent.putExtra(NoteDetailsActivity.EXTRA_ID, id);
        startActivity(intent);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!newText.equals(ALL_ENTRIES) && menu != null) {
            menu.findItem(R.id.menuSort).setVisible(false);
        } else if (newText.equals(ALL_ENTRIES) && menu != null) {
            menu.findItem(R.id.menuSort).setVisible(true);
        }
        if (desc) {
            mNoteViewModel.getAllFilterNotesDesc(ALL_ENTRIES).removeObservers(this);
            mNoteViewModel.getAllFilterNotesDesc(newText).observe(this, mObserver);
        } else {
            mNoteViewModel.getAllFilterNotesAsc(ALL_ENTRIES).removeObservers(this);
            mNoteViewModel.getAllFilterNotesAsc(newText).observe(this, mObserver);
        }
        return false;
    }
}
