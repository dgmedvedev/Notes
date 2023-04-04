package com.demo.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private final ArrayList<Note> notes = new ArrayList<>();
    NotesAdapter adapter;
    private NotesDBHelper dbHelper; // 1 DB
    private SQLiteDatabase database;// 3 DB

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonAddNote = findViewById(R.id.buttonAddNote);
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
        dbHelper = new NotesDBHelper(this); // 2 DB
        database = dbHelper.getWritableDatabase(); // 4 DB

        getData(); // Получение данных из БД и присваивание их массиву

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
        int id = notes.get(position).getId();
        // where - что конкретно хотим удалить из таблицы в БД (удалить _ID, которое чему-то равно)
        String where = NotesContract.NotesEntry._ID + " = ?";
        // whereArgs - это ? из where. Т.е. какое-то конкретное значение.
        String[] whereArgs = new String[]{Integer.toString(id)};
        // Внизу строка: удалить все из таблицы notes, где (where) _ID = whereArgs
        database.delete(NotesContract.NotesEntry.TABLE_NAME, where, whereArgs);
        getData();
        // мгновенно обновляет RecyclerView, при добавлении или удалении элемента
        // без метода notifyDataSetChanged() приложение ломается
        adapter.notifyDataSetChanged();
    }

    // Получение данных из БД и присваивание их массиву
    private void getData() {
        notes.clear();
        // Cursor используется для получения информации из БД. В нем хранятся все записи из БД
        Cursor cursor = database.query(NotesContract.NotesEntry.TABLE_NAME,
                null, null, null, null, null, NotesContract.NotesEntry.COLUMN_DAY_OF_WEEK);
        while (cursor.moveToNext()) { // вызвав этот метод, перешли к нулевому элементу
            // Получаем имена колонок. getString() принимает индекс колонки, но т.к. индекс не известен,
            // вызываем метод getColumnIndexOrThrow(), который принимает имя колонки
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(NotesContract.NotesEntry._ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(NotesContract.NotesEntry.COLUMN_TITLE));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(NotesContract.NotesEntry.COLUMN_DESCRIPTION));
            int dayOfWeek = cursor.getInt(cursor.getColumnIndexOrThrow(NotesContract.NotesEntry.COLUMN_DAY_OF_WEEK));
            int priority = cursor.getInt(cursor.getColumnIndexOrThrow(NotesContract.NotesEntry.COLUMN_PRIORITY));
            Note note = new Note(id, title, description, dayOfWeek, priority);
            notes.add(note);
        }
        cursor.close(); // обязательно нужно закрывать Cursor, после выполнения всех действий
    }
}