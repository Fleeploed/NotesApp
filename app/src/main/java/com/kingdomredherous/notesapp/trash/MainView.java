package com.kingdomredherous.notesapp.trash;

import com.kingdomredherous.notesapp.Note;

import java.util.List;

public interface MainView {
    void showLoading();
    void hideLoading();
    void onGetResult(List<Note> notes);
    void onErrorLoading(String message);
}
