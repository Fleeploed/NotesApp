package com.kingdomredherous.notesapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kingdomredherous.notesapp.ApiFolder.API;
import com.kingdomredherous.notesapp.ApiFolder.APIClient;
import com.kingdomredherous.notesapp.ApiFolder.Note;
import com.kingdomredherous.notesapp.NoteFolder.EditorActivity;
import com.kingdomredherous.notesapp.NoteFolder.MyAdapter;

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
    String  jmeno;
    int id_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        id_user = getIntent().getExtras().getInt("id_user");
        jmeno = getIntent().getExtras().getString("jmeno");
        getSupportActionBar().setTitle("Dobrý den, " + jmeno + "!");
        fab = findViewById(R.id.add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                intent.putExtra("id_user", id_user);
                MainActivity.this.startActivityForResult(intent, INTENT_ADD);
            }
        });

        swipeRefresh = findViewById(R.id.swipe_refresh);

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
        callNoteData();
        swipeRefresh.setOnRefreshListener(this::callNoteData);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_ADD && resultCode == RESULT_OK) {
            callNoteData();
        } else if (requestCode == INTENT_EDIT && resultCode == RESULT_OK) {
            callNoteData();
        }
    }

    private void callNoteData() {
        API api = APIClient.getClient().create(API.class);
        Call<List<Note>> call = api.getNote(id_user);
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