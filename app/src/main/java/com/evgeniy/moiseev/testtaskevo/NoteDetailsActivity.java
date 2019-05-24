package com.evgeniy.moiseev.testtaskevo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.ViewModelProviders;

public class NoteDetailsActivity extends AppCompatActivity {
    public static final int FLAG_CREATE = 0;
    public static final int FLAG_EDIT = 1;
    public static final String EXTRA_FLAG = "flag";
    public static final String EXTRA_ID = "id";
    public static final String EXTRA_SHARE_TEXT = "share_text";

    private int currentFlag;
    private long currentId;
    private String currentContent;
    private NoteViewModel mNoteViewModel;
    private EditText mEditText;

    private View.OnClickListener mNavigationButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!TextUtils.isEmpty(mEditText.getText().toString().trim())) {
                if (currentFlag == FLAG_CREATE) {
                    mNoteViewModel.insertNote(new Note(System.currentTimeMillis(), mEditText.getText().toString()));
                } else {
                    if (!currentContent.equals(mEditText.getText().toString())) {
                        mNoteViewModel.updateNote(currentId, new Note(System.currentTimeMillis(), mEditText.getText().toString()));
                    }
                }
            }
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(mNavigationButtonListener);

        mNoteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);

        mEditText = findViewById(R.id.editText);
        if (getIntent().getIntExtra(EXTRA_FLAG, FLAG_CREATE) == FLAG_EDIT) {
            currentFlag = FLAG_EDIT;
            currentId = getIntent().getLongExtra(EXTRA_ID, 0);
            currentContent = mNoteViewModel.getNoteContent(currentId);
            mEditText.setText(currentContent);
        } else {
            currentFlag = FLAG_CREATE;
            currentContent = "";
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (currentFlag == FLAG_EDIT) {
            getMenuInflater().inflate(R.menu.menu_details, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuDelete:
                mNoteViewModel.deleteNote(currentId, new Note(System.currentTimeMillis(), mEditText.getText().toString()));
                finish();
                return true;
            case R.id.menuShare:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(EXTRA_SHARE_TEXT, mEditText.getText().toString());
                startActivity(Intent.createChooser(intent, getString(R.string.title_share)));

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {
    }
}
