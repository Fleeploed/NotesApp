package com.kingdomredherous.notesapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Note {
    @Expose
    @SerializedName("id_seznam")
    private int id;
    @Expose
    @SerializedName("title")
    private String title;
    @Expose
    @SerializedName("popis")
    private String popis;
    @Expose
    @SerializedName("datum")
    private String datum;
    @Expose
    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPopis() {
        return popis;
    }

    public void setPopis(String popis) {
        this.popis = popis;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }
}
