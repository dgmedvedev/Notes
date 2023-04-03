package com.demo.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class AddNoteActivity extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextDescription;
    private Spinner spinnerDaysOfWeek;
    private RadioGroup radioGroupPriority;
    private Button buttonSaveNote;

    private NotesDBHelper dbHelper; // 1 DB
    private SQLiteDatabase database;// 3 DB

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        dbHelper = new NotesDBHelper(this); // 2 DB
        database = dbHelper.getWritableDatabase(); // 4 DB
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        spinnerDaysOfWeek = findViewById(R.id.spinnerDaysOfWeek);
        radioGroupPriority = findViewById(R.id.radioGroupPriority);
        buttonSaveNote = findViewById(R.id.buttonSaveNote);

        buttonSaveNote.setOnClickListener(view -> {
            String title = editTextTitle.getText().toString().trim();
            String description = editTextDescription.getText().toString().trim();
            String dayOfWeek = spinnerDaysOfWeek.getSelectedItem().toString();
            int radioButtonId = radioGroupPriority.getCheckedRadioButtonId();
            RadioButton radioButton = findViewById(radioButtonId);
            int priority = Integer.parseInt(radioButton.getText().toString());

            // 5 DB. Добавляем данные в БД
            if (isFilled(title, description)) {
                ContentValues contentValues = new ContentValues(); // Для того, чтобы добавить запись
                // Данные добавляются в виде пар ключ-значение (заголовок столбца-значение из заметки)
                contentValues.put(NotesContract.NotesEntry.COLUMN_TITLE, title);
                contentValues.put(NotesContract.NotesEntry.COLUMN_DESCRIPTION, description);
                contentValues.put(NotesContract.NotesEntry.COLUMN_DAY_OF_WEEK, dayOfWeek);
                contentValues.put(NotesContract.NotesEntry.COLUMN_PRIORITY, priority);
                // Вставляем объект contentValues в БД
                database.insert(NotesContract.NotesEntry.TABLE_NAME, null, contentValues);

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.warning_fill_fields, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Проверяем, заполнены ли значения
    private boolean isFilled(String title, String description) {
        return !title.isEmpty() && !description.isEmpty();
    }
}