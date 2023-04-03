package com.demo.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton buttonAddNote;
    private RecyclerView recyclerViewNotes;
    public static final ArrayList<Note> notes = new ArrayList<>();
    NotesAdapter adapter;
    private NotesDBHelper dbHelper; // 1 DB

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonAddNote = findViewById(R.id.buttonAddNote);
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
        dbHelper = new NotesDBHelper(this); // 2 DB
        SQLiteDatabase database = dbHelper.getWritableDatabase(); // 3 DB
        if (notes.isEmpty()) {
            notes.add(new Note("Видеоконференция", "РФ", "Вторник", 2));
            notes.add(new Note("Планерка", "Гомель", "Понедельник", 1));
            notes.add(new Note("Магазин", "ОМА", "Четверг", 2));
            notes.add(new Note("Видеоконференция", "Казахстан", "Вторник", 3));
            notes.add(new Note("Шашлык", "Из свинины", "Пятница", 1));
        }
        for (Note note : notes) { // 4 DB
            ContentValues contentValues = new ContentValues(); // Для того, чтобы вставить запись
            // Данные добавляются в виде пар ключ-значение (заголовок столбца-значение из заметки)
            contentValues.put(NotesContract.NotesEntry.COLUMN_TITLE, note.getTitle());
            contentValues.put(NotesContract.NotesEntry.COLUMN_DESCRIPTION, note.getDescription());
            contentValues.put(NotesContract.NotesEntry.COLUMN_DAY_OF_WEEK, note.getDayOfWeek());
            contentValues.put(NotesContract.NotesEntry.COLUMN_PRIORITY, note.getPriority());
            // Вставляем объект contentValues в БД
            database.insert(NotesContract.NotesEntry.TABLE_NAME, null, contentValues);
        }
        // Проверяем, что БД работает (получаем из нее информацию)
        ArrayList<Note> notesFromDB = new ArrayList<>();
        // Cursor используется для получения информации из БД. В нем хранятся все записи из БД
        Cursor cursor = database.query(NotesContract.NotesEntry.TABLE_NAME,
                null, null, null, null, null, null);
        while (cursor.moveToNext()) { // вызвав этот метод, перешли к нулевому элементу
            // Получаем имена колонок. getString() принимает индекс колонки, но т.к. индекс не известен,
            // вызываем метод getColumnIndexOrThrow(), который принимает имя колонки
            String title = cursor.getString(cursor.getColumnIndexOrThrow(NotesContract.NotesEntry.COLUMN_TITLE));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(NotesContract.NotesEntry.COLUMN_DESCRIPTION));
            String dayOfWeek = cursor.getString(cursor.getColumnIndexOrThrow(NotesContract.NotesEntry.COLUMN_DAY_OF_WEEK));
            int priority = cursor.getInt(cursor.getColumnIndexOrThrow(NotesContract.NotesEntry.COLUMN_PRIORITY));
            Note note = new Note(title, description, dayOfWeek, priority);
            notesFromDB.add(note);
        }
        cursor.close(); // обязательно нужно закрывать Cursor, после выполнения всех действий
        // добавляем созданный адаптер
        adapter = new NotesAdapter(notes);
        // указываем расположение элементов, в данном случае (вертикальное последовательное)
        recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));

        // горизонтальное последовательное расположение
        //recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        // расположение сеткой
        //recyclerViewNotes.setLayoutManager(new GridLayoutManager(this, 3));

        // устанавливаем у RecyclerView созданный адаптер
        recyclerViewNotes.setAdapter(adapter);

        // устанавливаем слушателя событий, которого создали. в RecyclerView нет встроенного метода
        adapter.setOnNoteClickListener(new NotesAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(int position) {
                Toast.makeText(MainActivity.this, "Номер позиции: " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(int position) {
                remove(position);
            }
        });

        buttonAddNote.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddNoteActivity.class);
            startActivity(intent);
        });

        // создаем объект itemTouchHelper для управления RecyclerView с помощью свайпа - сдвига элемента
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    // direction отвечает за действие, в зависимости от сдвига влево или вправо (swipeDirs)
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        remove(viewHolder.getAdapterPosition());
                    }
                });
        // теперь необходимо объект itemTouchHelper применить к RecyclerView
        itemTouchHelper.attachToRecyclerView(recyclerViewNotes);
    }

    private void remove(int position) {
        notes.remove(position);
        // мгновенно обновляет RecyclerView, при добавлении или удалении элемента
        // без метода notifyDataSetChanged() приложение ломается
        adapter.notifyDataSetChanged();
    }
}