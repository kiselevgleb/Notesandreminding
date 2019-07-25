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
    private static EditText dueDate;
    private static EditText title;
    private static EditText description;

    private CheckBox checkBoxDate;
    private Calendar dateAndTime = Calendar.getInstance();
    private Note notes;
    private String sethd = null;
    private String setText = null;
    private String dateText = null;
    private long zero = 1;
    private int checkBox;
    private int impText = 0;
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
        impText = getIntent().getExtras().getInt("impText");

        if (impText == 1) {
            sethd = getIntent().getExtras().getString("haderEditText");
            setText = getIntent().getExtras().getString("textEditText");
            dateText = getIntent().getExtras().getString("dateEditText");
            checkBox = getIntent().getExtras().getInt("checkBox");
            if (checkBox == 1) {
                checkBoxDate.setChecked(true);
            }
            title.setText(sethd);
            description.setText(setText);
            dueDate.setText(dateText);
        } else {
            sethd = null;
            setText = null;
            dateText = null;
            title.setText(sethd);
            description.setText(setText);
            dueDate.setText(dateAndTime.toString());
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
                if (impText == 0) {
                    if (checkBoxDate.isChecked()) {
                        String textHader = title.getText().toString();
                        String textBody = description.getText().toString();
                        String cal = dueDate.getText().toString();
                        Date dateDate = Calendar.getInstance().getTime();
                        SimpleDateFormat formatter = null;
                        if (cal.split("")[2].equals(" ")){formatter = new SimpleDateFormat("dd MMMM yyyy г., hh:mm");}
                        else {formatter = new SimpleDateFormat("dd/MM/yy hh:mm");}
                        try { dateDate = formatter.parse(cal);
                        } catch (ParseException e) {
                            e.printStackTrace(); }
                        Long newCalendar = dateDate.getTime();
                        notes = new Note(textHader, textBody, newCalendar, 1);
                        NotesRepository.saveNote(newCalendar, notes);
                        Intent intent = new Intent(WriteNotesActivity.this, NotesAct.class);
                        startActivity(intent);
                        finish();
                        return true;
                    } else {
                        String textHader = title.getText().toString();
                        String textBody = description.getText().toString();
                        notes = new Note(textHader, textBody, zero, 0);
                        NotesRepository.saveNote(Calendar.getInstance().getTimeInMillis(), notes);
                        Intent intent = new Intent(WriteNotesActivity.this, NotesAct.class);
                        startActivity(intent);
                        finish();
                        return true;
                    }
                } else if (impText == 1) {
                    if (checkBoxDate.isChecked()) {

                        String textHader = title.getText().toString();
                        String textBody = description.getText().toString();
                        String cal = dueDate.getText().toString();
                        Date dateDate = Calendar.getInstance().getTime();
                        SimpleDateFormat formatter = null;
                        if (cal.split("")[2].equals(" ")){formatter = new SimpleDateFormat("dd MMMM yyyy г., hh:mm"); }
                        else { formatter = new SimpleDateFormat("dd/MM/yy hh:mm"); }
                        try { dateDate = formatter.parse(cal);
                        } catch (ParseException e) { e.printStackTrace(); }
                        Long newCalendar = dateDate.getTime();

                        for (Map.Entry<Long, Note> entry : NotesRepository.getNotes().entrySet()) {
                            Long calLong = entry.getKey();
                            Note n = entry.getValue();

                            if (n.getTitle().equals(sethd) && n.getText().equals(setText)) {
                                notes = new Note(textHader, textBody, newCalendar, 1);
                                NotesRepository.removeNote(calLong);
                                NotesRepository.saveNote(newCalendar, notes);
                            }
                        }
                        Intent intent = new Intent(WriteNotesActivity.this, NotesAct.class);
                        startActivity(intent);
                        finish();

                        return true;
                    } else {
                        String textHader = title.getText().toString();
                        String textBody = description.getText().toString();
                        for (Map.Entry<Long, Note> entry : NotesRepository.getNotes().entrySet()) {
                            Long cal = entry.getKey();
                            Note n = entry.getValue();
                            if (n.getTitle().equals(sethd) && n.getText().equals(setText)) {
                                n.setTitle(textHader);
                                n.setText(textBody);
                                cal = Calendar.getInstance().getTimeInMillis();
                            }
                        }
                        Intent intent = new Intent(WriteNotesActivity.this, NotesAct.class);
                        startActivity(intent);
                        finish();
                        return true;
                    }
                }
            case android.R.id.home:
//                impText = 0;
                finish();
//                Intent intent = new Intent(WriteNotesActivity.this, NotesAct.class);
//                intent.putExtra("impText", impText);
//                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setDate() {
        new DatePickerDialog(WriteNotesActivity.this, dateSetListener,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    public void setTime(View v) {
        new TimePickerDialog(WriteNotesActivity.this, timeSetListener,
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

    TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            setInitialDateTime();
        }
    };
    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
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


}
