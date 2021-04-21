package com.kingdomredherous.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.kingdomredherous.notesapp.apiFolder.API;
import com.kingdomredherous.notesapp.apiFolder.APIClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditorActivity extends AppCompatActivity {
    EditText et_title, et_popis;
    ProgressDialog progressDialog;
    API api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        et_title = findViewById(R.id.title);
        et_popis = findViewById(R.id.popis);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cekejte prosim...");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_editor, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                String title = et_title.getText().toString().trim();
                String popis = et_popis.getText().toString().trim();
                if (title.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Zadejte prosim nazev!", Toast.LENGTH_SHORT).show();
                } else if (popis.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Zadejte prosim popis!", Toast.LENGTH_SHORT).show();
                } else
                    saveNote(title, popis);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void saveNote(final String title, final String popis) {
        progressDialog.show();
        api = APIClient.getClient().create(API.class);
        Call<Note> call = api.saveNote(title, popis);

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