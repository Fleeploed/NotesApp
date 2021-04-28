package com.kingdomredherous.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.kingdomredherous.notesapp.ApiFolder.API;
import com.kingdomredherous.notesapp.ApiFolder.APIClient;
import com.kingdomredherous.notesapp.ApiFolder.Note;
import com.kingdomredherous.notesapp.NoteFolder.EditorActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    EditText et_login, et_heslo;
    Button buttonSignIn;
    TextView createText;
    ProgressBar progressBar;
    API api;
    CheckBox checkbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_login = findViewById(R.id.login);
        et_heslo = findViewById(R.id.password);
        buttonSignIn = findViewById(R.id.btnLogin);
        createText = findViewById(R.id.createText);
        progressBar = findViewById(R.id.progressLogin);

        checkbox = (CheckBox) findViewById(R.id.checkbox);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    et_heslo.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    et_heslo.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
        createText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, SignUp.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(intent, 0);
                overridePendingTransition(0, 0);
                startActivity(intent);
                finish();
            }
        });

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String login = et_login.getText().toString().trim();
                String heslo = et_heslo.getText().toString().trim();
                if (login.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Zadejte prosim login!", Toast.LENGTH_SHORT).show();
                } else if (heslo.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Zadejte prosim heslo!", Toast.LENGTH_SHORT).show();
                } else
                    signIn(login, heslo);
            }
        });
    }

    private void signIn(final String login, final String heslo) {
        progressBar.setVisibility(View.VISIBLE);
        api = APIClient.getClient().create(API.class);
        Call<Note> call = api.signIn(login, heslo);
        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(@NonNull Call<Note> call, @NonNull Response<Note> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getSuccess()) {
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        int id_user = response.body().getId_user();
                        String jmeno = response.body().getJmeno();
                        intent.putExtra("id_user", id_user);
                        intent.putExtra("jmeno", jmeno);
                        startActivity(intent);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Login.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Note> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Login.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}