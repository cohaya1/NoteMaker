package com.example.notemaker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    private Notes currentNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*
         * Bundle stores the noteID of the note which the user clicked
         * on in ListActivity.
         */
        Bundle extras = getIntent().getExtras();

        if(extras != null){
            //Load the note using the noteID
            initNote(extras.getInt("noteid"));
        }else {
            //create a new blank note
            currentNotes = new Notes();
        }

        initNotesButton();
        initListButton();
        initSettingsButton();
        initTextChangedEvents();
        initRadioButtons();
        initSaveButton();
    }


    private void initNotesButton() {
        ImageButton noteButton = findViewById(R.id.ButtonNotesPage);
        noteButton.setEnabled(false);

    }
    private void initListButton(){
        ImageButton listButton = findViewById(R.id.ButtonAddNotes);
        listButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,NotesListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }


        });
    }
    private void initSettingsButton(){
        ImageButton settingButton = findViewById(R.id.ButtonSettings);
        settingButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


    }

    /**
     * Opens a connection to the database and uses getSpecificNote() to retrieve the
     * Note and then sets the TextViews with the current note values
     * @param id identifier for the note
     *
     */
    private void initNote(int id){

        NotesDataSource ds = new NotesDataSource(MainActivity.this);
        try{
            ds.open();
            currentNotes = ds.getSpecificNote(id);
            ds.close();

        } catch (Exception e) {
            Log.w(TAG,e);
            Snackbar.make(findViewById(R.id.main_activity),
                    "Something went wrong loading your note, please try again",
                    Snackbar.LENGTH_INDEFINITE);
        }

        EditText editName = findViewById(R.id.entertitle);
        EditText editSubject = findViewById(R.id.editSubject);
        EditText editNote = (EditText) findViewById(R.id.editText);
        RadioButton rbHigh = findViewById(R.id.High);
        RadioButton rbMedium = findViewById(R.id.Medium);
        RadioButton rbLow = findViewById(R.id.Low);


        editName.setText(currentNotes.getNoteName());
        editSubject.setText(currentNotes.getSubject());
        editNote.setText(currentNotes.getContent());


        //Check the correct radiobutton for the Note being loaded
        if(currentNotes.getPriority() == 3){
            rbHigh.setChecked(true);
        }else if(currentNotes.getPriority() == 2){
            rbMedium.setChecked(true);
        }else{
            rbLow.setChecked(true);
        }


    }

    /**
     * Sets event listener TextWatcher to each of the input fields in the NoteActivity
     */
    private void initTextChangedEvents() {
        final EditText etNoteName = findViewById(R.id.entertitle);
        etNoteName.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            /**
             * Updates the current Note with the users input
             * @param editable reference to the editTitle EditText
             */

            @Override
            public void afterTextChanged(Editable editable) {
                currentNotes.setNoteName(etNoteName.getText().toString());

            }
        });


        final EditText etNoteSubject = findViewById(R.id.editSubject);
        etNoteSubject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                currentNotes.setSubject(etNoteSubject.getText().toString());

            }
        });

        final EditText etNoteBody =(EditText) findViewById(R.id.editText);
        etNoteBody.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                currentNotes.setNoteContent(etNoteBody.getText().toString());

            }
        });


    }
    private void initRadioButtons(){

        final RadioGroup radioButtons = findViewById(R.id.radioGroupPriority);
        final RadioButton rbHigh = findViewById(R.id.High);
        final RadioButton rbMedium = findViewById(R.id.Medium);
        radioButtons.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton selectedButton = findViewById(checkedId);

                if (selectedButton.getId() == rbHigh.getId()) {
                    currentNotes.setPriority(3);

                } else if (selectedButton.getId() == rbMedium.getId()) {
                    currentNotes.setPriority(2);

                } else {
                    currentNotes.setPriority(1);
                }
            }

        });

    }

    private void initSaveButton () {

        Button saveButton = findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (currentNotes.getNoteName() == null || currentNotes.getContent() == null) {
                    Toast.makeText(getBaseContext(), "Make sure to fill in the name and the " +
                            "\n note content fields of the note before saving", Toast.LENGTH_LONG).show();
                }else {

                    currentNotes.setDateCreated(Calendar.getInstance());

                    hideKeyboard();
                    boolean wasSuccess = false;
                    NotesDataSource dataSource = new NotesDataSource(MainActivity.this);
                    try {
                        dataSource.open();

                        if (currentNotes.getNoteID() == -1) {
                            wasSuccess = dataSource.insertNote(currentNotes);

                            if (wasSuccess) {
                                int newId = dataSource.getLastNoteId();
                                currentNotes.setNoteID(newId);
                            }

                        } else {
                            wasSuccess = dataSource.updateNote(currentNotes);

                        }
                        dataSource.close();
                    } catch (Exception e1) {
                        Log.w(TAG, e1);
                        wasSuccess = false;

                    }

                    if (wasSuccess) {
                        Toast.makeText(getBaseContext(), "Success, Your new note was saved. " +
                                        "\nClick on the List icon on the navigation bar to manage your notes. ",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        EditText editName = findViewById(R.id.entertitle);
        assert imm != null;
        imm.hideSoftInputFromWindow(editName.getWindowToken(), 0);
        EditText editSubject = findViewById(R.id.editSubject);
        imm.hideSoftInputFromWindow(editSubject.getWindowToken(), 0);
        EditText editNote = (EditText) findViewById(R.id.editText);
        imm.hideSoftInputFromWindow(editNote.getWindowToken(), 0);

    }
}
