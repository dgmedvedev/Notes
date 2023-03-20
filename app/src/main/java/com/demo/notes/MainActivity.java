package com.demo.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewNotes;
    private ArrayList<Note> notes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
        notes.add(new Note("Видеоконференция", "РФ", "Вторник",2));
        notes.add(new Note("Планерка", "Гомель", "Понедельник",1));
        notes.add(new Note("Магазин", "ОМА", "Четверг",2));
        notes.add(new Note("Видеоконференция", "Казахстан", "Вторник",3));
        notes.add(new Note("Шашлык", "Из свинины", "Пятница",1));
    }
}