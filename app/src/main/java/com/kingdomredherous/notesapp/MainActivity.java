package com.kingdomredherous.notesapp;

import androidx.annotation.NonNull;
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
                startActivity(new Intent(this, EditorActivity.class)));

        swipeRefresh = findViewById(R.id.swipe_refresh);

        callNoteData();
        swipeRefresh.setOnRefreshListener(this::callNoteData);

        itemClickListener = (((view, position) -> {
            String title = notes.get(position).getTitle();
            Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
        }));
    }

    private void callNoteData() {
        API api = APIClient.getClient().create(API.class);
        Call<List<Note>> call = api.getNote();
        call.enqueue(new Callback<List<Note>>() {
            @Override
            public void onResponse(@NonNull Call<List<Note>> call,@NonNull Response<List<Note>> response) {
                loadData(response.body());
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<List<Note>> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadData(List<Note> notes){
        recyclerView = findViewById(R.id.recycler_view);
        myAdapter = new MyAdapter(notes,this,itemClickListener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        myAdapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myAdapter);
        this.notes = notes;
    }

}