package com.example.notesandreminding;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WriteNotesActivity extends AppCompatActivity {
    private EditText dueDate;
    private static EditText title;
    private static EditText description;

    private CheckBox checkBoxDate;
    private Calendar dateAndTime = Calendar.getInstance();
    private static HashMap<String, Note> dateBase = new HashMap<>();
    private Note notes;
    private String sethd = null;
    private String setText = null;
    private String dateText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_notes);
        dueDate = (EditText) findViewById(R.id.date);
        title = (EditText) findViewById(R.id.hader);
        description = (EditText) findViewById(R.id.text);
        checkBoxDate = (CheckBox) findViewById(R.id.checkBox);
        ImageButton datebut = (ImageButton) findViewById(R.id.imageButton);
        datebut.setOnClickListener(clickListener);
        if (NotesAct.impText == 1) {
            sethd = getIntent().getExtras().getString("haderEditText");
            setText = getIntent().getExtras().getString("textEditText");
            dateText = getIntent().getExtras().getString("dateEditText");
            title.setText(sethd);
            description.setText(setText);
            dueDate.setText(dateText);
        } else {
            setInitialDateTime();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.savemenu, menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemMenuSave:
                if (sethd == null || setText == null) {
                    if (checkBoxDate.isChecked()) {
                        String textHader = title.getText().toString();
                        String textBody = description.getText().toString();
                        String newCalendar = dueDate.getText().toString();
                        notes = new Note(textHader, textBody, newCalendar);
                        dateBase.put(newCalendar, notes);
                        Intent intent = new Intent(WriteNotesActivity.this, NotesAct.class);
                        startActivity(intent);
                        NotesAct.impText = 0;
                        return true;
                    } else {
                        String textHader = title.getText().toString();
                        String textBody = description.getText().toString();
                        notes = new Note(textHader, textBody);
                        dateBase.put(dateAndTime.toString(), notes);
                        Intent intent = new Intent(WriteNotesActivity.this, NotesAct.class);
                        startActivity(intent);
                        NotesAct.impText = 0;
                        return true;
                    }
                } else {
                    if (checkBoxDate.isChecked()) {
                        String textHader = title.getText().toString();
                        String textBody = description.getText().toString();
                        String newCalendar = dueDate.getText().toString();

                        for (Map.Entry<String, Note> entry : dateBase.entrySet()) {
                            String cal = entry.getKey();
                            Note n = entry.getValue();
                            if (n.getTitle().equals(sethd) || n.getText().equals(setText)) {
                                n.setTitle(textHader);
                                n.setText(textBody);
                                n.setCalendar(newCalendar);

                            }
                        }
                        finish();
                        Intent intent = new Intent(WriteNotesActivity.this, NotesAct.class);
                        startActivity(intent);

                        NotesAct.impText = 0;
                        return true;
                    } else {
                        String textHader = title.getText().toString();
                        String textBody = description.getText().toString();
                        for (Map.Entry<String, Note> entry : dateBase.entrySet()) {
                            String cal = entry.getKey();
                            Note n = entry.getValue();
                            if (n.getTitle().equals(sethd) || n.getText().equals(setText)) {
                                n.setTitle(textHader);
                                n.setText(textBody);
                            }
                        }
                        finish();
                        Intent intent = new Intent(WriteNotesActivity.this, NotesAct.class);
                        startActivity(intent);
                        NotesAct.impText = 0;
                        return true;
                    }
                }
            case android.R.id.home:
                Intent intent = new Intent(WriteNotesActivity.this, NotesAct.class);
                startActivity(intent);
                NotesAct.impText = 0;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setDate() {
        new DatePickerDialog(WriteNotesActivity.this, dateDialog,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    public void setTime(View v) {
        new TimePickerDialog(WriteNotesActivity.this, timeDialog,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true)
                .show();
    }

    private void setInitialDateTime() {
        dueDate.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
                        | DateUtils.FORMAT_SHOW_TIME));
    }

    TimePickerDialog.OnTimeSetListener timeDialog = new TimePickerDialog.OnTimeSetListener() {

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            setInitialDateTime();
        }
    };
    DatePickerDialog.OnDateSetListener dateDialog = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setDate();
        }
    };

    public static HashMap<String, Note> getDateBase() {
        return dateBase;
    }

}
