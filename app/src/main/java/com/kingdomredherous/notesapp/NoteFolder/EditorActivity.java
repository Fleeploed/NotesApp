package com.kingdomredherous.notesapp.NoteFolder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.kingdomredherous.notesapp.ApiFolder.API;
import com.kingdomredherous.notesapp.ApiFolder.APIClient;
import com.kingdomredherous.notesapp.ApiFolder.Note;
import com.kingdomredherous.notesapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditorActivity extends AppCompatActivity {
    EditText et_title, et_popis;
    ProgressDialog progressDialog;
    API api;
    int id,id_user;
    String title, popis;
    Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        getSupportActionBar().setTitle("Nová poznámka");
        et_title = findViewById(R.id.title);
        et_popis = findViewById(R.id.popis);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cekejte prosim...");
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        title = intent.getStringExtra("title");
        popis = intent.getStringExtra("popis");
        setDataFromIntent();
    }

    private void setDataFromIntent() {
        if (id != 0) {
            et_title.setText(title);
            et_popis.setText(popis);
            getSupportActionBar().setTitle("Aktualizovat poznámku");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_editor, menu);
        this.menu = menu;
        if (id != 0) {
            this.menu.findItem(R.id.update).setVisible(true);
            this.menu.findItem(R.id.delete).setVisible(true);
            this.menu.findItem(R.id.save).setVisible(false);
        } else {
            this.menu.findItem(R.id.delete).setVisible(false);
            this.menu.findItem(R.id.save).setVisible(true);
            this.menu.findItem(R.id.update).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String title = et_title.getText().toString().trim();
        String popis = et_popis.getText().toString().trim();
        id_user = getIntent().getExtras().getInt("id_user");
        switch (item.getItemId()) {
            case R.id.save:
                if (title.isEmpty()) {
                    Toast.makeText(this, "Zadejte prosím název!", Toast.LENGTH_SHORT).show();
                } else if (popis.isEmpty()) {
                    Toast.makeText(this, "Zadejte prosím popis!", Toast.LENGTH_SHORT).show();
                } else
                    saveNote(id_user,title, popis);
                return true;
            case R.id.update:
                if (title.isEmpty()) {
                    Toast.makeText(this, "Zadejte prosím název!", Toast.LENGTH_SHORT).show();
                } else if (popis.isEmpty()) {
                    Toast.makeText(this, "Zadejte prosím popis!", Toast.LENGTH_SHORT).show();
                } else {
                    updateNote(id, title, popis);
                }
                return true;
            case R.id.delete:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Potvrdit!");
                alertDialog.setMessage("Jste si jistý?");
                alertDialog.setNegativeButton("Ano", (dialogInterface, i) -> {
                    deleteNote(id);
                });
                alertDialog.setPositiveButton("Ne", ((dialogInterface, i) -> {
                    dialogInterface.dismiss();
                }));
                alertDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void deleteNote(int id) {
        progressDialog.show();
        api = APIClient.getClient().create(API.class);
        Call<Note> call = api.deleteNote(id);
        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(@NonNull Call<Note> call, @NonNull Response<Note> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(EditorActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Note> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditorActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void updateNote(int id, String title, String popis) {
        progressDialog.show();
        api = APIClient.getClient().create(API.class);
        Call<Note> call = api.updateNote(id, title, popis);
        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(@NonNull Call<Note> call, @NonNull Response<Note> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(EditorActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Note> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditorActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void saveNote(int id_user, final String title, final String popis) {
        progressDialog.show();
        api = APIClient.getClient().create(API.class);

        Call<Note> call = api.saveNote(id_user, title, popis);


        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(@NonNull Call<Note> call, @NonNull Response<Note> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(EditorActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Note> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditorActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}