package com.example.notesandreminding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;


public class NewPassActivity extends AppCompatActivity {
    EditText pass;
    CheckBox eye;
    Button save;
    public static SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sp = getSharedPreferences("0", Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pass);
        pass = (EditText) findViewById(R.id.passNew);
        eye = (CheckBox) findViewById(R.id.checkBoxPass);
        save = (Button) findViewById(R.id.buttonSavePass);
        eye.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        save.setOnClickListener(clickListener);

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (pass.getText().toString().length() == 4) {
                String p = pass.getText().toString();
                sp = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor e = sp.edit();
                e.putString("777", p);
                e.commit();
                Intent intent = new Intent(NewPassActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Error password", Toast.LENGTH_SHORT).show();
            }
        }
    };
}