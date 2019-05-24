package com.evgeniy.moiseev.testtaskevo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Note {
    @PrimaryKey(autoGenerate = true)
    public long nid;

    @ColumnInfo(name = "date")
    public long date;

    @ColumnInfo(name = "content")
    public String content;

    public Note(long date, String content) {
        this.date = date;
        this.content = content;
    }

    public void setId(long nid) {
        this.nid = nid;
    }

    public long getId() {
        return nid;
    }

    public long getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }
}
