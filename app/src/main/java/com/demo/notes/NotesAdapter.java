package com.demo.notes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    ArrayList<Note> notes;

    public NotesAdapter(ArrayList<Note> notes) {
        this.notes = notes;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // передаем макет заметки в конструктор NotesViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new NotesViewHolder(view);
    }

    @Override
    // принимает объект NotesViewHolder и порядковый номер элемента массива
    public void onBindViewHolder(@NonNull NotesViewHolder notesViewHolder, int position) {
        Note note = notes.get(position);
        notesViewHolder.textViewTitle.setText(note.getTitle());
        notesViewHolder.textViewDescription.setText(note.getDescription());
        notesViewHolder.textViewDayOfWeek.setText(note.getDayOfWeek());
        notesViewHolder.textViewPriority.setText(String.format("%s", note.getPriority()));
    }

    @Override
    // возвращает кол-во элементов в массиве
    public int getItemCount() {
        return notes.size();
    }

    class NotesViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewDayOfWeek;
        private TextView textViewPriority;

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewDayOfWeek = itemView.findViewById(R.id.textViewDayOfWeek);
            textViewPriority = itemView.findViewById(R.id.textViewPriority);
        }
    }
}