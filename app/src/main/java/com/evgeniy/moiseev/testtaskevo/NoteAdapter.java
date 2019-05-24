package com.evgeniy.moiseev.testtaskevo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.Date;

public class NoteAdapter extends PagedListAdapter<Note, NoteAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private OnNoteClickListener mListener;

    private static DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getDate() == newItem.getDate() && oldItem.getContent().equals(newItem.getContent());
        }
    };

    protected NoteAdapter(Context context) {
        super(DIFF_CALLBACK);
        mInflater = LayoutInflater.from(context);
        mListener = (OnNoteClickListener) context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public interface OnNoteClickListener {
        void onNoteClicked(long id);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDate;
        TextView textViewTime;
        TextView textViewContent;
        View rootView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            textViewContent = itemView.findViewById(R.id.textViewContent);
        }

        void bind(final Note note) {
            DateFormat dateFormatDate = DateFormat.getDateInstance(DateFormat.SHORT);
            textViewDate.setText(dateFormatDate.format(new Date(note.date)));
            DateFormat dateFormatTime = DateFormat.getTimeInstance(DateFormat.SHORT);
            textViewTime.setText(dateFormatTime.format(new Date(note.date)));
            textViewContent.setText(note.content);
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onNoteClicked(note.nid);
                    }
                }
            });
        }
    }
}
