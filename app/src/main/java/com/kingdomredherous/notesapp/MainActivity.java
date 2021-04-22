package com.kingdomredherous.notesapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kingdomredherous.notesapp.apiFolder.API;
import com.kingdomredherous.notesapp.apiFolder.APIClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final int INTENT_EDIT = 200;
    private static final int INTENT_ADD = 100;
    FloatingActionButton fab;
    SwipeRefreshLayout swipeRefresh;
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    MyAdapter.ItemClickListener itemClickListener;
    List<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.add);
        fab.setOnClickListener(view ->
                startActivityForResult(new Intent(this, EditorActivity.class),
                        INTENT_ADD)
        );

        swipeRefresh = findViewById(R.id.swipe_refresh);

        callNoteData();
        swipeRefresh.setOnRefreshListener(this::callNoteData);

        itemClickListener = (((view, position) -> {
            int id = notes.get(position).getId();
            String title = notes.get(position).getTitle();
            String popis = notes.get(position).getPopis();

            Intent intent = new Intent(this, EditorActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("title", title);
            intent.putExtra("popis", popis);
            startActivityForResult(intent, INTENT_EDIT);
        }));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_ADD && resultCode == RESULT_OK) {
            callNoteData();
        } else if (requestCode==INTENT_EDIT && resultCode == RESULT_OK){
            callNoteData();
        }
    }

    private void callNoteData() {
        API api = APIClient.getClient().create(API.class);
        Call<List<Note>> call = api.getNote();
        call.enqueue(new Callback<List<Note>>() {
            @Override
            public void onResponse(@NonNull Call<List<Note>> call, @NonNull Response<List<Note>> response) {
                loadData(response.body());
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<List<Note>> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadData(List<Note> notes) {
        recyclerView = findViewById(R.id.recycler_view);
        myAdapter = new MyAdapter(notes, this, itemClickListener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        myAdapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myAdapter);
        this.notes = notes;
    }

}