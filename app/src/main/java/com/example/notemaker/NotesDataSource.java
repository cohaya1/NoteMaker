package com.example.notemaker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;


public class NotesDataSource {

    private final String TAG = this.getClass().getSimpleName();

    private SQLiteDatabase database;
    private DBHelper dbHelper;


    NotesDataSource(Context context){
        dbHelper = new DBHelper(context);

    }


    void open() throws SQLException {
        database = dbHelper.getWritableDatabase();

    }

    void close() {
        dbHelper.close();
    }


    /**
     * @param note inserts new note into the database.
     * @return true for success and false for failure to insert note obj
     */
    boolean insertNote(Notes note) {
        boolean didSucceed = false;
        try {

            ContentValues initialValues = new ContentValues();

            initialValues.put("notename", note.getNoteName());
            initialValues.put("subject", note.getSubject());
            initialValues.put("notecontent",note.getContent());
            initialValues.put("datecreated",String.valueOf(note.getDateCreated().getTimeInMillis()));
            initialValues.put("priority",note.getPriority());

            didSucceed = database.insert("note", null, initialValues) > 0;

        } catch (Exception ex) {

            Log.e(TAG,String.valueOf(ex));
            ex.printStackTrace();

        }
        return didSucceed;
    }


    boolean updateNote(Notes note) {

        boolean didSucceed = false;

        try {
            long rowId = (long) note.getNoteID();
            ContentValues updateValues = new ContentValues();

            updateValues.put("notename",note.getNoteName());
            updateValues.put("subject", note.getSubject());
            updateValues.put("notecontent",note.getContent());
            updateValues.put("datecreated",String.valueOf(note.getDateCreated().getTimeInMillis()));
            updateValues.put("priority",note.getPriority());


            didSucceed = database.update("note", updateValues, "_id=" + rowId, null) > 0;

        } catch (Exception ex) {

            Log.e(TAG,String.valueOf(ex));
        }
        return didSucceed;
    }

    /**
     * @return the ID aka.'noteID', of the last note
     *  to be inserted into the database
     */
    int getLastNoteId() {
        int lastId = -1;
        try {
            String query = "Select MAX(_id) from note";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            lastId = cursor.getInt(0);
            cursor.close();

        } catch (Exception e) {
            lastId = -1;
        }
        return lastId;
    }

    public ArrayList<Notes> getNotes(String sortField, String sortOrder) {

        ArrayList<Notes> notes = new ArrayList<>();

        try {
            String query = "SELECT * FROM note ORDER BY " + sortField + " " + sortOrder;

            Cursor cursor = database.rawQuery(query, null);

            Notes note;
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                note = new Notes();

                note.setNoteID(cursor.getInt(0));
                note.setNoteName(cursor.getString(1));
                note.setSubject(cursor.getString(2));
                note.setNoteContent(cursor.getString(3));
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.valueOf(cursor.getString(4)));
                note.setDateCreated(calendar);
                note.setPriority(cursor.getInt(5));

                notes.add(note);
                cursor.moveToNext();
            }
            cursor.close();


        } catch (Exception e) {

            notes = new ArrayList<>();

        }
        return notes;

    }

    Notes getSpecificNote(int noteID){
        Notes note = new Notes();
        String query = "SELECT * FROM note WHERE _id =" + noteID;
        Cursor cursor = database.rawQuery(query,null);

        if (cursor.moveToFirst()) {

            note.setNoteID(cursor.getInt(0));
            note.setNoteName(cursor.getString(1));
            note.setSubject(cursor.getString(2));
            note.setNoteContent(cursor.getString(3));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.valueOf(cursor.getString(4)));
            note.setDateCreated(calendar);
            note.setPriority(cursor.getInt(5));

            cursor.close();
        }

        return note;

    }
    boolean delete(int noteID) {
        boolean didDelete = false;
        try {
            didDelete = database.delete("note", "_id=" + noteID, null) > 0;
        } catch (Exception e) {
            //Do nothing -return value already set to false
        }
        return didDelete;
    }

}
