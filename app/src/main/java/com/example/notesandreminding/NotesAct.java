package com.example.notesandreminding;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NotesAct extends AppCompatActivity {
    private ListView listView;
    private int impText = 0;
    MapAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        listView = findViewById(R.id.listView);
        showNotes(NotesRepository.getNotes());

        findViewById(R.id.fab).setOnClickListener(addNoteClickListener);
        listView.setOnItemClickListener(itemClickListener);
        listView.setOnItemLongClickListener(onItemLongClickListener);
    }

    AdapterView.OnItemLongClickListener onItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            final Note val = adapter.mData.get(position);

            AlertDialog.Builder builder = new AlertDialog.Builder(NotesAct.this)
                    .setTitle(R.string.Warning)
                    .setMessage(R.string.Sure)
                    .setCancelable(false)
                    .setPositiveButton(R.string.DELETE, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            NotesRepository.removeNote(val);
                            Intent intent = new Intent(NotesAct.this, NotesAct.class);
                            startActivity(intent);
                            finish();

                        }
                    });
            builder.setNegativeButton(R.string.CANCEL, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                    dialog.cancel();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
            return true;
        }
    };

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final Note val = adapter.mData.get(position);
            impText = 1;
            if (val != null) {

                String timeString = " ";
                Calendar d = Calendar.getInstance();
                d.setTimeInMillis(val.getDeadline());
                Date convertDate = d.getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy hh:mm");
                timeString = formatter.format(convertDate);
                Intent intent = new Intent(NotesAct.this, WriteNotesActivity.class);
                intent.putExtra("impText", impText);
                intent.putExtra("Note", val.getDeadline());
                startActivity(intent);
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showNotes(ArrayList<Note> not) {
        adapter = new MapAdapter(not);
        listView.setAdapter(adapter);
        NotesRepository.sort();
    }

    private View.OnClickListener addNoteClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            impText = 0;
            finish();
            Intent intent = new Intent(NotesAct.this, WriteNotesActivity.class);
            startActivity(intent);

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notes_activity_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_pass_action:
                Intent intent = new Intent(NotesAct.this, NewPassActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
