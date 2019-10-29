package com.example.notemaker;

import java.util.Calendar;

public class Notes {
    private int noteID;
    private String notesName;
    private String subject;
    private String noteContent;
    private Calendar dateCreated;
    private Boolean isExpanded;
    private int priority;

    public Notes(){
        noteID = -1;
        isExpanded = false;
        priority = 0;
    }


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }


    public int getNoteID() {
        return noteID;
    }

    public void setNoteID(int n) {
        this.noteID = n;
    }


    public String getContent() {
        return noteContent;
    }

    public void setNoteContent(String c) {
        this.noteContent = c;
    }

    public Calendar getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Calendar c) {
        this.dateCreated= c; }

    public String getNoteName() {
        return notesName;
    }

    public void setNoteName(String noteName) {
        this.notesName = noteName;
    }

    public Boolean getExpanded() {
        return isExpanded;
    }

    public void setExpanded(Boolean expanded) {
        isExpanded = expanded;
    }


    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}

