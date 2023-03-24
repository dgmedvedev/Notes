package com.demo.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class AddNoteActivity extends AppCompatActivity {

    EditText editTextTitle;
    EditText editTextDescription;
    Spinner spinnerDaysOfWeek;
    RadioGroup radioGroupPriority;
    Button buttonSaveNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
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
            Note note = new Note(title, description, dayOfWeek, priority);
            MainActivity.notes.add(note);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }
}