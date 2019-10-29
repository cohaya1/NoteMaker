package com.example.notemaker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initSortOrderClick();
        initSortByClick();
        initListButton();
        initNotesButton();
        initSettingsButton();
        initSettings();
    }

    private void initSortByClick(){
        RadioGroup rgSortBy = findViewById(R.id.radioGroupSortBy);
        rgSortBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {


            @SuppressLint("ApplySharedPref")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                RadioButton rbPriority = (RadioButton) findViewById(R.id.radioGroupPriority);


                if (rbPriority.isChecked()) {
                    getSharedPreferences("NoteMakerPreferences",
                            Context.MODE_PRIVATE).edit()
                            .putString("sortfield", "priority").commit();
                } else {
                    getSharedPreferences("NoteMakerPreferences",
                            Context.MODE_PRIVATE).edit()
                            .putString("sortfield", "priority").commit();
                }
            }

        });
    }
    private void initSortOrderClick(){
        RadioGroup rgSortOrder = findViewById(R.id.radioGroupSortOrder);
        rgSortOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @SuppressLint("ApplySharedPref")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rbAscending = findViewById(R.id.radioAscending);
                if (rbAscending.isChecked()){
                    getSharedPreferences("NoteMakerPreferences",
                            Context.MODE_PRIVATE).edit()
                            .putString("sortorder","ASC").commit();
                }else {
                    getSharedPreferences("NoteMakerPreferences",
                            Context.MODE_PRIVATE).edit()
                            .putString("sortorder","DESC").commit();

                }

            }
        });
    }

    private void initSettings() {
        String sortBy = getSharedPreferences("NoteMakerPreferences",
                Context.MODE_PRIVATE).getString("sortfield", "priority");

        String sortOrder = getSharedPreferences("NoteMakerPreferences",
                Context.MODE_PRIVATE).getString("sortorder", "ASC");


        RadioButton rbPriority = findViewById(R.id.radioGroupSortBy);


        if (sortBy.equalsIgnoreCase("priority")) {
            rbPriority.setChecked(true);

        } else {
            rbPriority.setChecked(true);
        }

        RadioButton rbAscending = findViewById(R.id.radioAscending);
        RadioButton rbDescending = findViewById(R.id.radioDescending);

        if (sortOrder.equalsIgnoreCase(("ASC"))) {
            rbAscending.setChecked(true);

        } else {
            rbDescending.setChecked(true);
        }

    }




    private void initNotesButton() {
        ImageButton noteButton = findViewById(R.id.addnotes);
        noteButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    private void initListButton(){
        ImageButton listButton = findViewById(R.id.notespage);
        listButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                Intent intent = new Intent(SettingsActivity.this,NotesListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }
    private void initSettingsButton(){
        ImageButton settingButton = findViewById(R.id.notessettings);
        settingButton.setEnabled(false);
    }
}
